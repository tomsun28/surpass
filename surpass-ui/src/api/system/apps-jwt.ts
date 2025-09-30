import request from '@/utils/Request'

export function getAppsJwtInit(): any {
  return request({
    url: '/apps/jwt/init',
    method: 'get'
  })
}

export function getAppsJwt(id : any): any {
  return request({
    url: '/apps/jwt/get/' + id,
    method: 'get'
  })
}

export function addAppsJwt(data : any): any {
  return request({
    url: '/apps/jwt/add',
    method: 'post',
    data: data
  })
}

export function updateAppsJwt(data : any): any {
  return request({
    url: '/apps/jwt/update',
    method: 'put',
    data: data
  })
}
