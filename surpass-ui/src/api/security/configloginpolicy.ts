import request from '@/utils/Request'


export function getPolicy(): any {
  return request({
    url: '/security/configLoginPolicy/get',
    method: 'get'
  })
}


export function updateSecurityPolicy(data : any): any {
  return request({
    url: '/security/configLoginPolicy/update',
    method: 'put',
    data: data
  })
}
