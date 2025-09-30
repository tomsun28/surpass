<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="85%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="88px" style="display: flex;justify-content: space-between">
        <el-form-item label="" prop="periodType">
            <el-radio-group v-model="queryParams.cashFlowItemType" @change="handleType">
              <el-radio-button label="主表项目" :value="0"></el-radio-button>
              <el-radio-button label="补充资料项目" :value="1"></el-radio-button>
            </el-radio-group>
        </el-form-item>
        <div style="text-align: right">
          <i class="iconfont icon-tianping" :class="isBalance ? 'iconfont-balance' : 'iconfont-unbalance'"></i>
          <span class="hint">{{ isBalance ? '平衡' : '不平衡' }}</span>
        </div>
      </el-form>


      <el-table v-loading="loading" :data="recordsList" border stripe :span-method="objectSpanMethod">
        <el-table-column label="凭证信息" align="center">
          <el-table-column label="分录号" align="center" prop="entryNo" min-width="30"/>
          <el-table-column label="凭证字号" align="left" header-align="center" prop="word" min-width="100"
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
              <el-select v-model="scope.row.cashFlowItemCode"
                         style="width: 100%"
                         @change="handleSelect(scope.row)"
                         placement="bottom">
                <el-option
                    value="no-select"
                    label="不指定"
                />
                <el-option
                    v-for="dict in itemList"
                    :key="dict.itemCode"
                    :label="dict.itemName"
                    :value="dict.itemCode"
                />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="金额" align="right" prop="cashFlowBalance" min-width="60">
            <template #default="scope">
              <!-- 其他行正常显示可编辑内容或只读内容 -->
              <div v-if="scope.row.cashFlowItemCode !== 'no-select'">
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
                {{ formatBalance(scope.row.cashFlowBalance) }}
                   <el-icon size="15" style="vertical-align: middle"><Edit/></el-icon>
                </span>
              </div>
              <span v-else></span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" fixed="right" width="120">
            <template #default="scope">
              <el-button link type="primary" size="small" @click="splitAssignment(scope.row)">
                拆分指定
              </el-button>
              <el-button type="danger" link size="small" v-if="canDelete(scope.row.entryNo)"
                         @click="deleteRow(scope.row)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table-column>
      </el-table>
    </template>
    <template #footer>
      <div style="flex: auto">
        <el-button @click="dialogOfClosedMethods(false)">{{ t('org.cancel') }}</el-button>
        <el-button type="primary" @click="submitForm(false)">{{ t('org.confirm') }}</el-button>
      </div>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import {reactive, ref, toRefs, watch, computed, nextTick, getCurrentInstance} from "vue";
import {useI18n} from "vue-i18n";
import {getSelectItem} from "@/api/config/cash-flow-balance";
import modal from "@/plugins/modal";
import {formatAmount} from "@/utils";
import {getCashFlowItems, saveItemCodes} from "@/api/system/statement/statement-cash-flow";
import {Action, ElMessage, ElMessageBox} from 'element-plus'


const {t} = useI18n()
const {proxy} = getCurrentInstance()!;
const inputRef = ref<any>(null);
const emit: any = defineEmits(['dialogOfClosedMethods'])
const dialogStatus: any = ref(false);
const data = reactive({
  form: {},
  queryParams: {
    cashFlowItemType: 0
  },
});
const {form, queryParams} = toRefs(data);
const itemList = ref<any>([]);
const loading = ref(true);
const recordsList = ref<any>([]);
const isBalance = ref(true);
const directionMap = ref(new Map());

const props: any = defineProps({
  title: {
    type: String,
    default: ""
  },
  open: {
    type: Boolean,
    default: false
  },
  type: {
    type: Number,
    default: 0
  },
  voucherId: {
    type: String,
    default: ""
  },
  voucherDate: {
    type: String,
    default: ""
  }
})

watch(
    () => props.open,
    (val: any) => {
      if (val) {
        queryParams.value.cashFlowItemType = props.type;
        getCashFlowItem();
        getList();
        dialogStatus.value = props.open;
      } else {
        reset();
      }
    },
    {immediate: true}
);

function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('dialogOfClosedMethods', val);
}

function reset() {
  queryParams.value = {
    cashFlowItemType: 0
  }
  isBalance.value = true;
}

function handleType() {
  if (recordsList.value.length > 0) {
    ElMessageBox.confirm(
        '请先保存修改的数据，是否确认保存？',
        '提示',
        {
          confirmButtonText: '保存并切换',
          cancelButtonText: '取消',
          type: 'warning',
        }
    )
        .then(() => {
          submitForm(true).then(() => {
            getCashFlowItem();
            resetQuery();
          }).catch((error) => {
            // ⭐ 关键：捕获 submitForm 的异常
            console.log('submitForm 错误：', error);
            // 这里相当于用户点了取消（不切换类型）
            if (queryParams.value.cashFlowItemType === 0) {
              queryParams.value.cashFlowItemType = 1;
            } else {
              queryParams.value.cashFlowItemType = 0;
            }
          });
        })
        .catch(() => {
          if (queryParams.value.cashFlowItemType === 0) {
            queryParams.value.cashFlowItemType = 1
          } else {
            queryParams.value.cashFlowItemType = 0
          }
        })
  } else {
    getCashFlowItem();
    resetQuery();
  }
}

function getCashFlowItem() {
  getSelectItem({ cashFlowItemType: queryParams.value.cashFlowItemType })
      .then((response: any) => {
        if (response.code === 0) {
          itemList.value = response.data.map((item: any) => {
            directionMap.value.set(item.itemCode, item.direction);
            if (item.itemCode === "35-hl-djje" && item.itemName.startsWith("四、")) {
              return {
                ...item,
                itemName: item.itemName.replace(/^四、/, "")
              };
            }
            return item;
          });
        } else {
          modal.msgError(response.message);
        }
      })
      .catch((error: any) => {
        console.error('Failed to fetch cash flow items:', error);
        modal.msgError('Failed to fetch cash flow items');
      });
}

function resetQuery() {
  getList();
}

function getList() {
  const params = {
    cashFlowItemType: queryParams.value.cashFlowItemType,
    voucherId: props.voucherId
  }
  getCashFlowItems(params).then((response: any) => {
    loading.value = true;
    if (response.code === 0) {
      recordsList.value = response.data;
      loading.value = false;
      if (recordsList.value.length === 0) {
        ElMessageBox.alert('当前凭证无需指定主表现金流量项目', '提示', {
          // if you want to disable its autofocus
          // autofocus: false,
          confirmButtonText: '确认',
          type: 'warning'
        })
      }
    }
  })
}

function submitForm(isChange: boolean): Promise<any> {
  // 创建已处理的分录号集合，用于追踪哪些分录已经被计算过
  const processedEntries = new Set();

  // 计算借方金额和贷方金额总和（每个分录只计算一次）
  const { totalDebit, totalCredit } = recordsList.value.reduce((acc, row) => {
    // 如果这个分录号还没处理过，那么计算它的借贷金额
    if (!processedEntries.has(row.entryNo)) {
      // 将分录号添加到已处理集合
      processedEntries.add(row.entryNo);

      // 确保金额是数字并加总
      const debitAmount = parseFloat(row.debitAmount || 0);
      const creditAmount = parseFloat(row.creditAmount || 0);

      return {
        totalDebit: acc.totalDebit + debitAmount,
        totalCredit: acc.totalCredit + creditAmount
      };
    }
    // 如果已经处理过这个分录号，则不重复计算
    return acc;
  }, { totalDebit: 0, totalCredit: 0 });

  // 计算现金流量金额总和（每一行都要计算）
  const totalCashFlow = recordsList.value.reduce((sum, row) => {
    // 只计算已指定现金流量项的行
    if (row.cashFlowItemCode && row.cashFlowItemCode !== 'no-select') {
      let cashFlowAmount = parseFloat(row.cashFlowBalance || 0);
      if (directionMap.value.get(row.cashFlowItemCode) === 2) {
        cashFlowAmount = -cashFlowAmount;
      }
      return sum + cashFlowAmount;
    }
    return sum;
  }, 0);

  // 计算差值
  const difference = totalDebit - totalCredit;

  // 比较差值与现金流量金额总和是否相等
  // 使用小数点精度容差处理浮点数精度问题
  isBalance.value = Math.abs(difference + totalCashFlow) < 0.01;

  console.log(totalDebit, "借方")
  console.log(totalCredit, "贷方")
  console.log(difference, "借贷差")
  console.log(totalCashFlow, "现金流量")
  console.log(isBalance.value)

  //是否平衡
  if (isBalance.value) {
    let cashFlowItemTypeParams = 0;
    if (isChange) {
      // 调用 submitForm 并在完成后执行后续方法
      if (queryParams.value.cashFlowItemType === 0) {
        cashFlowItemTypeParams = 1
      }
    } else {
      cashFlowItemTypeParams = queryParams.value.cashFlowItemType;
    }

    const dataToSave = {
      voucherItemCashFlowDtos: recordsList.value,
      isEdit: true,
      cashFlowItemType: cashFlowItemTypeParams,
      voucherDate: props.voucherDate.substring(0, 7)
    };

    // 返回保存接口调用的 Promise
    return saveItemCodes(dataToSave).then((res: any) => {
      if (res.code === 0) {
        proxy?.$modal.msgSuccess("保存成功");
        if (!isChange) {
          dialogOfClosedMethods(true);
          reset();
        }
      } else {
        proxy?.$modal.msgError(res.message);
      }
      return res;
    });
  } else {
    proxy?.$modal.msgError("现金流量不平衡，请重新调整！");
    return Promise.reject("现金流量不平衡");
  }
}

function splitAssignment(row: any, index?: number) {
  // 创建新对象的深拷贝，避免引用原始对象
  const addObject = {...row};

  // 重置ID和相关字段
  addObject.id = null;
  addObject.cashFlowItemCode = "no-select";
  addObject.cashFlowBalance = null;

  // 如果没有提供索引，则查找当前行的索引
  if (index === undefined) {
    index = recordsList.value.findIndex(item => item === row);
  }

  // 在当前行的下一个位置插入新对象
  if (index !== -1) {
    recordsList.value.splice(index + 1, 0, addObject);
  } else {
    // 如果找不到当前行，则添加到末尾
    recordsList.value.push(addObject);
  }
}

const objectSpanMethod = ({rowIndex, columnIndex}: { rowIndex: number; columnIndex: number }) => {
  if (columnIndex === 0 || columnIndex === 1 || columnIndex === 2 || columnIndex === 3 || columnIndex === 4 || columnIndex === 5) {
    const currentEntryNo = recordsList.value[rowIndex].entryNo;
    let rowSpan = 0;
    if (rowIndex === 0 || recordsList.value[rowIndex - 1].entryNo !== currentEntryNo) {
      for (let i = rowIndex; i < recordsList.value.length; i++) {
        if (recordsList.value[i].entryNo === currentEntryNo) {
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

// 计算 EntryNo 出现的次数
const entryCount = computed(() => {
  return recordsList.value.reduce((acc: any, item: any) => {
    acc[item.entryNo] = (acc[item.entryNo] || 0) + 1;
    return acc;
  }, {} as Record<string, number>);
});


// 判断当前行是否可以删除
const canDelete = (entryNo: string) => {
  return entryCount.value[entryNo] > 1;
};

// 删除行
const deleteRow = (row: any) => {
  const index = recordsList.value.findIndex(item => item === row);
  if (index !== -1) {
    recordsList.value.splice(index, 1);
  }
};

// 验证输入的是否为有效数字（允许负号和小数点）
const validateNumber = (row: any) => {
  // 只允许数字、负号和小数点
  if (typeof row.inputBalance === 'string') {
    // 允许的格式: -123.45 或 123.45 或 123 或 -123
    // 保留有效的数字、最多一个负号（只能在开头）、最多一个小数点
    const validInput = row.inputBalance.replace(/[^0-9.-]/g, '');

    // 处理多个小数点的情况
    const parts = validInput.split('.');
    if (parts.length > 2) {
      row.inputBalance = parts[0] + '.' + parts.slice(1).join('');
    }

    // 处理多个负号的情况，只保留开头的负号
    if (validInput.lastIndexOf('-') > 0) {
      const firstPart = validInput.charAt(0) === '-' ? '-' : '';
      row.inputBalance = firstPart + validInput.replace(/-/g, '');
    } else {
      row.inputBalance = validInput;
    }
  }
};

// 处理输入框失去焦点
const handleBlur = (row: any) => {
  row.cashFlowBalance = row.inputBalance;
  row.editing = false;
};

// 处理输入框获取焦点
const handleFocus = (row: any) => {
  row.inputBalance = row.cashFlowBalance;
};

// 处理单元格点击事件
const handleClick = (row: any) => {
  row.editing = true;
  row.inputBalance = row.cashFlowBalance;

  // 使用nextTick确保DOM更新后聚焦
  nextTick(() => {
    // 如果有多个输入框，需要使用ref数组或其他方式获取正确的输入框
    if (inputRef.value) {
      inputRef.value.focus();
    }
  });
};

// 格式化余额显示
const formatBalance = (value: number | string) => {
  if (value === undefined || value === null || value === '') return '';

  const num = parseFloat(String(value));
  if (isNaN(num)) return '';

  // 格式化为带千分位和两位小数的格式
  return num.toLocaleString('zh-CN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
    useGrouping: true
  });
};

function handleSelect(row: any) {
  if (row.cashFlowItemCode === 'no-select') {
    row.cashFlowBalance = ""
  }
}

</script>

<style lang="scss" scoped>
/* 在<style>部分添加 */
.editable-cell {
  cursor: pointer;
  display: block;
  width: 100%;
  padding: 5px 0;
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
</style>
