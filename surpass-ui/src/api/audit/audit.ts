import request from '@/utils/Request'

export function loginHistory(query : any): any {
  return request({
    url: '/historys/loginHistory/fetch',
    method: 'get',
    params: query
  })
}

export function loginAppsHistory(query : any): any {
  return request({
    url: '/historys/loginAppsHistory/fetch',
    method: 'get',
    params: query
  })
}

export function synchronizerHistory(query : any): any {
  return request({
    url: '/historys/synchronizerHistory/fetch',
    method: 'get',
    params: query
  })
}

export function connectorHistory(query : any): any {
  return request({
    url: '/historys/connectorHistory/fetch',
    method: 'get',
    params: query
  })
}

export function systemLogs(query : any): any {
  return request({
    url: '/historys/systemLogs/fetch',
    method: 'get',
    params: query
  })
}
