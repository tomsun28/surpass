<template>
  <div class="app-container" @keydown="handleKeydown">
    <!--  功能区左侧  -->
    <div v-if="!isPrintMode" class="top-funs top-funs-left" :class="{topFunsUpdate: !!formData.id && props.dialog}">
      <span class="bottom-counts-item" :class="{isNotPh: !loanBalance()}">
        借贷平衡：{{ loanBalance() ? "是" : "否" }}</span>
    </div>
    <!--  功能区  -->
    <div v-if="route.query.mode !== 'print'" class="top-funs" :style="{top: auto? '204px' : '80px'}">
      <el-button v-if="props.edit" @click="onAddItem">
        添加一项
      </el-button>
      <el-tooltip v-if="props.edit" content="确保总账科目对应的金额已正确录入！">
        <el-button v-loading="submitButtonLoading" @click="onSubmitDraft">
          暂存
        </el-button>
      </el-tooltip>
      <el-tooltip
          v-if="props.edit && formData.voucherDate && formData.voucherDate.startsWith(currBookStore.termCurrent)"
          content="确保总账科目对应的金额已正确录入！">
        <el-button v-loading="submitButtonLoading" @click="onSubmit">
          提交
        </el-button>
      </el-tooltip>
      <el-tooltip v-if="props.edit && !props.dialog" content="清空当前数据，建立新的凭证信息！">
        <el-button @click="onReset">
          新增凭证
        </el-button>
      </el-tooltip>
      <el-button @click="onPrint">
        打印
      </el-button>
    </div>
    <div ref="printMe" class="printable-content" id="printable-content"
         @click="closeEditAll"
         :style="{margin: !isPrintMode ? '65px 0 0 0' : '0 auto', width: isPrintMode ? '980px' : '100%'}">
      <!--   标题头   -->
      <div class="header-title">
        <span class="header-title-text">
          <span class="text">记&nbsp;&nbsp;&nbsp;账&nbsp;&nbsp;&nbsp;凭&nbsp;&nbsp;&nbsp;证</span>
        </span>
        <span class="header-title-time">
          <el-date-picker v-if="!isPrintMode"
                          :clearable="false"
                          :disabled-date="isCurrentOrFutureMonth"
                          style="width: 120px" v-model="formData.voucherDate"
                          type="date" placeholder="选择日期"
                          value-format="YYYY-MM-DD" format="YYYY-MM-DD"
                          :shortcuts="shortcuts"
                          @change="handleVoucherDate"></el-date-picker>
          <span v-else>{{ formData.voucherDate }}</span>
        </span>
      </div>
      <!-- 公司信息部分 -->
      <div class="company-info" style="width: calc(100% - 50px)">
        <div class="company-info-left company-info-item no-border-input">
          <div class="company-info-item">
            <span>公司名称：</span>
            <!--            <el-input v-if="!isPrintMode"-->
            <!--                      style="width: 300px" :input-style="{}"-->
            <!--                      v-model="formData.companyName"/>-->
            <span>{{ formData.companyName }}</span>
          </div>
        </div>
        <div class="company-info-right">
          <div class="company-info-item">
            <div v-if="!isPrintMode" class="no-border-input input-number">
              <el-select v-model="formData.wordHead" placeholder="字头"
                         style="width: 50px" size="small" @change="handleWordHead">
                <el-option label="记" value="记"/>
                <el-option label="收" value="收"/>
                <el-option label="付" value="付"/>
                <el-option label="转" value="转"/>
              </el-select>
              <span>字{{ parseTime(new Date(formData.voucherDate), "{y}{m}") }}第</span>
              <el-input-number style="width: 90px"
                               v-model="formData.wordNum"
                               :min="1"
                               :max="9999"
                               size="small"/>
              <span>号</span>
            </div>
            <span v-if="isPrintMode">
               {{ formatVoucherWordNum() }}
            </span>
          </div>
          <div class="company-info-item">
            <div class="no-border-input" v-if="!isPrintMode">
              <span>附件：</span>
              <el-input style="width: 40px" :input-style="{textAlign: 'center'}"
                        v-model="formData.receiptNum">
              </el-input>
              <span>张</span>
            </div>
            <span v-if="isPrintMode">附件：{{ formData.receiptNum }}张</span>
          </div>
        </div>
      </div>
      <!--  中间表格区域  -->
      <div style="display: flex;justify-content: space-between">
        <!-- 主内容区域 -->
        <div style="width: 100%">
          <el-table :data="tableSumData.slice(0,-1)"
                    :height="!isPrintMode ? '320' : undefined"
                    @row-contextmenu="rowContextmenu"
                    @cell-click="cellClick"
                    @header-click="headerClick"
                    header-row-class-name="rv-table-header-row"
                    header-cell-class-name="rv-table-header-cell"
                    row-class-name="rv-table-row"
                    :cell-class-name="tableCellClassName"
                    class="rv-table">
            <el-table-column prop="summary" align="left" header-align="center" label="摘要">
              <template #default="scope">
                <span v-if="!scope.row.editing">{{ scope.row.summary }}</span>
                <el-input v-else v-model="scope.row.summary"
                          @keydown="handleInputKeydown($event, scope, 0)"
                          :ref="(el) => setRef(el, `input-${scope.$index}-0`)"></el-input>
              </template>
            </el-table-column>
            <el-table-column align="left" header-align="center" label="会计科目">
              <el-table-column prop="subjectId" align="left" header-align="center" label="科目">
                <template #default="scope">
                  <div v-if="!scope.row.editing" v-html="getSubjectName(scope)"></div>
                  <el-cascader v-else style="width: 100%" filterable
                               v-model="scope.row.subjectCode"
                               :options="subjectList"
                               :props="cascaderSubjectProps"
                               @change="handleSubjectChange(scope, $event)"
                               @keydown="handleCascaderKeydown($event, scope, 1)"
                               :ref="(el) => setRef(el, `cascader-${scope.$index}-1`)"
                               :filter-method="cascaderSubjectProps.filterMethod"/>
                </template>
              </el-table-column>
              <el-table-column prop="auxiliary" align="left" header-align="center" label="辅助核算">
                <template #default="scope">
                  <span v-if="!scope.row.editing">{{
                      getSubjectDetailName(scope)
                    }}</span>
                  <el-popover v-else title="辅助核算" :width="400" trigger="click"
                              :visible="auxiliaryVisible[scope.$index]">
                    <template #reference>
                      <el-input readonly autosize type="textarea" :value="getSubjectDetailName(scope)"
                                @click="handleAuxiliaryShow(scope)"
                                @keydown="handleInputKeydown($event, scope, 2)"
                                :ref="(el) => setRef(el, `input-${scope.$index}-2`)"></el-input>
                    </template>
                    <template #default>
                      <div>
                        <select-auxiliary
                            :show="auxiliaryVisible[scope.$index]"
                            :subjectId="scope.row.subjectId"
                            :auxiliary="subjectKeyItem[scope.row.subjectCode]?.auxiliary"
                            v-model="scope.row.auxiliary"></select-auxiliary>
                      </div>
                    </template>
                  </el-popover>
                </template>
              </el-table-column>
            </el-table-column>
            <el-table-column prop="debitAmount" align="right" header-align="center" label="借方金额">
              <template #default="scope">
                <span v-if="!scope.row.editing" :class="{redWord:isRedWord(scope.row.debitAmount)}">
                  {{ formatAmountRed(scope.row.debitAmount) }}
                </span>
                <el-input v-else v-model="scope.row.debitAmount"
                          @change="createTableData(scope.row, 1)"
                          :formatter="(value:any) => `￥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                          :parser="(value:any) => value.replace(/￥\s?|(,*)/g, '')"
                          @keydown="handleInputKeydown($event, scope, 3)"
                          :ref="(el) => setRef(el, `input-${scope.$index}-3`)"></el-input>
              </template>
            </el-table-column>
            <el-table-column prop="creditAmount" align="right" header-align="center" label="贷方金额">
              <template #default="scope">
                <span v-if="!scope.row.editing">
                  {{ formatAmountRed(scope.row.creditAmount) }}
                </span>
                <el-input v-else v-model="scope.row.creditAmount"
                          @change="createTableData(scope.row, 2)"
                          :formatter="(value:any) => `￥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                          :parser="(value:any) => value.replace(/￥\s?|(,*)/g, '')"
                          @keydown="handleInputKeydown($event, scope, 4)"
                          :ref="(el) => setRef(el, `input-${scope.$index}-4`)"></el-input>
              </template>
            </el-table-column>
            <el-table-column prop="index" align="center" width="60">
              <template #default="scope">
                <span>{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
          </el-table>
          <el-table :data="tableSumData.slice(-1)" :span-method="spanMethod"
                    :show-header="false"
                    @row-click="closeEditAll"
                    header-row-class-name="rv-table-header-row"
                    header-cell-class-name="rv-table-header-cell"
                    row-class-name="rv-table-count-row"
                    cell-class-name="rv-table-cell"
                    class="rv-table-count">
            <el-table-column prop="summary" align="center" header-align="center" label="">
              <template #default="scope">
                <span v-if="!scope.row.editing">{{ scope.row.summary }}</span>
                <el-input v-else v-model="scope.row.summary"></el-input>
              </template>
            </el-table-column>
            <el-table-column align="left" header-align="center" label=""></el-table-column>
            <el-table-column align="left" header-align="center" label=""></el-table-column>
            <el-table-column prop="debitAmount" align="right" header-align="center" label="">
              <template #default="scope">
                <span>
                  {{ formatAmount(scope.row.debitAmount) }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="creditAmount" align="right" header-align="center" label="">
              <template #default="scope">
                <span>
                  {{ formatAmount(scope.row.creditAmount) }}
                </span>
              </template>
            </el-table-column>
            <el-table-column label="" width="60"/>
          </el-table>
        </div>
      </div>
      <!--   备注   -->
      <div class="remark-info" @click="closeEditAll">
        <span class="remark-info-name">备注</span>
        <el-input :readonly="isPrintMode" :required="true"
                  class="remark-info-value" type="textarea"
                  v-model="formData.remark"
                  :input-style="{'box-shadow': 'none'}"
                  :show-word-limit="!isPrintMode"
                  :maxlength="128" resize="none"></el-input>
      </div>
      <!--   审核人信息   -->
      <div class="apply-info">
        <div class="apply-info-item">
          <span>会计主管：</span>
          <span>{{ formData.managerName }}</span>
        </div>
        <div class="apply-info-item">
          <span>过账：</span>
          <span>{{ formData.senderName }}</span>
        </div>
        <div class="apply-info-item">
          <span>复核：</span>
          <span>{{ formData.auditMemberName }}</span>
        </div>
        <div class="apply-info-item">
          <span>制单：</span>
          <span>{{ formData.createdName }}</span>
        </div>
      </div>
    </div>
    <!-- 右键功能区域 -->
    <div v-if="visibleContextmenu && !isPrintMode" class="contextmenu"
         :style="{ left: leftMenu + 'px', top: topMenu + 'px' }">
      <el-button :disabled="currentRow.cfg_index === 0" plain @click="handleMoveUp">向上移动</el-button>
      <el-button :disabled="currentRow.cfg_index === formData.items.length - 1" plain @click="handleMoveDown">向下移动
      </el-button>
      <el-button :disabled="currentRow.cfg_index === 0" plain @click="handleMoveToStart">移至首位</el-button>
      <el-button :disabled="currentRow.cfg_index === formData.items.length - 1" plain @click="handleMoveToEnd">移至末尾
      </el-button>
      <el-button plain @click="handleDelete">删除本项</el-button>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {computed, onBeforeUpdate, onMounted, reactive, ref, watch} from 'vue'
import SelectAuxiliary from "./SelectAuxiliary/index.vue"
import {ElLoading, ElMessage, ElMessageBox, ElSelect, TableColumnCtx} from 'element-plus'
import {parseTime} from "@/utils/Surpass";
import * as subjectApi from "@/api/system/standard/standard-subject"
import {draftVoucher, getOneVoucher, getVoucherAbleWordNum, submitVoucher} from "@/api/system/voucher/voucher";
import {validateForm} from "@/utils"
import {useRoute} from "vue-router";
import bookStore from "@/store/modules/bookStore";
import Decimal from 'decimal.js'


interface RecordingVoucher {
  id: any,
  summary: string,
  subjectId: string,
  detailedSubjectCode: string,
  detailedAccounts: string,
  debitAmount: number | string,
  creditAmount: number | string,
  subjectBalance: number | undefined,
  auxiliary: Array<any>,
  editing: boolean,
  columnIndex: number,
}

interface SpanMethodProps {
  row: RecordingVoucher
  column: TableColumnCtx<RecordingVoucher>
  rowIndex: number
  columnIndex: number
}

const currBookStore = bookStore()
let voucherDate = parseTime(new Date(), "{y}-{m}") === currBookStore.termCurrent
    ? parseTime(new Date(), "{y}-{m}-{d}")
    : currBookStore.termCurrent ? currBookStore.termCurrent + "-01" : parseTime(new Date(), "{y}-{m}-{d}")
const props: any = defineProps({
  // 自动提交，内部直接调用接口提交
  auto: {
    type: Boolean,
    default: true
  },
  dialog: {
    type: Boolean,
    default: false
  },
  edit: {
    type: Boolean,
    default: true
  },
  // 数据
  modelValue: {
    type: Object,
    default: {
      id: null,
      word: null,
      bookId: null,
      wordHead: '记',
      voucherWordNumber: '',
      wordNum: null,
      companyId: null,
      companyName: null,
      remark: '',
      receiptNum: 0,
      debitAmount: null,
      creditAmount: null,
      voucherYear: null,
      voucherMonth: null,
      voucherDate: null,
      carryForward: null,
      auditMemberId: null,
      auditMemberName: null,
      auditDate: null,
      status: null,
      items: []
    },
  }
})
const auxiliaryVisible = ref<Array<boolean>>([])
const route: any = useRoute()
// 定义回调接口，提交按钮触发
const emit: any = defineEmits(['submit', "update:modelValue"])
const visibleContextmenu = ref(false)
const submitButtonLoading = ref(false)
const leftMenu = ref(0)
const topMenu = ref(0)
const currentRow = ref<any>(null)
const printMe = ref(null)
// 会计科目数据
const subjectList = ref<any>([])
const subjectKeyItem = ref<any>({})
const subjectKeyIdItem = ref<any>({})
const shortcuts = [
  {
    text: '当天',
    value: parseTime(new Date(), '{y}-{m}-{d}'),
  },
  {
    text: '当期第一天',
    value: () => {
      return currBookStore.termCurrent + '-01'
    },
  },
  {
    text: '当期最后一天',
    value: () => {
      const parts = currBookStore.termCurrent.split('-')
      const year = parseInt(parts[0], 10)
      const month = parseInt(parts[1], 10)
      const date = new Date(year, month, 0) // 下月的第0天 = 本月最后一天
      return parseTime(date, '{y}-{m}-{d}')
    },
  },
  {
    text: '上一个工作日',
    value: () => {
      const date = new Date()
      const day = date.getDay()
      if (day === 1) {
        // 周一 -> 周五
        date.setDate(date.getDate() - 3)
      } else if (day === 0) {
        // 周日 -> 周五
        date.setDate(date.getDate() - 2)
      } else {
        // 其他工作日 -> 前一天
        date.setDate(date.getDate() - 1)
      }
      return parseTime(date, '{y}-{m}-{d}')
    },
  },
  {
    text: '本月第一天',
    value: () => {
      const date = new Date()
      date.setDate(1)
      return parseTime(date, '{y}-{m}-{d}')
    },
  },
  {
    text: '本月最后一天',
    value: () => {
      const date = new Date()
      const year = date.getFullYear()
      const month = date.getMonth() + 1
      const lastDay = new Date(year, month, 0) // 下月0号 = 本月最后一天
      return parseTime(lastDay, '{y}-{m}-{d}')
    },
  },
  {
    text: '上月第一天',
    value: () => {
      const date = new Date()
      date.setMonth(date.getMonth() - 1)
      date.setDate(1)
      return parseTime(date, '{y}-{m}-{d}')
    },
  },
  {
    text: '上月最后一天',
    value: () => {
      const date = new Date()
      date.setDate(0) // 当前月的第0天即上月最后一天
      return parseTime(date, '{y}-{m}-{d}')
    },
  },
  {
    text: '本季度第一天',
    value: () => {
      const date = new Date()
      const currentMonth = date.getMonth()
      const quarterStartMonth = currentMonth - (currentMonth % 3)
      date.setMonth(quarterStartMonth)
      date.setDate(1)
      return parseTime(date, '{y}-{m}-{d}')
    },
  },
  {
    text: '今年第一天',
    value: () => {
      const date = new Date()
      date.setMonth(0)
      date.setDate(1)
      return parseTime(date, '{y}-{m}-{d}')
    },
  }
]

// 凭证明细数据-列表展示
const tableSumData = ref<RecordingVoucher[]>([]);
const countRow = ref<RecordingVoucher>({
  columnIndex: 0,
  editing: false,
  id: -1,
  summary: `合  计`,
  subjectId: '',
  detailedSubjectCode: "",
  detailedAccounts: '',
  debitAmount: 0,
  creditAmount: 0,
  subjectBalance: undefined,
  auxiliary: []
});
const formData = ref<any>({...props.modelValue})
const isPrintMode = computed(() => {
  return route.query.mode === 'print' || !props.edit
})
const cascaderSubjectProps = {
  expandTrigger: 'hover',
  label: 'name',
  value: 'code',
  children: 'children',
  checkStrictly: false,
  emitPath: false,
  defaultExpandAll: true,
  showAllLevels: false,
  clearable: false,
  filterable: true,
  filterMethod: (item: any, keyword: any) => {
    if (!keyword) return true
    return item.text.toLowerCase().indexOf(keyword.toLowerCase()) > -1
        || item.value.indexOf(keyword) > -1
        || (item.data.pinyinDisplayCode)?.toLowerCase().indexOf(keyword.toLowerCase()) > -1
  }
}
// 创建一个响应式的对象来存储 refs
const refs = reactive<{ [key: string]: any }>({});
// 创建一个响应式的对象来存储 refs
const setRef = (el: any, key: string) => {
  if (el) {
    refs[key] = el;
    // 当元素被设置时，如果它应该被聚焦，则聚焦它
    if (shouldFocusRef(key)) {
      setTimeout(() => {
        focusRefElement(el);
      }, 0);
    }
  }
};
// 存储应该聚焦的元素的 key
const focusedRefKey = ref('');
// 判断是否应该聚焦某个 ref 元素
const shouldFocusRef = (key: string) => {
  return key === focusedRefKey.value;
};
// 聚焦到 ref 元素
const focusRefElement = (el: any) => {
  if (el && el.focus) {
    el.focus();
  } else if (el && el.$el && el.$el.focus) {
    el.$el.focus();
  } else if (el && el.$refs && el.$refs.input && el.$refs.input.focus) {
    el.$refs.input.focus();
  }
};
// 新增一项
const onAddItem = () => {
  formData.value.items.push({
    id: new Date().getTime(),
    summary: '摘要',
    subjectId: '',
    subjectName: '',
    subjectCode: '',
    detailedAccounts: '',
    debitAmount: "",
    creditAmount: "",
    subjectBalance: undefined,
    auxiliary: [],
  })
  createTableData()
}

// 用于合并合计行的span-method
const spanMethod = ({row, column, rowIndex, columnIndex}: SpanMethodProps) => {
  if (rowIndex === tableSumData.value.length - 1) {  // 判断是否为合计行
    if (columnIndex === 0) {
      // 合并合计行的第 1、2、3 列
      return {rowspan: 1, colspan: 3};
    } else if (columnIndex === 1 || columnIndex === 2) {
      return {rowspan: 0, colspan: 0};
    }
  }
  return {rowspan: 1, colspan: 1};
};

/**
 * 刷新table，建立汇总行
 */
const createTableData = (row?: RecordingVoucher, direction?: any) => {
  if (row && direction === 1) {
    row.creditAmount = ""
  } else if (row && direction === 2) {
    row.debitAmount = ""
  }
  auxiliaryVisible.value.length = 0
  // 对应科目余额变动
  const subjectBalances: any = {}
  formData.value.items.filter((item: any) => {
    return item.subjectId && props.edit && route.query.mode !== 'print'
  }).forEach((item: any) => {
    // 初始化科目余额
    if (!subjectBalances[item.subjectId]) {
      item.subjectBalance = new Decimal(subjectKeyIdItem.value[item.subjectId]?.balance || 0)
      subjectBalances[item.subjectId] = item.subjectBalance
    } else {
      item.subjectBalance = new Decimal(subjectBalances[item.subjectId])
    }
    if (parseFloat(item.creditAmount) >= 0) {
      item.subjectBalance = item.subjectBalance.minus(new Decimal(item.creditAmount || 0))
    } else if (parseFloat(item.debitAmount) >= 0) {
      item.subjectBalance = item.subjectBalance.plus(new Decimal(item.debitAmount || 0))
    }
    subjectBalances[item.subjectId] = item.subjectBalance
  });
  const debitAmount = formData.value.items.reduce((total: Decimal, item: any) => {
    if (!total) {
      return new Decimal(item.debitAmount || 0)
    }
    return total.plus(new Decimal(item.debitAmount || 0));
  }, 0);
  const creditAmount = formData.value.items.reduce((total: Decimal, item: any) => {
    if (!total) {
      return new Decimal(item.creditAmount || 0)
    }
    return total.plus(new Decimal(item.creditAmount || 0));
  }, 0);
  const data = formData.value.items.map((t: any) => {
    auxiliaryVisible.value.push(false)
    return t
  })
  // 初始数据量不够则用空行填充
  if (data.length < 7) {
    const maxRow = 7 - data.length
    for (let i = 0; i < maxRow; i++) {
      data.push({
        id: 0,
        summary: '',
        subjectId: '',
        subjectName: '',
        subjectCode: '',
        detailedAccounts: '',
        debitAmount: '',
        creditAmount: '',
        subjectBalance: undefined,
        auxiliary: [],
      })
    }
  }
  countRow.value = {
    id: -1,
    summary: `合  计`,
    subjectId: '',
    detailedSubjectCode: "",
    detailedAccounts: '',
    debitAmount: debitAmount,
    creditAmount: creditAmount,
    subjectBalance: undefined,
    auxiliary: [],
  }
  formData.value.debitAmount = debitAmount
  formData.value.creditAmount = creditAmount
  data.push(countRow.value)
  tableSumData.value = data
}

/**
 * 金额转中文大写
 * @param num
 */
const convertToChinese = (num: number): string => {
  const CN_NUM = ['零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'];
  const CN_UNIT = ['', '拾', '佰', '仟', '万', '亿', '兆'];
  const CN_DECIMAL_UNIT = ['角', '分']; // 小数部分单位：角，分

  // 处理负数
  if (num < 0) {
    return '负' + convertToChinese(-num);
  }

  // 处理整数部分和小数部分
  let integerPart = Math.floor(num);  // 整数部分
  let decimalPart = Math.round((num - integerPart) * 100); // 小数部分，保留两位小数

  let integerStr = '';
  let decimalStr = '';

  // 处理整数部分
  if (integerPart === 0) {
    integerStr = CN_NUM[0]; // 0对应"零"
  } else {
    let unitPos = 0;
    let zeroFlag = false; // 处理连续的零
    while (integerPart > 0) {
      const digit = integerPart % 10;
      if (digit === 0) {
        if (!zeroFlag) {
          zeroFlag = true;
          integerStr = CN_NUM[digit] + integerStr; // 插入"零"
        }
      } else {
        zeroFlag = false;
        integerStr = CN_NUM[digit] + CN_UNIT[unitPos] + integerStr;
      }
      integerPart = Math.floor(integerPart / 10);
      unitPos++;
    }
  }

  // 处理小数部分
  if (decimalPart > 0) {
    decimalStr = '';
    for (let i = 0; i < CN_DECIMAL_UNIT.length; i++) {
      const digit = Math.floor(decimalPart / Math.pow(10, 1 - i)) % 10;
      if (digit > 0) {
        decimalStr += CN_NUM[digit] + CN_DECIMAL_UNIT[i];
      }
    }
  }

  // 如果没有小数部分，删除"角"和"分"
  if (decimalStr === '') {
    decimalStr = '';
  }

  // 拼接整数部分和小数部分
  let result = integerStr + '元' + decimalStr;

  // 如果是整数且没有小数部分，去掉"角分"
  if (decimalStr === '') {
    result = result.replace(/元$/, '元整');
  }

  return result;
};

/**
 * 格式化金额到千分位
 * @param value
 */
const formatAmount = (value: any) => {
  if (!value && value !== 0) return '';
  value = `￥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  return value
}

const formatAmountRed = (value: any) => {
  if (!value && value !== 0) return '';
  if (new Decimal(value).lt(0)) {
    value = new Decimal(0).minus(new Decimal(value))
  }
  return formatAmount(value)
}

const isRedWord = (value: any) => {
  if (!value || value == "") {
    return false;
  }
  return new Decimal(value).lt(0);
}

function tableCellClassName({row, column, rowIndex, columnIndex}: any) {
  //注意这里是解构
  //利用单元格的 className 的回调方法，给行列索引赋值
  row.index = rowIndex;
  column.index = columnIndex;
  return "rv-table-cell"
}

// 行点击,启用编辑状态
const rowClick = (row: any, column: any, event: Event) => {
  if (route.query.mode === 'print' || !props.edit) {
    return
  }
  closeEditAll()
  if (row.id > 0) {
    row.editing = true
  } else {
    onAddItem()
  }
  // 设置焦点
  const rowIndex = tableSumData.value.indexOf(row);
  if (rowIndex >= 0) {
    focusedRefKey.value = getColumnRefKey(rowIndex, row.columnIndex || 0);
  }
  event.stopPropagation()
}

const cellClick = (row: any, column: any, cell: HTMLTableCellElement, event: Event) => {
  row.columnIndex = column.index
  rowClick(row, column, event)
  console.log(column, row.columnIndex)
}

const headerClick = (column: any, event: Event) => {
  closeEditAll()
}
const closeEditAll = () => {
  visibleContextmenu.value = false
  tableSumData.value.forEach((t: any) => {
    t.editing = false
  })
}

// 右键
const rowContextmenu = (row: any, column: any, event: Event) => {
  if (row.id > 0) {
    leftMenu.value = event?.clientX - 20
    topMenu.value = event?.clientY
    currentRow.value = JSON.parse(JSON.stringify(row))
    currentRow.value.cfg_index = tableSumData.value.indexOf(row);
    visibleContextmenu.value = true
    event.preventDefault();
  }
}
const moveItemInArray = (arr: any, fromIndex: any, toIndex: any) => {
  const item = arr.splice(fromIndex, 1)[0];
  arr.splice(toIndex, 0, item);
}
const handleMoveUp = () => {
  if (currentRow.value.cfg_index > 0) {
    moveItemInArray(formData.value.items, currentRow.value.cfg_index, currentRow.value.cfg_index - 1);
    createTableData();
  }
  visibleContextmenu.value = false;
};

const handleMoveDown = () => {
  if (currentRow.value.cfg_index < formData.value.items.length - 1) {
    moveItemInArray(formData.value.items, currentRow.value.cfg_index, currentRow.value.cfg_index + 1);
    createTableData();
  }
  visibleContextmenu.value = false;
};

const handleMoveToStart = () => {
  if (currentRow.value.cfg_index !== 0) {
    moveItemInArray(formData.value.items, currentRow.value.cfg_index, 0);
    createTableData();
  }
  visibleContextmenu.value = false;
};

const handleMoveToEnd = () => {
  if (currentRow.value.cfg_index !== formData.value.items.length - 1) {
    moveItemInArray(formData.value.items, currentRow.value.cfg_index, formData.value.items.length);
    createTableData();
  }
  visibleContextmenu.value = false;
};
const handleDelete = () => {
  tableSumData.value.splice(currentRow.value.cfg_index, 1)
  formData.value.items.splice(currentRow.value.cfg_index, 1)
  visibleContextmenu.value = false
  createTableData()
}

function isCurrentOrFutureMonth(date: Date) {
  const now = new Date(currBookStore.termCurrent + "-01")
  return parseTime(date, "{y}-{m}-{d}") < parseTime(now, "{y}-{m}-{d}")
}

// 打印
const onPrint = () => {
  const url = "/surpass/temporary/voucher-print?mode=print";
  window.localStorage.setItem("voucher-print-data", JSON.stringify(formData.value))
  window.open(url, "_blank")

}

function printSpecificDiv(printcontent: any) {
  const restorepage = document.body.innerHTML; // 保存当前页面的HTML结构
  document.body.innerHTML = printcontent   // 将body的内容替换为要打印的div内容
  window.print(); // 打印当前页面（即现在的body内容）
  document.body.innerHTML = restorepage; // 恢复页面内容
  window.close();
}

function resetList() {
  formData.value = {...props.modelValue}
  formData.value.id = null
  formData.value.bookId = currBookStore.bookId
  formData.value.companyName = currBookStore.getBookItem().companyName
  formData.value.items = []
  // 获取可用凭证子号
  const now = new Date(voucherDate)
  getVoucherAbleWordNum(formData.value.wordHead, parseTime(now, "{y}"), parseTime(now, "{m}")
  ).then((res: any) => {
    formData.value.wordNum = res.data
  })
  createTableData()
}

// 更新会计科目ID关联
const updateSubjectKeys = (items: any) => {
  for (let valueKey in items) {
    const item = items[valueKey]
    item.auxiliary = item.auxiliary && item.auxiliary.startsWith("[") ? JSON.parse(item.auxiliary) : []
    subjectKeyItem.value[item.code] = item
    subjectKeyIdItem.value[item.id] = item
    if (item.children && item.children.length > 0) {
      updateSubjectKeys(item.children)
    }
  }
}

const onSubmitDraft = () => {
  formData.value.items = formData.value.items.filter((item: any) => {
    return item.subjectCode
  })
  let firstItemDebit: any = undefined;
  formData.value.items.forEach((item: any, index: number) => {
    if (!firstItemDebit) {
      firstItemDebit = item
    }
  });

  if (firstItemDebit && (formData.value.remark == null || formData.value.remark === '')) {
    formData.value.remark = firstItemDebit.summary
  }

  submitButtonLoading.value = true
  draftVoucher(formData.value).then((res: any) => {
    ElMessage.info(res.message || `暂存成功`)
    getOneVoucher(res.data).then((voucherRes: any) => {
      formData.value = voucherRes.data
    })
  }).finally(() => {
    submitButtonLoading.value = false
  })
}

const onReset = () => {
  let bl = false
  formData.value.items.forEach((item: any, index: number) => {
    if (item.subjectId && !formData.value.id) {
      bl = true
    }
  })
  if (bl) {
    ElMessageBox.confirm(
        '尚未保存当前已录入凭证，确认要清空吗？',
        '系统提示',
        {confirmButtonText: '清空并重新录入', cancelButtonText: '继续录入', type: 'warning'}
    )
        .then(() => {
          resetList()
        })
        .catch(() => {
        });
  } else {
    resetList()
  }
}

// 提交数据
const onSubmit = () => {
  if (!formData.value.id) {
    ElMessage.error(`请先暂存后提交`)
    return false;
  }

  validateForm(formData.value, {
    bookId: [
      {required: true, message: '所属账套不能为空', trigger: 'blur'},
    ],
    word: [
      {required: true, message: '凭证字不能为空', trigger: 'blur'},
    ],
    companyName: [
      {required: true, message: '公司名称不能为空', trigger: 'blur'},
    ],
    items: [
      {required: true, message: '凭证明细不能为空', trigger: 'blur'},
      {min: 1, message: '请添加凭证明细项', trigger: 'blur'},
    ],
  }).then((res: any) => {
    let validRes = true
    let firstItemDebit: any = undefined;
    formData.value.items = formData.value.items
        .filter((item: any) => {
          return item.subjectId || item.creditAmount || item.debitAmount || (item.auxiliary && item.auxiliary.length > 0)
        })
        .map((item: any, index: number) => {
          if (!item.subjectId) {
            ElMessage.error(`第${index + 1}项没有选择总账科目`)
            validRes = false
            return false;
          }
          if (item.subjectId && !item.creditAmount && !item.debitAmount) {
            ElMessage.error(`第${index + 1}项没有输入借方/贷方金额`)
            validRes = false
            return false;
          }

          if (!item.summary) {
            ElMessage.error(`第${index + 1}项没有输入摘要`)
            validRes = false
            return false;
          }

          if (!firstItemDebit && item.subjectId && item.debitAmount > 0) {
            firstItemDebit = item
          }

          const subject = subjectKeyItem.value[item.subjectCode]
          const auxiliary = subject.auxiliary || []
          auxiliary.forEach((aux: any) => {
            const auxItems = item.auxiliary.filter((auxItem: any) => {
              return auxItem.id === aux.value
            })
            // 必选，没有选择
            if (aux.must && (auxItems.length === 0 || auxItems[0].value.length === 0)) {
              ElMessage.error(`第${index + 1}项没有选择辅助核算项目`)
              validRes = false
              return false;
            }
          })

          return item
        })
    if (validRes) {
      if (firstItemDebit && !formData.value.remark) {
        formData.value.remark = firstItemDebit.summary
      }
      submitButtonLoading.value = true
      submitVoucher(formData.value).then((res: any) => {
        ElMessage.info(res.message || `提交成功`)
        resetList()
        emit("submit", res)
      }).catch((err: any) => {
        ElMessage.error(err.message)
        emit("submit", err)
      }).finally(() => {
        submitButtonLoading.value = false
      })
    }
  }).catch((err: any) => {
    console.error(err)
    for (let errKey in err) {
      ElMessage.error(err[errKey])
      break
    }
  })
}

const handleSubjectChange = (scope: any, value: any) => {
  scope.row.auxiliary = []
  const subject = subjectKeyItem.value[scope.row.subjectCode]
  scope.row.subjectId = subject.id
  scope.row.subjectName = subject.name

  createTableData()
  auxiliaryVisible.value[scope.$index] = false
  // handleAuxiliaryShow(scope)
}

function getSubjectName(scope: any) {
  let name = ""
  let code = scope.row.subjectCode
  let item = subjectKeyItem.value[code]
  if (!item) {
    return ''
  }
  name = scope.row.subjectCode + " " + item.displayName;
  const color = (scope.row.subjectBalance || 0) >= 0 ? '#8c939d' : '#red'
  name += "<br /><span style='color: " + color + "'>余额：" + formatAmount((scope.row.subjectBalance || 0)) + "</span>"

  return name ? name.substring(0, name.length - 1) : ''
}

function getSubjectDetailName(scope: any) {
  if (scope.row.auxiliary && scope.row.auxiliary.length > 0) {
    const labels = scope.row.auxiliary.map((item: any) => {
      const values = item.value.map((v: any) => {
        return v.label
      })
      return item.label + ":" + values.join(",")
    })
    return `${labels.join("；")}`
  } else {
    return ''
  }
}

const handleAuxiliaryShow = (scope: any) => {
  if (!scope.row.subjectCode) {
    return
  }
  if (subjectKeyItem.value[scope.row.subjectCode].auxiliary
      && subjectKeyItem.value[scope.row.subjectCode]?.auxiliary.length > 0) {
    scope.row.columnIndex = 2
    auxiliaryVisible.value[scope.$index] = !auxiliaryVisible.value[scope.$index]
  } else {
    auxiliaryVisible.value[scope.$index] = false
  }
  auxiliaryVisible.value = [...auxiliaryVisible.value]; // 触发响应式
}

function handleWordHead(v: any) {
  getVoucherAbleWordNum(
      formData.value.wordHead,
      parseTime(voucherDate, "{y}"),
      parseTime(voucherDate, "{m}")
  ).then((res: any) => {
    formData.value.wordNum = res.data
  })
}

function handleVoucherDate(v: any) {
  voucherDate = v
  // getVoucherAbleWordNum(
  //     formData.value.wordHead,
  //     parseTime(voucherDate, "{y}"),
  //     parseTime(voucherDate, "{m}")
  // )
  //     .then((res: any) => {
  //       formData.value.wordNum = res.data
  //     })
}

// ******************键盘监听支持 开始******************
const handleKeydown = (event: KeyboardEvent) => {
  if (event.key !== 'Tab') return;

  const acData = tableSumData.value.filter((t: any) => {
    return t.editing
  })
  if (acData.length > 0) {
    const refName = getColumnRefKey(acData[0].index, 0)
    const activeEl = document.activeElement as HTMLElement;
    const isInputOrTextarea = activeEl.tagName === 'INPUT' || activeEl.tagName === 'TEXTAREA';
    if (!isInputOrTextarea) {
      refs[refName].$el.click()
    }
    // console.log('存在激活项', refName, refs[refName], activeEl)
    return
  }
};
// 处理 input 元素的键盘事件
const handleInputKeydown = (event: KeyboardEvent, scope: any, columnIndex: number) => {
  const key = event.key;
  if (key === 'Tab' || (key === 'Enter' && columnIndex !== 2 && columnIndex !== 1)) {
    event.preventDefault();
    console.log('key', key, columnIndex, scope)

    // 阻止默认行为
    if (event.shiftKey && key === 'Tab') {
      // Shift + Tab 向前移动
      moveToPreviousCell(scope, columnIndex);
    } else if (key === 'Tab' || key === 'Enter') {
      // Tab 或 Enter 向后移动
      moveToNextCell(scope, columnIndex);
    }
    if (focusedRefKey.value.indexOf('cascader') > -1) {
      refs[focusedRefKey.value].togglePopperVisible(true)
    }
  } else if (key === 'Enter' && columnIndex === 2) {
    event.preventDefault();
    handleAuxiliaryShow(scope)
  }
};

// 处理 cascader 组件的键盘事件
const handleCascaderKeydown = (event: KeyboardEvent, scope: any, columnIndex: number) => {
  const key = event.key;
  if (key === 'Tab') {
    event.preventDefault();
    console.log('key', key, columnIndex, scope)

    if (event.shiftKey && key === 'Tab') {
      // Shift + Tab 向前移动
      moveToPreviousCell(scope, columnIndex);
    } else if (key === 'Tab' || key === 'Enter') {
      // Tab 或 Enter 向后移动
      moveToNextCell(scope, columnIndex);
    }
  }
};

const moveToNextCell = (scope: any, columnIndex: number) => {
  const row = scope.row;
  const rowIndex = tableSumData.value.indexOf(row);

  // 关闭当前编辑状态
  closeEditAll();

  // 如果当前是最后一列（贷方金额），则移动到下一行的第一列（摘要）
  if (columnIndex === 4) {
    // 移动到下一行
    if (rowIndex < tableSumData.value.length - 2) { // 不包括合计行
      const nextRow = tableSumData.value[rowIndex + 1];
      nextRow.editing = true;
      nextRow.columnIndex = 0;
      if (nextRow.id <= 0) {
        onAddItem()
      }
      // 设置焦点 key
      focusedRefKey.value = `input-${rowIndex + 1}-0`;
      // 确保数据同步
      const itemIndex = formData.value.items.indexOf(row);
      if (itemIndex !== -1 && itemIndex + 1 < formData.value.items.length) {
        formData.value.items[itemIndex + 1].editing = true;
        formData.value.items[itemIndex + 1].columnIndex = 0;
      }
    } else {
      onAddItem()
      moveToNextCell(scope, columnIndex)
    }
  } else {
    // 同行移动到下一列
    row.editing = true;
    row.columnIndex = columnIndex + 1;
    focusedRefKey.value = getColumnRefKey(rowIndex, columnIndex + 1);

    // 特殊处理：从科目列(1)到辅助核算列(2)时，需要打开辅助核算
    if (columnIndex === 1) {
      setTimeout(() => {
        handleAuxiliaryShow(scope);
      }, 0);
    }
  }
};

const moveToPreviousCell = (scope: any, columnIndex: number) => {
  const row = scope.row;
  const rowIndexOld = tableSumData.value.indexOf(row);

  // 关闭当前编辑状态
  closeEditAll();

  // 如果当前是第一列（摘要），则移动到上一行的最后一列（贷方金额）
  if (columnIndex === 0) {
    // 移动到上一行
    let rowIndex = rowIndexOld
    if (rowIndex === 0) {
      rowIndex += 1
    }
    if (rowIndex > 0) {
      const prevRow = tableSumData.value[rowIndex - 1];
      prevRow.editing = true;
      prevRow.columnIndex = rowIndexOld === 0 ? 0 : 4;
      focusedRefKey.value = `input-${rowIndex - 1}-${prevRow.columnIndex}`;
      // 确保数据同步
      const itemIndex = formData.value.items.indexOf(row);
      if (itemIndex > 0) {
        formData.value.items[itemIndex - 1].editing = true;
        formData.value.items[itemIndex - 1].columnIndex = prevRow.columnIndex;
      }
    }
  } else {
    // 同行移动到前一列
    row.editing = true;
    row.columnIndex = columnIndex - 1;
    focusedRefKey.value = getColumnRefKey(rowIndexOld, columnIndex - 1);
  }
};

// 获取列对应的 ref key
const getColumnRefKey = (rowIndex: number, columnIndex: number) => {
  // 根据列索引确定组件类型
  if (columnIndex === 1) { // 科目列使用 cascader
    return `cascader-${rowIndex}-1`;
  } else { // 其他列使用 input
    return `input-${rowIndex}-${columnIndex}`;
  }
};

// ******************键盘监听支持 结束******************

watch(
    () => props.modelValue,
    (newVal: any) => {
      if (newVal && newVal.id) {
        formData.value = {...newVal};
        createTableData()
      }
    },
    {immediate: true}
);
watch(
    () => props.edit,
    (newVal: boolean) => {
      console.log(newVal)
    },
    {immediate: true}
);

function loanBalance() {
  //console.log("debitAmount "+countRow.value.debitAmount);
  return new Decimal(countRow.value.debitAmount).eq(new Decimal(countRow.value.creditAmount));
  //return false;
}

function formatVoucherWordNum() {
  return formData.value.wordHead + parseTime(new Date(), "{y}") + '第' + String(formData.value.wordNum).padStart(4, '0') + '号';
}

onMounted(() => {
  props.modelValue.voucherDate = voucherDate
  if (!formData.value.voucherDate) {
    formData.value.voucherDate = voucherDate
  }
  if (!props.dialog) {
    resetList()
  }
  if (!formData.value.companyName) {
    formData.value.companyName = currBookStore.getBookItem().companyName
  }
  if (!formData.value.bookId) {
    formData.value.bookId = currBookStore.bookId
  }
  if (!formData.value.wordHead) {
    formData.value.wordHead = '收'
  }
  if (!formData.value.wordNum) {
    const now = new Date(voucherDate)
    // 获取可用凭证子号
    getVoucherAbleWordNum(formData.value.wordHead, parseTime(now, "{y}"), parseTime(now, "{m}"))
        .then((res: any) => {
          formData.value.wordNum = res.data
        })
  }

  if (route.query.mode === 'print') {

    const tempFormData = JSON.parse(window.localStorage.getItem("voucher-print-data") || '')
    tempFormData.items = tempFormData.items.filter((item: any) => {
      return item.subjectCode
    })
    formData.value = tempFormData
    const loadingInstance = ElLoading.service({fullscreen: true})
    setTimeout(() => {
      loadingInstance.close()
      setTimeout(() => {
        document.title = "凭证打印-" + formatVoucherWordNum();
        printSpecificDiv(document.body.innerHTML)
      }, 100)
    }, 1000)
  }

  //传入当前账套ID
  subjectApi.getTree({
    bookId: currBookStore.bookId
  }).then((res: any) => {
    subjectList.value = res.data
    updateSubjectKeys(subjectList.value)
    createTableData()
  })
})


// 清空 refs 对象以防止重复引用
onBeforeUpdate(() => {
  Object.keys(refs).forEach(key => {
    delete refs[key];
  });
  focusedRefKey.value = '';
});

// 监听整个页面的焦点变化
document.addEventListener('focusin', (event: FocusEvent) => {
  const target = event.target as HTMLElement;
  const isInputOrTextarea =
      target.tagName === 'INPUT' || target.tagName === 'TEXTAREA' || target.tagName === 'LI';
  // console.log('是否是输入区域:', target.tagName, isInputOrTextarea);

  const acData = tableSumData.value.filter((t: any) => {
    return t.editing
  })
  if (acData.length > 0) {
    const refName = getColumnRefKey(acData[0].index, 0)
    if (!isInputOrTextarea) {
      refs[refName].$el.click()
    }
  }
});
//
// document.addEventListener('focusout', (event: FocusEvent) => {
//   const target = event.target as HTMLElement;
//   console.log('失去焦点的元素:', target);
// });
</script>

<style scoped lang="scss">
@import "../../assets/styles/recording-voucher";

@media print and (orientation: landscape) {
  .app-container {
    width: 1090px;
  }
}

@media print and (orientation: portrait) {
  .app-container {
    width: 1090px; /* 纵向模式下的宽度 */
  }
}

.redWord {
  color: red;
}

.app-container {
  background-color: #FFFFFF !important;
  padding: 30px 20px;

  .top-funs {
    position: fixed;
    top: calc(64px + 100px + 40px);
    right: 50px;
    z-index: 1000;

    &.top-funs-left {
      top: calc(64px + 100px + 46px);
      left: 230px;
      right: auto;

      display: flex;
      justify-content: space-between;
      align-items: center;
      text-align: center;
      color: $primary-color;
      font-size: 0.9em;

      .bottom-counts-item {
        width: 200px;
        color: green;

        &.isNotPh {
          color: red;
        }
      }
    }

    &.topFunsUpdate {
      top: 82px;
      left: auto;
      right: 1000px;
    }
  }

  .printable-content {
    margin-top: 65px;

    .header-title {
      margin-bottom: 10px;
      text-align: center;

      &-text {
        text-align: justify;
        text-align-last: justify;
        font-size: 2em;
        color: $primary-color;
        border-bottom: 4px double $border-color;
      }

      &-time {
        padding-top: 5px;
        color: $primary-color;
        font-size: 0.9em;
        display: block;
      }
    }

    .company-info {
      margin-bottom: 3px;
      display: flex;
      justify-content: space-between;
      align-items: center;

      &-left {

      }

      &-right {
        display: flex;
        justify-content: flex-end;
        align-items: center;
      }

      .company-info-item {
        margin-right: 15px;
        font-size: 0.9em;
        font-weight: bold;
        color: $primary-color !important;
      }
    }

    .remark-info {
      width: 100%;
      border: 1px solid $border-color; /* 第一条线 */
      outline: 1px solid $border-color; /* 第二条线 */
      outline-offset: 1px; /* 控制两条线之间的距离 */
      font-size: 0.9em;
      color: $primary-color;
      height: 70px;

      display: flex;
      justify-content: flex-start;
      align-items: center;

      &-name {
        height: 70px;
        line-height: 70px;
        text-align: center;
        width: 100px;
        border-right: 1px solid $border-color;
      }

      &-value {
        flex: 1;
      }
    }

    .apply-info {
      font-size: 0.9em;
      margin: 8px 100px 8px 20px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      color: $primary-color;
    }
  }

  // 右键
  .contextmenu {
    position: fixed;
    z-index: 1000;
    text-align: left;
    display: flex;
    justify-content: flex-start;
    flex-direction: column;
    border: 1px solid #8c939d;
    border-radius: 5px;

    .el-button {
      width: 100px;
      margin-left: 0 !important;
    }
  }

  .no-border-input {
    :deep(.el-input__wrapper) {
      padding: 0 3px; /* 减少顶部和底部的内边距 */
      margin-top: -3px; /* 使用负的外边距使元素上移 */
      box-shadow: none;
      border-bottom: 1px solid $border-color;
      border-radius: 0;

      input {
        color: $primary-color !important;
      }
    }

    &.input-number {
      display: flex;
      justify-content: flex-end;
      align-items: center;

      :deep(.el-input__wrapper) {
        input {
          text-align: center;
        }
      }
    }
  }

  .bottom-counts {
    position: fixed;
    bottom: 40px;
    right: 100px;
    z-index: 1000;
    display: flex;
    justify-content: space-between;
    align-items: center;
    text-align: center;
    color: $primary-color;
    font-size: 0.9em;

    .bottom-counts-item {
      width: 200px;
      color: green;

      &.isNotPh {
        color: red;
      }
    }
  }
}
</style>
<style lang="scss">
@import "../../assets/styles/recording-voucher";

.rv-table, .rv-table-count {
  border: 1px solid $border-color; /* 第一条线 */
  outline: 1px solid $border-color; /* 第二条线 */
  outline-offset: 1px; /* 控制两条线之间的距离 */

  &.el-table .el-table__header-wrapper th {
    height: 30px !important;
  }
}

.rv-table-header-row {
  border: 2px solid $border-color !important;
}

.rv-table-header-cell, .rv-table-cell {
  border: 1px dashed $border-color;
}

.rv-table-header-cell {
  color: $primary-color !important;
  padding: 0 !important;
}

.rv-table-cell {
  height: 36px !important;
  padding: 0 !important;
}

.rv-table-row:first-child {
  .rv-table-cell {
    border-top: 2px solid $border-color;
  }
}

.rv-table-count-row:last-child {
  .rv-table-cell {
    border-top: none;
  }
}

.rv-table-count-row:last-child {
  .rv-table-cell {
    font-weight: bold;
  }

  .rv-table-cell:first-child {
    color: $primary-color;
  }
}

// 序号表格
.rvo-table {
  border: none !important;

  &.el-table .el-table__header-wrapper th {
    height: 30px !important;
    background-color: transparent !important;
  }

  &.el-table--border:after {
    width: 0;
  }

  &.el-table--border .el-table__inner-wrapper:after {
    height: 0;
  }

  &.el-table--fit .el-table__inner-wrapper:before {
    height: 0;
  }
}

.rvo-table-header-row {
  border: none !important;
}

.rvo-table-header-cell, .rvo-table-cell {
  border: none !important;
}

.rvo-table-header-cell {
  padding: 0 !important;
}

.rvo-table-cell {
  height: 36px !important;
  padding: 0 !important;
  color: $serial-color;
}

</style>
