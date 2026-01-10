<template>
  <el-card class="version-list-card" shadow="never">
    <template #header>
      <div class="card-header">
        <span>版本列表</span>
        <div class="list-info">
          <span class="total-count">共 {{ versionList.length }} 个版本</span>
        </div>
      </div>
    </template>

    <el-table
        :data="versionList"
        v-loading="loading"
        border
    >
      <el-table-column header-align="center" align="center" prop="version" label="版本号" width="80">
        <template #default="{ row }">
          <span class="version-number">v{{ row.version }}</span>
        </template>
      </el-table-column>

      <el-table-column header-align="center" prop="sqlTemplate" label="SQL模板" min-width="200">
        <template #default="{ row }">
          <div class="sql-preview">
            <code>{{ truncateSQL(row.sqlTemplate) }}</code>
          </div>
        </template>
      </el-table-column>

      <el-table-column header-align="center" align="center" label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="getStatusTagType(row.status)" size="large" effect="light">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="supportsPaging" label="操作类型" width="100" align="center">
        <template #default="{ row }">
          {{ getOperationTypeText(row.supportsPaging) }}
        </template>
      </el-table-column>

      <el-table-column header-align="center" prop="description" label="描述" min-width="150">
        <template #default="{ row }">
          <span class="description-text">{{ row.description || '-' }}</span>
        </template>
      </el-table-column>

      <el-table-column header-align="center" prop="createdDate" label="创建时间" width="180"/>

      <el-table-column header-align="center" align="center" label="操作" width="220" fixed="right">
        <template #default="{ row }">
          <div class="action-buttons-group">
            <el-tooltip content="查看" placement="top">
              <el-button link type="primary" icon="View" @click="handleView(row)">
              </el-button>
            </el-tooltip>

            <el-tooltip content="编辑" placement="top">
              <el-button icon="Edit" link type="primary" @click="handleEdit(row)" v-if="row.status === 0">
              </el-button>
            </el-tooltip>
            <el-tooltip content="提交审核" placement="top">
              <el-button icon="Upload" link type="warning" @click="handleSubmitReview(row)" v-if="row.status === 0">
              </el-button>
            </el-tooltip>
            <el-tooltip content="发布" placement="top">
              <el-button icon="Check" link type="success" @click="handlePublish(row)" v-if="row.status === 1">
              </el-button>
            </el-tooltip>

            <el-tooltip content="驳回" placement="top">
              <el-button icon="Close" link type="danger" @click="handleReject(row)" v-if="row.status === 1">
              </el-button>
            </el-tooltip>
            <el-tooltip content="下线" placement="top">
              <el-button icon="Remove" link type="info" @click="handleOffline(row)" v-if="row.status === 2">
              </el-button>
            </el-tooltip>
            <el-tooltip content="上线" placement="top">
              <el-button icon="CircleCheck" link type="success" @click="handleOnline(row)"
                         v-if="row.status === 3">
              </el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button icon="Delete" link type="danger" @click="handleDelete(row)" v-if="row.status === 0">
              </el-button>
            </el-tooltip>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 空状态 -->
    <div v-if="!loading && versionList.length === 0" class="empty-state">
      <el-empty description="暂无版本数据" :image-size="200">
        <el-button type="primary" @click="handleCreate">创建第一个版本</el-button>
      </el-empty>
    </div>
  </el-card>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'

const props = defineProps({
  versionList: {
    type: Array,
    default: () => []
  },
  loading: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits([
  'view',
  'edit',
  'submit-review',
  'publish',
  'reject',
  'offline',
  'online',
  'delete',
  'create'
])

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

const getOperationTypeText = (type) => {
  const texts = {
    '1': '分页查询',
    '2': '列表查询',
    '3': '单个查询',
    '4': '增加操作',
    '5': '修改操作',
    '6': '删除操作'
  }
  return texts[type] || '未知类型'
}

const truncateSQL = (sql) => {
  if (!sql) return ''
  if (sql.length <= 80) return sql
  return sql.substring(0, 80) + '...'
}

const handleView = (row) => {
  emit('view', row)
}

const handleEdit = (row) => {
  emit('edit', row)
}

const handleSubmitReview = (row) => {
  emit('submit-review', row)
}

const handlePublish = (row) => {
  emit('publish', row)
}

const handleReject = (row) => {
  emit('reject', row)
}

const handleOffline = (row) => {
  emit('offline', row)
}

const handleOnline = (row) => {
  emit('online', row)
}

const handleDelete = (row) => {
  emit('delete', row)
}

const handleCreate = () => {
  emit('create')
}
</script>

<style scoped lang="scss">
.version-list-card {
  border-radius: 12px;
  border: 1px solid #e4e7ed;
  overflow: hidden;
}

.version-list-card .card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0px;
  border-bottom: 1px solid #f0f2f5;
  font-weight: 600;
  color: #303133;
}

.list-info {
  font-size: 14px;
  color: #909399;
}

.total-count {
  background: #f5f7fa;
  padding: 4px 8px;
  border-radius: 4px;
}

.version-number {
  font-weight: 600;
  color: #409eff;
  font-size: 14px;
}

.sql-preview {
  max-width: 300px;
}

.sql-preview code {
  background: #f5f7fa;
  padding: 4px 8px;
  border-radius: 4px;
  font-family: 'JetBrains Mono', 'Fira Code', monospace;
  font-size: 12px;
  color: #606266;
  line-height: 1.4;
}

.description-text {
  color: #606266;
  line-height: 1.5;
}

.action-buttons-group {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  justify-content: center;
}

.action-buttons-group .el-button {
  margin: 0;
}

.empty-state {
  padding: 60px 0;
}
</style>