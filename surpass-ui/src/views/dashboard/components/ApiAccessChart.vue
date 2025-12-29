<template>
  <div class="api-access-chart">
    <div class="chart-header">
      <div class="title">API访问趋势</div>
      <div class="time-range">
        <el-radio-group v-model="timeRange" size="small" @change="handleTimeRangeChange">
          <el-radio-button label="day">今日</el-radio-button>
          <el-radio-button label="week">本周</el-radio-button>
          <el-radio-button label="month">本月</el-radio-button>
        </el-radio-group>
      </div>
    </div>
    <div ref="chartRef" class="chart-container"></div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue'
import * as echarts from 'echarts'
import { getApiAccessTrend } from '@/api/dashboard-statistics'
import type { ApiAccessData, TimeRange } from '../types'

const chartRef = ref<HTMLDivElement>()
const chart = ref<echarts.ECharts>()
const timeRange = ref<'day' | 'week' | 'month'>('day')
const apiData = ref<ApiAccessData[]>([])

const initChart = () => {
  if (!chartRef.value) return
  
  chart.value = echarts.init(chartRef.value)
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
    endTime: new Date().toISOString(),
    type: timeRange.value
  }
  
  try {
    const response = await getApiAccessTrend(params)
    apiData.value = response.data
  } catch (error) {
    console.error('获取API访问数据失败:', error)
    // 使用模拟数据
    apiData.value = generateMockData()
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

const updateChart = () => {
  if (!chart.value || !apiData.value.length) return
  
  const option = {
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderWidth: 0,
      textStyle: {
        color: '#333'
      },
      formatter: (params: any[]) => {
        const data = params[0]
        const successRate = data.data.successRate || 0
        return `
          <div style="padding: 8px;">
            <div style="font-weight: bold; margin-bottom: 4px;">${data.name}</div>
            <div>总调用: <b>${data.data.total || 0}</b> 次</div>
            <div>成功: <b>${data.data.success || 0}</b> 次</div>
            <div>失败: <b>${data.data.failed || 0}</b> 次</div>
            <div>成功率: <b>${successRate.toFixed(1)}%</b></div>
            <div>平均响应: <b>${data.data.avgResponseTime || 0}ms</b></div>
          </div>
        `
      }
    },
    legend: {
      data: ['总调用量', '成功调用', '失败调用'],
      top: 10,
      textStyle: {
        color: '#666'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: apiData.value.map(item => item.date),
      axisLine: {
        lineStyle: {
          color: '#dcdfe6'
        }
      },
      axisLabel: {
        color: '#666'
      }
    },
    yAxis: [
      {
        type: 'value',
        name: '调用次数',
        axisLine: {
          lineStyle: {
            color: '#dcdfe6'
          }
        },
        axisLabel: {
          color: '#666'
        },
        splitLine: {
          lineStyle: {
            type: 'dashed',
            color: '#e8e8e8'
          }
        }
      },
      {
        type: 'value',
        name: '成功率(%)',
        min: 0,
        max: 100,
        position: 'right',
        axisLine: {
          lineStyle: {
            color: '#dcdfe6'
          }
        },
        axisLabel: {
          color: '#666',
          formatter: '{value}%'
        },
        splitLine: {
          show: false
        }
      }
    ],
    series: [
      {
        name: '总调用量',
        type: 'line',
        smooth: true,
        lineStyle: {
          width: 3,
          color: '#1890ff'
        },
        itemStyle: {
          color: '#1890ff'
        },
        data: apiData.value.map(item => item.count)
      },
      {
        name: '成功调用',
        type: 'line',
        smooth: true,
        lineStyle: {
          width: 3,
          color: '#52c41a'
        },
        itemStyle: {
          color: '#52c41a'
        },
        data: apiData.value.map(item => item.success)
      },
      {
        name: '失败调用',
        type: 'line',
        smooth: true,
        lineStyle: {
          width: 3,
          color: '#ff4d4f'
        },
        itemStyle: {
          color: '#ff4d4f'
        },
        data: apiData.value.map(item => item.failed)
      },
      {
        name: '成功率',
        type: 'line',
        yAxisIndex: 1,
        smooth: true,
        lineStyle: {
          width: 2,
          type: 'dashed',
          color: '#722ed1'
        },
        itemStyle: {
          color: '#722ed1'
        },
        data: apiData.value.map(item => 
          item.count > 0 ? ((item.success / item.count) * 100).toFixed(1) : 0
        )
      }
    ]
  }
  
  chart.value.setOption(option)
}

const generateMockData = (): ApiAccessData[] => {
  const data: ApiAccessData[] = []
  const now = new Date()
  
  for (let i = 0; i < 24; i++) {
    const date = new Date(now)
    date.setHours(i, 0, 0, 0)
    
    const count = Math.floor(Math.random() * 1000) + 500
    const successRate = Math.random() * 0.2 + 0.8 // 80-100%
    const success = Math.floor(count * successRate)
    const failed = count - success
    
    data.push({
      date: date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' }),
      count,
      success,
      failed,
      avgResponseTime: Math.floor(Math.random() * 200) + 50
    })
  }
  
  return data
}

onMounted(async () => {
  await fetchData()
  initChart()
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  chart.value?.dispose()
})

watch(apiData, updateChart, { deep: true })
</script>

<style scoped lang="scss">
.api-access-chart {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  
  .chart-header {
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
  
  .chart-container {
    width: 100%;
    height: 400px;
  }
}
</style>