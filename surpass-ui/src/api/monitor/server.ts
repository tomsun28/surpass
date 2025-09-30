import request from '@/utils/Request'

// 获取服务信息
export function getServer(): any {
  return request({
    url: '/monitor/server',
    method: 'get'
  })
}