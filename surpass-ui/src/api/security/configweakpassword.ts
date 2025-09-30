import request from '@/utils/Request'

export function fetch(query : any): any {
  return request({
    url: '/security/configWeakPassword/fetch',
    method: 'get',
    params: query
  })
}

export function get(id : any): any {
  return request({
    url: '/security/configWeakPassword/get/' + id,
    method: 'get'
  })
}

export function create(data : any): any {
  return request({
    url: '/security/configWeakPassword/add',
    method: 'post',
    data: data
  })
}

export function update(data : any): any {
  return request({
    url: '/security/configWeakPassword/update',
    method: 'put',
    data: data
  })
}

export function deleteById(id : any): any {
  if (!(id instanceof Array)) {
    id = [id]
  }
  return request({
    url: '/security/configWeakPassword/delete?ids=' + id.join(','),
    method: 'delete'
  })
}
