import request from '@/utils/Request'


export function getSecurityPasswordpolicy(): any {
  return request({
    url: '/security/passwordpolicy/get',
    method: 'get'
  })
}


export function updateSecurityPasswordpolicy(data : any): any {
  return request({
    url: '/security/passwordpolicy/update',
    method: 'put',
    data: data
  })
}
