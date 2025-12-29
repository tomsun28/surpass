export interface StatisticItem {
  title: string
  value: number
  icon: string
  color: string
}

export interface ApiAccessData {
  date: string
  count: number
  success: number
  failed: number
  avgResponseTime: number
}

export interface RegionAccessData {
  province: string
  count: number
  users: number
  successRate: number
}

export interface ApiTopData {
  apiName: string
  path: string
  count: number
  avgResponseTime: number
  successRate: number
}

export interface DashboardData {
  // 基础统计
  appCount: number
  apiCount: number
  roleCount: number
  userCount: number
  clientCount: number
  datasourceCount: number
  
  // API访问统计
  todayApiCalls: number
  weekApiCalls: number
  monthApiCalls: number
  apiSuccessRate: number
  
  // 趋势数据
  apiAccessTrend: ApiAccessData[]
  regionAccessData: RegionAccessData[]
  apiTopList: ApiTopData[]
}

export interface TimeRange {
  startTime: string
  endTime: string
  type: 'day' | 'week' | 'month' | 'year'
}