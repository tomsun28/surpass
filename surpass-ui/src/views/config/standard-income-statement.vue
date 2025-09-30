<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="会计准则">
            <el-select v-model="queryParams.standardId" @change="handleQuery" style="width: 200px">
              <el-option
                  v-for="item in standardList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="common-card">
      <el-table v-loading="loading" :data="statementIncomeList" border
                :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
                row-key="id" default-expand-all
                height="650"
                @cell-mouse-enter="cellMouseEnter"
                @cell-mouse-leave="cellMouseLeave">
        <el-table-column label="编码" align="left" header-align="left" prop="itemCode" width="100"></el-table-column>
        <el-table-column label="项目" align="left" header-align="center" prop="itemName" width="500">
          <template #default="scope">
            <span :style="{'text-indent': scope.row.level + 'em',
             display: 'inline-block', 'margin-right': '30px'}">
              {{ scope.row.itemName }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="行次" align="center" prop="sortIndex" width="100"/>
        <el-table-column label="计算" align="center" prop="symbol" width="100"/>
        <el-table-column  label="操作" align="center" header-align="center" width="160" prop="sortIndex">
          <template #default="scope">
           <el-tooltip content="新增">
            <el-button type="primary" link @click="handleAdd(scope.row)" icon="Plus"></el-button>
          </el-tooltip>
          <el-tooltip content="编辑">
            <el-button type="primary" link @click="handleEdit(scope.row)" icon="Edit"></el-button>
          </el-tooltip>
          <el-tooltip content="移除">
            <el-button type="primary" link @click="handleDel(scope.row, 'asset')" icon="Delete"></el-button>
          </el-tooltip>
          </template>
        </el-table-column>
        <template #empty>
          <div class="empty-text">暂无数据</div>
          <el-button @click="handleAdd(null)">立即添加</el-button>
        </template>
      </el-table>
    </el-card>

    <el-dialog v-model="dialog.visible" :close-on-click-modal="false" width="800" style="margin-top: 30vh !important;">
      <template #default>
        <el-form :model="form" :rules="rules" ref="statementIncomeRef" label-width="68px"
                 inline-message>
          <el-form-item v-if="!form.id" label="级别" prop="level">
            <el-radio-group :disabled="!form.parentCode" v-model="form.level">
              <el-radio-button label="本级" :value="level"/>
              <el-radio-button label="下级" :value="level + 1"/>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="编码" prop="itemCode">
            <el-input style="width: 300px" v-model="form.itemCode" placeholder="请输入编码"/>
          </el-form-item>
          <el-form-item label="名称" prop="itemName">
            <el-input style="width: 300px" v-model="form.itemName" placeholder="请输入名称"/>
          </el-form-item>
          <el-form-item label="计算" prop="symbol">
            <el-radio-group v-model="form.symbol">
              <el-radio-button label="加" value="+"/>
              <el-radio-button label="减" value="-"/>
              <el-radio-button label="函数" value="f"/>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="行号" prop="sortIndex">
            <el-input-number :min="1" v-model="form.sortIndex" placeholder="请输入行号"/>
          </el-form-item>
          <el-form-item label="科目标志" prop="subjectFlag">
            <el-select  v-model="form.subjectFlag" placeholder="选择" style="width: 300px">
                  <el-option label="是" value="y"></el-option>
                  <el-option label="否" value="n"></el-option>
                </el-select>
          </el-form-item>
          <el-table v-loading="loading" :data="form.rules" border size="small" v-if="form.subjectFlag=='y'"
                    :cell-class-name="tableCellClassName"
                    @cell-mouse-enter="cellMouseEnter"
                    :row-style="{height: '46px'}">
            <el-table-column label="科目" align="left" header-align="center" prop="subjectCode" width="400">
              <template #default="scope">
                <span v-if="!scope.row.editing || scope.row.columnIndex !== 0">
                  {{ scope.row.subjectCode+" "+subjectKeyIdItem[scope.row.subjectCode]?.displayName }}
                </span>
                <el-cascader v-else style="width: 100%" filterable
                             v-model="scope.row.subjectCode"
                             :options="subjectList"
                             :props="cascaderSubjectProps"
                             @change="handleSubjectChange(scope, $event)"
                             :filter-method="cascaderSubjectProps.filterMethod"
                             @visible-change="handleSubjectVisibleChange"/>
              </template>
            </el-table-column>
            <el-table-column label="计算" align="center" prop="symbol" width="100">
              <template #default="scope">
                <span v-if="!scope.row.editing || scope.row.columnIndex !== 1">
                 {{ scope.row.symbol }}
                </span>
                <el-select v-else v-model="scope.row.symbol" placeholder="选择" @blur="closeEditAll">
                  <el-option label="+" value="+"></el-option>
                  <el-option label="-" value="-"></el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="取数规则" align="center" prop="rule">
              <template #default="scope">
                 <span v-if="!scope.row.editing || scope.row.columnIndex !== 2">
                   <dict-tag :options="account_income_balance_type" :value="scope.row.rule"></dict-tag>
                </span>
                <el-select v-else v-model="scope.row.rule" placeholder="选择" @blur="closeEditAll">
                  <el-option
                      v-for="dict in account_income_balance_type"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                  />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="110">
              <template #default="scope">
                <el-popconfirm title="确认删除吗？" @confirm="form.rules.splice(scope.$index, 1)">
                  <template #reference>
                    <el-button size="small" icon="Delete"></el-button>
                  </template>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>
          <el-button  icon="Plus" style="width: 100%" @click="form.rules.push({})" v-if="form.subjectFlag=='y'"></el-button>
        </el-form>
      </template>
      <template #footer>
        <div style="flex: auto">
          <el-button @click="dialog.visible = false">{{ t('org.cancel') }}</el-button>
          <el-button :loading="buttonLoading" type="primary" @click="submitForm">{{ t('org.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="ReportBalanceSheet" lang="ts">
import * as reportApis from "@/api/system/statement/statement";
import {getCurrentQuarter, parseTime} from '@/utils/Jinbooks'
import {getCurrentInstance, h, ref, shallowRef, reactive, toRefs, VNode} from 'vue'
import {formatAmount} from "@/utils";
import bookStore from "@/store/modules/bookStore";
import {ElForm, FormInstance, TableColumnCtx} from "element-plus";
import {cascaderSubjectProps} from "@/utils/Subjects"
import * as standardStatementIncome from "@/api/system/standard/standard-statement-income";
import {getSubjectBalance, selectGroupIncome} from "@/api/system/statement/statement";
import {useI18n} from "vue-i18n";
import DictTag from "@/components/DictTag/index.vue";
import * as subjectApi from "@/api/system/standard/standard-subject";
import {listStandardsAll} from "@/api/system/standard/standard";

const {t} = useI18n()
const {proxy} = getCurrentInstance();
const {account_income_balance_type} = proxy?.useDict("account_income_balance_type");
const currBookStore = bookStore()
// 会计科目数据
const subjectList = ref<any>([])
const statementIncomeList = ref<any>([]);
const subjectKeyIdItem = ref<any>({})
const loading = ref(true);
const buttonLoading = ref(false);
const showSearch = ref(true);
const level = ref(1);
const visibleSubjectStatus = ref(false);
//会计准则
const standardList: any = ref<any>([]);
const dialog = reactive<any>({
  visible: false,
  title: ''
});
const statementIncomeRef = ref<FormInstance>();
const initFormData: any = {
  bookId: currBookStore.bookId,
  parentCode: undefined,
  yearPeriod: parseTime(new Date(), "{y}-{m}"),
  periodType: "month",
  assetOrLiability: "asset",
  sortIndex: 1,
  level: 1,
  itemName: "",
  rules: [{
    subjectCode: undefined,
  }]
}
const data = reactive({
  form: {...initFormData},
  queryParams: {
    periodType: 'month',
    standardId:'',
    date: parseTime(new Date(), "{y}-{m}"),
    reportQuarter: getCurrentQuarter(),
    reportDate: parseTime(new Date(), "{y}-{m}"),
  },
  rules: {
    itemCode: [
      {required: true, message: '编码不能为空', trigger: 'blur'}
    ],
    itemName: [
      {required: true, message: '名称不能为空', trigger: 'blur'}
    ],
  }
});

const {queryParams, form, rules} = toRefs(data);

const customPrefix = shallowRef({
  render() {
    return h('p', '年')
  },
})

const disabledDate = (time: any) => {
  const now = new Date();
  return time.getTime() > now.getTime(); // 禁用过去的日期
}

/** 查询列表 */
function getList() {
  loading.value = true;
  standardStatementIncome.listConfigIncome(queryParams.value.standardId).then((response: any) => {
    statementIncomeList.value = response.data
    loading.value = false;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  getList();
}

function handleExport() {

}

function closeEditAll() {
  form.value.rules.forEach((item: any) => {
    item.editing = false
  })
}

const cellMouseEnter = (row: any, column: any, cell: HTMLTableCellElement, event: Event) => {
  if (!visibleSubjectStatus.value) {
    closeEditAll()
    row.columnIndex = column.index
    row.editing = true
  }
  event.stopPropagation()
}

const cellMouseLeave = (row: any, column: any, cell: HTMLTableCellElement, event: Event) => {
  row.editing = false
}

// 更新会计科目ID关联
const updateSubjectKeys = (items: any) => {
  for (let valueKey in items) {
    const item = items[valueKey]
    subjectKeyIdItem.value[item.code] = item
    if (item.children && item.children.length > 0) {
      updateSubjectKeys(item.children)
    }
  }
}

function getSubjectList() {
  //传入当前账套ID
  subjectApi.getTree({ standardId:queryParams.value.standardId}).then((res: any) => {
    subjectList.value = res.data
    updateSubjectKeys(subjectList.value)
  })
}

const handleSubjectChange = (scope: any, value: any) => {
  const subject = subjectKeyIdItem.value[scope.row.subjectCode]
  scope.row.subjectId = subject.id

  getSubjectBalance({
    bookId: currBookStore.bookId,
    subjectCode: scope.row.subjectCode,
    yearPeriod: queryParams.value.reportDate,
  }).then((res: any) => {
    scope.row.closingBalance = res.data?.balance
    scope.row.openingYearBalance = (res.data?.openingYearBalanceDebit || 0) - (res.data?.openingYearBalanceCredit || 0)
  })
}

const handleSubjectVisibleChange = (show: any) => {
  if (show) {
    visibleSubjectStatus.value = true
    setTimeout(() => {
      visibleSubjectStatus.value = false
    }, 500)
  }
}

/** 表单重置 */
const reset = () => {
  form.value = {...initFormData};
  statementIncomeRef.value?.resetFields();
};

function handleAdd(row?: any) {
  reset();
  if (row) {
    form.value.parentCode = row.itemCode;
    form.value.level = row.level || 1;
    form.value.sortIndex = row.sortIndex + 1 || 1;
    level.value = form.value.level
  }
  
  dialog.visible = true;
  dialog.title = "添加";
}

function handleEdit(row: any) {
  reset();
  standardStatementIncome.getConfigIncome(queryParams.value.standardId,row.itemCode).then((res: any) => {
    form.value = res.data
    if (!form.value.rules || form.value.rules.length === 0) {
      form.value.rules = [{}]
    }
    dialog.visible = true;
    dialog.title = "修改";
  })
}

/**
 * 删除
 * @param row
 * @param assetOrLiability
 */
function handleDel(row: any, assetOrLiability?: string) {
  standardStatementIncome.delConfigIncome(row.id).then(() => {
    proxy?.$modal.msgSuccess('删除成功');
    getList();
  })
}

/** 提交按钮 */
const submitForm = () => {
  statementIncomeRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      form.value.rules = form.value.rules.filter((item: any) => {
        return item.subjectCode && item.symbol && item.rule
      })
      form.value.standardId =form.value.standardId||queryParams.value.standardId;

      buttonLoading.value = true;
      await standardStatementIncome.saveConfigIncome(form.value).finally(() => buttonLoading.value = false);
      proxy?.$modal.msgSuccess('操作成功');
      dialog.visible = false;
      await getList();
    }
  });
};

function tableCellClassName({row, column, rowIndex, columnIndex}: any) {
  //注意这里是解构
  //利用单元格的 className 的回调方法，给行列索引赋值
  row.index = rowIndex;
  column.index = columnIndex;
  return ""
}

/*获取准则列表*/
function getStandardList() {
  listStandardsAll({}).then((res: any) => {
    if (res.code === 0) {
      if (Array.isArray(res.data) && res.data.length > 0) {
        standardList.value = res.data;
        queryParams.value.standardId = standardList.value[0].id;
      } else {
        // 如果数据为空数组时，确保有默认处理逻辑
        standardList.value = [];
        queryParams.value.standardId = ''; // 或设置为适当的默认值
      }
      getSubjectList();
      getList(); // 确保在赋值完成后调用
    }
  });
}

getStandardList();

</script>

<style lang="scss" scoped>
.app-container {
  padding: 0;
  background-color: #f5f7fa;
}

</style>
