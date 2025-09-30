import request from '@/utils/Request'

export function getSecuritySmsprovider(): any {
  return request({
    url: '/security/smsprovider/get',
    method: 'get'
  })
}


export function updateSecuritySmsprovider(data : any): any {
  return request({
    url: '/security/smsprovider/update',
    method: 'put',
    data: data
  })
}

