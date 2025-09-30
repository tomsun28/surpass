<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="100px">
          <el-form-item label="" prop="periodType">
            <el-radio-group v-model="queryParams.periodType" @change="handlePeriodType">
              <el-radio-button label="月度" value="month"></el-radio-button>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="选择月度" prop="reportDate" v-if="queryParams.periodType === 'month'">
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

          <el-form-item :label="t('subjectCode')">
            <el-input
                v-model="queryParams.subjectCode"
                clearable
                style="width: 200px"
                @keyup.enter="handleQuery"
                placeholder="输入科目编码"
            />
          </el-form-item>

          <el-form-item :label="t('subjectName')">
            <el-input
                v-model="queryParams.subjectName"
                clearable
                style="width: 200px"
                @keyup.enter="handleQuery"
                placeholder="输入科目名称"
            />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="handleQuery">查询</el-button>
            <el-button @click="resetQuery">{{ t('org.button.reset') }}</el-button>
            <el-button type="warning" @click="performBalanceCheck">余额检查</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 平衡检查结果 -->
    <el-card class="common-card" v-if="balanceCheckVisible">
      <template #header>
        <div class="balance-check-header">
          <span>余额平衡检查结果</span>
          <el-button @click="balanceCheckVisible = false" link>
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
      </template>
      <div class="balance-check-results">
        <el-alert
            :title="balanceCheckSummary.title"
            :type="balanceCheckSummary.type"
            :description="balanceCheckSummary.description"
            show-icon
            :closable="false"
        />
        <el-table :data="balanceCheckResults" style="margin-top: 15px;" size="small">
          <el-table-column prop="subjectCode" label="科目编码" width="120" />
          <el-table-column prop="subjectName" label="科目名称" width="180" />
          <el-table-column prop="openingBalance" label="期初余额" width="120" align="right">
            <template #default="{ row }">
              {{ formatNumber(row.openingBalance) }}
            </template>
          </el-table-column>
          <el-table-column prop="occurrenceAmount" label="发生额" width="120" align="right">
            <template #default="{ row }">
              {{ formatNumber(row.occurrenceAmount) }}
            </template>
          </el-table-column>
          <el-table-column prop="calculatedBalance" label="计算余额" width="120" align="right">
            <template #default="{ row }">
              {{ formatNumber(row.calculatedBalance) }}
            </template>
          </el-table-column>
          <el-table-column prop="actualBalance" label="实际余额" width="120" align="right">
            <template #default="{ row }">
              {{ formatNumber(row.actualBalance) }}
            </template>
          </el-table-column>
          <el-table-column prop="difference" label="差异" width="120" align="right">
            <template #default="{ row }">
              <span :class="{'error-amount': Math.abs(row.difference) > 0.01}">
                {{ formatNumber(row.difference) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="isBalanced" label="状态" width="80" align="center">
            <template #default="{ row }">
              <el-tag :type="row.isBalanced ? 'success' : 'danger'" size="small">
                {{ row.isBalanced ? '平衡' : '不平衡' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>

    <!-- 丁字账显示 -->
    <el-card class="common-card" v-loading="loading">
      <div class="t-account-container" v-if="groupedAccounts.length > 0">
        <div v-for="account in groupedAccounts" :key="account.subjectCode" class="t-account-item">
          <!-- 科目标题 -->
          <div class="account-header">
            <div class="account-title">
              {{ account.subjectCode }} - {{ account.subjectName }}
            </div>
            <div class="account-stats">
              <span class="stat-item">借方笔数: {{ account.debitEntries.length }}</span>
              <span class="stat-item">贷方笔数: {{ account.creditEntries.length }}</span>
              <span class="stat-item balance" :class="getBalanceClass(account.calculatedBalance)">
                计算余额: {{ formatNumber(Math.abs(account.calculatedBalance)) }}
                ({{ getBalanceDirection(account.calculatedBalance, account.subjectCode) }})
              </span>
              <span class="stat-item" v-if="account.actualBalance !== undefined">
                实际余额: {{ formatNumber(Math.abs(account.actualBalance)) }}
              </span>
              <span class="stat-item" v-if="account.balanceCheck">
                <el-tag :type="account.balanceCheck.isBalanced ? 'success' : 'danger'" size="small">
                  {{ account.balanceCheck.isBalanced ? '余额平衡' : '余额不平衡' }}
                </el-tag>
              </span>
            </div>
          </div>

          <!-- T字账主体 -->
          <div class="t-account">
            <!-- 借方 -->
            <div class="debit-side">
              <div class="side-header">借方</div>
              <div class="entries">
                <div v-for="(entry, index) in account.debitEntries" :key="index" class="entry-item">
                  <div class="entry-info">
                    <div class="voucher-info">
                      <span class="voucher-number">{{ entry.voucherNumber }}</span>
                      <span class="voucher-date">{{ formatDate(entry.voucherDate) }}</span>
                    </div>
                    <div class="entry-summary">{{ entry.summary || '无摘要' }}</div>
                  </div>
                  <div class="entry-amount">{{ formatNumber(entry.debit) }}</div>
                </div>
                <div v-if="account.debitEntries.length === 0" class="empty-entry">
                  <div class="empty-text">本期无借方发生额</div>
                </div>
              </div>
              <div class="side-total">
                <div class="total-info">
                  <span class="total-label">借方合计</span>
                  <span class="total-count">({{ account.debitEntries.length }}笔)</span>
                </div>
                <div class="total-amount">{{ formatNumber(account.totalDebit) }}</div>
              </div>
            </div>

            <!-- 中央T字分隔线 -->
            <div class="t-divider">
              <div class="divider-line"></div>
              <div class="divider-cross"></div>
            </div>

            <!-- 贷方 -->
            <div class="credit-side">
              <div class="side-header">贷方</div>
              <div class="entries">
                <div v-for="(entry, index) in account.creditEntries" :key="index" class="entry-item">
                  <div class="entry-info">
                    <div class="voucher-info">
                      <span class="voucher-number">{{ entry.voucherNumber }}</span>
                      <span class="voucher-date">{{ formatDate(entry.voucherDate) }}</span>
                    </div>
                    <div class="entry-summary">{{ entry.summary || '无摘要' }}</div>
                  </div>
                  <div class="entry-amount">{{ formatNumber(entry.credit) }}</div>
                </div>
                <div v-if="account.creditEntries.length === 0" class="empty-entry">
                  <div class="empty-text">本期无贷方发生额</div>
                </div>
              </div>
              <div class="side-total">
                <div class="total-info">
                  <span class="total-label">贷方合计</span>
                  <span class="total-count">({{ account.creditEntries.length }}笔)</span>
                </div>
                <div class="total-amount">{{ formatNumber(account.totalCredit) }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <el-empty v-else description="暂无数据" />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import {getCurrentInstance, h, reactive, ref, shallowRef, toRefs, onMounted, computed} from "vue";
import {getCurrentQuarter} from "@/utils/Jinbooks";
import booksSetStore from "@/store/modules/bookStore";
import {useI18n} from "vue-i18n";
import * as reportApis from "@/api/system/statement/statement";
import modal from "@/plugins/modal";
import { Close } from '@element-plus/icons-vue';

const {t} = useI18n()
const {proxy} = getCurrentInstance();
const {period_type} = proxy?.useDict("period_type");

interface StatementSubjectBalance {
  subjectCode: string;
  subjectName: string;
  voucherNumber: string;
  voucherDate: string;
  summary: string;
  debit: number;
  credit: number;
  balance?: number;
  recordType: string; // opening, detail, closing
}

interface TAccountEntry {
  voucherNumber: string;
  voucherDate: string;
  summary: string;
  debit?: number;
  credit?: number;
}

interface BalanceCheck {
  isBalanced: boolean;
  difference: number;
}

interface TAccount {
  subjectCode: string;
  subjectName: string;
  debitEntries: TAccountEntry[];
  creditEntries: TAccountEntry[];
  totalDebit: number;
  totalCredit: number;
  openingBalance: number;
  calculatedBalance: number;
  actualBalance?: number;
  balanceCheck?: BalanceCheck;
}

interface BalanceCheckResult {
  subjectCode: string;
  subjectName: string;
  openingBalance: number;
  occurrenceAmount: number;
  calculatedBalance: number;
  actualBalance: number;
  difference: number;
  isBalanced: boolean;
}

const tableData = ref<StatementSubjectBalance[]>([]);
const loading = ref(true);
const showSearch = ref(true);
const balanceCheckVisible = ref(false);
const balanceCheckResults = ref<BalanceCheckResult[]>([]);
const booksSet = booksSetStore()

const data = reactive({
  form: {},
  queryParams: {
    bookId: booksSet.bookId,
    periodType: 'month',
    date: booksSet.termCurrent,
    reportQuarter: getCurrentQuarter(),
    reportDate: booksSet.termCurrent,
    subjectCode: '',
    subjectName: '',
  },
});

const {queryParams} = toRefs(data);

// 将表格数据转换为丁字账格式，只显示有发生额的科目
const groupedAccounts = computed(() => {
  const accountsMap = new Map<string, TAccount>();

  // 分离不同类型的记录
  const openingRecords = tableData.value.filter(item => item.recordType === 'opening');
  const detailRecords = tableData.value.filter(item => item.recordType === 'detail');
  const closingRecords = tableData.value.filter(item => item.recordType === 'closing');

  // 首先处理有明细记录的科目
  detailRecords.forEach((item) => {
    const key = item.subjectCode;

    if (!accountsMap.has(key)) {
      // 查找对应的期初余额
      const openingRecord = openingRecords.find(o => o.subjectCode === item.subjectCode);
      const closingRecord = closingRecords.find(c => c.subjectCode === item.subjectCode);

      accountsMap.set(key, {
        subjectCode: item.subjectCode,
        subjectName: item.subjectName,
        debitEntries: [],
        creditEntries: [],
        totalDebit: 0,
        totalCredit: 0,
        openingBalance: openingRecord ? (openingRecord.debit - openingRecord.credit) : 0,
        calculatedBalance: 0,
        actualBalance: closingRecord ? closingRecord.balance : undefined
      });
    }

    const account = accountsMap.get(key)!;

    // 添加借方记录
    if (item.debit && item.debit > 0) {
      account.debitEntries.push({
        voucherNumber: item.voucherNumber,
        voucherDate: item.voucherDate,
        summary: item.summary,
        debit: item.debit
      });
      account.totalDebit += item.debit;
    }

    // 添加贷方记录
    if (item.credit && item.credit > 0) {
      account.creditEntries.push({
        voucherNumber: item.voucherNumber,
        voucherDate: item.voucherDate,
        summary: item.summary,
        credit: item.credit
      });
      account.totalCredit += item.credit;
    }
  });

  // 计算每个账户的余额和平衡检查
  accountsMap.forEach((account) => {
    // 计算余额：期初余额 + 借方发生额 - 贷方发生额
    account.calculatedBalance = account.openingBalance + account.totalDebit - account.totalCredit;

    // 进行平衡检查
    if (account.actualBalance !== undefined) {
      const difference = account.calculatedBalance - account.actualBalance;
      const isBalanced = Math.abs(difference) <= 0.01; // 允许1分钱误差

      account.balanceCheck = {
        isBalanced,
        difference
      };
    }
  });

  return Array.from(accountsMap.values());
});

// 计算平衡检查汇总信息
const balanceCheckSummary = computed(() => {
  if (balanceCheckResults.value.length === 0) {
    return {
      title: '暂无检查结果',
      type: 'info',
      description: '请先点击"余额检查"按钮'
    };
  }

  const unbalancedCount = balanceCheckResults.value.filter(r => !r.isBalanced).length;
  const totalCount = balanceCheckResults.value.length;

  if (unbalancedCount === 0) {
    return {
      title: '余额检查通过',
      type: 'success',
      description: `共检查 ${totalCount} 个科目，全部平衡！`
    };
  } else {
    return {
      title: '发现余额不平衡',
      type: 'error',
      description: `共检查 ${totalCount} 个科目，其中 ${unbalancedCount} 个科目余额不平衡，请检查数据！`
    };
  }
});

// 执行余额平衡检查
const performBalanceCheck = () => {
  const results: BalanceCheckResult[] = [];

  groupedAccounts.value.forEach(account => {
    if (account.actualBalance !== undefined) {
      const occurrenceAmount = account.totalDebit - account.totalCredit;
      const difference = account.calculatedBalance - account.actualBalance;
      const isBalanced = Math.abs(difference) <= 0.01;

      results.push({
        subjectCode: account.subjectCode,
        subjectName: account.subjectName,
        openingBalance: account.openingBalance,
        occurrenceAmount: occurrenceAmount,
        calculatedBalance: account.calculatedBalance,
        actualBalance: account.actualBalance,
        difference: difference,
        isBalanced: isBalanced
      });
    }
  });

  balanceCheckResults.value = results;
  balanceCheckVisible.value = true;

  // 显示检查结果提示
  const unbalancedCount = results.filter(r => !r.isBalanced).length;
  if (unbalancedCount === 0) {
    modal.msgSuccess(`余额检查通过！共检查 ${results.length} 个科目，全部平衡。`);
  } else {
    modal.msgWarning(`发现 ${unbalancedCount} 个科目余额不平衡，请查看详细结果！`);
  }
};

// 获取余额方向
const getBalanceDirection = (balance: number, subjectCode: string) => {
  if (balance === 0) return '平';

  // 根据科目编码判断科目性质
  const code = subjectCode.replace(/_/g, '');
  const firstDigit = code.charAt(0);

  // 资产类(1)、成本类(5)、费用类(6) - 借方余额为正常
  if (['1', '5', '6'].includes(firstDigit)) {
    return balance > 0 ? '借' : '贷';
  }
  // 负债类(2)、所有者权益类(3)、收入类(4) - 贷方余额为正常
  else if (['2', '3', '4'].includes(firstDigit)) {
    return balance > 0 ? '借' : '贷';
  }

  return balance > 0 ? '借' : '贷';
};

// 获取余额样式类
const getBalanceClass = (balance: number) => {
  if (balance === 0) return 'balance-zero';
  return balance > 0 ? 'balance-debit' : 'balance-credit';
};

// 格式化日期
const formatDate = (dateStr: string) => {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  return `${date.getMonth() + 1}/${date.getDate()}`;
};

const handlePeriodType = (value: any) => {
  if (value === 'month' && queryParams.value.date.length < 7) {
    queryParams.value.date = queryParams.value.date + "-01"
  }
  handleQuery()
}

// 查询方法
async function handleQuery() {
  loading.value = true;
  queryParams.value.reportDate = queryParams.value.date;
  try {
    const res: any = await reportApis.tAccount(queryParams.value);
    tableData.value = res.data;
  } catch (e) {
    console.log("错误：", e);
    modal.msgError("查询失败");
  } finally {
    loading.value = false;
  }
}

// 重置
function resetQuery() {
  queryParams.value = {
    bookId: booksSet.bookId,
    periodType: 'month',
    date: booksSet.termCurrent,
    reportQuarter: getCurrentQuarter(),
    reportDate: booksSet.termCurrent,
    subjectCode: '',
    subjectName: '',
  }
  balanceCheckVisible.value = false;
  handleQuery();
}

const disabledDate = (time: any) => {
  const now = new Date(booksSet.termCurrent + "-01")
  const start = new Date(booksSet.termStart + "-01")
  return start.getTime() > time.getTime() || time.getTime() > now.getTime();
}

const customPrefix = shallowRef({
  render() {
    return h('p', '年')
  },
})

// 格式化数字
const formatNumber = (num: any) => {
  if (num === null || num === undefined || num === 0) return '';
  return num.toLocaleString('zh-CN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  });
};

onMounted(() => {
  handleQuery();
});
</script>

<style lang="scss" scoped>
.app-container {
  padding: 0;
  background-color: #f5f7fa;
}

.common-card {
  margin-bottom: 15px;
}

.query-box {
  .queryForm {
    .el-form {
      padding: 10px 0;
    }
  }
}

.balance-check-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.balance-check-results {
  .error-amount {
    color: #f56c6c;
    font-weight: bold;
  }
}

.t-account-container {
  .t-account-item {
    margin-bottom: 30px;
    border: 2px solid #e4e7ed;
    border-radius: 12px;
    background: #ffffff;
    overflow: hidden;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);

    &:last-child {
      margin-bottom: 0;
    }

    .account-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20px 25px;
      background: #f8f9fa;
      border-bottom: 2px solid #e4e7ed;

      .account-title {
        font-size: 18px;
        font-weight: 700;
        letter-spacing: 0.5px;
        color: #303133;
      }

      .account-stats {
        display: flex;
        gap: 15px;
        align-items: center;
        flex-wrap: wrap;

        .stat-item {
          font-size: 13px;
          padding: 4px 8px;
          background: rgba(64, 158, 255, 0.1);
          border-radius: 4px;
          color: #606266;

          &.balance {
            font-weight: 600;
            padding: 6px 12px;

            &.balance-debit {
              background: rgba(230, 162, 60, 0.9);
              color: white;
            }

            &.balance-credit {
              background: rgba(103, 194, 58, 0.9);
              color: white;
            }

            &.balance-zero {
              background: rgba(144, 147, 153, 0.9);
              color: white;
            }
          }
        }
      }
    }

    .t-account {
      display: flex;
      min-height: 350px;

      .debit-side,
      .credit-side {
        flex: 1;
        display: flex;
        flex-direction: column;
        background: #fefefe;

        .side-header {
          padding: 15px 20px;
          font-weight: 700;
          font-size: 16px;
          text-align: center;
          border-bottom: 2px solid #f0f2f5;
          position: relative;
        }

        .entries {
          flex: 1;
          min-height: 250px;
          max-height: 400px;
          overflow-y: auto;

          .entry-item {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            padding: 12px 20px;
            border-bottom: 1px solid #f8f9fa;
            transition: all 0.2s ease;

            &:hover {
              background-color: #f8f9fa;
              transform: translateX(2px);
            }

            .entry-info {
              flex: 1;
              margin-right: 15px;

              .voucher-info {
                display: flex;
                gap: 10px;
                margin-bottom: 4px;

                .voucher-number {
                  color: #409eff;
                  font-weight: 600;
                  font-size: 12px;
                }

                .voucher-date {
                  color: #909399;
                  font-size: 11px;
                }
              }

              .entry-summary {
                color: #606266;
                font-size: 13px;
                line-height: 1.4;
              }
            }

            .entry-amount {
              color: #303133;
              font-weight: 600;
              font-size: 14px;
              min-width: 100px;
              text-align: right;
              align-self: center;
            }
          }

          .empty-entry {
            display: flex;
            align-items: center;
            justify-content: center;
            height: 250px;

            .empty-text {
              color: #c0c4cc;
              font-style: italic;
              font-size: 14px;
            }
          }
        }

        .side-total {
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding: 15px 20px;
          background: #f8f9fa;
          border-top: 2px solid #e4e7ed;

          .total-info {
            display: flex;
            flex-direction: column;
            gap: 2px;

            .total-label {
              font-weight: 700;
              color: #303133;
            }

            .total-count {
              font-size: 11px;
              color: #909399;
            }
          }

          .total-amount {
            color: #303133;
            font-weight: 700;
            font-size: 16px;
            min-width: 100px;
            text-align: right;
          }
        }
      }

      .debit-side {
        border-right: 1px solid #e4e7ed;

        .side-header {
          background: linear-gradient(135deg, #ffeaa7, #fdcb6e);
          color: #d63031;
        }

        .side-total {
          background: linear-gradient(135deg, #fef4e6, #ffeaa7);
        }
      }

      .credit-side {
        .side-header {
          background: linear-gradient(135deg, #a8e6cf, #81ecec);
          color: #00b894;
        }

        .side-total {
          background: linear-gradient(135deg, #e8f8f5, #a8e6cf);
        }
      }

      .t-divider {
        position: relative;
        width: 4px;
        background: linear-gradient(to bottom, #667eea, #764ba2);

        .divider-line {
          width: 100%;
          height: 100%;
          background: inherit;
        }

        .divider-cross {
          position: absolute;
          top: 60px;
          left: -8px;
          width: 20px;
          height: 4px;
          background: inherit;
          border-radius: 2px;
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 968px) {
  .t-account {
    flex-direction: column !important;

    .t-divider {
      width: 100% !important;
      height: 4px !important;

      .divider-cross {
        top: -8px !important;
        left: 50% !important;
        transform: translateX(-50%) !important;
        width: 4px !important;
        height: 20px !important;
      }
    }

    .debit-side {
      border-right: none !important;
      border-bottom: 1px solid #e4e7ed;
    }
  }

  .account-header {
    flex-direction: column !important;
    align-items: flex-start !important;
    gap: 15px;

    .account-stats {
      flex-wrap: wrap;
      gap: 10px !important;
    }
  }
}

// 滚动条样式
.entries::-webkit-scrollbar {
  width: 6px;
}

.entries::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.entries::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;

  &:hover {
    background: #a8a8a8;
  }
}
</style>
