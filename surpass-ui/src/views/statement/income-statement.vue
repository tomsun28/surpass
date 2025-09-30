<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="" prop="periodType">
            <el-radio-group v-model="queryParams.periodType" @change="handlePeriodType">
              <el-radio-button label="月度" value="month"></el-radio-button>
              <el-radio-button label="季度" value="quarter"></el-radio-button>
              <el-radio-button label="年度" value="year"></el-radio-button>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="选择月度" prop="reportDate"
                        v-if="queryParams.periodType === 'month'">
            <el-date-picker
                style="width: 130px"
                v-model="queryParams.date"
                type="month"
                :clearable="false"
                format="YYYY年MM期"
                value-format="YYYY-MM"
                :disabled-date="disabledDate"
                @change="handleQuery"
                placeholder="选择月">
            </el-date-picker>
          </el-form-item>

          <el-form-item label="选择季度" prop="reportDate"
                        v-if="queryParams.periodType === 'quarter'">
            <el-date-picker
                style="width: 100px"
                v-model="queryParams.date"
                :clearable="false"
                type="year"
                value-format="YYYY"
                :disabled-date="disabledDate"
                :prefix-icon="customPrefix"
                @change="handleQuery"
                placeholder="选择年">
            </el-date-picker>
            <el-radio-group style="margin-left: 10px"
                            v-model="queryParams.reportQuarter" @change="handleQuery">
              <el-radio-button label="第一季度" value="Q1"></el-radio-button>
              <el-radio-button label="第二季度" value="Q2"></el-radio-button>
              <el-radio-button label="第三季度" value="Q3"></el-radio-button>
              <el-radio-button label="第四季度" value="Q4"></el-radio-button>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="选择年度" prop="reportDate"
                        v-if="queryParams.periodType === 'year'">
            <el-date-picker
                @change="handleQuery"
                v-model="queryParams.date"
                type="year"
                style="width: 100px"
                :clearable="false"
                :disabled-date="disabledDate"
                value-format="YYYY"
                placeholder="选择年">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleQuery">刷新</el-button>
          </el-form-item>
<!--          <el-form-item>-->
<!--            <el-button type="primary" @click="handleGenerate">生成</el-button>-->
<!--          </el-form-item>-->
        </el-form>
      </div>
    </el-card>
    <el-card class="common-card">
      <div class="btn-form">
<!--        <el-button type="danger" @click="handleDel">删除</el-button>-->
        <el-button :type="ableEdit ? 'warning' : 'primary'" @click="doConfig">
          {{ ableEdit ? '停止编辑' : '启用编辑' }}
        </el-button>
        <div class="btn-form-right">
          <el-button @click="handleExport">导出</el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="statementIncomeList" border
                :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
                row-key="id" default-expand-all
                height="610">
        <el-table-column label="项目" align="left" header-align="center" prop="itemName" width="500">
          <template #default="scope">
            <span :style="{'text-indent': scope.row.level + 'em',
             display: 'inline-block', 'margin-right': '30px'}">
              {{ scope.row.itemName }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="行次" align="center" prop="sortIndex" width="100"/>
        <el-table-column label="本月金额" align="right" header-align="center" width="160" prop="currentBalance">
          <template #default="scope">
            {{ formatAmount(scope.row.currentBalance, '') }}
          </template>

        </el-table-column>
        <el-table-column label="本年累计金额" align="right" header-align="center" width="160" prop="cumulativeBalance">
          <template #default="scope">
            {{ formatAmount(scope.row.cumulativeBalance, '') }}
          </template>
        </el-table-column>
        <el-table-column v-if="ableEdit" label="操作" align="center" header-align="center" width="160"
                         prop="cumulativeBalance">
          <template #default="scope">
            <!--
           <el-tooltip content="新增/编辑">
            <el-button type="primary" link @click="handleAdd(scope.row)" icon="Plus"></el-button>
          </el-tooltip>
          -->
            <el-tooltip content="编辑">
              <el-button type="primary" link @click="handleEdit(scope.row)" icon="Edit"></el-button>
            </el-tooltip>
            <!--
            <el-tooltip content="移除">
              <el-button type="primary" link @click="handleDel(scope.row, 'asset')" icon="Delete"></el-button>
            </el-tooltip>
            -->
          </template>
        </el-table-column>
        <template #empty>
          <div class="empty-text">暂无数据</div>
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
            <el-input style="width: 300px" v-model="form.itemCode" placeholder="请输入编码" disabled/>
          </el-form-item>
          <el-form-item label="名称" prop="itemName">
            <el-input style="width: 300px" v-model="form.itemName" placeholder="请输入名称"/>
          </el-form-item>
          <el-form-item  label="计算" prop="symbol">
            <el-radio-group v-model="form.symbol">
              <el-radio-button label="加" value="+"/>
              <el-radio-button label="减" value="-"/>
              <el-radio-button label="函数" value="f"/>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="行号" prop="sortIndex">
            <el-input-number :min="1" v-model="form.sortIndex" placeholder="请输入行号"/>
          </el-form-item>

          <el-table v-loading="loading" :data="form.rules" border size="small"
                    :cell-class-name="tableCellClassName"
                    @cell-click="cellMouseEnter"
                    :row-style="{height: '46px'}">
            <el-table-column label="科目" align="left" header-align="center" prop="subjectCode" width="200">
              <template #default="scope">
                <span v-if="!scope.row.editing || scope.row.columnIndex !== 0">
                  {{ scope.row.subjectCode + " " + subjectKeyIdItem[scope.row.subjectCode]?.displayName }}
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
            <el-table-column label="期末余额" align="right" header-align="center" prop="closingBalance">
              <template #default="scope">
                <span>{{ formatAmount(scope.row.closingBalance, '') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="年初余额" align="right" header-align="center" prop="openingYearBalance">
              <template #default="scope">
                <span>{{ formatAmount(scope.row.openingYearBalance, '') }}</span>
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
          <el-button icon="Plus" style="width: 100%" @click="form.rules.push({})"></el-button>
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
import * as incomeApis from "@/api/system/statement/statement-income";

import {getCurrentQuarter, parseTime} from '@/utils/Jinbooks'
import {getCurrentInstance, h, ref, shallowRef, reactive, toRefs} from 'vue'
import {downloadData, formatAmount} from "@/utils";
import bookStore from "@/store/modules/bookStore";
import {ElForm, FormInstance, TableColumnCtx} from "element-plus";
import {cascaderSubjectProps} from "@/utils/Subjects"
import {
  saveConfigIncome,
  getConfigIncome,
  listConfigIncome,
  delConfigIncome
} from "@/api/system/statement/statement-income-config";
import {getSubjectBalance} from "@/api/system/statement/statement";
import {useI18n} from "vue-i18n";
import DictTag from "@/components/DictTag/index.vue";
import * as subjectApi from "@/api/system/standard/standard-subject";

const {t} = useI18n()
const {proxy} = getCurrentInstance();
const {account_income_balance_type} = proxy?.useDict("account_income_balance_type");
const currBookStore = bookStore()
const ableEdit = ref(false);
// 会计科目数据
const subjectList = ref<any>([])
const statementIncomeList = ref<any>([]);
const subjectKeyIdItem = ref<any>({})
const loading = ref(true);
const buttonLoading = ref(false);
const showSearch = ref(true);
const level = ref(1);
const visibleSubjectStatus = ref(false);
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
  rules: []
}
const data = reactive({
  form: {...initFormData},
  queryParams: {
    periodType: 'month',
    date: currBookStore.termCurrent,
    reportQuarter: getCurrentQuarter(),
    reportDate: currBookStore.termCurrent,
  },
  rules: {
    yearPeriod: [
      {required: true, message: '期间不能为空', trigger: 'blur'}
    ],
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
  const now = new Date(currBookStore.termCurrent + "-01")
  const start = new Date(currBookStore.termStart + "-01")
  return start.getTime() > time.getTime() || time.getTime() > now.getTime();
}

function doConfig() {
  ableEdit.value = !ableEdit.value
  getList();
}

/** 查询列表 */
function getList() {
  loading.value = true;
  statementIncomeList.value = [];
  if (ableEdit.value == false) {
    incomeApis.selectIncome(queryParams.value).then((response: any) => {
      if (response.data && response.data.items) {
        statementIncomeList.value = response.data.items
      }

    });
  } else {
    listConfigIncome("").then((response: any) => {
      if (response.data) {
        statementIncomeList.value = response.data
      }
    });
  }
  loading.value = false;
}

/** 搜索按钮操作 */
function handleQuery() {
  if (queryParams.value.periodType === '季') {
    queryParams.value.reportDate = queryParams.value.date + ' ' + queryParams.value.reportQuarter
  } else {
    queryParams.value.reportDate = queryParams.value.date
  }
  getList();
}

function handleExport() {
  incomeApis.incomeExport(queryParams.value).then((data: any) => {
    downloadData(data, `利润表${queryParams.value.reportDate} ` + parseTime(new Date()) + ".xlsx")
  })
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
  subjectApi.getTree({
    bookId: currBookStore.bookId
  }).then((res: any) => {
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

function handleEdit(row: any) {
  reset();
  getConfigIncome(row.itemCode).then((res: any) => {
    form.value = res.data
    if (!form.value.rules || form.value.rules.length === 0) {
      form.value.rules = []
    }
    dialog.visible = true;
    dialog.title = "修改";
  })
}

/** 提交按钮 */
const submitForm = () => {
  statementIncomeRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      form.value.rules = form.value.rules.filter((item: any) => {
        return item.subjectCode && item.symbol && item.rule
      })
      buttonLoading.value = true;
      await saveConfigIncome(form.value).finally(() => buttonLoading.value = false);
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

const handlePeriodType = (value: string) => {
  if (value === 'month' && queryParams.value.date.length < 7) {
    queryParams.value.date = queryParams.value.date + "-01"
  } else if (value === 'quarter' || value === 'halfYear') {
    queryParams.value.date = queryParams.value.date.substring(0, 4)
    if (value === 'halfYear') {
      queryParams.value.reportQuarter = 'H1'
      queryParams.value.reportDate = queryParams.value.date + ' H1'
    } else if (value === 'quarter') {
      queryParams.value.reportQuarter = 'Q1'
      queryParams.value.reportDate = queryParams.value.date + ' Q1'
    }
  } else if (value === 'year') {
    queryParams.value.date = queryParams.value.date.substring(0, 4)
  }
  handleQuery()
}

getSubjectList();

getList();
</script>

<style lang="scss" scoped>
.app-container {
  padding: 0;
  background-color: #f5f7fa;
}

.btn-form {
  margin-bottom: 10px;
}

.common-card {
  margin-bottom: 15px;
}
</style>
