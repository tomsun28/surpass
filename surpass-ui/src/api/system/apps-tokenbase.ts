import request from '@/utils/Request'

export function getAppsTokenbasedInit(): any {
  return request({
    url: '/apps/tokenbased/init',
    method: 'get'
  })
}

export function getAppsTokenbased(id : any): any {
  return request({
    url: '/apps/tokenbased/get/' + id,
    method: 'get'
  })
}

export function addAppsTokenbased(data : any): any {
  return request({
    url: '/apps/tokenbased/add',
    method: 'post',
    data: data
  })
}

export function updateAppsTokenbased(data : any): any {
  return request({
    url: '/apps/tokenbased/update',
    method: 'put',
    data: data
  })
}
