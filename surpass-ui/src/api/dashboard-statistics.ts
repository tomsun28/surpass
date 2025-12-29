import request from '@/utils/Request'
import type { DashboardData, TimeRange } from '@/views/dashboard/types'

// 统计分析API
export function getStatisticsDashboard(): Promise<{ data: DashboardData }> {
  return request({
    url: '/statistics/dashboard',
    method: 'get'
  })
}

export function getApiAccessTrend(params: TimeRange): Promise<{ data: any[] }> {
  return request({
    url: '/statistics/api-access-trend',
    method: 'get',
    params
  })
}

export function getRegionAccessData(params: TimeRange): Promise<{ data: any[] }> {
  return request({
    url: '/statistics/region-access',
    method: 'get',
    params
  })
}

export function getApiTopList(params: TimeRange): Promise<{ data: any[] }> {
  return request({
    url: '/statistics/api-top',
    method: 'get',
    params
  })
}

export function getSystemStatus(): Promise<{ data: any }> {
  return request({
    url: '/statistics/system-status',
    method: 'get'
  })
}

// 基础统计数量
export function getAppCount(): Promise<{ data: number }> {
  return request({
    url: '/statistics/app-count',
    method: 'get'
  })
}

export function getRoleCount(): Promise<{ data: number }> {
  return request({
    url: '/statistics/role-count',
    method: 'get'
  })
}

export function getUserCount(): Promise<{ data: number }> {
  return request({
    url: '/statistics/user-count',
    method: 'get'
  })
}

export function getClientCount(): Promise<{ data: number }> {
  return request({
    url: '/statistics/client-count',
    method: 'get'
  })
}

export function getDatasourceCount(): Promise<{ data: number }> {
  return request({
    url: '/statistics/datasource-count',
    method: 'get'
  })
}