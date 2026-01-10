<template>
  <el-card class="api-selector-card" shadow="never">
    <template #header>
      <div class="card-header">
        <div style="display: flex;justify-content: flex-start;align-items: center">
          <span>选择API：</span>
          <div class="selector-content">
            <el-select
                :model-value="selectedApiId"
                placeholder="请选择要管理的API"
                style="width: 400px"
                @update:model-value="handleApiChange"
                :loading="loading"
            >
              <el-option
                  v-for="api in apiList"
                  :key="api.id"
                  :label="api.name"
                  :value="api.id"
              >
                <div class="api-option">
                  <span class="api-name">{{ api.name }}</span>
                  <span class="api-path">{{ api.path }}</span>
                </div>
              </el-option>
            </el-select>
          </div>
        </div>
        <div>
          <el-button type="primary" @click="handleCreate">
            新增版本
          </el-button>
          <el-button type="success" @click="handleCreateNext" v-if="hasPublishedVersion">
            新增下一个版本
          </el-button>
          <el-button type="primary" @click="handleRefresh" :loading="refreshing">
            刷新
          </el-button>
        </div>
      </div>
    </template>
  </el-card>
</template>

<script setup>
import { ref, defineProps, defineEmits } from 'vue'

const props = defineProps({
  apiList: {
    type: Array,
    default: () => []
  },
  selectedApiId: {
    type: [String, Number],
    default: null
  },
  loading: {
    type: Boolean,
    default: false
  },
  hasPublishedVersion: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:selectedApiId', 'create', 'create-next', 'refresh'])

const refreshing = ref(false)

const handleApiChange = (value) => {
  emit('update:selectedApiId', value)
}

const handleCreate = () => {
  emit('create')
}

const handleCreateNext = () => {
  emit('create-next')
}

const handleRefresh = async () => {
  refreshing.value = true
  try {
    await emit('refresh')
  } finally {
    refreshing.value = false
  }
}
</script>

<style scoped lang="scss">
.api-selector-card {
  margin-bottom: 24px;
  border-radius: 12px;
  border: 1px solid #e4e7ed;
}

.api-selector-card .card-header {
  padding: 10px 0px;
  border-bottom: 1px solid #f0f2f5;
  font-weight: 600;
  color: #303133;
  display: flex;
  justify-content: space-between;
}

.api-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.api-name {
  font-weight: 500;
  color: #303133;
}

.api-path {
  font-size: 12px;
  color: #909399;
  background: #f5f7fa;
  padding: 2px 6px;
  border-radius: 4px;
}
</style>