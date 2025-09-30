<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="68px">
          <el-form-item label="期间" prop="belongDate">
            <el-date-picker
                v-model="queryParams.belongDateRange"
                type="monthrange"
                unlink-panels
                range-separator="至"
                start-placeholder="开始期间"
                end-placeholder="结束期间"
                value-format="YYYY-MM"
                :shortcuts="shortcuts"
            />
          </el-form-item>
          <el-form-item>
            <el-button @click="handleQuery">{{ t('org.button.query') }}</el-button>
            <el-button @click="resetQuery">{{ t('org.button.reset') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="common-card">
      <div class="btn-form">
        <el-button type="primary" @click="handleProvision">计提折旧</el-button>
      </div>
      <el-table v-loading="loading" :data="dataList"
                border
                stripe
                :span-method="mergeCategoryColumn"
                show-summary
                :summary-method="getSummaries">
        <!-- 固定列 -->
        <el-table-column fixed prop="categoryName" label="类别" width="190" :show-overflow-tooltip="true"/>
        <el-table-column fixed prop="assetCode" label="编码" width="190" :show-overflow-tooltip="true"/>
        <el-table-column fixed prop="assetName" label="资产名称" width="190" :show-overflow-tooltip="true"/>
        <el-table-column prop="originalValue" label="原值" width="190">
          <template #default="scope">
            {{ formatAmountEmpty(scope.row.originalValue) }}
          </template>
        </el-table-column>
        <el-table-column prop="beginAccumulatedDepreciation" label="期初累计折旧" width="190">
          <template #default="scope">
            {{ formatAmountEmpty(scope.row.beginAccumulatedDepreciation) }}
          </template>
        </el-table-column>
        <!-- 多个月份，显示分组列 -->
        <el-table-column
            v-if="monthlyList.length > 1"
            label="本期折旧"
            align="center"
        >
          <el-table-column
              v-for="month in monthlyList"
              :key="month"
              :prop="month"
              :label="month"
              align="left"
              width="190"
          >
            <template #default="scope">
              {{ formatAmountEmpty(scope.row[month]) }}
            </template>
          </el-table-column>
        </el-table-column>
        <!-- 只有一个月份时，不使用分组 -->
        <el-table-column
            v-else
            :prop="monthlyList[0]"
            :label="monthlyList[0] + '期折旧'"
            align="left"
            width="190"
        >
          <template #default="scope">
            {{ formatAmountEmpty(scope.row[monthlyList[0]]) }}
          </template>
        </el-table-column>
        <el-table-column prop="currentYearDepreciation" label="本年折旧" width="190">
          <template #default="scope">
            {{ formatAmountEmpty(scope.row.currentYearDepreciation) }}
          </template>
        </el-table-column>
        <el-table-column prop="endAccumulatedDepreciation" label="期末累计折旧" width="190">
          <template #default="scope">
            {{ formatAmountEmpty(scope.row.endAccumulatedDepreciation) }}
          </template>
        </el-table-column>
        <el-table-column prop="endNetValue" label="期末净值" width="190">
          <template #default="scope">
            {{ formatAmountEmpty(scope.row.endNetValue) }}
          </template>
        </el-table-column>
      </el-table>
      <pagination
          v-show="total > 0"
          :total="total"
          v-model:page="queryParams.pageNumber"
          v-model:limit="queryParams.pageSize"
          :page-sizes="queryParams.pageSizeOptions"
          @pagination="getList"
      />
    </el-card>
    <el-dialog v-model="open" width="500px" append-to-body :title="title" :close-on-click-modal="false"
               @close="cancel">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="110px" inline-message>
        <el-form-item prop="voucherDate" label="凭证日期" :required="true">
          <el-date-picker
              v-model="form.voucherDate"
              type="date"
              placeholder="选择日期"
              :disabled-date="disabledDate"
              value-format="YYYY-MM-DD"
              format="YYYY-MM-DD"
              :clearable="false"
              style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="凭证字" :required="true">
          <el-select v-model="form.wordHead" style="width: 240px">
            <el-option v-for="item in wordHeads" :label="item.label" :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="摘要" prop="summary" :required="true">
          <el-input v-model="form.summary" placeholder="请输入摘要" style="width: 240px"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">{{ $t('systemCancel') }}</el-button>
        <el-button type="primary" @click="submitForm">{{ t('org.confirm') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import {reactive, ref, toRefs, getCurrentInstance, type VNode, h} from "vue";
import {useI18n} from "vue-i18n";
import {ElForm} from "element-plus";
import modal from "@/plugins/modal";
import {useRouter} from "vue-router";
import bookStore from "@/store/modules/bookStore";
import {generateProvision, fetchPage} from "@/api/fa/depreciation";
import {formatAmountEmpty} from "@/utils";
import type { TableColumnCtx } from 'element-plus'


const currBookStore = bookStore()
const {t} = useI18n()
const {proxy} = getCurrentInstance()!;
const {depreciation_method, wordHeads}
    = proxy?.useDict("depreciation_method", "wordHeads");
const router = useRouter()

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50],
    belongDateRange: [getCurrentYearMonth(), getCurrentYearMonth()],//默认查当前期数据
  },
  form: {
    voucherDate: `${currBookStore.termCurrent}-01`,
    wordHead: "记",
    summary: "计提折旧费用"
  },
  rules: {},
});
const dataList = ref([]);
const monthlyList = ref([]);
const open = ref(false);
const loading = ref(true);
const id: any = ref(undefined);
const total = ref(0);
const title = ref("");
const ids = ref([]);
const selectionlist: any = ref<any>([]);
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const {queryParams, form, rules} = toRefs(data);
const deptOptions: any = ref<any[]>([]);
// 解析账期年月
const [currYear, currMonth] = currBookStore.termCurrent.split('-').map(Number);
const shortcuts = [
  {
    text: '本月',
    value: [new Date(), new Date()],
  },
  {
    text: '本年',
    value: () => {
      const end = new Date()
      const start = new Date(new Date().getFullYear(), 0)
      return [start, end]
    },
  },
  {
    text: '过去6个月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setMonth(start.getMonth() - 6)
      return [start, end]
    },
  },
]

const mergeCategoryColumn = ({ row, column, rowIndex, columnIndex }: any) => {
  if (column.property === 'categoryName') {

    const currentCode = row.categoryCode;
    let rowspan = 1;
    let colspan = 1;

    // 如果不是第一行，判断是否和上一行相同，若相同则 return [0, 0]
    if (rowIndex > 0) {
      const prevRow = dataList.value[rowIndex - 1];
      if (prevRow.categoryCode == currentCode) {
        return [0, 0];
      }
    }

    // 向下遍历，看还能合并几行
    for (let i = rowIndex + 1; i < dataList.value.length; i++) {
      if (dataList.value[i].categoryCode == currentCode) {
        rowspan++;
      } else {
        break;
      }
    }

    return [rowspan, colspan];
  }
};


interface SummaryParam {
  columns: TableColumnCtx<any>[]
  data: any[]
}

const getSummaries = ({ columns, data }: SummaryParam): (string | number | VNode)[] => {
  const sums: (string | number | VNode)[] = []

  if (!data || data.length === 0) {
    return sums
  }

  columns.forEach((column, index) => {
    const prop = column.property as string

    if (index === 0) {
      sums[index] = h('div', {style: {textDecoration: 'underline'}}, ['合计',])
    } else if (index === 1 || index === 2) {
      sums[index] = ''
    } else if (prop && typeof data[0][prop] === 'number') {
      const total = data.reduce((sum, row) => {
        const value = row[prop]
        return sum + (typeof value === 'number' ? value : 0)
      }, 0)
      sums[index] = formatAmountEmpty(total) // 👈 在这里格式化
    } else {
      sums[index] = ''
    }
  })

  return sums
}




// 直接传入函数（不需要再包装成 pickerOptions 对象）
function disabledDate(time: Date): boolean {
  const year = time.getFullYear();
  const month = time.getMonth() + 1;
  return !(year === currYear && month === currMonth);
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  queryParams.value. belongDateRange = [getCurrentYearMonth(), getCurrentYearMonth()];
  handleQuery();
}

function getList() {
  loading.value = true;
  fetchPage(queryParams.value).then((response: any) => {
    dataList.value = response.data.records.records;
    monthlyList.value = response.data.monthList;
    total.value = response.data.records.total;
    loading.value = false;
  });
}


function handleProvision() {
  id.value = undefined;
  title.value = "计提折旧";
  open.value = true;
}

function handleSelectionChange(selection: any) {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item: any) => item.id);
}

function cancel(): any {
  open.value = false;
  resetForm();
}

function resetForm() {
  form.value = {
    voucherDate: `${currBookStore.termCurrent}-01`,
    wordHead: "记",
    summary: "计提折旧费用"
  }
  formRef?.value?.resetFields();
}

function submitForm() {
  generateProvision(form.value).then((response: any) => {
      if (response.code === 0) {
        open.value = false;
        getList()
        modal.msgSuccess("操作成功")
      } else {
        modal.msgError(response.message);
      }
  })
}

function getCurrentYearMonth() {
  return currBookStore.termCurrent;
}

getList()
</script>

<style lang="scss" scoped>
.btn-form {
  margin-bottom: 10px;
}

.common-card {
  margin-bottom: 10px;
}

.app-container {
  padding: 0;
  background-color: #f5f7fa;
}
</style>
