<template>
  <div class="region-map">
    <div class="map-header">
      <div class="title">API调用区域分布</div>
      <div class="time-range">
        <el-radio-group v-model="timeRange" size="small" @change="handleTimeRangeChange">
          <el-radio-button label="day">今日</el-radio-button>
          <el-radio-button label="week">本周</el-radio-button>
          <el-radio-button label="month">本月</el-radio-button>
        </el-radio-group>
      </div>
    </div>
    
    <div class="map-container">
      <div ref="mapRef" class="map-chart"></div>
      <div class="map-legend">
        <div class="legend-title">调用量分级</div>
        <div class="legend-items">
          <div v-for="(item, index) in legendItems" :key="index" class="legend-item">
            <div class="legend-color" :style="{ backgroundColor: item.color }"></div>
            <div class="legend-text">{{ item.label }}</div>
          </div>
        </div>
      </div>
    </div>
    
    <div class="region-table">
      <el-table :data="regionData" style="width: 100%" height="280">
        <el-table-column prop="province" label="省份" width="120" align="center">
          <template #default="{ row }">
            <div class="province-cell">
              <span class="province-name">{{ row.province }}</span>
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
        
        <el-table-column prop="users" label="用户数" width="120" align="center">
          <template #default="{ row }">
            <div class="users-cell">
              <span class="users">{{ row.users.toLocaleString() }}</span>
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
        
        <el-table-column label="占比" width="180" align="center">
          <template #default="{ row }">
            <div class="percentage-cell">
              <el-progress 
                :percentage="row.percentage" 
                :stroke-width="12" 
                :show-text="false"
                color="#1890ff"
              />
              <span class="percentage-text">{{ row.percentage.toFixed(1) }}%</span>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import chinaJson from '@/assets/china-map.json'
import { getRegionAccessData } from '@/api/dashboard-statistics'
import type { RegionAccessData, TimeRange } from '../types'
import {parseTime} from "@/utils/Surpass";

const mapRef = ref<HTMLDivElement>()
const chart = ref<echarts.ECharts>()
const timeRange = ref<'day' | 'week' | 'month'>('day')
const regionData = ref<RegionAccessData[]>([])

const legendItems = [
  { color: '#9fb5ea', label: '0-100次' },
  { color: '#e6ac53', label: '100-500次' },
  { color: '#74e2ca', label: '500-1000次' },
  { color: '#85daef', label: '1000-5000次' },
  { color: '#9feaa5', label: '5000-10000次' },
  { color: '#5475f5', label: '10000次以上' }
]

const initChart = () => {
  if (!mapRef.value) return
  
  echarts.registerMap('china', chinaJson as any)
  chart.value = echarts.init(mapRef.value)
  updateChart()
  
  window.addEventListener('resize', handleResize)
}

const handleResize = () => {
  chart.value?.resize()
}

const handleTimeRangeChange = async () => {
  await fetchData()
  updateChart()
}

const fetchData = async () => {
  const params: TimeRange = {
    startTime: getStartTime(),
    endTime: parseTime(new Date(), '{y}-{m}-{d}'),
    type: timeRange.value
  }
  
  try {
    const response = await getRegionAccessData(params)
    const data = response.data
    const total = data.reduce((sum: number, item: any) => sum + item.count, 0)
    
    regionData.value = data.map((item: any) => ({
      ...item,
      percentage: total > 0 ? (item.count / total) * 100 : 0
    }))
  } catch (error) {
    console.error('获取区域访问数据失败:', error)
    regionData.value = generateMockData()
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
  return parseTime(now, '{y}-{m}-{d}')
}

const updateChart = () => {
  if (!chart.value || !regionData.value.length) return
  
  const option = {
    series: [{
      type: 'map',
      map: 'china',
      layoutCenter: ['50%', '50%'],
      layoutSize: '90%',
      label: {
        show: true,
        color: '#333',
        fontSize: 10,
        fontWeight: 'normal'
      },
      itemStyle: {
        areaColor: '#f5f5f5',
        borderColor: '#ccc',
        borderWidth: 1
      },
      roam: true,
      zoom: 1,
      emphasis: {
        label: {
          color: '#fff',
          fontSize: 12,
          fontWeight: 'bold'
        },
        itemStyle: {
          areaColor: '#1890ff',
          borderColor: '#1890ff',
          borderWidth: 2
        }
      },
      data: regionData.value.map(item => ({
        name: item.province,
        value: item.count
      }))
    }],
    visualMap: {
      type: 'piecewise',
      show: true,
      pieces: [
        { min: 0, max: 100 },
        { min: 100, max: 500 },
        { min: 500, max: 1000 },
        { min: 1000, max: 5000 },
        { min: 5000, max: 10000 },
        { min: 10000 }
      ],
      textStyle: {
        color: '#666'
      },
      itemWidth: 20,
      itemHeight: 12,
      top: '20px',
      left: '20px',
      inRange: {
        color: [
          '#9fb5ea',
          '#e6ac53',
          '#74e2ca',
          '#85daef',
          '#9feaa5',
          '#5475f5'
        ]
      }
    },
    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderWidth: 0,
      formatter: (params: any) => {
        const data = regionData.value.find(item => item.province === params.name)
        if (!data) return `地区：${params.name}<br/>调用量：${params.value || 0}次`
        
        return `
          <div style="padding: 8px;">
            <div style="font-weight: bold; margin-bottom: 4px;">${params.name}</div>
            <div>调用次数：<b>${data.count.toLocaleString()}</b> 次</div>
            <div>用户数量：<b>${data.users.toLocaleString()}</b> 人</div>
            <div>成功率：<b>${data.successRate.toFixed(1)}%</b></div>
            <div>占比：<b>${data.percentage?.toFixed(1) || 0}%</b></div>
          </div>
        `
      }
    },
    toolbox: {
      show: true,
      orient: 'vertical',
      right: '20px',
      top: '20px',
      feature: {
        restore: { show: true },
        saveAsImage: { show: true }
      }
    }
  }
  
  chart.value.setOption(option)
}

const getSuccessClass = (successRate: number) => {
  if (successRate >= 99) return 'success-excellent'
  if (successRate >= 95) return 'success-good'
  if (successRate >= 90) return 'success-normal'
  return 'success-poor'
}

const generateMockData = (): RegionAccessData[] => {
  const provinces = [
    '北京', '上海', '广东', '江苏', '浙江', '山东', '河南', '四川',
    '湖北', '湖南', '福建', '安徽', '河北', '陕西', '辽宁', '江西',
    '重庆', '广西', '云南', '山西', '黑龙江', '吉林', '天津', '贵州',
    '甘肃', '内蒙古', '新疆', '海南', '宁夏', '青海', '西藏'
  ]
  
  const data = provinces.map(province => {
    const count = Math.floor(Math.random() * 10000) + 100
    const users = Math.floor(count * (Math.random() * 0.5 + 0.1))
    const successRate = Math.random() * 10 + 90
    
    return {
      province,
      count,
      users,
      successRate
    }
  })
  
  const total = data.reduce((sum, item) => sum + item.count, 0)
  
  return data.map(item => ({
    ...item,
    percentage: total > 0 ? (item.count / total) * 100 : 0
  })).sort((a, b) => b.count - a.count)
}

onMounted(async () => {
  await fetchData()
  initChart()
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  chart.value?.dispose()
})
</script>

<style scoped lang="scss">
.region-map {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  
  .map-header {
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
  
  .map-container {
    display: flex;
    gap: 20px;
    margin-bottom: 20px;
    
    .map-chart {
      flex: 1;
      height: 400px;
      min-width: 0;
    }
    
    .map-legend {
      width: 180px;
      background: #fafafa;
      border-radius: 4px;
      padding: 16px;
      
      .legend-title {
        font-size: 14px;
        font-weight: 500;
        color: #333;
        margin-bottom: 12px;
      }
      
      .legend-items {
        .legend-item {
          display: flex;
          align-items: center;
          margin-bottom: 8px;
          
          &:last-child {
            margin-bottom: 0;
          }
          
          .legend-color {
            width: 20px;
            height: 12px;
            border-radius: 2px;
            margin-right: 8px;
          }
          
          .legend-text {
            font-size: 12px;
            color: #666;
          }
        }
      }
    }
  }
  
  .region-table {
    .province-cell {
      .province-name {
        font-weight: 500;
        color: #333;
      }
    }
    
    .count-cell {
      .count {
        font-weight: bold;
        color: #1890ff;
      }
    }
    
    .users-cell {
      .users {
        font-weight: 500;
        color: #52c41a;
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
    
    .percentage-cell {
      display: flex;
      align-items: center;
      gap: 8px;
      
      .percentage-text {
        font-size: 12px;
        font-weight: 500;
        color: #666;
        min-width: 40px;
      }
    }
  }
}
</style>