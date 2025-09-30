<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="" prop="periodType">
            <el-radio-group v-model="queryParams.periodType" @change="handlePeriodType">
              <el-radio-button label="月度" value="month"></el-radio-button>
              <el-radio-button label="季度" value="quarter"></el-radio-button>
<!--              <el-radio-button label="年度" value="year"></el-radio-button>-->
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
            <el-button @click="handleQuery" type="primary">刷新</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="common-card">
      <div class="btn-form">
        <el-button
            type="primary"
            @click="selectCashItem"
        >指定现金流量项
        </el-button>
        <div style="margin-left: 20px;line-height: 30px;display: inline-block">
          <i class="iconfont icon-tianping" :class="isBalance ? 'iconfont-balance' : 'iconfont-unbalance'"></i>
          <span class="hint">{{ isBalance ? '平衡' : '不平衡' }}</span>
        </div>
        <el-button
            v-if="!isBalance"
            style="margin-left: 5px;"
            link
            type="primary"
            @click="checkDetails"
        >查看详情
        </el-button>
        <div class="btn-form-right">
          <!--          <el-button type="primary" @click="handlePrint">打印</el-button>-->
          <el-button @click="handleExport">导出</el-button>
        </div>
      </div>
      <el-table v-loading="loading" :data="cashFlowStatementList" border height="600" stripe>
        <el-table-column prop="itemName" label="项目" align="left" min-width="160"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            <span :class="{ 'indented-item': scope.row.isTitle !== 1 }">{{ scope.row.itemName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="sortIndex" label="行次" align="center" min-width="20">
        </el-table-column>
        <el-table-column prop="monthlyAmount" :label="monthlyAmountLabel" align="right" min-width="100"
                         :show-overflow-tooltip="true">
          <template #default="{ row }">
            <template v-if="['1-jy-xjll', '12-tz-xjll', '25-cz-xjll', '39-xj-bczl',
        '40-xj-xjll', '58-xj-tzhd', '62-xj-xjqk'].includes(row.itemCode)">
              <span></span>
            </template>
            <template
                v-else-if="['59-xj-zwzb', '60-xj-gszq', '61-xj-rzzc'].includes(row.itemCode) && queryParams.periodType === 'month'">
              <div style="display: flex;align-items: center;">
                <el-input
                    v-if="row.editing"
                    v-model="row.inputBalance"
                    type="text"
                    @input="validateNumber(row)"
                    @blur="handleBlur(row)"
                    @focus="handleFocus(row)"
                    style="text-align: right"
                    ref="inputRef"
                />
                <span v-else @click="handleClick(row)" class="editable-cell">
                {{ formatBalance(row.monthlyAmount) }}
                </span>
                <el-icon size="15">
                  <EditPen/>
                </el-icon>
              </div>
            </template>
            <template v-else>
        <span
            :class="{ 'clickable-amount': row.isMain === 1 || row.isAdditional === 1 && row.itemCode != '56-xj-qita'}"
            @click="row.isMain === 1 || row.isAdditional === 1 ? handleAmountClick(row) : undefined"
        >
          {{ formatBalance(row.monthlyAmount) }}
        </span>
            </template>
          </template>
        </el-table-column>
        <el-table-column prop="currentAmount" label="本年累计金额" align="right" min-width="100"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            <template v-if="['1-jy-xjll', '12-tz-xjll', '25-cz-xjll', '39-xj-bczl',
            '40-xj-xjll', '58-xj-tzhd', '62-xj-xjqk'].includes(scope.row.itemCode)">
              <span></span>
            </template>
            <!--            <template v-else-if="scope.row.itemCode === '41-xj-jlr'">
                          {{ formatBalance(netProfitYear) }}
                        </template>-->
            <template v-else>
              <span>{{ formatBalance(scope.row.currentAmount) }}</span>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog v-model="dialogOpen" width="700px" append-to-body :title="dialogTitle" :close-on-click-modal="false"
               @close="cancel">
      <el-alert type="warning" show-icon :closable="false">
        <template #default>
          <div class="custom-alert-content">
            <div>{{balanceTitle}}</div>
            <div>现金流量初始余额不平衡, 请<el-button type="primary" link @click="goToCheck">前往检查</el-button></div>
          </div>
        </template>
      </el-alert>
      <div class="balance-check-content">
        <div class="balance-calculation">
          <div class="balance-item">
            <p class="balance-amount" v-if="endingYear !== 0.00">{{formatBalance(endingYear)}}</p>
            <p class="balance-amount" v-else>0.00</p>
            <p class="balance-label">期末现金及现金等价物余额(38行)</p>
          </div>
          <div class="operator">−</div>
          <div class="balance-item">
            <p class="balance-amount" v-if="endingTerm !== 0.00">{{formatBalance(endingTerm)}}</p>
            <p class="balance-amount" v-else>0.00</p>
            <p class="balance-label">现金及现金等价物科目余额</p>
          </div>
          <div class="operator">=</div>

          <div class="balance-item">
            <p class="balance-amount" v-if="diff !== 0.00">{{formatBalance(diff)}}</p>
            <p class="balance-amount" v-else>0.00</p>
            <p class="balance-label">差额</p>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">{{ $t('systemCancel') }}</el-button>
      </div>
    </el-dialog>
    <edit-form :title="title" :open="open"
               :selected-item-code="selectedCode"
               :selected-type="selectedType"
               :date-range="dateRange"
               @dialogOfClosedMethods="dialogOfClosedMethods"></edit-form>
  </div>
</template>

<script setup lang="ts">
import * as statementApis from "@/api/system/statement/statement";
import {useI18n} from "vue-i18n";
import {useRouter} from "vue-router";
import {parseTime, getCurrentQuarter} from '@/utils/Jinbooks'
import {getCurrentInstance, reactive, ref, toRefs, shallowRef, h, computed, nextTick} from "vue";
import editForm from "./cash-flow/edit.vue"
import {formatBalance, validateNumber} from "@/utils/BalanceFormat";
import {getNetProfit} from "@/api/system/statement/statement-income";
import {saveCashFlowItem} from "@/api/system/statement/statement-cash-flow";
import modal from "@/plugins/modal";
import booksSetStore from "@/store/modules/bookStore";
import {downloadData} from "@/utils";

const router = useRouter();
const currBookStore = booksSetStore()
const {proxy} = getCurrentInstance()!;
const {t} = useI18n()
const cashFlowStatementList = ref([]);
const loading = ref(true);
const showSearch = ref(true);
const title = ref("");
const id: any = ref(undefined);
const open = ref(false);
const selectedCode = ref("")
const selectedType = ref(0)
const inputRef = ref<any>(null);
const netProfitCurrent = ref<number>(0)
const netProfitYear = ref<number>(0)
const dialogOpen = ref(false);
const dialogTitle = ref("");
const balanceTitle = ref("");
const isBalance = ref(true);
const endingYear = ref(0.00);
const endingTerm = ref(0.00);
const diff = ref(0.00);



const data = reactive({
  form: {},
  queryParams: {
    periodType: 'month',
    date: currBookStore.termCurrent,
    reportQuarter: getCurrentQuarter(),
    reportDate: currBookStore.termCurrent,
  },
});

const {queryParams, form} = toRefs(data);

const customPrefix = shallowRef({
  render() {
    return h('p', '年')
  },
})

const monthlyAmountLabel = computed(() => {
  if (queryParams.value.periodType === 'month') {
    return "本月金额"
  } else if (queryParams.value.periodType === 'quarter') {
    return "本季金额"
  }
  return "本年金额";
})

const dateRange = computed<[Date, Date]>(() => {
  const {periodType, date, reportQuarter} = queryParams.value
  let start: Date, end: Date

  if (periodType === 'month') {
    // 如果 date 中带有 '-', 就当做 'YYYY-MM'，否则给一个默认当月
    const [y, m] = date.includes('-')
        ? date.split('-').map(Number)
        : [new Date().getFullYear(), new Date().getMonth() + 1]
    start = new Date(y, m - 1, 1, 12)
    end = new Date(y, m, 0, 12)
  } else if (periodType === 'quarter') {
    // 如果 date 是 'YYYY-MM'，先取前面的年份部分
    const y = Number(date.split('-')[0])
    const q = Number((reportQuarter || 'Q1').slice(1))
    const startMonth = (q - 1) * 3
    start = new Date(y, startMonth, 1, 12)
    end = new Date(y, startMonth + 3, 0, 12)
  } else { // year
    const y = Number(date.split('-')[0])
    start = new Date(y, 0, 1, 12)
    end = new Date(y + 1, 0, 0, 12)
  }

  return [start, end]
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
  statementApis.selectCashFlowSheet(queryParams.value).then((response: any) => {
    cashFlowStatementList.value = response.data;
    loading.value = false;
    checkBalance();
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
  statementApis.cashFlowExport(queryParams.value).then((data: any) => {
    downloadData(data, `现金流量表${queryParams.value.reportDate} ` + parseTime(new Date()) + ".xlsx")
  })
}

/*关闭抽屉*/
function dialogOfClosedMethods(val: any): any {
  open.value = false;
  id.value = undefined;
  selectedType.value = 0;
  selectedCode.value = "";
  if (val) {
    getList();
  }
}

function selectCashItem() {
  title.value = "现金流量项目指定";
  open.value = true;
}

function handleAmountClick(row: any) {
  selectedCode.value = row.itemCode;
  if (row.isMain === 1) {
    selectedType.value = 0
  } else if (row.isAdditional === 1) {
    selectedType.value = 1
  }
  selectCashItem();
}

function cancel() {
  dialogOpen.value = false;
}


// 处理输入框失去焦点
function handleBlur(row: any) {
  // 将字符串转换为数字
  const numValue = parseFloat(row.inputBalance as string) || 0;
  row.monthlyAmount = numValue;
  row.editing = false;

  //保存报表项
  const dataSave = {
    yearPeriod: queryParams.value.reportDate,
    periodType: 'month',
    itemCode: row.itemCode,
    monthlyAmount: row.monthlyAmount
  }
  saveCashFlowItem(dataSave).then((res: any) => {
    if (res.code === 0) {
      modal.msgSuccess("保存成功")
    } else {
      modal.msgError(res.message);
    }
  })
}

// 处理输入框获取焦点
const handleFocus = (row: any) => {
  row.inputBalance = row.monthlyAmount;
};

// 处理单元格点击事件
const handleClick = (row: any) => {
  row.editing = true;
  row.inputBalance = row.monthlyAmount;

  // 使用nextTick确保DOM更新后聚焦
  nextTick(() => {
    // 如果有多个输入框，需要使用ref数组或其他方式获取正确的输入框
    if (inputRef.value) {
      inputRef.value.focus();
    }
  });
};

function getNetProfitIncome() {
  getNetProfit(queryParams.value).then((response: any) => {
    const dataItem = response.data?.[0] || {};
    netProfitCurrent.value = dataItem.currentBalance ?? 0;
    netProfitYear.value = dataItem.cumulativeBalance ?? 0;

  })
}

function checkBalance() {
  const targetItem = cashFlowStatementList.value.find(
      (item: any) => item.itemCode === '38-xj-qmye'
  );

  if (targetItem) {
    console.log(targetItem.currentAmount)
    console.log(targetItem.monthlyAmount)
    endingYear.value = targetItem.currentAmount;
    endingTerm.value = targetItem.monthlyAmount;
    diff.value = endingYear.value - endingTerm.value;
    isBalance.value = diff.value === 0;
    balanceTitle.value = `期末现金及现金等价物余额(38行)，与现金及现金等价物科目余额不相符，本期差额 ${formatBalance(diff.value)}`;
  }
}

function checkDetails() {
  dialogTitle.value = "平衡检查";
  dialogOpen.value = true;
}

function goToCheck() {
  router.push({
    path: "/config/cash-flow-balance"
  })
}


getList();
// getNetProfitIncome();
</script>

<style lang="scss" scoped>
.app-container {
  padding: 0;
  background-color: #f5f7fa;
}

.common-card {
  margin-bottom: 15px;
}

.indented-item {
  padding-left: 32px; /* 或者使用 margin-left */
}

.clickable-amount {
  color: dodgerblue;
  cursor: pointer;
}

/* 在<style>部分添加 */
.editable-cell {
  cursor: pointer;
  display: block;
  width: 100%;
  padding: 2px 0;
  height: 24px;
}

.editable-cell:hover {
  background-color: #f5f7fa;
}

.hint {
  font-size: 14px;
}

.iconfont-balance {
  color: lawngreen;
}

.iconfont-unbalance {
  color: orange;
}

.balance-check-content {
  padding: 0 20px;
}

.balance-calculation {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  margin: 30px 0;
  gap: 20px;
}

.balance-item {
  text-align: center;
  display: flex;
  flex-direction: column;
}

.balance-amount {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin: 0 0 5px 0;
}

.balance-label {
  font-size: 14px;
  color: #606266;
  margin: 0;
}

.operator {
  font-size: 24px;
  font-weight: bold;
  color: #909399;
  margin-top: 4px; /* 调整操作符垂直对齐位置 */
}
</style>
