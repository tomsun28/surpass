import request from '@/utils/Request'

export function getAppsFormbasedInit(): any {
  return request({
    url: '/apps/formbased/init',
    method: 'get'
  })
}

export function getAppsFormbased(id : any): any {
  return request({
    url: '/apps/formbased/get/' + id,
    method: 'get'
  })
}

export function addAppsFormbased(data : any): any {
  return request({
    url: '/apps/formbased/add',
    method: 'post',
    data: data
  })
}

export function updateAppsFormbased(data : any): any {
  return request({
    url: '/apps/formbased/update',
    method: 'put',
    data: data
  })
}
