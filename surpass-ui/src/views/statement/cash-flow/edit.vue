<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="85%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ props.title }}</h4>
    </template>
    <template #default>
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="88px">
          <el-form-item label="" prop="periodType">
            <el-radio-group v-model="queryParams.cashFlowItemType" @change="handleType">
              <el-radio-button label="主表项目" :value="0"></el-radio-button>
              <el-radio-button label="补充资料项目" :value="1"></el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="所属期间" prop="belongDate">
            <el-date-picker
                v-model="queryParams.belongDateRange"
                type="daterange"
                unlink-panels
                range-separator="至"
                start-placeholder="起始日期"
                end-placeholder="结束日期"
                value-format="YYYY-MM-DD"
            />
          </el-form-item>
          <el-form-item label="现金流量项">
            <el-select v-model="queryParams.cashFlowItemCode" clearable style="width: 240px">
              <el-option
                  v-for="dict in itemList"
                  :key="dict.itemCode"
                  :label="dict.itemName"
                  :value="dict.itemCode"
              />
              <el-option
                  value="no-select"
                  label="不指定"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button @click="handleQuery">{{ t('org.button.query') }}</el-button>
            <el-button @click="resetQuery">{{ t('org.button.reset') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table v-loading="loading" :data="recordsList" border stripe :span-method="objectSpanMethod">
        <el-table-column label="凭证信息" align="center">
          <el-table-column label="日期" align="center" prop="voucherDate" min-width="55" :show-overflow-tooltip="true"/>
          <el-table-column label="凭证字号" align="left" header-align="center" prop="word" min-width="100"
                           :show-overflow-tooltip="true"/>
          <el-table-column label="摘要" align="left" header-align="center" prop="summary" min-width="100"
                           :show-overflow-tooltip="true"/>
          <el-table-column label="科目" align="left" header-align="center" prop="subjectName" min-width="100"
                           :show-overflow-tooltip="true"/>
          <el-table-column label="辅助核算" align="left" header-align="center" prop="auxiliaryLabel" min-width="100"
                           :show-overflow-tooltip="true"/>
          <el-table-column label="借方金额" align="right" prop="debitAmount" min-width="60"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.debitAmount, '') }}
            </template>
          </el-table-column>
          <el-table-column label="贷方金额" align="right" prop="creditAmount" min-width="60"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.creditAmount, '') }}
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="现金流量信息" align="center">
          <el-table-column label="现金流量项" align="center" min-width="100" :show-overflow-tooltip="true">
            <template #default="scope">
              <span v-if="!scope.row.cashFlowItemCode">不指定</span>
              <el-select v-else v-model="scope.row.cashFlowItemCode"
                         style="width: 100%"
                         @change="handleItemCodeChange(scope.row)"
                         placement="bottom">
                <el-option
                    v-for="dict in itemList"
                    :key="dict.itemCode"
                    :label="dict.itemName"
                    :value="dict.itemCode"
                />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="金额" align="center" prop="cashFlowBalance" min-width="60">
            <template #default="scope">
              {{ formatAmount(scope.row.cashFlowBalance, '') }}
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" fixed="right" width="90">
            <template #default="scope">
              <el-button @click="handleUpdate(scope.row)">
                {{ t('org.edit') }}
              </el-button>
            </template>
          </el-table-column>
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

      <edit-form :title="title" :open="open"
                 :type="queryParams.cashFlowItemType"
                 :voucher-id="updateVoucherId"
                 :voucher-date="updateVoucherDate"
                 @dialogOfClosedMethods="dialogOfClosedMethodsDrawer"></edit-form>
    </template>
    <template #footer>
      <div style="flex: auto">
        <el-button @click="dialogOfClosedMethods(false)">{{ t('org.cancel') }}</el-button>
      </div>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import {useI18n} from "vue-i18n";
import * as apis from "@/api/system/voucher/voucher";
import {getCurrentInstance, reactive, ref, toRefs, watch, computed, onMounted} from "vue";
import {useRoute} from "vue-router";
import {formatAmount} from "@/utils";
import {getSelectItem} from "@/api/config/cash-flow-balance";
import modal from "@/plugins/modal";
import {saveItemCodes} from "@/api/system/statement/statement-cash-flow";
import editForm from "./edit-drawer.vue"
import bookStore from "@/store/modules/bookStore";


const {t} = useI18n()
const emit: any = defineEmits(['dialogOfClosedMethods'])
const dialogStatus: any = ref(false);
const {proxy} = getCurrentInstance()!;
const route = useRoute();

const loading = ref(true);
const recordsList = ref<any>([]);
const itemList = ref<any>([]);
const total = ref(0);
const title = ref("");
const id: any = ref(undefined);
const open = ref(false);
const currBookStore = bookStore()


const props: any = defineProps({
  title: {
    type: String,
    default: ""
  },
  open: {
    type: Boolean,
    default: false
  },
  selectedItemCode: {
    type: String,
    default: ""
  },
  selectedType: {
    type: Number,
    default: 0
  },
  dateRange: {
    type: Array,
    default: []
  }
})

const data: any = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    cashFlowItemCode: "",
    pageSizeOptions: [10, 20, 50],
    belongDateRange: ['', ''],
    cashFlowItemType: 0
  }
})

const {queryParams} = toRefs(data);
// 选中的行
const selectedRows = ref([]);
const updateVoucherId: any = ref(undefined);
const updateVoucherDate: any = ref(undefined);
// 格式化日期
const formatDate = (date: Date): string => {
  return date.toISOString().split('T')[0]
}


watch(
    () => props.open,
    (val: any) => {
      if (val) {
        const [startDate, endDate] = props.dateRange
        queryParams.value.belongDateRange = [formatDate(startDate), formatDate(endDate)]
        dialogStatus.value = props.open;
        queryParams.value.cashFlowItemCode = props.selectedItemCode;
        queryParams.value.cashFlowItemType = props.selectedType;
        getCashFlowItem();
        getList();
      } else {
        reset();
      }
    },
    {immediate: true}
);

// 处理行选择变化
const handleSelectionChange = (selection: any) => {
  selectedRows.value = selection
}

function checkIfSameTerm(row: any): boolean {
  const termCurrent = currBookStore.termCurrent;
  const voucherDate = row.voucherDate;
  const voucherYearMonth = voucherDate.substring(0, 7);

  if (termCurrent !== voucherYearMonth) {
    modal.msgError("不能修改非当前账套期间的凭证项的现金流量项");
    return false; // 关键：返回 false，外部可判断后面是否继续执行
  } else {
    return true; // 返回 true，外部可继续执行
  }
}

// 单行数据改变时立即保存
const handleItemCodeChange = async (row: any) => {
  // 调用 checkIfSameTerm
  if (!checkIfSameTerm(row)) {
    return; // 如果不是当前账套期间，直接 return，后续不执行
  }

  try {
    // 准备保存的数据
    const voucherItemCashFlowDtos = [{
      id: row.id,
      voucherItemId: row.voucherItemId,
      cashFlowItemCode: row.cashFlowItemCode,
      cashFlowItemType: queryParams.value.cashFlowItemType,
      voucherDate: row.voucherDate
    }]

    const dataToSave = {
      voucherItemCashFlowDtos: voucherItemCashFlowDtos,
      isEdit: false
    };

    // 调用保存接口
    saveItemCodes(dataToSave).then((res: any) => {
      if (res.code !== 0) {
        modal.msgError(res.message)
      }
    })
  } catch (error: any) {
    modal.msgError(error.message);
    // 如果保存失败，可能需要回滚选择
    row.cashFlowItemCode = ''
  }
}

function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('dialogOfClosedMethods', val);
}

function dialogOfClosedMethodsDrawer(val: any): any{
  open.value = false;
  updateVoucherId.value = undefined;
  updateVoucherDate.value = undefined;
  getList();
}

/** 查询凭证记录列表 */
function getList() {
  loading.value = true;
  apis.fetchByCashFlow(queryParams.value).then((response: any) => {
    recordsList.value = response.data.records;
    total.value = response.data.total;
    loading.value = false;
  });
}

const objectSpanMethod = ({rowIndex, columnIndex}: { rowIndex: number; columnIndex: number }) => {
  if (columnIndex === 0 || columnIndex === 1) { // 仅合并 "日期" (voucherDate) 和 "凭证字号" (word)
    const currentVoucherId = recordsList.value[rowIndex].voucherId;
    let rowSpan = 0;
    if (rowIndex === 0 || recordsList.value[rowIndex - 1].voucherId !== currentVoucherId) {
      for (let i = rowIndex; i < recordsList.value.length; i++) {
        if (recordsList.value[i].voucherId === currentVoucherId) {
          rowSpan++;
        } else {
          break;
        }
      }
      return {rowspan: rowSpan, colspan: 1};
    } else {
      return {rowspan: 0, colspan: 0};
    }
  }  else if (columnIndex === 2 || columnIndex === 3 || columnIndex === 4 || columnIndex === 5 || columnIndex === 6) {
    // 针对相同的voucherItemId合并第三列和第四列
    const currentVoucherItemId = recordsList.value[rowIndex].voucherItemId;
    let rowSpan = 0;
    if (rowIndex === 0 || recordsList.value[rowIndex - 1].voucherItemId !== currentVoucherItemId) {
      for (let i = rowIndex; i < recordsList.value.length; i++) {
        if (recordsList.value[i].voucherItemId === currentVoucherItemId) {
          rowSpan++;
        } else {
          break;
        }
      }
      return {rowspan: rowSpan, colspan: 1};
    } else {
      return {rowspan: 0, colspan: 0};
    }
  }
};

function getCashFlowItem() {
  getSelectItem({cashFlowItemType: queryParams.value.cashFlowItemType}).then((response: any) => {
    if (response.code === 0) {
      itemList.value = response.data.map((item: any) => {
            return item.itemCode === "35-hl-djje" && item.itemName.startsWith("四、")
                ? {...item, itemName: item.itemName.replace(/^四、/, "")}
                : item;
          }
      );
    } else {
      modal.msgError(response.message);
    }
  })
}

function handleUpdate(row: any) {
  open.value = true;
  title.value = "指定现金流量项目"
  updateVoucherId.value = row.voucherId;
  updateVoucherDate.value = row.voucherDate;
}

function reset() {
  queryParams.value = {
    pageNum: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50],
    itemCode: "",
    belongDateRange: ['', ''],
    cashFlowItemType: 0
  }
}

function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  queryParams.value.cashFlowItemCode = "";
  handleQuery();
}

function handleType() {
  getCashFlowItem();
  resetQuery();
}
</script>
