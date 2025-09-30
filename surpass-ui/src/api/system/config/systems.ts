import request from '@/utils/Request'

// 获取系统配置
export function systemInfo(): any {
  return request({
    url: '/systems/get',
    method: 'get',
  })
}

// 保存登录策略
export function saveSystemsInfo(data : any): any {
  return request({
    url: '/systems/save',
    method: 'post',
    data: data
  })
}
