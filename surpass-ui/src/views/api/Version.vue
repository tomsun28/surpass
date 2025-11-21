<template>
  <div class="version-page">
    <div class="page-header">
      <h2>版本管理</h2>
      <p>管理API版本，配置SQL模板和响应模板</p>
    </div>

    <div class="page-content">
      <!-- API选择 -->
      <div class="api-selector">
        <el-select
          v-model="selectedApiId"
          placeholder="请选择API"
          style="width: 300px"
          @change="loadVersions"
          :loading="apiLoading"
        >
          <el-option
            v-for="api in apiList"
            :key="api.id"
            :label="api.name"
            :value="api.id"
          />
        </el-select>
      </div>

      <!-- 操作栏 -->
      <div class="action-bar" v-if="selectedApiId">
        <el-button type="primary" @click="showCreateDialog">
          <el-icon><Plus /></el-icon>
          新增版本
        </el-button>
        <el-button type="success" @click="createNextVersion" v-if="hasPublishedVersion">
          <el-icon><CopyDocument /></el-icon>
          新增下一个版本
        </el-button>
        <el-button @click="refreshList">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>

      <!-- 版本统计 -->
      <div class="version-statistics" v-if="selectedApiId && versionStatistics">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-statistic title="总版本数" :value="versionStatistics.total" />
          </el-col>
          <el-col :span="6">
            <el-statistic title="草稿" :value="versionStatistics.draft" />
          </el-col>
          <el-col :span="6">
            <el-statistic title="待发布" :value="versionStatistics.pending" />
          </el-col>
          <el-col :span="6">
            <el-statistic title="已发布" :value="versionStatistics.published" />
          </el-col>
        </el-row>
      </div>

      <!-- 版本列表 -->
      <el-table :data="versionList" v-loading="loading" style="width: 100%" v-if="selectedApiId">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="version" label="版本号" width="100" />
        <el-table-column prop="sqlTemplate" label="SQL模板" />
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="350" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="viewVersion(row)">查看</el-button>
            <el-button size="small" type="primary" @click="editVersion(row)" v-if="row.status === 0">编辑</el-button>
            <el-button size="small" type="warning" @click="submitForReview(row)" v-if="row.status === 0">
              提交审核
            </el-button>
            <el-button size="small" type="success" @click="publishVersion(row)" v-if="row.status === 1">
              发布
            </el-button>
            <el-button size="small" type="danger" @click="rejectVersion(row)" v-if="row.status === 1">
              驳回
            </el-button>
            <el-button size="small" type="info" @click="offlineVersion(row)" v-if="row.status === 2">
              下线
            </el-button>
            <el-button size="small" type="success" @click="onlineVersion(row)" v-if="row.status === 3">
              上线
            </el-button>
            <el-button size="small" type="danger" @click="deleteVersion(row)" v-if="row.status === 0">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 空状态 -->
      <el-empty
        v-if="selectedApiId && !loading && versionList.length === 0"
        description="暂无版本"
      />
      <el-empty
        v-if="!selectedApiId"
        description="请选择API"
      />
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="800px"
      :before-close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="版本号" prop="version">
          <el-input-number
            v-model="formData.version"
            :min="1"
            :max="999"
            placeholder="请输入版本号"
          />
        </el-form-item>

        <el-form-item label="SQL模板" prop="sqlTemplate">
          <el-input
            v-model="formData.sqlTemplate"
            type="textarea"
            :rows="4"
            placeholder="请输入SQL模板，支持命名参数如 :id"
          />
        </el-form-item>

        <el-form-item label="参数定义" prop="paramDefinition">
          <el-input
            v-model="formData.paramDefinition"
            type="textarea"
            :rows="3"
            placeholder="请输入参数定义JSON"
          />
        </el-form-item>

        <el-form-item label="响应模板" prop="responseTemplate">
          <el-input
            v-model="formData.responseTemplate"
            type="textarea"
            :rows="3"
            placeholder="请输入响应模板，支持Mustache语法"
          />
        </el-form-item>

        <el-form-item label="描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="2"
            placeholder="请输入版本描述"
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

    <!-- 版本详情对话框 -->
    <el-dialog
      title="版本详情"
      v-model="detailDialogVisible"
      width="800px"
    >
      <el-descriptions :column="1" border v-if="currentVersion">
        <el-descriptions-item label="版本号">
          {{ currentVersion.version }}
        </el-descriptions-item>
        <el-descriptions-item label="SQL模板">
          <pre>{{ currentVersion.sqlTemplate }}</pre>
        </el-descriptions-item>
        <el-descriptions-item label="参数定义">
          <pre>{{ currentVersion.paramDefinition || '无' }}</pre>
        </el-descriptions-item>
        <el-descriptions-item label="响应模板">
          <pre>{{ currentVersion.responseTemplate || '无' }}</pre>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTagType(currentVersion.status)">
            {{ getStatusText(currentVersion.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="描述">
          {{ currentVersion.description || '无' }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ currentVersion.createTime }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import * as publishApi from '@/api/api-service/publishApi.ts'
import * as apiDefinitionApi from '@/api/api-service/apiDefinitionApi.ts'
import * as apiVersionApi from '@/api/api-service/apiVersionApi.ts'
import { Plus, Refresh, CopyDocument } from '@element-plus/icons-vue'

const route = useRoute()

// 响应式数据
const loading = ref(false)
const apiLoading = ref(false)
const dialogVisible = ref(false)
const detailDialogVisible = ref(false)
const submitting = ref(false)
const isEdit = ref(false)
const formRef = ref()

const selectedApiId = ref(null)
const apiList = ref([])
const versionList = ref([])
const currentVersion = ref(null)
const versionStatistics = ref(null)

const formData = reactive({
  id: null,
  apiId: null,
  version: 1,
  sqlTemplate: '',
  paramDefinition: '',
  responseTemplate: '',
  description: ''
})

// 表单验证规则
const formRules = {
  version: [
    { required: true, message: '请输入版本号', trigger: 'blur' }
  ],
  sqlTemplate: [
    { required: true, message: '请输入SQL模板', trigger: 'blur' }
  ]
}

// 计算属性
const dialogTitle = computed(() => isEdit.value ? '编辑版本' : '新增版本')

const hasPublishedVersion = computed(() => {
  return versionList.value.some(version => version.status === 2) // 2 = 已发布
})

// 生命周期
onMounted(() => {
  loadApis()
  // 如果有URL参数，自动选择API
  if (route.query.apiId) {
    selectedApiId.value = parseInt(route.query.apiId)
    loadVersions()
  }
})

// 方法
const loadApis = async () => {
  try {
    apiLoading.value = true
    const response = await apiDefinitionApi.list()
    apiList.value = response || []
  } catch (error) {
    ElMessage.error('加载API列表失败')
    console.error('加载API列表失败:', error)
  } finally {
    apiLoading.value = false
  }
}

const loadVersions = async () => {
  if (!selectedApiId.value) return

  try {
    loading.value = true
    const [versionsResponse, statisticsResponse] = await Promise.all([
      apiVersionApi.getByApiId(selectedApiId.value),
      apiVersionApi.getVersionStatistics(selectedApiId.value)
    ])

    versionList.value = versionsResponse || []
    versionStatistics.value = statisticsResponse || null
  } catch (error) {
    ElMessage.error('加载版本列表失败')
    console.error('加载版本列表失败:', error)
  } finally {
    loading.value = false
  }
}

const refreshList = () => {
  loadVersions()
}

const showCreateDialog = () => {
  if (!selectedApiId.value) {
    ElMessage.warning('请先选择API')
    return
  }

  isEdit.value = false
  resetForm()
  formData.apiId = selectedApiId.value

  // 自动设置下一个版本号
  if (versionList.value.length > 0) {
    const maxVersion = Math.max(...versionList.value.map(v => v.version))
    formData.version = maxVersion + 1
  } else {
    formData.version = 1
  }

  dialogVisible.value = true
}

const editVersion = (row) => {
  if (row.status !== 0) {
    ElMessage.warning('只有草稿状态的版本可以编辑')
    return
  }

  isEdit.value = true
  Object.assign(formData, { ...row })
  dialogVisible.value = true
}

const viewVersion = (row) => {
  currentVersion.value = row
  detailDialogVisible.value = true
}

const resetForm = () => {
  Object.assign(formData, {
    id: null,
    apiId: null,
    version: 1,
    sqlTemplate: '',
    paramDefinition: '',
    responseTemplate: '',
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
      await apiVersionApi.update(formData.id, formData)
      ElMessage.success('更新成功')
    } else {
      await apiVersionApi.create(formData)
      ElMessage.success('创建成功')
    }

    dialogVisible.value = false
    loadVersions()
  } catch (error) {
    ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
    console.error('保存失败:', error)
  } finally {
    submitting.value = false
  }
}

const submitForReview = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要提交版本 v${row.version} 审核吗？`,
      '确认提交审核',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await publishApi.submitForReview(row.apiId, row.id)
    ElMessage.success('提交审核成功')
    loadVersions()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('提交审核失败')
      console.error('提交审核失败:', error)
    }
  }
}

const publishVersion = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要发布版本 v${row.version} 吗？`,
      '确认发布',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await publishApi.publishVersion(row.apiId, row.id)
    ElMessage.success('发布成功')
    loadVersions()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('发布失败')
      console.error('发布失败:', error)
    }
  }
}

const rejectVersion = async (row) => {
  try {
    const { value: reason } = await ElMessageBox.prompt(
      '请输入驳回原因',
      '驳回版本',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'textarea',
        inputPlaceholder: '请输入驳回原因...'
      }
    )

    if (reason) {
      await publishApi.rejectVersion(row.apiId, row.id, reason)
      ElMessage.success('版本已驳回')
      loadVersions()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('驳回失败')
      console.error('驳回失败:', error)
    }
  }
}

const offlineVersion = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要下线版本 v${row.version} 吗？`,
      '确认下线',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await publishApi.offlineVersion(row.apiId, row.id)
    ElMessage.success('下线成功')
    loadVersions()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('下线失败')
      console.error('下线失败:', error)
    }
  }
}

const onlineVersion = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要重新上线版本 v${row.version} 吗？`,
      '确认上线',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await publishApi.onlineVersion(row.apiId, row.id)
    ElMessage.success('上线成功')
    loadVersions()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('上线失败')
      console.error('上线失败:', error)
    }
  }
}

const createNextVersion = async () => {
  if (!selectedApiId.value) {
    ElMessage.warning('请先选择API')
    return
  }

  try {
    // 查找当前已发布的版本
    const publishedVersion = versionList.value.find(v => v.status === 2)
    if (!publishedVersion) {
      ElMessage.warning('没有找到已发布的版本')
      return
    }

    // 计算下一个版本号
    const maxVersion = Math.max(...versionList.value.map(v => v.version))
    const nextVersion = maxVersion + 1

    // 填充表单数据
    isEdit.value = false
    resetForm()
    formData.apiId = selectedApiId.value
    formData.version = nextVersion
    formData.sqlTemplate = publishedVersion.sqlTemplate
    formData.paramDefinition = publishedVersion.paramDefinition
    formData.responseTemplate = publishedVersion.responseTemplate
    formData.description = `基于版本 v${publishedVersion.version} 迭代`

    dialogVisible.value = true

  } catch (error) {
    ElMessage.error('创建下一个版本失败')
    console.error('创建下一个版本失败:', error)
  }
}

const deleteVersion = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除版本 v${row.version} 吗？此操作不可恢复。`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await apiVersionApi.deleteData(row.id)
    ElMessage.success('删除成功')
    loadVersions()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
      console.error('删除失败:', error)
    }
  }
}

const getStatusTagType = (status) => {
  const types = {
    0: 'info',    // 草稿
    1: 'warning', // 待发布
    2: 'success', // 已发布
    3: 'danger'   // 下线
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    0: '草稿',
    1: '待发布',
    2: '已发布',
    3: '下线'
  }
  return texts[status] || '未知'
}
</script>

<style scoped>
.version-page {
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

.api-selector {
  margin-bottom: 20px;
}

.action-bar {
  margin-bottom: 20px;
}

.version-statistics {
  margin-bottom: 20px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

pre {
  background: #f5f7fa;
  padding: 8px;
  border-radius: 4px;
  margin: 0;
  font-family: 'Courier New', monospace;
  font-size: 12px;
  white-space: pre-wrap;
  word-break: break-all;
}
</style>
