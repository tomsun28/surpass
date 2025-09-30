import request from '@/utils/Request'

export function getSecurityEmailsenders(): any {
  return request({
    url: '/security/emailsenders/get',
    method: 'get'
  })
}


export function updateSecurityEmailsenders(data : any): any {
  return request({
    url: '/security/emailsenders/update',
    method: 'put',
    data: data
  })
}

