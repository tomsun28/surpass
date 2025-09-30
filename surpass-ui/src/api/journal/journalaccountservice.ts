import request from '@/utils/Request'

export function fetch(query : any): any {
  return request({
    url: '/journal/account/fetch',
    method: 'get',
    params: query
  })
}

export function findAll(query : any): any {
  return request({
    url: '/journal/account/findAll',
    method: 'get',
    params: query
  })
}

export function allBalance(query : any): any {
  return request({
    url: '/journal/account/allBalance',
    method: 'get',
    params: query
  })
}

export function get(id : any): any {
  return request({
    url: '/journal/account/get/' + id,
    method: 'get'
  })
}

export function add(data : any): any {
  return request({
    url: '/journal/account/add',
    method: 'post',
    data: data
  })
}

export function update(data : any): any {
  return request({
    url: '/journal/account/update',
    method: 'put',
    data: data
  })
}

export function del(id : any): any {
  if (!(id instanceof Array)) {
    id = [id]
  }
  return request({
    url: '/journal/account/delete?listIds=' + id.join(','),
    method: 'delete'
  })
}
