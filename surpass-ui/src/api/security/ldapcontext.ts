import request from '@/utils/Request'


export function getSecurityLdapcontext(): any {
  return request({
    url: '/security/ldapcontext/get',
    method: 'get'
  })
}


export function updateSecurityLdapcontext(data : any): any {
  return request({
    url: '/security/ldapcontext/update',
    method: 'put',
    data: data
  })
}

export function testSecurityLdapcontext(data : any): any {
  return request({
    url: '/security/ldapcontext/test',
    method: 'get',
    params: data
  })
}

