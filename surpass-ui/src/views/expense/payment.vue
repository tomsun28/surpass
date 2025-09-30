<template>
  <div class="app-container">
    <!-- 查询 -->
    <el-card class="common-card query-box">
      <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="100px">
        <el-form-item label="报销单">
          <el-input v-model="queryParams.reportId" placeholder="" clearable/>
        </el-form-item>
        <el-form-item label="支付方式">
          <el-select style="width: 200px" v-model="queryParams.paymentMethod" clearable placeholder="请选择支付方式">
            <el-option v-for="(item, index) in payment_method" :key="index"
                       :label="item.label" :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card class="common-card">
      <div class="btn-form">
        <el-button type="primary" @click="handleAdd">新增付款记录</el-button>
      </div>
      <el-table :data="paymentList" border v-loading="loading" row-key="id" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50"/>
        <el-table-column prop="reportId" label="报销单" align="center">
          <template #default="scope">
            {{scope.row.reportTitle}}[{{ scope.row.reportId }}]
          </template>
        </el-table-column>
        <el-table-column prop="paymentMethod" label="支付方式" align="center">
          <template #default="scope">
            <dict-tag :options="payment_method" :value="scope.row.paymentMethod"></dict-tag>
          </template>
        </el-table-column>
        <el-table-column prop="bankAccountName" label="付款账户" align="center"/>
        <el-table-column prop="paidAmount" label="实付金额" align="center"/>
        <el-table-column prop="paidTime" label="付款时间" align="center" width="180"/>
        <el-table-column prop="operatorName" label="出纳" align="center"/>
        <el-table-column label="操作" align="center" width="120">
          <template #default="scope">
            <el-button type="text" icon="Edit" @click="handleEdit(scope.row)"/>
            <el-button type="text" icon="Delete" @click="handleDel(scope.row)"/>
          </template>
        </el-table-column>
      </el-table>
      <pagination
          v-show="total > 0"
          :total="total"
          v-model:page="queryParams.pageNumber"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
      />
    </el-card>

    <!-- 弹窗表单 -->
    <el-dialog v-model="dialog.visible" :title="dialog.title" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="报销单" prop="reportId">
          <expense-report-selector placeholder="请选择报销单" v-model="form.reportId" />
        </el-form-item>
        <el-form-item label="支付方式" prop="paymentMethod">
          <el-select v-model="form.paymentMethod" placeholder="请选择">
            <el-option v-for="(item, index) in payment_method" :key="index"
                       :label="item.label" :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="付款账户" prop="bankAccountId">
          <bank-account-selector v-model="form.bankAccountId"/>
        </el-form-item>
        <el-form-item label="实付金额" prop="paidAmount">
          <el-input-number v-model="form.paidAmount" :min="0" :precision="2" style="width: 200px"/>
        </el-form-item>
        <el-form-item label="付款时间" prop="paidTime">
          <el-date-picker v-model="form.paidTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss"/>
        </el-form-item>
        <el-form-item label="操作人" prop="operatorId">
          <employee-selector v-model="form.operatorId"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" :loading="buttonLoading" @click="submitForm">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {ref, reactive, getCurrentInstance} from 'vue'
import {ElForm} from 'element-plus'
import * as paymentApi from '@/api/expense/expensePayment' // 假设API路径
import BankAccountSelector from '@/components/BankAccountSelector/index.vue'
import EmployeeSelector from '@/components/EmployeeSelector/index.vue'
import ExpenseReportSelector from '@/components/ExpenseReportSelector/index.vue'
import DictTag from "@/components/DictTag/index.vue";

const loading = ref(false)
const total = ref(0)
const paymentList = ref([])
const ids = ref([])
const formRef = ref<InstanceType<typeof ElForm>>()
const buttonLoading = ref(false)
const {proxy} = getCurrentInstance()!
const {payment_method} = proxy?.useDict("payment_method")
const queryRef = ref()
const queryParams = reactive({
  pageNumber: 1,
  pageSize: 10,
  reportId: '',
  paymentMethod: ''
})

const form = reactive({
  id: '',
  reportId: '',
  paymentMethod: '',
  bankAccountId: '',
  paidAmount: 0,
  paidTime: '',
  operatorId: ''
})

const rules = {
  reportId: [{required: true, message: '请输入报销单ID', trigger: 'blur'}],
  paymentMethod: [{required: true, message: '请选择支付方式', trigger: 'change'}],
  bankAccountId: [{required: true, message: '请选择付款账户', trigger: 'change'}],
  paidAmount: [{required: true, message: '请输入付款金额', trigger: 'blur'}],
  paidTime: [{required: true, message: '请选择付款时间', trigger: 'change'}],
  operatorId: [{required: true, message: '请选择操作人', trigger: 'change'}]
}

const dialog = reactive({visible: false, title: ''})

function handleQuery() {
  queryParams.pageNumber = 1
  getList()
}

function resetQuery() {
  queryRef.value?.resetFields()
  handleQuery()
}

function getList() {
  loading.value = true
  paymentApi.listExpensePayment(queryParams).then(res => {
    paymentList.value = res.data.records
    total.value = res.data.total
  }).finally(() => (loading.value = false))
}

function handleAdd() {
  Object.assign(form, {
    id: '', reportId: '', paymentMethod: '', bankAccountId: '',
    paidAmount: 0, paidTime: '', operatorId: ''
  })
  dialog.visible = true
  dialog.title = '新增付款记录'
}

function handleEdit(row) {
  paymentApi.getExpensePayment(row.id).then(res => {
    Object.assign(form, res.data)
    dialog.visible = true
    dialog.title = '编辑付款记录'
  })
}

function handleDel(row) {
  paymentApi.deleteExpensePayment(row.id).then(() => getList())
}

function submitForm() {
  formRef.value?.validate(valid => {
    if (!valid) return
    buttonLoading.value = true
    const fn = form.id ? paymentApi.updateExpensePayment : paymentApi.saveExpensePayment
    fn(form).then(() => {
      buttonLoading.value = false
      dialog.visible = false
      getList()
    }).catch(() => buttonLoading.value = false)
  })
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
}

getList()
</script>
