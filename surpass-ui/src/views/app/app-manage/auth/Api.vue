<template>
  <div class="api-page">
    <div class="page-content">
      <!-- 操作栏 -->
      <div class="action-bar">
        <el-button type="primary" @click="showCreateDialog">
          新增API
        </el-button>
        <el-button @click="refreshList">
          刷新
        </el-button>
      </div>

      <!-- API列表 -->
      <el-table :data="apiList" border v-loading="loading" style="width: 100%">
        <el-table-column header-align="center" prop="name" label="API名称" />
        <el-table-column header-align="center" prop="path" label="路径" />
        <el-table-column header-align="center" prop="method" label="方法" width="100">
          <template #default="{ row }">
            <el-tag :type="getMethodTagType(row.method)">
              {{ row.method }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column header-align="center" prop="datasourceId" label="数据源">
          <template #default="{ row }">
            <el-tag v-if="row.datasourceId">
              {{ dataSourceList.find(ds => ds.id === row.datasourceId)?.name }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column header-align="center" prop="description" label="描述" />
        <el-table-column header-align="center" prop="createdDate" label="创建时间" width="180" />
        <el-table-column header-align="center" label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-tooltip content="版本管理" placement="top">
              <el-button link icon="Document" @click="viewVersions(row)"></el-button>
            </el-tooltip>
            <el-tooltip content="编辑" placement="top">
              <el-button link icon="Edit" type="primary" @click="editApi(row)"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link icon="Delete" type="danger" @click="deleteApi(row)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <!-- 空状态 -->
      <el-empty v-if="!loading && apiList.length === 0" description="暂无API定义" />
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
        <el-form-item label="API名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入API名称" />
        </el-form-item>

        <el-form-item label="路径" prop="path">
          <el-input v-model="formData.path" placeholder="请输入API路径，如：/users" />
        </el-form-item>

        <el-form-item label="HTTP方法" prop="method">
          <el-select v-model="formData.method" placeholder="请选择HTTP方法" style="width: 100%">
            <el-option label="GET" value="GET" />
            <el-option label="POST" value="POST" />
            <el-option label="PUT" value="PUT" />
            <el-option label="DELETE" value="DELETE" />
            <el-option label="PATCH" value="PATCH" />
          </el-select>
        </el-form-item>

        <el-form-item label="数据源" prop="datasourceId">
          <el-select
              v-model="formData.datasourceId"
              placeholder="请选择数据源"
              style="width: 100%"
              :loading="dataSourceLoading"
          >
            <el-option
                v-for="ds in dataSourceList"
                :key="ds.id"
                :label="ds.name"
                :value="ds.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="描述" prop="description">
          <el-input
              v-model="formData.description"
              type="textarea"
              :rows="3"
              placeholder="请输入API描述"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleDialogClose">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">
            保存
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted, computed, toRefs} from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import * as dataSourceApi from '@/api/api-service/dataSource.ts'
import * as apiDefinitionApi from '@/api/api-service/apiDefinitionApi.ts'
import {list} from "@/api/api-service/apps.js";

const router = useRouter()

const props = defineProps({
  appId: {
    type: String,
    default: undefined
  }
});

// 响应式数据
const loading = ref(false)
const dataSourceLoading = ref(false)
const dialogVisible = ref(false)
const submitting = ref(false)
const isEdit = ref(false)
const formRef = ref()

const apiList = ref([])
const total = ref(0);
const dataSourceList = ref([])

const data = reactive({
  queryParams: {
    appId: null,
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50]
  }
});

const {queryParams} = toRefs(data);

const formData = reactive({
  id: null,
  name: '',
  path: '',
  method: '',
  datasourceId: null,
  description: ''
})

// 表单验证规则
const formRules = {
  name: [
    { required: true, message: '请输入API名称', trigger: 'blur' }
  ],
  path: [
    { required: true, message: '请输入API路径', trigger: 'blur' }
  ],
  method: [
    { required: true, message: '请选择HTTP方法', trigger: 'change' }
  ],
  datasourceId: [
    { required: true, message: '请选择数据源', trigger: 'change' }
  ]
}

// 计算属性
const dialogTitle = computed(() => isEdit.value ? '编辑API' : '新增API')

// 方法
const loadApis = async () => {
  apiDefinitionApi.pageApi(queryParams.value).then((res) => {
    if (res.code === 0) {
      loading.value = false;
      apiList.value = res.data.rows;
      total.value = res.data.records;
    }
  })
}

const loadDataSources = async () => {
  try {
    dataSourceLoading.value = true
    const response = await dataSourceApi.list()
    dataSourceList.value = response.data || []
  } catch (error) {
    ElMessage.error('加载数据源列表失败')
    console.error('加载数据源列表失败:', error)
  } finally {
    dataSourceLoading.value = false
  }
}

const showCreateDialog = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const editApi = (row) => {
  isEdit.value = true
  Object.assign(formData, { ...row })
  dialogVisible.value = true
}

const resetForm = () => {
  Object.assign(formData, {
    id: null,
    name: '',
    path: '',
    method: '',
    datasourceId: null,
    description: ''
  })
  formRef.value?.clearValidate()
}

const handleDialogClose = () => {
  dialogVisible.value = false
  resetForm()
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitting.value = true

    if (isEdit.value) {
      await apiDefinitionApi.update(formData.id, formData)
      ElMessage.success('更新成功')
    } else {
      await apiDefinitionApi.create(formData)
      ElMessage.success('创建成功')
    }

    dialogVisible.value = false
    loadApis()
  } catch (error) {
    ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
    console.error('保存失败:', error)
  } finally {
    submitting.value = false
  }
}

const viewVersions = (row) => {
  router.push(`/api/Version?apiId=${row.id}`)
}

const deleteApi = async (row) => {
  try {
    await ElMessageBox.confirm(
        `确定要删除API "${row.name}" 吗？此操作不可恢复。`,
        '确认删除',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
    )

    await apiDefinitionApi.deleteData(row.id)
    ElMessage.success('删除成功')
    loadApis()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
      console.error('删除失败:', error)
    }
  }
}

const getMethodTagType = (method) => {
  const types = {
    'GET': 'success',
    'POST': 'primary',
    'PUT': 'warning',
    'DELETE': 'danger',
    'PATCH': 'info'
  }
  return types[method] || 'info'
}

const refreshList = () => {
  loadApis()
}

watch(
    () => props.appId,
    (val) => {
      if (val != null) {
        queryParams.value.appId = val
        loadApis()
        loadDataSources()
      }
    },
    { immediate: true }
)
</script>

<style scoped>
.api-page {
  background: #fff;
  border-radius: 4px;
  padding: 20px;
  min-height: calc(100vh - 140px);
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
