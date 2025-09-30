<template>
  <div class="app-container">
    <el-card class="common-card">
      <div class="btn-form">
        <el-button
            type="primary"
            @click="handleSave"
            :loading="saveLoading"
        >保存
        </el-button>
        <el-button
            @click="trialBalance(false)"
        >试算平衡
        </el-button>
      </div>

      <el-table
          v-loading="loading"
          :data="dataList"
          border
          :row-class-name="tableRowClassName"
          height="700"
      >
        <el-table-column prop="itemName" label="项目" align="left" min-width="160"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            <span :class="{ 'indented-item': scope.row.isTitle !== 1 }">{{ scope.row.itemName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="sortIndex" label="行次" align="center" min-width="20">
        </el-table-column>
        <el-table-column prop="balance" label="本年累计" align="right" min-width="100"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            <!-- 特定行号不显示任何内容 -->
            <template v-if="['1-jy-xjll', '12-tz-xjll', '25-cz-xjll', '39-xj-bczl',
            '40-xj-xjll', '58-xj-tzhd', '62-xj-xjqk'].includes(scope.row.itemCode)">
              <span></span>
            </template>
            <!-- 其他行正常显示可编辑内容或只读内容 -->
            <template v-else>
              <div v-if="scope.row.isEdit === 1">
                <el-input
                    v-if="scope.row.editing"
                    v-model="scope.row.inputBalance"
                    type="text"
                    @input="validateNumber(scope.row)"
                    @blur="handleBlur(scope.row)"
                    @focus="handleFocus(scope.row)"
                    style="text-align: right"
                    ref="inputRef"
                />
                <span v-else @click="handleClick(scope.row)" class="editable-cell">
                {{ formatBalance(scope.row.balance) }}
                </span>
              </div>
              <span v-else>{{ formatBalance(scope.row.balance) }}</span>
            </template>
          </template>
        </el-table-column>
        <el-table-column align="right" min-width="150">
        </el-table-column>
      </el-table>

      <el-dialog v-model="dialogOpen" width="800px" append-to-body :title="dialogTitle" :close-on-click-modal="false"
                 @close="cancel">
        <el-alert v-if="isBalance" type="success" show-icon :closable="false"
                  center
                  title="报表数据平衡！">
        </el-alert>
        <el-alert v-if="!isBalance" type="warning" show-icon :closable="false"
                  :title="balanceTitle">
        </el-alert>
        <div class="balance-check-content" v-if="!isBalance">
          <div class="balance-calculation">
            <div class="balance-item">
              <p class="balance-amount" v-if="endingBalance !== 0.00">{{ formatBalance(endingBalance) }}</p>
              <p class="balance-amount" v-else>0.00</p>
              <p class="balance-label">期末现金及现金等价物余额(38行)</p>
            </div>
            <div class="operator">−</div>
            <div class="balance-item" style="width: 40%">
              <p class="balance-amount" v-if="startingBalance !== 0.00">{{ formatBalance(startingBalance) }}</p>
              <p class="balance-amount" v-else>0.00</p>
              <p class="balance-label">科目初始余额</p>
              <!-- 分割线 -->
              <hr class="balance-divider"/>
              <!-- 明细列表 -->
              <div
                  class="balance-detail"
                  v-for="(item, index) in bookInitBalances"
                  :key="index"
              >
                <span class="balance-detail-left">·{{ item.code }}{{ item.name }}</span>
                <span class="balance-detail-right">{{ formatBalance(item.balance)}}</span>
              </div>
            </div>
            <div class="operator">=</div>

            <div class="balance-item">
              <p class="balance-amount" v-if="difference !== 0.00">{{ formatBalance(difference) }}</p>
              <p class="balance-amount" v-else>0.00</p>
              <p class="balance-label">差额</p>
            </div>
          </div>
        </div>
        <div slot="footer" class="dialog-footer" v-if="!isBalance">
          <el-button type="primary" @click="autosave" :loading="saveLoading">自动调平并保存</el-button>
          <el-button @click="forceSave" v-if="ifSaveType" :loading="saveLoading">直接保存</el-button>
          <el-button @click="cancel">{{ $t('systemCancel') }}</el-button>
        </div>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import {useI18n} from "vue-i18n";
import {getCurrentInstance, reactive, ref, toRefs, nextTick, watch} from "vue";
import {fetchPage, saveBalanceData} from "@/api/config/cash-flow-balance";
import modal from "@/plugins/modal";
import {formatBalance, validateNumber} from "@/utils/BalanceFormat";
import {ElForm} from "element-plus";

const {t} = useI18n()
const {proxy} = getCurrentInstance()!;
const dataList: any = ref<any>([]);
const bookInitBalances: any = ref<any>([]);
const loading: any = ref(false);
const data = reactive({
  queryParams: {},
  form: {}
});
const {queryParams, form} = toRefs(data);
const inputRef = ref<any>(null);
const saveLoading = ref(false);
const dialogOpen = ref(false);
const dialogTitle = ref("");
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const endingBalance = ref(0.00);
const startingBalance = ref(0.00);
const difference = ref(0.00);
const balanceTitle = ref("");
const isBalance = ref(true);
const ifSaveType = ref(false)


// 定义数据类型接口
interface TableItem {
  itemName: string;
  sortIndex: number;
  isTitle: number;
  isResult: number;
  balance: any; // 使用any类型避免类型问题
  inputBalance?: any;
  editing?: boolean;
  itemCode?: string;
}

// 定义手动输入的行
const manualInputRows = [2, 3, 4, 6, 7, 8, 9, 13, 14, 15, 16, 17, 19, 20, 21, 22, 26, 27, 28, 30, 31, 32, 35,
  42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55];

// 按依赖关系排序的计算规则
const calculationRules = [
  // 一级计算 (直接依赖手动输入的行)
  {targetIndex: 5, type: 'sum', sourceIndices: [2, 3, 4]},                // 经营活动现金流入小计
  {targetIndex: 10, type: 'sum', sourceIndices: [6, 7, 8, 9]},            // 经营活动现金流出小计
  {targetIndex: 18, type: 'sum', sourceIndices: [13, 14, 15, 16, 17]},    // 投资活动现金流入小计
  {targetIndex: 23, type: 'sum', sourceIndices: [19, 20, 21, 22]},        // 投资活动现金流出小计
  {targetIndex: 29, type: 'sum', sourceIndices: [26, 27, 28]},            // 筹资活动现金流入小计
  {targetIndex: 33, type: 'sum', sourceIndices: [30, 31, 32]},            // 筹资活动现金流出小计

  // 二级计算 (依赖一级计算结果)
  {targetIndex: 11, type: 'diff', minuend: 5, subtrahend: 10},           // 经营活动产生的现金流量净额
  {targetIndex: 24, type: 'diff', minuend: 18, subtrahend: 23},          // 投资活动产生的现金流量净额
  {targetIndex: 34, type: 'diff', minuend: 29, subtrahend: 33},          // 筹资活动产生的现金流量净额

  // 三级计算 (依赖二级计算结果)
  {targetIndex: 36, type: 'sum', sourceIndices: [11, 24, 34, 35]},       // 现金及现金等价物净增加额

  // 四级计算
  {targetIndex: 38, type: 'sum', sourceIndices: [36, 37]},               // 期末现金及现金等价物余额 = 期初余额 + 净增加额

  // 现金及现金等价物净增加额计算
  {targetIndex: 67, type: 'complexDiff', sourceIndices: [63, 64, 65, 66]}, // 现金及现金等价物净增加额 = 现金的期末余额 - 减：现金的期初余额 + 加：现金等价物的期末余额 - 减：现金等价物的期初余额

  // 新增：第二种方法计算经营活动现金流量净额，除了"其他"项
  {
    targetIndex: 57,
    type: 'sumBeforeBalance',
    sourceIndices: [41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55],
    balanceTarget: 11,
    otherItemIndex: 56
  }
];

// 主计算函数
function calculateAllValues() {
  if (!dataList.value || dataList.value.length === 0) return;

  // 创建行索引映射
  const rowMap = new Map<number, any>();
  const itemCodeMap = new Map<string, any>();

  dataList.value.forEach((item: any) => {
    rowMap.set(item.sortIndex, item);
    if (item.itemCode) {
      itemCodeMap.set(item.itemCode, item);
    }
  });

  // 计算求和
  const calculateSum = (indices: number[]): number => {
    return indices.reduce((acc: number, index: number) => {
      const row = rowMap.get(index);
      if (row) {
        const value = parseFloat(row.balance) || 0;
        return acc + value;
      }
      return acc;
    }, 0);
  };

  // 计算差值
  const calculateDifference = (minuend: number, subtrahend: number): number => {
    const minuendRow = rowMap.get(minuend);
    const subtrahendRow = rowMap.get(subtrahend);

    const minuendValue = minuendRow ? (parseFloat(minuendRow.balance) || 0) : 0;
    const subtrahendValue = subtrahendRow ? (parseFloat(subtrahendRow.balance) || 0) : 0;

    return minuendValue - subtrahendValue;
  };

  // 计算复杂差值（用于索引67）
  const calculateComplexDifference = (indices: number[]): number => {
    if (!indices || indices.length < 2) return 0;

    let result = 0;
    indices.forEach((index, i) => {
      const row = rowMap.get(index);
      if (!row) return;

      const value = parseFloat(row.balance) || 0;

      if (i === 0) {
        // 第一个值为基数
        result = value;
      } else if (i % 2 === 1) {
        // 减去偶数位置的值（索引1,3,5...）
        result -= value;
      } else {
        // 加上奇数位置的值（索引2,4,6...）
        result += value;
      }
    });

    return result;
  };

  // 更新行值
  const updateRowValue = (rowIndex: number, value: number) => {
    const row = rowMap.get(rowIndex);
    if (row) {
      row.balance = value;
      row.inputBalance = value;
    }
  };

  // 先执行所有基本计算规则（除了索引67的计算）
  calculationRules.filter(rule => rule.targetIndex !== 67).forEach(rule => {
    if (rule.type === 'sum') {
      const sum = calculateSum(rule.sourceIndices);
      updateRowValue(rule.targetIndex, sum);
    } else if (rule.type === 'diff') {
      const diff = calculateDifference(rule.minuend, rule.subtrahend);
      updateRowValue(rule.targetIndex, diff);
    } else if (rule.type === 'sumBeforeBalance') {
      // 先计算除了"其他"项外的所有项的和
      const sumBeforeBalance = calculateSum(rule.sourceIndices);

      // 获取方法1计算的经营活动产生的现金流量净额
      const targetRow = rowMap.get(rule.balanceTarget);
      const targetValue = targetRow ? (parseFloat(targetRow.balance) || 0) : 0;

      // 计算"其他"项的值，使方法2的结果等于方法1的结果
      const otherValue = targetValue - sumBeforeBalance;

      // 更新"其他"项的值
      updateRowValue(rule.otherItemIndex, otherValue);

      // 更新方法2的合计（应该等于方法1的结果）
      updateRowValue(rule.targetIndex, targetValue);
    }
  });

  // 特殊处理：从索引38(期末现金及现金等价物余额)获取值，赋给索引63(现金的期末余额)
  const endingBalanceRow = rowMap.get(38);
  if (endingBalanceRow) {
    const endingBalanceValue = parseFloat(endingBalanceRow.balance) || 0;
    updateRowValue(63, endingBalanceValue);
  }

  // 然后计算索引67(现金及现金等价物净增加额)
  const rule67 = calculationRules.find(rule => rule.targetIndex === 67);
  if (rule67 && rule67.sourceIndices) {
    const result = calculateComplexDifference(rule67.sourceIndices);
    updateRowValue(67, result);
  }
}

// 处理输入框失去焦点
function handleBlur(row: any) {
  // 将字符串转换为数字
  const numValue = parseFloat(row.inputBalance) || 0;
  row.balance = numValue;
  row.editing = false;

  // 当手动输入行发生变化时，触发所有计算
  if (manualInputRows.includes(row.sortIndex)) {
    calculateAllValues();
  }
}

// 监听数据变化
watch(
    () => [...dataList.value],
    (newVal, oldVal) => {
      if (newVal.length > 0 && !loading.value) {
        // 检查手动输入行是否有变化
        const manualRows = newVal.filter((item: TableItem) =>
            manualInputRows.includes(item.sortIndex)
        );

        const oldManualRows = oldVal ? oldVal.filter((item: TableItem) =>
            manualInputRows.includes(item.sortIndex)
        ) : [];

        // 检查是否有值变化
        const hasChanges = manualRows.some((row: TableItem, index: number) => {
          if (!oldManualRows[index]) return true;
          return row.balance !== oldManualRows[index].balance;
        });

        if (hasChanges) {
          calculateAllValues();
        }
      }
    },
    {deep: true}
);

// 行类名方法
const tableRowClassName = ({row, rowIndex}: { row: TableItem; rowIndex: number }): string => {
  if (row.isResult === 1) {
    return 'warning-row'; // 优先级最高
  }
  return rowIndex % 2 === 1 ? 'stripe-row' : ''; // 其他行使用斑马纹
};

function getList() {
  loading.value = true;
  fetchPage(queryParams.value).then((response: any) => {
    dataList.value = response.data.configCashFlowBalances.map((item: any) => ({
      ...item,
      editing: false,
      inputBalance: item.balance
    }));

    bookInitBalances.value = response.data.bookInitBalances;

    loading.value = false;
    // 在数据加载完成后执行一次计算
    calculateAllValues();
  });
}


// 处理单元格点击事件
const handleClick = (row: any) => {
  row.editing = true;
  row.inputBalance = row.balance;

  // 使用nextTick确保DOM更新后聚焦
  nextTick(() => {
    // 如果有多个输入框，需要使用ref数组或其他方式获取正确的输入框
    if (inputRef.value) {
      inputRef.value.focus();
    }
  });
};

// 处理输入框获取焦点
const handleFocus = (row: any) => {
  row.inputBalance = row.balance;
};

function submitForm() {
  try {
    saveLoading.value = true;

    // 保存前确保所有自动计算的值都是最新的
    calculateAllValues();

    const items = dataList.value.map((item: any) => {
      return {
        id: item.id,
        sortIndex: item.sortIndex,
        balance: item.balance
      };
    });

    // 按照后端 DTO 格式包装数据
    const dataToSave = {
      cashFlowItemDtos: items
    };

    saveBalanceData(dataToSave).then((res: any) => {
      // 处理保存成功
      if (res.code === 0) {
        modal.msgSuccess('保存成功');
        saveLoading.value = false;
        dialogOpen.value = false;
        // 可选：重新获取数据
        getList();
      } else {
        modal.msgError(res?.message || '保存失败');
        saveLoading.value = false;
      }
    });
  } catch (error) {
    console.error('保存失败:', error);
    modal.msgError('保存失败，请重试');
  } finally {
    // saveLoading.value = false; // 确保无论成功与否都关闭加载状态
  }
}

function handleSave() {
  ifSaveType.value = true;
  if (trialBalance(ifSaveType.value)) {
    submitForm();
  }
}

function cancel() {
  dialogOpen.value = false;
}

function trialBalance(ifSave: boolean): boolean {
  isBalance.value = true;

  const getRowBalance = (index: number): number => {
    return dataList.value.find((row: { sortIndex: number }) => row.sortIndex === index)?.balance ?? 0;
  };

  const ending = getRowBalance(38);
  const starting = getRowBalance(37);

  endingBalance.value = ending;
  startingBalance.value = starting;

  const diff = ending - starting;
  difference.value = diff;

  if (diff !== 0) {
    isBalance.value = false;
    balanceTitle.value = `期末现金及现金等价物余额(38行)与科目初始余额不相符，差额 ${formatBalance(diff)}，请调整`;
  } else {
    balanceTitle.value = ""
  }

  if (!ifSave) {
    ifSaveType.value = false;
  }

  if (!ifSave || !isBalance.value) {
    dialogTitle.value = "平衡检查";
    dialogOpen.value = true;
  }

  return isBalance.value;
}

function autosave() {
  // 如果差额为零，无需调整
  if (Math.abs(difference.value) < 0.001) {
    modal.msgSuccess('现金流量已平衡，无需调整');
    dialogOpen.value = false;
    return;
  }

  // 确定需要调整的项目
  // 我们选择调整"支付其他与经营活动有关的现金"(行号9)
  const targetRow = dataList.value.find((row: any) => row.sortIndex === 9);

  if (!targetRow) {
    modal.msgError('无法找到需要调整的项目');
    return;
  }

  // 计算新的余额：如果差额为正，需要增加支出；如果差额为负，需要减少支出
  const currentValue = parseFloat(targetRow.balance) || 0;
  const newValue = currentValue + difference.value;

  // 更新目标行的值
  targetRow.balance = newValue;
  targetRow.inputBalance = newValue;

  // 重新计算所有值
  calculateAllValues();

  // 确认差额是否已经平衡
  const newEndingBalanceRow = dataList.value.find((row: any) => row.sortIndex === 38);
  const newEndingBalanceValue = parseFloat(newEndingBalanceRow.balance) || 0;
  const newDifference = newEndingBalanceValue - startingBalance.value;

  if (newDifference !== 0) {
    modal.msgError('调整后仍未平衡，请手动调整');
    return;
  }

  submitForm();
}

function forceSave() {
  submitForm();
}

// 初始化获取数据
getList();
</script>

<style lang="scss" scoped>
.app-container {
  padding: 0;
  background-color: #f5f7fa;
}

.btn-form {
  text-align: right;
  margin-bottom: 10px;
}

.common-card {
  margin-bottom: 15px;
}

.indented-item {
  padding-left: 32px; /* 或者使用 margin-left */
}

:deep(.el-table .warning-row) {
  --el-table-tr-bg-color: var(--el-color-warning-light-9);
}

:deep(.el-table .stripe-row) {
  --el-table-tr-bg-color: #f5faff; /* 非常浅的蓝色 */
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

:deep(.el-table) {
  /* 减小表格行高 */
  --el-table-row-height: 32px; /* 默认可能是 50px 左右，可以根据需要调整 */

  /* 减小单元格内边距 */
  td.el-table__cell {
    padding: 4px 0; /* 上下内边距减小 */
  }

  /* 调整表头行高 */
  th.el-table__cell {
    padding: 6px 0; /* 表头可以稍微高一点 */
  }

  /* 调整输入框大小 */
  .el-input {
    --el-input-height: 24px; /* 减小输入框高度 */
    line-height: 24px;
  }

  .el-input__inner {
    height: 24px;
    line-height: 24px;
  }
}

.balance-check-content {
  padding: 0 20px;
}

.balance-calculation {
  display: flex;
  justify-content: space-around;
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

.balance-divider {
  margin: 10px auto;
  border: none;
  border-top: 1px solid #eee;
  width: 80%;
}

.balance-detail {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #666;
  padding: 2px 20px;
}

.balance-detail-left {
  text-align: left;
}

.balance-detail-right {
  text-align: right;
}
</style>
