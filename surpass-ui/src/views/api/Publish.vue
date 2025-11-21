<template>
  <div class="publish-page">
    <div class="page-header">
      <h2>发布管理</h2>
      <p>查看API发布历史和发布状态</p>
    </div>

    <div class="page-content">
      <!-- API选择 -->
      <div class="api-selector">
        <el-select
          v-model="selectedApiId"
          placeholder="请选择API"
          style="width: 300px"
          @change="loadPublishHistory"
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

      <!-- 当前发布状态 -->
      <div class="current-publish" v-if="selectedApiId && latestPublish">
        <el-card class="publish-status-card">
          <template #header>
            <div class="card-header">
              <span>当前发布状态</span>
            </div>
          </template>
          <div class="publish-info">
            <div class="info-item">
              <span class="label">当前版本：</span>
              <el-tag type="success">v{{ latestPublish.apiVersion?.version }}</el-tag>
            </div>
            <div class="info-item">
              <span class="label">发布时间：</span>
              <span>{{ latestPublish.publishTime }}</span>
            </div>
            <div class="info-item">
              <span class="label">操作人：</span>
              <span>{{ latestPublish.operator || '系统' }}</span>
            </div>
            <div class="info-item">
              <span class="label">描述：</span>
              <span>{{ latestPublish.description || '无' }}</span>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 发布历史 -->
      <div class="publish-history" v-if="selectedApiId">
        <h3>发布历史</h3>
        <el-table :data="publishHistory" v-loading="loading" style="width: 100%">
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column label="版本" width="100">
            <template #default="{ row }">
              <el-tag v-if="row.apiVersion">v{{ row.apiVersion.version }}</el-tag>
              <el-tag v-else type="info">v{{ row.versionId }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="publishTime" label="发布时间" width="180" />
          <el-table-column prop="operator" label="操作人" width="120" />
          <el-table-column prop="description" label="描述" />
          <el-table-column label="操作" width="120" fixed="right">
            <template #default="{ row }">
              <el-button size="small" @click="viewVersionDetail(row)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 空状态 -->
        <el-empty
          v-if="!loading && publishHistory.length === 0"
          description="暂无发布历史"
        />
      </div>

      <!-- 空状态 -->
      <el-empty
        v-if="!selectedApiId"
        description="请选择API"
      />
    </div>

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
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import * as publishApi from '@/api/api-service/publishApi.ts'
import * as apiDefinitionApi from '@/api/api-service/apiDefinitionApi.ts'
import * as apiVersionApi from '@/api/api-service/apiVersionApi.ts'

// 响应式数据
const loading = ref(false)
const apiLoading = ref(false)
const detailDialogVisible = ref(false)

const selectedApiId = ref(null)
const apiList = ref([])
const publishHistory = ref([])
const latestPublish = ref(null)
const currentVersion = ref(null)

// 生命周期
onMounted(() => {
  loadApis()
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

const loadPublishHistory = async () => {
  if (!selectedApiId.value) return

  try {
    loading.value = true

    // 加载发布历史
    const historyResponse = await publishApi.getPublishHistory(selectedApiId.value)
    publishHistory.value = historyResponse || []

    // 加载已发布版本
    const versionResponse = await apiVersionApi.getPublishedVersion(selectedApiId.value)
    if (versionResponse) {
      // 直接使用已发布版本信息
      latestPublish.value = {
        apiVersion: versionResponse,
        publishTime: versionResponse.updateTime || versionResponse.createTime,
        operator: '系统',
        description: '当前已发布版本'
      }
    } else {
      latestPublish.value = null
    }

  } catch (error) {
    ElMessage.error('加载发布历史失败')
    console.error('加载发布历史失败:', error)
  } finally {
    loading.value = false
  }
}

const viewVersionDetail = async (row) => {
  try {
    // 如果发布记录中有版本信息，直接使用
    if (row.apiVersion) {
      currentVersion.value = row.apiVersion
      detailDialogVisible.value = true
    } else {
      // 如果没有版本信息，通过API获取
      const versionResponse = await apiVersionApi.getById(row.versionId)
      currentVersion.value = versionResponse
      detailDialogVisible.value = true
    }
  } catch (error) {
    ElMessage.error('加载版本详情失败')
    console.error('加载版本详情失败:', error)
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
.publish-page {
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

.current-publish {
  margin-bottom: 30px;
}

.publish-status-card {
  max-width: 600px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.publish-info {
  display: grid;
  gap: 12px;
}

.info-item {
  display: flex;
  align-items: center;
}

.info-item .label {
  font-weight: bold;
  min-width: 80px;
  color: #606266;
}

.publish-history h3 {
  margin: 0 0 16px 0;
  color: #303133;
  font-size: 16px;
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
