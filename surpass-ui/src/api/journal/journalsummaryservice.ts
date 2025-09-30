import request from '@/utils/Request'

export function fetch(query : any): any {
  return request({
    url: '/journal/summary/fetch',
    method: 'get',
    params: query
  })
}

export function summaryAccount(query : any): any {
  return request({
    url: '/journal/summary/summaryAccount',
    method: 'get',
    params: query
  })
}

export function get(id : any): any {
  return request({
    url: '/journal/summary/get/' + id,
    method: 'get'
  })
}

export function add(data : any): any {
  return request({
    url: '/journal/summary/add',
    method: 'post',
    data: data
  })
}

export function update(data : any): any {
  return request({
    url: '/journal/summary/update',
    method: 'put',
    data: data
  })
}

export function del(id : any): any {
  if (!(id instanceof Array)) {
    id = [id]
  }
  return request({
    url: '/journal/summary/delete?listIds=' + id.join(','),
    method: 'delete'
  })
}
