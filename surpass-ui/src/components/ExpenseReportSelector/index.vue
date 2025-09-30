<template>
  <div class="expense-report-selector">
    <el-input v-model="selectedTitle" readonly placeholder="请选择报销单" style="width: 100%">
      <template #append>
        <el-button icon="Search" @click="openDialog"></el-button>
      </template>
    </el-input>

    <el-dialog v-model="dialogVisible" title="选择报销单" width="1000px">
      <!-- 查询 -->
      <el-form :inline="true" :model="queryParams" class="query-form" label-width="80px">
        <el-form-item label="报销人">
          <employee-selector v-model="queryParams.employeeId" @change="fetchList" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable @change="fetchList">
            <el-option label="草稿" value="draft"/>
            <el-option label="已提交" value="submitted"/>
            <el-option label="已审批" value="approved"/>
            <el-option label="已驳回" value="rejected"/>
            <el-option label="已记账" value="posted"/>
            <el-option label="已付款" value="paid"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchList">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table
          :data="reportList"
          v-loading="loading"
          highlight-current-row
          @current-change="handleCurrentChange"
          :row-key="row => row.id"
          height="400px"
          style="margin-top: 10px"
      >
        <el-table-column prop="employeeName" label="报销人" align="center" />
        <el-table-column prop="title" label="标题" align="center" />
        <el-table-column prop="totalAmount" label="金额" align="center" />
        <el-table-column prop="status" label="状态" align="center" />
        <el-table-column prop="submitTime" label="提交时间" align="center" width="180" />
      </el-table>

      <!-- 分页 -->
      <pagination
          v-show="total > 0"
          :total="total"
          v-model:page="queryParams.pageNumber"
          v-model:limit="queryParams.pageSize"
          @pagination="fetchList"
      />

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :disabled="!selectedRow" @click="confirmSelect">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {ref, reactive, watch, defineEmits, defineProps} from 'vue'
import {ElMessage} from 'element-plus'
import * as expenseReportApi from '@/api/expense/expenseReport'
import EmployeeSelector from "@/components/EmployeeSelector/index.vue"

const props = defineProps({
  modelValue: String
})
const emit = defineEmits(['update:modelValue', 'change'])

const dialogVisible = ref(false)
const selectedRow = ref<any>(null)
const selectedTitle = ref('')

const queryParams = reactive({
  pageNumber: 1,
  pageSize: 10,
  employeeId: '',
  status: ''
})
const total = ref(0)
const loading = ref(false)
const reportList = ref([])

function openDialog() {
  dialogVisible.value = true
  fetchList()
}

function fetchList() {
  loading.value = true
  expenseReportApi.listExpenseReport({...queryParams})
      .then(res => {
        reportList.value = res.data.records
        total.value = res.data.total
      })
      .finally(() => loading.value = false)
}

function resetQuery() {
  queryParams.employeeId = ''
  queryParams.status = ''
  queryParams.pageNumber = 1
  fetchList()
}

function handleCurrentChange(row) {
  selectedRow.value = row
}

function confirmSelect() {
  if (!selectedRow.value) {
    ElMessage.warning('请选择一条数据')
    return
  }
  selectedTitle.value = selectedRow.value.title
  emit('update:modelValue', selectedRow.value.id)
  emit('change', selectedRow.value)
  dialogVisible.value = false
}

// 当外部设置了 v-model 值，尝试回显标题
watch(() => props.modelValue, async (newVal) => {
  if (newVal) {
    const res = await expenseReportApi.getExpenseReport(newVal)
    selectedRow.value = res.data
    selectedTitle.value = res.data.title
  } else {
    selectedRow.value = null
    selectedTitle.value = ''
  }
}, {immediate: true})
</script>

<style scoped>
.expense-report-selector {
  width: 100%;
}
.query-form {
  margin-bottom: 10px;
}
</style>
