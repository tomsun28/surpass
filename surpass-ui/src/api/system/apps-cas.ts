import request from '@/utils/Request'

export function getAppsCasInit(): any {
  return request({
    url: '/apps/cas/init',
    method: 'get'
  })
}

export function getAppsCas(id : any): any {
  return request({
    url: '/apps/cas/get/' + id,
    method: 'get'
  })
}

export function addAppsCas(data : any): any {
  return request({
    url: '/apps/cas/add',
    method: 'post',
    data: data
  })
}

export function updateAppsCas(data : any): any {
  return request({
    url: '/apps/cas/update',
    method: 'put',
    data: data
  })
}
