<template>
  <div class="data-source-page">
    <div class="page-header">
      <h2>数据源管理</h2>
      <p>管理数据库连接配置，支持多种数据库类型</p>
    </div>

    <div class="page-content">
      <!-- 操作栏 -->
      <div class="action-bar">
        <el-button type="primary" @click="showCreateDialog">
          新增数据源
        </el-button>
        <el-button @click="refreshList">
          刷新
        </el-button>
      </div>

      <!-- 数据源列表 -->
      <el-table :data="dataSourceList" border v-loading="loading" style="width: 100%">
        <el-table-column header-align="center" prop="name" label="名称"/>
        <el-table-column header-align="center" prop="driverClassName" label="驱动类型"/>
        <el-table-column header-align="center" prop="url" label="连接URL" show-overflow-tooltip/>
        <el-table-column header-align="center" prop="username" label="用户名"/>
        <el-table-column header-align="center" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column header-align="center" prop="createdDate" label="创建时间" width="180"/>
        <el-table-column header-align="center" label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-tooltip content="测试连接" placement="top">
              <el-button link icon="Position" @click="testConnection(row)"></el-button>
            </el-tooltip>
            <el-tooltip content="编辑" placement="top">
              <el-button link icon="Edit" type="primary" @click="editDataSource(row)"></el-button>
            </el-tooltip>
            <el-tooltip content="移除" placement="top">
              <el-button link icon="Delete" type="danger" @click="deleteDataSource(row)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <!-- 空状态 -->
      <el-empty v-if="!loading && dataSourceList.length === 0" description="暂无数据源"/>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
        :title="dialogTitle"
        v-model="dialogVisible"
        width="600px"
        :before-close="handleDialogClose"
    >
      <el-form
          ref="formRef"
          :model="formData"
          :rules="formRules"
          label-width="100px"
      >
        <el-form-item label="数据源名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入数据源名称"/>
        </el-form-item>

        <el-form-item label="驱动类型" prop="driverClassName">
          <el-select v-model="formData.driverClassName" placeholder="请选择驱动类型" style="width: 100%">
            <el-option label="MySQL" value="com.mysql.cj.jdbc.Driver"/>
            <el-option label="PostgreSQL" value="org.postgresql.Driver"/>
            <el-option label="Oracle" value="oracle.jdbc.OracleDriver"/>
            <el-option label="SQL Server" value="com.microsoft.sqlserver.jdbc.SQLServerDriver"/>
            <el-option label="H2" value="org.h2.Driver"/>
          </el-select>
        </el-form-item>

        <el-form-item label="连接URL" prop="url">
          <el-input v-model="formData.url" placeholder="请输入数据库连接URL"/>
        </el-form-item>

        <el-form-item label="用户名" prop="username">
          <el-input v-model="formData.username" placeholder="请输入用户名"/>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
              v-model="formData.password"
              type="password"
              placeholder="请输入密码"
              show-password
          />
        </el-form-item>

        <el-form-item label="测试SQL" prop="testSql">
          <el-input v-model="formData.testSql" placeholder="请输入测试SQL，默认为 SELECT 1"/>
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleDialogClose">取消</el-button>
          <el-button type="primary" @click="testConnectionBeforeSave" :loading="testing">
            测试连接并保存
          </el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">
            保存
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted, computed} from 'vue'
import {ElLoading, ElMessage, ElMessageBox} from 'element-plus'
import * as dataSourceApi from '@/api/api-service/dataSource.ts'

// 响应式数据
const loading = ref(false)
const dialogVisible = ref(false)
const testing = ref(false)
const submitting = ref(false)
const isEdit = ref(false)
const formRef = ref()

const dataSourceList = ref([])

const formData = reactive({
  id: null,
  name: '',
  driverClassName: '',
  url: '',
  username: '',
  password: '',
  testSql: 'SELECT 1',
  status: 1
})

// 表单验证规则
const formRules = {
  name: [
    {required: true, message: '请输入数据源名称', trigger: 'blur'}
  ],
  driverClassName: [
    {required: true, message: '请选择驱动类型', trigger: 'change'}
  ],
  url: [
    {required: true, message: '请输入连接URL', trigger: 'blur'}
  ],
  username: [
    {required: true, message: '请输入用户名', trigger: 'blur'}
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'}
  ]
}

// 计算属性
const dialogTitle = computed(() => isEdit.value ? '编辑数据源' : '新增数据源')

// 生命周期
onMounted(() => {
  loadDataSources()
})

// 方法
const loadDataSources = async () => {
  try {
    loading.value = true
    const response = await dataSourceApi.list()
    dataSourceList.value = response.data || []
  } catch (error) {
    ElMessage.error('加载数据源列表失败')
    console.error('加载数据源列表失败:', error)
  } finally {
    loading.value = false
  }
}

const refreshList = () => {
  loadDataSources()
}

const showCreateDialog = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const editDataSource = (row) => {
  isEdit.value = true
  Object.assign(formData, {...row})
  // 注意：编辑时密码字段需要特殊处理，这里简单置空
  formData.password = ''
  dialogVisible.value = true
}

const resetForm = () => {
  Object.assign(formData, {
    id: null,
    name: '',
    driverClassName: '',
    url: '',
    username: '',
    password: '',
    testSql: 'SELECT 1',
    status: 1
  })
  formRef.value?.clearValidate()
}

const handleDialogClose = () => {
  dialogVisible.value = false
  resetForm()
}

const testConnectionBeforeSave = async () => {
  try {
    await formRef.value.validate()
    testing.value = true

    const testData = {...formData}
    const response = await dataSourceApi.testConnectionWithData(testData)

    if (response.data) {
      ElMessage.success('连接测试成功')
      await handleSubmit()
    } else {
      ElMessage.error('连接测试失败')
    }
  } catch (error) {
    ElMessage.error('连接测试失败')
    console.error('连接测试失败:', error)
  } finally {
    testing.value = false
  }
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitting.value = true

    if (isEdit.value) {
      await dataSourceApi.update(formData.id, formData)
      ElMessage.success('更新成功')
    } else {
      await dataSourceApi.create(formData)
      ElMessage.success('创建成功')
    }

    dialogVisible.value = false
    loadDataSources()
  } catch (error) {
    ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
    console.error('保存失败:', error)
  } finally {
    submitting.value = false
  }
}

const testConnection = async (row) => {
  const loadingInstance = ElLoading.service({})
  try {
    const response = await dataSourceApi.testConnection(row.id)
    console.log(response)
    if (response) {
      ElMessage.success('连接测试成功')
    } else {
      ElMessage.error('连接测试失败')
    }
  } catch (error) {
    ElMessage.error('连接测试失败')
    console.error('连接测试失败:', error)
  } finally {
    loadingInstance.close()
  }
}

const deleteDataSource = async (row) => {
  try {
    await ElMessageBox.confirm(
        `确定要删除数据源 "${row.name}" 吗？此操作不可恢复。`,
        '确认删除',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
    )

    await dataSourceApi.deleteData(row.id)
    ElMessage.success('删除成功')
    loadDataSources()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
      console.error('删除失败:', error)
    }
  }
}
</script>

<style scoped>
.data-source-page {
  background: #fff;
  border-radius: 4px;
  padding: 20px;
  min-height: calc(100vh - 140px);
}

.page-header {
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e6e6e6;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.action-bar {
  margin-bottom: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
