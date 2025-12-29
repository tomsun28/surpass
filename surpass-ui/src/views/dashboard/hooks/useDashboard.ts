import { ref, onMounted, onUnmounted } from 'vue'
import { getStatisticsDashboard } from '@/api/dashboard-statistics'
import type { DashboardData } from '../types'

export function useDashboard() {
  const dashboardData = ref<DashboardData>({
    appCount: 0,
    apiCount: 0,
    roleCount: 0,
    userCount: 0,
    clientCount: 0,
    datasourceCount: 0,
    todayApiCalls: 0,
    weekApiCalls: 0,
    monthApiCalls: 0,
    apiSuccessRate: 0,
    apiAccessTrend: [],
    regionAccessData: [],
    apiTopList: []
  })

  const loading = ref(false)
  const error = ref<string | null>(null)
  let refreshTimer: number | null = null

  const fetchDashboardData = async () => {
    loading.value = true
    error.value = null
    
    try {
      const [dashboardResponse] = await Promise.all([
        getStatisticsDashboard()
      ])
      
      dashboardData.value = {
        ...dashboardResponse.data
      }
    } catch (err: any) {
      error.value = err.message || '获取数据失败'
      console.error('获取仪表盘数据失败:', err)
      useMockData()
    } finally {
      loading.value = false
    }
  }

  const useMockData = () => {
    dashboardData.value = {
      appCount: 156,
      apiCount: 265,
      roleCount: 42,
      userCount: 2345,
      clientCount: 89,
      datasourceCount: 23,
      todayApiCalls: 123456,
      weekApiCalls: 856789,
      monthApiCalls: 3456789,
      apiSuccessRate: 98.7,
      apiAccessTrend: generateMockTrendData(),
      regionAccessData: generateMockRegionData(),
      apiTopList: generateMockApiTopData()
    }
  }

  const generateMockTrendData = () => {
    const data = []
    const now = new Date()
    
    for (let i = 0; i < 24; i++) {
      const date = new Date(now)
      date.setHours(i, 0, 0, 0)
      
      const count = Math.floor(Math.random() * 1000) + 500
      const successRate = Math.random() * 0.2 + 0.8
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

  const generateMockRegionData = () => {
    const provinces = [
      '北京', '上海', '广东', '江苏', '浙江', '山东', '河南', '四川',
      '湖北', '湖南', '福建', '安徽', '河北', '陕西', '辽宁', '江西'
    ]
    
    return provinces.map(province => {
      const count = Math.floor(Math.random() * 10000) + 100
      const users = Math.floor(count * (Math.random() * 0.5 + 0.1))
      const successRate = Math.random() * 10 + 90
      
      return {
        province,
        count,
        users,
        successRate,
        percentage: Math.random() * 15 + 5
      }
    }).sort((a, b) => b.count - a.count)
  }

  const generateMockApiTopData = () => {
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
      trend: Math.floor(Math.random() * 40) - 20
    })).sort((a, b) => b.count - a.count)
  }

  const startAutoRefresh = (interval = 60000) => {
    if (refreshTimer) {
      clearInterval(refreshTimer)
    }
    
    refreshTimer = window.setInterval(() => {
      fetchDashboardData()
    }, interval)
  }

  const stopAutoRefresh = () => {
    if (refreshTimer) {
      clearInterval(refreshTimer)
      refreshTimer = null
    }
  }

  onMounted(() => {
    fetchDashboardData()
    startAutoRefresh()
  })

  onUnmounted(() => {
    stopAutoRefresh()
  })

  return {
    dashboardData,
    loading,
    error,
    fetchDashboardData,
    startAutoRefresh,
    stopAutoRefresh
  }
}