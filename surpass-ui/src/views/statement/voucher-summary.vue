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
                value-format="YYYY-MM"
                format="YYYY年MM期"
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
<!--          <el-form-item>-->
<!--            <el-checkbox @change="defaultExpandAll" :model-value="showTreeAll" label="展开所有层级"/>-->
<!--          </el-form-item>-->
          <el-form-item>
            <el-button @click="handleQuery">刷新</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="common-card">
      <div class="btn-form">
        <div class="btn-form-right">
          <el-button @click="handleExport">导出</el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="balanceSheetList"
                default-expand-all
                row-key="id"
                border
                show-summary
                :summary-method="handleSummaryMethod2"
                :expand-row-keys="expandsIds"
                :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
        <el-table-column label="科目编码" align="left" header-align="center" prop="subjectCode">
        </el-table-column>
        <el-table-column label="科目名称" align="left" header-align="center"  prop="subjectName">
          <template #default="scope">
            <div :style="{'text-indent': getSubjectIndent(scope.row.subjectCode) + 'em'}">
              {{ scope.row.subjectName }}
            </div>
          </template>
        </el-table-column>
        <el-table-column label="借方金额" align="right" header-align="center"  prop="currentPeriodDebit">
          <template #default="scope">
            {{ formatAmount(scope.row.currentPeriodDebit, '') }}
          </template>
        </el-table-column>
        <el-table-column label="贷方金额" align="right" header-align="center"  prop="currentPeriodCredit">
          <template #default="scope">
            {{ formatAmount(scope.row.currentPeriodCredit, '') }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center"  fixed="right" width="100">
          <template #default="scope">
            <el-tooltip content="查看明细账">
              <router-link :to="`/voucher/sub-ledger?subjectCode=${scope.row.subjectCode}`">
                <el-button type="primary" link icon="View" ></el-button>
              </router-link>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup name="ReportBalanceSheet" lang="ts">
import * as reportApis from "@/api/system/statement/statement";
import {parseTime, getCurrentQuarter, handleTree} from '@/utils/Surpass'
import {computed, h, ref, shallowRef, reactive, toRefs} from 'vue'
import {downloadData, formatAmount} from "@/utils"
import {getSubjectAllNodeIds, getSubjectIndent, handleSummaryMethod, SummaryMethodProps} from "@/utils/Subjects"
import booksSetStore from "@/store/modules/bookStore";

const currBookStore = booksSetStore()
const loading = ref(true);
const showSearch = ref(true);
const balanceSheetList = ref([]);
const recordsList = ref<any>([]);
const expandsIds = ref([]);
const showTreeAll = computed(() => {
  return expandsIds.value.length >= recordsList.value.length
})
const data = reactive({
  form: {},
  queryParams: {
    periodType: 'month',
    date: currBookStore.termCurrent,
    reportQuarter: getCurrentQuarter(),
    reportDate: currBookStore.termCurrent,
  },
});

const {queryParams} = toRefs(data);

const customPrefix = shallowRef({
  render() {
    return h('p', '年')
  },
})
const handlePeriodType = (value: string) => {
  if (value === 'month' && queryParams.value.date.length < 7) {
    queryParams.value.date = queryParams.value.date + "-01"
  }
  handleQuery()
}
const disabledDate = (time: any) => {
  const now = new Date(currBookStore.termCurrent + "-01")
  const start = new Date(currBookStore.termStart + "-01")
  return start.getTime() > time.getTime() || time.getTime() > now.getTime();
}

/** 查询凭证记录列表 */
function getList() {
  loading.value = true;
  reportApis.selectGroupVoucherSummary(queryParams.value).then((response: any) => {
    //balanceSheetList.value = handleTree(response.data, "sourceId", "parentId", "children")
    balanceSheetList.value = response.data;
    loading.value = false;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  if (queryParams.value.periodType === 'quarter') {
    queryParams.value.reportDate = queryParams.value.date.substring(0, 4) + ' ' + queryParams.value.reportQuarter
  } else {
    queryParams.value.reportDate = queryParams.value.date
  }
  getList();
}

function handleExport() {
  reportApis.voucherSummaryExport(queryParams.value).then((data: any) => {
    downloadData(data, `凭证汇总表${queryParams.value.reportDate} ` + parseTime(new Date()) + ".xlsx")
  })
}

function defaultExpandAll() {
  if (expandsIds.value.length === 0) {
    expandsIds.value = getSubjectAllNodeIds(recordsList.value);
  } else {
    expandsIds.value = []
  }
}

function handleSummaryMethod2(param: SummaryMethodProps) {
  return handleSummaryMethod(param, balanceSheetList.value)
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
