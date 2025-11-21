<template>
  <div class="version-page">
    <div class="page-header">
      <h2>版本管理</h2>
      <p>管理API版本，配置SQL模板和响应模板</p>
    </div>

    <div class="page-content">
      <div class="api-selector">
        <el-select
          v-model="selectedApiId"
          placeholder="请选择API"
          style="width: 300px"
        >
          <el-option
            v-for="api in apiList"
            :key="api.id"
            :label="api.name"
            :value="api.id"
          />
        </el-select>
      </div>

      <div class="action-bar" v-if="selectedApiId">
        <el-button type="primary" @click="showCreateDialog">
          <el-icon><Plus /></el-icon>
          新增版本
        </el-button>
        <el-button @click="refreshList">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>

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
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="viewVersion(row)">查看</el-button>
            <el-button size="small" type="primary" @click="editVersion(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteVersion(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="!loading && selectedApiId && versionList.length === 0" description="暂无版本" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import * as apiDefinitionApi from '@/api/api-service/apiDefinitionApi.ts'
import * as apiVersionApi from '@/api/api-service/apiVersionApi.ts'
import { Plus, Refresh } from '@element-plus/icons-vue'

const route = useRoute()

// 响应式数据
const loading = ref(false)
const apiLoading = ref(false)
const selectedApiId = ref(null)
const apiList = ref([])
const versionList = ref([])

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

const loadVersions = async () => {
  if (!selectedApiId.value) return

  try {
    loading.value = true
    const response = await apiVersionApi.getByApiId(selectedApiId.value)
    versionList.value = response.data || []
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
  // 简化版本，不实现对话框
  ElMessage.info('新增版本功能')
}

const editVersion = (row) => {
  ElMessage.info(`编辑版本 ${row.id}`)
}

const viewVersion = (row) => {
  ElMessage.info(`查看版本 ${row.id}`)
}

const deleteVersion = async (row) => {
  try {
    await ElMessageBox.confirm('确定删除该版本吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

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
    0: 'info',
    1: 'success',
    2: 'warning'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    0: '草稿',
    1: '已发布',
    2: '审核中'
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
</style>
