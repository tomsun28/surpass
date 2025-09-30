import request from '@/utils/Request'

export function listSecuritySocialsprovider(query : any): any {
  return request({
    url: '/security/socialsprovider/fetch',
    method: 'get',
    params: query
  })
}

export function getSecuritySocialsprovider(id : any): any {
  return request({
    url: '/security/socialsprovider/get/' + id,
    method: 'get'
  })
}

export function addSecuritySocialsprovider(data : any): any {
  return request({
    url: '/security/socialsprovider/add',
    method: 'post',
    data: data
  })
}

export function updateSecuritySocialsprovider(data : any): any {
  return request({
    url: '/security/socialsprovider/update',
    method: 'put',
    data: data
  })
}

export function delSecuritySocialsprovider(id : any): any {
  if (!(id instanceof Array)) {
    id = [id]
  }
  return request({
    url: '/security/socialsprovider/delete?ids=' + id.join(','),
    method: 'delete'
  })
}
