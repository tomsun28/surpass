<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="100px">
          <el-form-item label="显示辅助核算" prop="showAux">
            <el-switch v-model="queryParams.showAux" @change="handleQuery"/>
          </el-form-item>

<!--          <el-form-item label="显示所有科目" prop="showAll">-->
<!--            <el-switch v-model="queryParams.showAll" @change="handleQuery"/>-->
<!--          </el-form-item>-->

          <el-form-item label="" prop="defaultExpandAll">
            <el-checkbox @change="defaultExpandAll" :model-value="showTreeAll" label="展开所有层级"/>
          </el-form-item>

          <el-form-item label="" prop="periodType">
            <el-radio-group v-model="queryParams.periodType" @change="handlePeriodType">
              <el-radio-button label="月度" value="month"></el-radio-button>
              <!--              <el-radio-button label="季度报表" value="季"></el-radio-button>-->
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
                placeholder="选择季度">
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

        </el-form>
      </div>
    </el-card>
    <el-card class="common-card">
      <div class="btn-form">
        <div class="btn-form-right">
          <el-button @click="handleExport">导出</el-button>
          <el-button type="danger" @click="recoverInitial" v-if="queryParams.periodType === 'month'" >恢复期初</el-button>
        </div>
      </div>
      <el-table ref="tableRef" v-loading="loading" :data="recordsList"
                :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
                :expand-row-keys="expandsIds"
                show-summary
                :summary-method="handleSummaryMethod2"
                row-key="sourceId" height="590">
        <el-table-column label="科目编码" align="left" prop="subjectCode" width="250" show-overflow-tooltip/>
        <el-table-column label="科目名称" align="left" prop="subjectName" width="300" show-overflow-tooltip>
          <template #default="scope">
            <div :style="{'text-indent': getSubjectIndent(scope.row.subjectCode) + 'em'}">
              {{ scope.row.subjectName }}
            </div>
          </template>
        </el-table-column>
        <el-table-column label="期初余额" align="center">
          <el-table-column prop="openingBalanceDebit" align="center" label="借方">
            <template #default="scope">
              {{ formatAmount(scope.row.openingBalanceDebit, '') }}
            </template>
          </el-table-column>
          <el-table-column prop="openingBalanceCredit" align="center" label="贷方">
            <template #default="scope">
              {{ formatAmount(scope.row.openingBalanceCredit, '') }}
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="本期发生额" align="center">
          <el-table-column prop="currentPeriodDebit" align="center" label="借方">
            <template #default="scope">
              {{ formatAmount(scope.row.currentPeriodDebit, '') }}
            </template>
          </el-table-column>
          <el-table-column prop="currentPeriodCredit" align="center" label="贷方">
            <template #default="scope">
              {{ formatAmount(scope.row.currentPeriodCredit, '') }}
            </template>
          </el-table-column>
        </el-table-column>

        <el-table-column label="本年累计发生额" align="center">
          <el-table-column prop="yearToDateDebit" align="center" label="借方">
            <template #default="scope">
              {{ formatAmount(scope.row.yearToDateDebit, '') }}
            </template>
          </el-table-column>
          <el-table-column prop="yearToDateCredit" align="center" label="贷方">
            <template #default="scope">
              {{ formatAmount(scope.row.yearToDateCredit, '') }}
            </template>
          </el-table-column>
        </el-table-column>

        <el-table-column label="期末余额" align="center">
          <el-table-column prop="closingBalanceDebit" align="center" label="借方">
            <template #default="scope">
              <div :style="{backgroundColor: scope.row.closingBalanceDebit >= 0 ? '' : 'bisque'}">
                 {{ formatAmount(scope.row.closingBalanceDebit, '') }}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="closingBalanceCredit" align="center" label="贷方">
            <template #default="scope">
               <div :style="{backgroundColor: scope.row.closingBalanceCredit >= 0 ? '' : 'bisque'}">
                 {{ formatAmount(scope.row.closingBalanceCredit, '') }}
              </div>
            </template>
          </el-table-column>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup name="ReportIncomeStatement" lang="ts">
import * as reportApis from "@/api/system/statement/statement";
import {getCurrentQuarter, handleTree, parseTime} from '@/utils/Jinbooks'
import {getSubjectIndent, getSubjectAllNodeIds, handleSummaryMethod, SummaryMethodProps} from '@/utils/Subjects'
import {getCurrentInstance,h, reactive, ref, shallowRef, toRefs, computed, VNode} from 'vue'
import {downloadData, formatAmount} from "@/utils"
import booksSetStore from "@/store/modules/bookStore";
import Decimal from "decimal.js";

const booksSet = booksSetStore()
const recordsList = ref([]);
const recordsAllList = ref<any>([]);
const loading = ref(true);
const showSearch = ref(true);
const expandsIds = ref([]);
const {proxy} = getCurrentInstance();

const showTreeAll = computed(() => {
  return expandsIds.value.length >= recordsList.value.length
})

const data = reactive({
  form: {},
  queryParams: {
    bookId: booksSet.bookId,
    periodType: 'month',
    date: booksSet.termCurrent,
    reportQuarter: getCurrentQuarter(),
    reportDate: booksSet.termCurrent,
    showAux: false,
    showAll: true,
  },
});

const {queryParams} = toRefs(data);

const customPrefix = shallowRef({
  render() {
    return h('p', '年')
  },
})

const disabledDate = (time: any) => {
  const now = new Date(booksSet.termCurrent + "-01")
  const start = new Date(booksSet.termStart + "-01")
  return start.getTime() > time.getTime() || time.getTime() > now.getTime();
}

/** 查询列表 */
function getList() {
  loading.value = true;
  reportApis.selectGroupSubjectBalance(queryParams.value).then((response: any) => {
    recordsList.value = handleTree(response.data, "sourceId", "parentId", "children")
    // expandsIds.value = getSubjectAllNodeIds(recordsList.value);
    recordsAllList.value = response.data;
    loading.value = false;
  });
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

const handlePeriodType = (value: any) => {
  if (value === 'month' && queryParams.value.date.length < 7) {
    queryParams.value.date = queryParams.value.date + "-01"
  }
  handleQuery()
}

function handleExport() {
  reportApis.subjectBalanceExport(queryParams.value).then((data: any) => {
    downloadData(data, `科目余额表${queryParams.value.reportDate} ` + parseTime(new Date()) + ".xlsx")
  })
}

function recoverInitial() {
    proxy.$modal.confirm('确认恢复期初余额？').then(function () {
    return reportApis.recoverInitialSubjectBalance(queryParams.value);
  }).then((res: any) => {
    getList();
    proxy.$modal.msgSuccess(res.message);
  }).catch(() => {
  });
}

function defaultExpandAll() {
  if (expandsIds.value.length === 0) {
    expandsIds.value = getSubjectAllNodeIds(recordsList.value);
  } else {
    expandsIds.value = []
  }
}

function handleSummaryMethod2(param: SummaryMethodProps) {
  return handleSummaryMethod(param, recordsList.value, 1, [2, 3, 4, 5, 6, 7, 8, 9])
}

getList();
</script>

<style lang="scss" scoped>
.app-container {
  padding: 0;
  background-color: #f5f7fa;
}

.btn-form-right {
  margin-bottom: 15px;
}
</style>
