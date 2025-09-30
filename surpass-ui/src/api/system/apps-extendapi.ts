import request from '@/utils/Request'

export function getAppsExtendapiInit(): any {
  return request({
    url: '/apps/extendapi/init',
    method: 'get'
  })
}

export function getAppsExtendapi(id : any): any {
  return request({
    url: '/apps/extendapi/get/' + id,
    method: 'get'
  })
}

export function addAppsExtendapi(data : any): any {
  return request({
    url: '/apps/extendapi/add',
    method: 'post',
    data: data
  })
}

export function updateAppsExtendapi(data : any): any {
  return request({
    url: '/apps/extendapi/update',
    method: 'put',
    data: data
  })
}
