<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="100px">
          <el-form-item label="" prop="periodType">
            <el-radio-group v-model="queryParams.periodType" @change="handlePeriodType">
              <template v-for="(item, index) in period_type" :key="index">
                <el-radio-button :label="item.label" :value="item.value"></el-radio-button>
              </template>
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

          <el-form-item label="选择" prop="reportDate"
                        v-if="queryParams.periodType === 'quarter' || queryParams.periodType === 'halfYear'">
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
            <el-radio-group v-if="queryParams.periodType === 'quarter'" style="margin-left: 10px"
                            v-model="queryParams.reportQuarter" @change="handleQuery">
              <el-radio-button label="第一季度" value="Q1"></el-radio-button>
              <el-radio-button v-if="booksSet.termCurrent >= (new Date().getFullYear() + '-04')"
                               label="第二季度"
                               value="Q2"></el-radio-button>
              <el-radio-button v-if="booksSet.termCurrent >= (new Date().getFullYear() + '-07')"
                               label="第三季度" value="Q3"></el-radio-button>
              <el-radio-button v-if="booksSet.termCurrent >= (new Date().getFullYear() + '-10')"
                               label="第四季度" value="Q4"></el-radio-button>
            </el-radio-group>
            <el-radio-group v-if="queryParams.periodType === 'halfYear'" style="margin-left: 10px"
                            v-model="queryParams.reportQuarter" @change="handleQuery">
              <el-radio-button label="上半年" value="H1"></el-radio-button>
              <el-radio-button label="下半年" value="H2"></el-radio-button>
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
          <el-form-item :label="t('subjectCode')">
            <el-input
                v-model="queryParams.subjectCode"
                clearable
                style="width: 200px"
                @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-form-item :label="t('subjectName')">
            <el-input
                v-model="queryParams.subjectName"
                clearable
                style="width: 200px"
                @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleQuery">查询</el-button>
            <el-button @click="resetQuery">{{ t('org.button.reset') }}</el-button>
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

      <el-table
          :data="tableData"
          border
          style="width: 100%"
          header-cell-class-name="table-header"
          :span-method="spanMethod"
          v-loading="loading"
      >
        <!-- 科目编码 -->
        <el-table-column prop="subjectCode" label="科目编码" min-width="150" fixed>
          <template #default="{ row }">
            <span :style="{ paddingLeft: row.subjectCode.includes('_') ? '20px' : '0px' }">
              {{ row.subjectCode }}
            </span>
          </template>
        </el-table-column>

        <!-- 科目名称 -->
        <el-table-column prop="subjectName" label="科目名称" min-width="180" fixed>
          <template #default="{ row }">
            <span :style="{ paddingLeft: row.subjectCode.includes('_') ? '20px' : '0px' }">
              {{ row.subjectName }}
            </span>
          </template>
        </el-table-column>

        <!-- 期间 -->
        <el-table-column prop="period" label="期间" min-width="80" />

        <!-- 摘要 -->
        <el-table-column prop="summary" label="摘要" min-width="100" />

        <!-- 借方 -->
        <el-table-column prop="debit" label="借方" min-width="120" align="right">
          <template #default="{ row }">
            {{ formatNumber(row.debit) }}
          </template>
        </el-table-column>

        <!-- 贷方 -->
        <el-table-column prop="credit" label="贷方" min-width="120" align="right">
          <template #default="{ row }">
            {{ formatNumber(row.credit) }}
          </template>
        </el-table-column>

        <!-- 方向 -->
        <el-table-column prop="direction" label="方向" min-width="60" align="center" />

        <!-- 余额 -->
        <el-table-column prop="balance" label="余额" min-width="120" align="right">
          <template #default="{ row }">
            <div :style="{backgroundColor: row.balance >= 0 ? '' : 'bisque'}">
              {{ formatNumber(row.balance) }}
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import {ElForm} from "element-plus";
import Template from "@/views/hr/salary-voucher-rules/template.vue";
import {getCurrentInstance, h, reactive, ref, shallowRef, toRefs, onMounted, computed} from "vue";
import {getCurrentQuarter, parseTime} from "@/utils/Jinbooks";
import booksSetStore from "@/store/modules/bookStore";
import * as reportApis from "@/api/system/statement/statement";
import modal from "@/plugins/modal";
// 导出Excel功能实现
import * as XLSX from 'xlsx'
import {useI18n} from "vue-i18n";

const {t} = useI18n()
const {proxy} = getCurrentInstance();
const {period_type} = proxy?.useDict("period_type");
const booksSet = booksSetStore()
const loading = ref(true);
const showSearch = ref(true);
const recordsList = ref([]);

// 定义单条记录类型
interface PeriodInfo {
  debit: number;
  credit: number;
  openingBalanceDebit?: number;
  openingBalanceCredit?: number;
  currentPeriodDebit?: number;
  currentPeriodCredit?: number;
  closingBalanceDebit?: number;
  closingBalanceCredit?: number;
}

interface GeneralLedgerRow {
  subjectCode: string;
  subjectName: string;
  periodData: Record<string, PeriodInfo>; // key: '2025-01', value: {debit, credit}
}

// 原始数据
const rawData = ref<GeneralLedgerRow[]>([]);
// 动态月份数组
const months = ref<string[]>([]);

// 转换后的扁平表格数据
const tableData = computed(() => {
  const result = [];

  rawData.value.forEach((subject, subjectIndex) => {
    const sortedPeriods = Object.keys(subject.periodData).sort((a, b) => a.localeCompare(b));

    sortedPeriods.forEach((period, periodIndex) => {
      const periodInfo = subject.periodData[period];

      // 期初余额行
      result.push({
        subjectCode: subject.subjectCode,
        subjectName: subject.subjectName,
        period: period,
        summary: '期初余额',
        debit: periodInfo.openingBalanceDebit || null,
        credit: periodInfo.openingBalanceCredit || null,
        direction: getDirection(periodInfo.openingBalanceDebit, periodInfo.openingBalanceCredit),
        balance: getBalance(periodInfo.openingBalanceDebit, periodInfo.openingBalanceCredit),
        _subjectIndex: subjectIndex,
        _periodIndex: periodIndex,
        _rowType: 'opening',
        _totalRows: sortedPeriods.length * 3
      });

      // 本期合计行
      result.push({
        subjectCode: subject.subjectCode,
        subjectName: subject.subjectName,
        period: period,
        summary: '本期合计',
        debit: periodInfo.currentPeriodDebit || null,
        credit: periodInfo.currentPeriodCredit || null,
        direction: getDirection(periodInfo.currentPeriodDebit, periodInfo.currentPeriodCredit),
        balance: getBalance(periodInfo.currentPeriodDebit, periodInfo.currentPeriodCredit),
        _subjectIndex: subjectIndex,
        _periodIndex: periodIndex,
        _rowType: 'current',
        _totalRows: sortedPeriods.length * 3
      });

      // 本年累计行（期末余额）
      result.push({
        subjectCode: subject.subjectCode,
        subjectName: subject.subjectName,
        period: period,
        summary: '本年累计',
        debit: periodInfo.closingBalanceDebit || null,
        credit: periodInfo.closingBalanceCredit || null,
        direction: getDirection(periodInfo.closingBalanceDebit, periodInfo.closingBalanceCredit),
        balance: getBalance(periodInfo.closingBalanceDebit, periodInfo.closingBalanceCredit),
        _subjectIndex: subjectIndex,
        _periodIndex: periodIndex,
        _rowType: 'yearly',
        _totalRows: sortedPeriods.length * 3
      });
    });
  });

  return result;
});

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

// 合并单元格方法
const spanMethod = ({ row, column, rowIndex, columnIndex }) => {
  // 科目编码和科目名称列需要合并
  if (columnIndex === 0 || columnIndex === 1) {
    const currentSubjectCode = row.subjectCode;

    // 检查是否是该科目的第一行
    const isFirstRowOfSubject = rowIndex === 0 ||
        (rowIndex > 0 && tableData.value[rowIndex - 1].subjectCode !== currentSubjectCode);

    if (isFirstRowOfSubject) {
      // 计算该科目的总行数
      let rowspan = 0;
      for (let i = rowIndex; i < tableData.value.length; i++) {
        if (tableData.value[i].subjectCode === currentSubjectCode) {
          rowspan++;
        } else {
          break;
        }
      }
      return {
        rowspan,
        colspan: 1
      };
    } else {
      return {
        rowspan: 0,
        colspan: 0
      };
    }
  }

  return {
    rowspan: 1,
    colspan: 1
  };
};

// 计算余额显示（借方、贷方、方向）
const getBalanceDisplay = (debit: any, credit: any) => {
  const netBalance = (debit || 0) - (credit || 0);

  if (netBalance === 0) {
    return {
      debitBalance: null,
      creditBalance: null,
      direction: '平',
      balance: 0
    };
  } else if (netBalance > 0) {
    // 借方余额
    return {
      debitBalance: netBalance,
      creditBalance: null,
      direction: '借',
      balance: netBalance
    };
  } else {
    // 贷方余额
    return {
      debitBalance: null,
      creditBalance: Math.abs(netBalance),
      direction: '贷',
      balance: netBalance
    };
  }
};

// 计算方向
const getDirection = (debit: any, credit: any) => {
  if (!debit && !credit) return '平';
  if (debit > credit) return '借';
  if (credit > debit) return '贷';
  return '平';
};

// 计算余额
const getBalance = (debit: any, credit: any) => {
  if (!debit && !credit) return null;
  return (debit || 0) - (credit || 0);
};

// 格式化数字
const formatNumber = (num: any) => {
  if (num === null || num === undefined || num === 0) return '';
  return num.toLocaleString('zh-CN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  });
};

function resetQuery() {
  queryParams.value =  {
        bookId: booksSet.bookId,
        periodType: 'month',
        date: booksSet.termCurrent,
        reportQuarter: getCurrentQuarter(),
        reportDate: booksSet.termCurrent,
        subjectCode: '',
        subjectName: '',
  }
  handleQuery();
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

onMounted(() => {
  getList();
});

/** 查询列表 */
async function getList() {
  loading.value = true;
  try {
    const response: any = await reportApis.selectGeneralSubject(queryParams.value);
    const dataObj = response.data || {};

    // 转换成数组
    const rows: GeneralLedgerRow[] = [];

    for (const subjectCode in dataObj) {
      const entries = dataObj[subjectCode];
      const periodData: Record<string, PeriodInfo> = {};

      entries.forEach((item: any) => {
        const period = item.yearPeriod;
        if (!periodData[period]) {
          periodData[period] = {
            debit: 0,
            credit: 0,
            openingBalanceDebit: 0,
            openingBalanceCredit: 0,
            currentPeriodDebit: 0,
            currentPeriodCredit: 0,
            closingBalanceDebit: 0,
            closingBalanceCredit: 0
          };
        }

        // 累加各种类型的金额
        periodData[period].openingBalanceDebit += item.openingBalanceDebit || 0;
        periodData[period].openingBalanceCredit += item.openingBalanceCredit || 0;
        periodData[period].currentPeriodDebit += item.currentPeriodDebit || 0;
        periodData[period].currentPeriodCredit += item.currentPeriodCredit || 0;
        periodData[period].closingBalanceDebit += item.closingBalanceDebit || 0;
        periodData[period].closingBalanceCredit += item.closingBalanceCredit || 0;

        // 保持原有的debit和credit字段
        periodData[period].debit += item.currentPeriodDebit || 0;
        periodData[period].credit += item.currentPeriodCredit || 0;
      });

      if (entries.length > 0) {
        rows.push({
          subjectCode,
          subjectName: entries[0].subjectName,
          periodData
        });
      }
    }

    // 排序：按科目编码排序，辅助核算科目跟在主科目后面
    rows.sort((a, b) => {
      const codeA = a.subjectCode;
      const codeB = b.subjectCode;

      // 提取主科目编码（去掉下划线及后面的部分）
      const mainCodeA = codeA.includes('_') ? codeA.split('_')[0] : codeA;
      const mainCodeB = codeB.includes('_') ? codeB.split('_')[0] : codeB;

      // 先按主科目编码排序
      if (mainCodeA !== mainCodeB) {
        return mainCodeA.localeCompare(mainCodeB);
      }

      // 主科目相同时，主科目在前，辅助科目在后
      if (codeA.includes('_') && !codeB.includes('_')) {
        return 1; // A是辅助科目，B是主科目，B在前
      }
      if (!codeA.includes('_') && codeB.includes('_')) {
        return -1; // A是主科目，B是辅助科目，A在前
      }

      // 都是辅助科目或都是主科目，按完整编码排序
      return codeA.localeCompare(codeB);
    });

    rawData.value = rows;

    // 提取月份
    const monthSet = new Set<string>();
    rawData.value.forEach(item => {
      Object.keys(item.periodData || {}).forEach(m => monthSet.add(m));
    });
    months.value = Array.from(monthSet).sort((a, b) => a.localeCompare(b));

  } finally {
    loading.value = false;
  }
}

// 导出总账表到Excel
const handleExport = () => {
  try {
    // 创建工作簿
    const workbook = XLSX.utils.book_new()

    // 准备数据
    const exportData = []

    // 添加标题行
    exportData.push([
      '科目编码',
      '科目名称',
      '期间',
      '摘要',
      '借方',
      '贷方',
      '方向',
      '余额'
    ])

    // 处理表格数据
    tableData.value.forEach(row => {
      exportData.push([
        row.subjectCode,
        row.subjectName,
        row.period,
        row.summary,
        formatNumberForExcel(row.debit),
        formatNumberForExcel(row.credit),
        row.direction,
        formatNumberForExcel(row.balance)
      ])
    })

    // 创建工作表
    const worksheet = XLSX.utils.aoa_to_sheet(exportData)

    // 设置列宽
    worksheet['!cols'] = [
      { wch: 15 }, // 科目编码
      { wch: 20 }, // 科目名称
      { wch: 10 }, // 期间
      { wch: 12 }, // 摘要
      { wch: 15 }, // 借方
      { wch: 15 }, // 贷方
      { wch: 6 },  // 方向
      { wch: 15 }  // 余额
    ]

    // 设置表头样式
    const headerStyle = {
      font: { bold: true, sz: 12 },
      alignment: { horizontal: 'center', vertical: 'center' },
      fill: { fgColor: { rgb: 'E8F4FD' } },
      border: {
        top: { style: 'thin' },
        bottom: { style: 'thin' },
        left: { style: 'thin' },
        right: { style: 'thin' }
      }
    }

    // 应用表头样式
    for (let col = 0; col < 8; col++) {
      const cellAddress = XLSX.utils.encode_cell({ r: 0, c: col })
      if (!worksheet[cellAddress]) worksheet[cellAddress] = {}
      worksheet[cellAddress].s = headerStyle
    }

    // 设置数据行边框
    for (let row = 1; row < exportData.length; row++) {
      for (let col = 0; col < 8; col++) {
        const cellAddress = XLSX.utils.encode_cell({ r: row, c: col })
        if (!worksheet[cellAddress]) worksheet[cellAddress] = { v: '', t: 's' }
        if (!worksheet[cellAddress].s) worksheet[cellAddress].s = {}
        worksheet[cellAddress].s.border = {
          top: { style: 'thin' },
          bottom: { style: 'thin' },
          left: { style: 'thin' },
          right: { style: 'thin' }
        }

        // 数字列右对齐
        if ([4, 5, 7].includes(col)) { // 借方、贷方、余额列
          worksheet[cellAddress].s.alignment = { horizontal: 'right' }
        }

        // 方向列居中
        if (col === 6) {
          worksheet[cellAddress].s.alignment = { horizontal: 'center' }
        }
      }
    }

    // 处理合并单元格（科目编码和科目名称）
    const merges = []
    let currentSubjectCode = ''
    let startRow = 1

    for (let row = 1; row < exportData.length; row++) {
      const subjectCode = exportData[row][0]

      if (subjectCode !== currentSubjectCode) {
        // 前一个科目的合并处理
        if (currentSubjectCode && row - startRow > 1) {
          // 合并科目编码列
          merges.push({
            s: { r: startRow, c: 0 },
            e: { r: row - 1, c: 0 }
          })
          // 合并科目名称列
          merges.push({
            s: { r: startRow, c: 1 },
            e: { r: row - 1, c: 1 }
          })
        }

        currentSubjectCode = subjectCode
        startRow = row
      }
    }

    // 处理最后一个科目的合并
    if (exportData.length - startRow > 1) {
      merges.push({
        s: { r: startRow, c: 0 },
        e: { r: exportData.length - 1, c: 0 }
      })
      merges.push({
        s: { r: startRow, c: 1 },
        e: { r: exportData.length - 1, c: 1 }
      })
    }

    worksheet['!merges'] = merges

    // 添加工作表到工作簿
    const sheetName = `总账表_${queryParams.value.date || '全部'}`
    XLSX.utils.book_append_sheet(workbook, worksheet, sheetName)

    // 生成文件名
    const fileName = `总账表_${queryParams.value.date || '全部'}_${new Date().toISOString().slice(0, 10)}.xlsx`

    // 导出文件
    XLSX.writeFile(workbook, fileName)

    // 显示成功消息
    modal.msgSuccess('导出成功')

  } catch (error) {
    console.error('导出失败:', error)
    modal.msgError('导出失败，请重试')
  }
}

// 格式化数字用于Excel导出
const formatNumberForExcel = (num: any) => {
  if (num === null || num === undefined || num === 0) return ''
  return Number(num)
}
</script>

<style scoped>
.table-header {
  background-color: #f5f7fa;
  color: #606266;
  font-weight: bold;
}

.el-table {
  font-size: 12px;
}

.el-table .cell {
  padding: 8px;
}

.common-card {
  margin-bottom: 20px;
}

.queryForm {
  margin-bottom: 20px;
}

.btn-form {
  margin-bottom: 20px;
}

.btn-form-right {
  text-align: right;
  margin-bottom: 15px;
}

.app-container {
  padding: 0;
  background-color: #f5f7fa;
}
</style>
