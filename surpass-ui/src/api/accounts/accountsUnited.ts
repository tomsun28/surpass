import request from '@/utils/Request'


export function apiFetch(params : any): any {
  return request({
    url: '/accountsUnited/fetch',
    method: 'get',
    params: params
  })
}

export function apiGet(id : any): any {
  return request({
    url: '/accountsUnited/get/' + id,
    method: 'get'
  })
}

export function apiCreate(data : any): any {
  return request({
    url: '/accountsUnited/add',
    method: 'post',
    data: data
  })
}

export function apiUpdate(data : any): any {
  return request({
    url: '/accountsUnited/update',
    method: 'put',
    data: data
  })
}

export function apiDeleteById(ids : any): any {
  if (!(ids instanceof Array)) {
    ids = [ids]
  }
  return request({
    url: '/accountsUnited/delete?ids=' + ids.join(','),
    method: 'delete'
  })
}
