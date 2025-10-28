<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryFormRef" :inline="true"
                 v-show="showSearch" label-width="68px">
          <el-form-item label="科目" prop="category" style="width: 220px;">
            <!--
            <el-radio-group v-model="queryParams.category" @change="handleQuery">
              <el-radio-button v-for="(aux, index) in subjects_category"
                               :key="index"
                               :label="aux.label" :value="aux.value"/>
            </el-radio-group>
            -->
            <el-select v-model="queryParams.category" clearable @change="handleQuery">
              <el-option
                  v-for="dict in subjects_category"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="编码" prop="code">
            <el-input
                v-model="queryParams.code"
                placeholder="请输入编码"
                clearable
                @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-form-item label="名称" prop="name">
            <el-input
                v-model="queryParams.name"
                placeholder="请输入名称"
                clearable
                @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-form-item>
            <el-button @click="handleQuery">{{ t('org.button.query') }}</el-button>
            <el-button @click="resetQuery">{{ t('org.button.reset') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="common-card">
      <div class="btn-form">
        <el-button v-if="ableEdit" v-loading="buttonLoading" type="primary" @click="submitForm">
          保存
        </el-button>
        <el-button type="primary" @click="handleTrialBalance(true)">
          试算平衡
        </el-button>
      </div>

      <el-table v-loading="loading" :data="initBalanceList"
                :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
                row-key="originId" default-expand-all
                @cell-click="cellMouseEnter"
                border
                show-summary
                :summary-method="handleSummaryMethod2"
                height="560">
        <el-table-column type="index" label="序号" align="center" width="60"></el-table-column>
        <el-table-column prop="code" label="编码" align="left" width="160"></el-table-column>
        <el-table-column prop="name" label="名称" align="left" width="300">
          <template #default="scope">
            <div :style="{'text-indent': getIndent(scope.row) + 'em'}">
              {{ scope.row.name }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="direction" label="方向" align="center">
          <template #default="scope">
            <el-tag type="warning" v-if="scope.row.direction == 0">{{ t('subjectDirectionNone') }}</el-tag>
            <el-tag type="warning" v-if="scope.row.direction == 1">{{ t('subjectDebit') }}</el-tag>
            <el-tag type="success" v-if="scope.row.direction == 2">{{ t('subjectCredit') }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="balance" label="余额" align="right" header-align="center"
                         width="200">
          <template #default="scope">
            <span>{{ formatAmount(scope.row.balance) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="openingYearBalanceDebit" label="年初余额（借方）" align="right" header-align="center"
                         width="200">
          <template #default="scope">
            <span v-if="!scope.row.editing || (scope.row.children && scope.row.children.length > 0)">
                  {{ formatAmount(scope.row.openingYearBalanceDebit) }}
            </span>
            <el-input v-else v-model="scope.row.openingYearBalanceDebit"
                      @blur="calculateTotal(initBalanceList)"
                      :formatter="(value:any) => `￥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                      :parser="(value:any) => value.replace(/￥\s?|(,*)/g, '')"></el-input>
          </template>
        </el-table-column>
        <el-table-column prop="openingYearBalanceCredit" label="年初余额（贷方）" align="right" header-align="center"
                         width="200">
          <template #default="scope">
            <span v-if="!scope.row.editing || (scope.row.children && scope.row.children.length > 0)">
                {{ formatAmount(scope.row.openingYearBalanceCredit) }}
            </span>
            <el-input v-else v-model="scope.row.openingYearBalanceCredit"
                      @blur="calculateTotal(initBalanceList)"
                      :formatter="(value:any) => `￥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                      :parser="(value:any) => value.replace(/￥\s?|(,*)/g, '')"></el-input>
          </template>
        </el-table-column>
        <el-table-column prop="debitAmount" label="本年累计（借方）" align="right" header-align="center" width="200">
          <template #default="scope">
            <span v-if="!scope.row.editing || (scope.row.children && scope.row.children.length > 0)">
                  {{ formatAmount(scope.row.debitAmount) }}
            </span>
            <el-input v-else v-model="scope.row.debitAmount"
                      @blur="calculateTotal(initBalanceList)"
                      :formatter="(value:any) => `￥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                      :parser="(value:any) => value.replace(/￥\s?|(,*)/g, '')"></el-input>
          </template>
        </el-table-column>
        <el-table-column prop="creditAmount" label="本年累计（贷方）" align="right" header-align="center" width="200">
          <template #default="scope">
            <span v-if="!scope.row.editing || (scope.row.children && scope.row.children.length > 0)">
                 {{ formatAmount(scope.row.creditAmount) }}
            </span>
            <el-input v-else v-model="scope.row.creditAmount"
                      @blur="calculateTotal(initBalanceList)"
                      :formatter="(value:any) => `￥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                      :parser="(value:any) => value.replace(/￥\s?|(,*)/g, '')"></el-input>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 试算平衡结果弹窗 -->
    <el-dialog title="试算平衡结果" v-model="dialog.visible" width="700px">
      <el-alert v-if="trialDifference" title="恭喜您,您录入的财务初始余额平衡" type="success"
                :closable="false" show-icon/>
      <el-alert v-else title="您录入的财务初始余额不平衡" type="warning"
                :closable="false" show-icon/>
      <el-table :data="trialBalanceResult" style="width: 100%">
        <el-table-column prop="type" label="类型" align="center"></el-table-column>
        <el-table-column prop="debitTotal" label="借方总额" align="right"></el-table-column>
        <el-table-column prop="creditTotal" label="贷方总额" align="right"></el-table-column>
        <el-table-column prop="difference" label="差额" align="right"></el-table-column>
        <el-table-column prop="isBalanced" label="是否平衡" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.isBalanced ? 'success' : 'danger'">
              {{ scope.row.isBalanced ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="dialog.visible = false">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="BookInitBalance">
import {listBookInitBalance, saveBookInitBalance} from "@/api/config/bookInitBalance";
import {useI18n} from "vue-i18n";
import bookStore from "@/store/modules/bookStore";
import {reactive, ref, toRefs, getCurrentInstance} from "vue";
import {ElForm, FormInstance} from "element-plus";
import {formatAmount,} from "@/utils";
import {handleTree, handleTreeToList} from "@/utils/Surpass";
import Decimal from 'decimal.js'
import {handleSummaryMethod, SummaryMethodProps} from "@/utils/Subjects";

const {proxy} = getCurrentInstance()!;
const {subjects_category} = toRefs<any>(proxy?.useDict("subjects_category"));
const {t} = useI18n()
const currBookStore = bookStore()
const initBalanceList = ref([]);
const trialBalanceResult = ref<any>([]);
const trialDifference = ref(false);
const loading = ref(true);
const buttonLoading = ref(false);
const showSearch = ref(true);
const queryFormRef = ref<FormInstance>();
const ableEdit = ref(!currBookStore.initializeStatus);
const dialog = reactive<any>({
  visible: false,
  title: ''
});
const data: any = reactive({
  form: {},
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    orderByColumn: "code",
    isAsc: "asc",
    bookId: currBookStore.bookId,
    category: "",
    assistType: "",
    code: "",
    name: "",
  },
  rules: {}
});

const {queryParams} = toRefs(data);

const cellMouseEnter = (row: any, column: any, cell: HTMLTableCellElement, event: Event) => {
  if (ableEdit.value && !row.hasVoucher) {
    row.isChanged = true
    closeEditAll(initBalanceList.value)
    row.editing = true
  }
}

function closeEditAll(list: any) {
  list.forEach((item: any) => {
    item.editing = false
    if (item.children && item.children.length > 0) {
      closeEditAll(item.children)
    }
  })
}

/** 查询科目列表 */
function getList() {
  loading.value = true;
  listBookInitBalance(queryParams.value).then((response: any) => {
    initBalanceList.value = handleTree(response.data, "originId", "parentId", "children");
    calculateTotal(initBalanceList.value)
    loading.value = false;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  queryFormRef.value?.resetFields();
  handleQuery();
}

/**
 * 试算平衡算法，包括期初余额平衡和累计发生额平衡
 */
const handleTrialBalance = (show?: boolean) => {
  let totalInitialDebit = new Decimal(0); // 期初借方余额总和
  let totalInitialCredit = new Decimal(0); // 期初贷方余额总和
  let totalDebitAmount = new Decimal(0); // 累计借方发生额总和
  let totalCreditAmount = new Decimal(0); // 累计贷方发生额总和

  const list = handleTreeToList(initBalanceList.value, 'children')
  list.filter((item: any) => !item.parentId || item.parentId == item.originId)
      .forEach((item: any) => {
        totalDebitAmount = totalDebitAmount.plus(new Decimal(item.debitAmount || 0));
        totalCreditAmount = totalCreditAmount.plus(new Decimal(item.creditAmount || 0));
        totalInitialDebit = totalInitialDebit.plus(new Decimal(item.openingYearBalanceDebit || 0));
        totalInitialCredit = totalInitialCredit.plus(new Decimal(item.openingYearBalanceCredit || 0));
      });
  const initialBalanceDiff = new Decimal(totalInitialDebit).minus(new Decimal(totalInitialCredit));
  const accumulatedAmountDiff = new Decimal(totalDebitAmount).minus(new Decimal(totalCreditAmount));
  trialBalanceResult.value = [
    {
      type: '年初余额',
      debitTotal: totalInitialDebit.toFixed(2),
      creditTotal: totalInitialCredit.toFixed(2),
      difference: initialBalanceDiff.toFixed(2),
      isBalanced: new Decimal(totalInitialDebit).eq(new Decimal(totalInitialCredit))
    },
    {
      type: '本年累计发生额',
      debitTotal: totalDebitAmount.toFixed(2),
      creditTotal: totalCreditAmount.toFixed(2),
      difference: accumulatedAmountDiff.toFixed(2),
      isBalanced: new Decimal(totalDebitAmount).eq(new Decimal(totalCreditAmount))
    }
  ];
  console.log("initialBalanceDiff " + initialBalanceDiff)
  console.log("accumulatedAmountDiff " + accumulatedAmountDiff)
  trialDifference.value = new Decimal(initialBalanceDiff).eq(new Decimal(0)) && new Decimal(accumulatedAmountDiff).eq(new Decimal(0))
  if (!trialDifference.value || show) {
    dialog.title = '试算平衡结果';
    dialog.visible = true;
    return false;
  }
  return true
}

/** 提交按钮 */
const submitForm = async () => {
  buttonLoading.value = true;
  loading.value = true;

  // 获取所有被修改的科目，包括父级科目
  const dataList = handleTreeToList(initBalanceList.value, 'children')
  const dataMap: any = {}
  dataList.forEach((item: any) => {
    dataMap[item.originId] = item
  })
  dataList.forEach((item: any) => {
    if (item.isChanged) {
      let parent = item
      while (parent.parentId) {
        parent = dataMap[parent.parentId]
        parent.isChanged = true
      }
    }
  })
  const data = dataList.filter((item: any) => item.isChanged || !item.id)
  if (data.length === 0) {
    proxy?.$modal.msgWarning('未修改任何科目余额');
    loading.value = false
    buttonLoading.value = false
    return;
  }
  await saveBookInitBalance(data)
      .finally(() => {
        buttonLoading.value = false
        loading.value = false
      });
  proxy?.$modal.msgSuccess('操作成功');
  dialog.visible = false;
  getList();
  if (!handleTrialBalance(false)) {
    proxy?.$modal.msgError('请保持试算平衡');
  }
};

function getIndent(row: any) {
  if (row.code) {
    return (row.code.split('.').length - 1).toFixed(0)
  } else {
    return 0;
  }
}

/**
 * 计算父级科目金额
 */
const calculateTotal = (list: any) => {
  list.forEach((item: any) => {
    item.debitAmount = handleFormatAmount(item, 'debitAmount');
    item.creditAmount = handleFormatAmount(item, 'creditAmount');
    item.openingYearBalanceDebit = handleFormatAmount(item, 'openingYearBalanceDebit');
    item.openingYearBalanceCredit = handleFormatAmount(item, 'openingYearBalanceCredit');
    item.balance = item.openingYearBalanceDebit.plus(item.debitAmount)
        .minus(item.openingYearBalanceCredit).minus(item.creditAmount)
    if (item.children && item.children.length > 0) {
      calculateTotal(item.children);
    }
  })
}

/**
 * 递归计算树形结构中所有叶子节点指定字段的值的总和
 * @param row 行数据
 * @param field 字段名
 */
const handleFormatAmount = (row: any, field: string) => {
  if (row.children && row.children.length > 0) {
    let sum = new Decimal(0);
    return columnTotal(row, field, sum)
  } else {
    return new Decimal(row[field] || 0)
  }

  function columnTotal(item: any, field: string, sum: Decimal) {
    // 遍历所有子节点
    item.children.forEach((child: any) => {
      // 如果子节点还有自己的子节点，则递归调用columnTotal
      if (child.children && child.children.length > 0) {
        sum = columnTotal(child, field, sum);
      } else {
        // 子节点没有更多子节点（即叶子节点），累加其指定字段的值
        sum = sum.plus(new Decimal(child[field] || 0));
      }
    });
    return sum;
  }
}

function handleSummaryMethod2(param: SummaryMethodProps) {
  return handleSummaryMethod(param, initBalanceList.value, 2, [4, 5, 6, 7, 8])
}

getList();

</script>

<style lang="scss" scoped>
.btn-form {
  text-align: right;
  margin-bottom: 10px;
}

.common-card {
  margin-bottom: 15px;
}

.app-container {
  padding: 0;
  background-color: #f5f7fa;
}
</style>
