<template>
  <div class="statistics-cards" v-if="statistics">
    <el-card class="stat-card total" shadow="hover">
      <div class="stat-content">
        <div class="stat-icon">
          <el-icon>
            <Collection/>
          </el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ statistics.total }}</div>
          <div class="stat-label">总版本数</div>
        </div>
      </div>
    </el-card>
    <el-card class="stat-card draft" shadow="hover">
      <div class="stat-content">
        <div class="stat-icon">
          <el-icon>
            <Edit/>
          </el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ statistics.draft }}</div>
          <div class="stat-label">草稿</div>
        </div>
      </div>
    </el-card>
    <el-card class="stat-card pending" shadow="hover">
      <div class="stat-content">
        <div class="stat-icon">
          <el-icon>
            <Clock/>
          </el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ statistics.pending }}</div>
          <div class="stat-label">待发布</div>
        </div>
      </div>
    </el-card>
    <el-card class="stat-card published" shadow="hover">
      <div class="stat-content">
        <div class="stat-icon">
          <el-icon>
            <Check/>
          </el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ statistics.published }}</div>
          <div class="stat-label">已发布</div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { defineProps } from 'vue'
import { Collection, Edit, Clock, Check } from '@element-plus/icons-vue'

const props = defineProps({
  statistics: {
    type: Object,
    default: null
  }
})
</script>

<style scoped lang="scss">
.statistics-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  flex: 1;
}

.stat-card {
  border-radius: 12px;
  border: none;
  transition: all 0.3s ease;
  cursor: pointer;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1) !important;
}

.stat-card.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.stat-card.draft {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
}

.stat-card.pending {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
}

.stat-card.published {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  color: white;
}

.stat-content {
  display: flex;
  align-items: center;
  padding: 20px;
}

.stat-icon {
  margin-right: 16px;
  font-size: 32px;
  opacity: 0.9;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  line-height: 1;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
}

@media (max-width: 1200px) {
  .statistics-cards {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .statistics-cards {
    grid-template-columns: 1fr;
  }
}
</style>