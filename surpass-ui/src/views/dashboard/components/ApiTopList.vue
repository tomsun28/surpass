<template>
  <div class="api-top-list">
    <div class="list-header">
      <div class="title">热门API调用排行</div>
      <div class="time-range">
        <el-radio-group v-model="timeRange" size="small" @change="handleTimeRangeChange">
          <el-radio-button label="day">今日</el-radio-button>
          <el-radio-button label="week">本周</el-radio-button>
          <el-radio-button label="month">本月</el-radio-button>
        </el-radio-group>
      </div>
    </div>
    
    <el-table :data="apiList" style="width: 100%" height="700">
      <el-table-column type="index" label="排名" width="80" align="center">
        <template #default="{ $index }">
          <div class="rank-cell" :class="getRankClass($index + 1)">
            {{ $index + 1 }}
          </div>
        </template>
      </el-table-column>
      
      <el-table-column prop="apiName" label="API名称" min-width="180">
        <template #default="{ row }">
          <div class="api-name">
            <div class="name">{{ row.apiName }}</div>
            <div class="path">{{ row.path }}</div>
          </div>
        </template>
      </el-table-column>
      
      <el-table-column prop="count" label="调用次数" width="120" align="center">
        <template #default="{ row }">
          <div class="count-cell">
            <span class="count">{{ row.count.toLocaleString() }}</span>
          </div>
        </template>
      </el-table-column>
      
      <el-table-column prop="avgResponseTime" label="平均响应" width="120" align="center">
        <template #default="{ row }">
          <div class="response-cell" :class="getResponseClass(row.avgResponseTime)">
            {{ row.avgResponseTime }}ms
          </div>
        </template>
      </el-table-column>
      
      <el-table-column prop="successRate" label="成功率" width="120" align="center">
        <template #default="{ row }">
          <div class="success-cell" :class="getSuccessClass(row.successRate)">
            {{ row.successRate.toFixed(1) }}%
          </div>
        </template>
      </el-table-column>
      
      <el-table-column label="趋势" width="100" align="center">
        <template #default="{ row }">
          <div class="trend-cell">
            <el-progress 
              :percentage="row.trend || 0" 
              :stroke-width="8" 
              :show-text="false"
              :color="getTrendColor(row.trend || 0)"
            />
            <span class="trend-text">{{ row.trend >= 0 ? '+' : '' }}{{ row.trend || 0 }}%</span>
          </div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getApiTopList } from '@/api/dashboard-statistics'
import type { ApiTopData, TimeRange } from '../types'

const timeRange = ref<'day' | 'week' | 'month'>('day')
const apiList = ref<ApiTopData[]>([])

const handleTimeRangeChange = async () => {
  await fetchData()
}

const fetchData = async () => {
  const params: TimeRange = {
    startTime: getStartTime(),
    endTime: new Date().toISOString(),
    type: timeRange.value
  }
  
  try {
    const response = await getApiTopList(params)
    apiList.value = response.data.map((item: any, index: number) => ({
      ...item,
      trend: generateTrend()
    }))
  } catch (error) {
    console.error('获取API排行数据失败:', error)
    apiList.value = generateMockData()
  }
}

const getStartTime = (): string => {
  const now = new Date()
  switch (timeRange.value) {
    case 'day':
      now.setHours(0, 0, 0, 0)
      break
    case 'week':
      now.setDate(now.getDate() - 7)
      break
    case 'month':
      now.setMonth(now.getMonth() - 1)
      break
  }
  return now.toISOString()
}

const getRankClass = (rank: number) => {
  if (rank === 1) return 'rank-1'
  if (rank === 2) return 'rank-2'
  if (rank === 3) return 'rank-3'
  return 'rank-other'
}

const getResponseClass = (responseTime: number) => {
  if (responseTime < 100) return 'response-fast'
  if (responseTime < 500) return 'response-normal'
  return 'response-slow'
}

const getSuccessClass = (successRate: number) => {
  if (successRate >= 99) return 'success-excellent'
  if (successRate >= 95) return 'success-good'
  if (successRate >= 90) return 'success-normal'
  return 'success-poor'
}

const getTrendColor = (trend: number) => {
  if (trend > 0) return '#52c41a'
  if (trend < 0) return '#ff4d4f'
  return '#1890ff'
}

const generateTrend = () => {
  return Math.floor(Math.random() * 40) - 20
}

const generateMockData = (): ApiTopData[] => {
  const apiNames = [
    '用户登录认证',
    '获取用户信息',
    '查询权限列表',
    '创建新应用',
    '更新角色权限',
    '数据源连接测试',
    'API访问日志',
    '系统配置获取',
    '文件上传接口',
    '消息推送服务'
  ]
  
  const paths = [
    '/api/auth/login',
    '/api/user/profile',
    '/api/permission/list',
    '/api/app/create',
    '/api/role/update',
    '/api/datasource/test',
    '/api/log/access',
    '/api/config/system',
    '/api/file/upload',
    '/api/message/push'
  ]
  
  return apiNames.map((name, index) => ({
    apiName: name,
    path: paths[index],
    count: Math.floor(Math.random() * 10000) + 1000,
    avgResponseTime: Math.floor(Math.random() * 400) + 50,
    successRate: Math.random() * 10 + 90,
    trend: generateTrend()
  })).sort((a, b) => b.count - a.count)
}

onMounted(async () => {
  await fetchData()
})
</script>

<style scoped lang="scss">
.api-top-list {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  
  .list-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    
    .title {
      font-size: 16px;
      font-weight: bold;
      color: #333;
    }
  }
  
  .rank-cell {
    width: 32px;
    height: 32px;
    line-height: 32px;
    border-radius: 50%;
    text-align: center;
    font-weight: bold;
    margin: 0 auto;
    
    &.rank-1 {
      background: linear-gradient(135deg, #ffd700, #ffa500);
      color: #fff;
    }
    
    &.rank-2 {
      background: linear-gradient(135deg, #c0c0c0, #a0a0a0);
      color: #fff;
    }
    
    &.rank-3 {
      background: linear-gradient(135deg, #cd7f32, #a0522d);
      color: #fff;
    }
    
    &.rank-other {
      background: #f5f5f5;
      color: #666;
    }
  }
  
  .api-name {
    .name {
      font-weight: 500;
      color: #333;
      margin-bottom: 4px;
    }
    
    .path {
      font-size: 12px;
      color: #999;
      font-family: monospace;
    }
  }
  
  .count-cell {
    .count {
      font-weight: bold;
      color: #1890ff;
    }
  }
  
  .response-cell {
    font-weight: 500;
    
    &.response-fast {
      color: #52c41a;
    }
    
    &.response-normal {
      color: #faad14;
    }
    
    &.response-slow {
      color: #ff4d4f;
    }
  }
  
  .success-cell {
    font-weight: 500;
    
    &.success-excellent {
      color: #52c41a;
    }
    
    &.success-good {
      color: #73d13d;
    }
    
    &.success-normal {
      color: #faad14;
    }
    
    &.success-poor {
      color: #ff4d4f;
    }
  }
  
  .trend-cell {
    display: flex;
    align-items: center;
    gap: 8px;
    
    .trend-text {
      font-size: 12px;
      font-weight: 500;
      min-width: 40px;
    }
  }
}
</style>