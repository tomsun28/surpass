import request from '@/utils/Request'

export function accountsstrategy(query : any): any {
  return request({
    url: '/config/accountsstrategy/fetch',
    method: 'get',
    params: query
  })
}

// 查询参数列表
export function listConfig(query : any): any {
  return request({
    url: '/system/config/list',
    method: 'get',
    params: query
  })
}

// 查询参数详细
export function getConfig(configId : any): any {
  return request({
    url: '/system/config/' + configId,
    method: 'get'
  })
}

// 根据参数键名查询参数值
export function getConfigKey(configKey : any): any {
  return request({
    url: '/system/config/configKey/' + configKey,
    method: 'get'
  })
}

// 新增参数配置
export function addConfig(data : any): any {
  return request({
    url: '/system/config',
    method: 'post',
    data: data
  })
}

// 修改参数配置
export function updateConfig(data : any): any {
  return request({
    url: '/system/config',
    method: 'put',
    data: data
  })
}

// 删除参数配置
export function delConfig(configId : any): any {
  return request({
    url: '/system/config/' + configId,
    method: 'delete'
  })
}

// 刷新参数缓存
export function refreshCache(): any {
  return request({
    url: '/system/config/refreshCache',
    method: 'delete'
  })
}
