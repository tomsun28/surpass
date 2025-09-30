<template>
  <div class="employee-selector">
    <el-input
        v-model="inputLabel"
        :readonly="true"
        placeholder="请选择员工"
        clearable
        @clear="handleClear"
    >
      <template #append>
        <el-button icon="Search" @click="dialogVisible = true"></el-button>
      </template>
    </el-input>

    <el-dialog title="选择员工" v-model="dialogVisible" width="1000px" @close="handleClose">
      <div class="dialog-container">
        <!-- 搜索区域 -->
        <el-form :inline="true" class="query-form" @submit.prevent>
          <el-form-item label="姓名">
            <el-input v-model="queryParams.displayName" placeholder="请输入姓名" clearable @keyup.enter="handleQuery"/>
          </el-form-item>
          <el-form-item label="工号">
            <el-input v-model="queryParams.employeeNumber" placeholder="请输入工号" clearable
                      @keyup.enter="handleQuery"/>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleQuery">搜索</el-button>
            <el-button @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
        <!-- 表格区域 -->
        <el-table
            :data="employeeList"
            v-loading="loading"
            border
            highlight-current-row
            @current-change="handleRowClick"
            :row-class-name="rowClassName"
            style="margin-top: 10px;"
        >
          <el-table-column prop="employeeNumber" label="工号" align="center" min-width="100"/>
          <el-table-column prop="displayName" label="姓名" align="center" min-width="80"/>
          <el-table-column prop="departmentName" label="部门" align="center" min-width="100"/>
          <el-table-column prop="jobTitle" label="职务" align="center" min-width="100"/>
          <el-table-column
              prop="employeeType"
              label="类型"
              align="center"
              min-width="80"
              :formatter="formatEmployeeType"
          />
          <el-table-column
              prop="employeeStatus"
              label="状态"
              align="center"
              min-width="80"
              :formatter="formatEmployeeStatus"
          />
        </el-table>
        <!-- 分页器 -->
        <pagination
            v-show="total > 0"
            :total="total"
            v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize"
            @pagination="getList"
        />
      </div>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmSelect">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {ref, watch, computed, getCurrentInstance, reactive, toRefs} from 'vue'
import {listEmployee, getEmployee} from '@/api/system/hr/employee'

const props = defineProps<{ modelValue: string | null }>() // 传入的是 employeeId
const emit = defineEmits(['update:modelValue', 'change'])
const employeeInfo = ref<any>(null)
const dialogVisible = ref(false)
const loading = ref(false)
const employeeList = ref<any[]>([])
const total = ref(0)
const selectedRow = ref<any>(null)

const data = reactive({
  queryParams: {
    displayName: '',
    employeeNumber: '',
    pageNum: 1,
    pageSize: 10,
  }
})
const {queryParams} = toRefs(data)

const {proxy} = getCurrentInstance()!
const {users_idType, users_state, employee_types} =
    proxy?.useDict("users_idType", "users_state", "employee_types")

const inputLabel = computed(() => {
  if (employeeInfo.value) {
    return `${employeeInfo.value.displayName || ''} (${employeeInfo.value.employeeNumber || ''})`
  }
  return ''
})

// 清除选中
function handleClear() {
  selectedRow.value = null
  emit('update:modelValue', null)
}

// 获取列表
function getList() {
  loading.value = true
  listEmployee(queryParams.value).then((res) => {
    employeeList.value = res.data.records
    total.value = res.data.total
  }).finally(() => {
    loading.value = false
  })
}

// 搜索
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

// 重置搜索
function resetQuery() {
  queryParams.value.displayName = ''
  queryParams.value.employeeNumber = ''
  handleQuery()
}

// 选中行
function handleRowClick(row: any) {
  selectedRow.value = row
}

function confirmSelect() {
  if (selectedRow.value) {
    emit('update:modelValue', selectedRow.value.id)
    employeeInfo.value = selectedRow.value // 同步信息
    emit('change', selectedRow.value)
    dialogVisible.value = false
  }
}

// 关闭对话框
function handleClose() {
  selectedRow.value = null
}

// 格式化
function formatEmployeeType(row: any) {
  const type = employee_types.value?.find(i => i.value === row.employeeType)
  return type?.label || row.employeeType
}

function formatEmployeeStatus(row: any) {
  const status = users_state.value?.find(i => i.value === row.employeeStatus)
  return status?.label || row.employeeStatus
}

// 行高亮样式
function rowClassName({row}: any) {
  return props.modelValue?.id === row.id ? 'selected-row' : ''
}

function loadEmployeeById(id: string) {
  getEmployee(id).then(res => {
    employeeInfo.value = res.data
  }).catch(() => {
    employeeInfo.value = null
  })
}

// 打开弹窗时加载数据
watch(dialogVisible, (val) => {
  if (val) getList()
})
watch(() => props.modelValue, (val) => {
  if (val) {
    loadEmployeeById(val)
  } else {
    employeeInfo.value = null
  }
}, {immediate: true})
</script>

<style scoped>
.employee-selector {
  width: 100%;
}

.dialog-container {
  margin: 10px 20px;
}

.selected-row {
  background-color: #ecf5ff !important;
}
</style>
