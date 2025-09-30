import request from '@/utils/Request'

// 获取账簿配置参数
export function getBooksConfigList() {
  return request({
    url: '/config/sys/books',
    method: 'get'
  })
}

// 查询参数列表
export function listConfig(query: any) {
  return request({
    url: '/config/sys/fetch',
    method: 'get',
    params: query
  })
}

// 查询参数详细
export function getConfig(configId: any) {0
  return request({
    url: '/config/sys/get/' + configId,
    method: 'get'
  })
}

// 根据参数键名查询参数值
export function getConfigKey(configKey: any) {
  return request({
    url: '/config/sys/configKey/' + configKey,
    method: 'get'
  })
}

// 新增参数配置
export function addConfig(data: any) {
  return request({
    url: '/config/sys/save',
    method: 'post',
    data: data
  })
}

// 修改参数配置
export function updateConfig(data: any) {
  return request({
    url: '/config/sys/update',
    method: 'put',
    data: data
  })
}

// 修改参数配置
export function updateConfigByKey(key: any, value: any) {
  return request({
    url: '/config/sys/updateByKey',
    method: 'put',
    data: {
      configKey: key,
      configValue: value
    }
  })
}

// 删除参数配置
export function delConfig(data: any) {
  return request({
    url: '/config/sys/delete',
    method: 'delete',
    data: data
  })
}
