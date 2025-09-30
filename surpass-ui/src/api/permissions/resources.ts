import request from '@/utils/Request'


/**
 * 分页查询资源
 * @param query
 * @returns {*}
 */
export function apiFetch(query : any): any {
  return request({
    url: '/permissions/resources/fetch',
    method: 'get',
    params: query
  })
}

export function apiTree(query : any): any {
  return request({
    url: '/permissions/resources/tree',
    method: 'get',
    params: query
  })
}


export function apiGet(id : any): any {
  return request({
    url: '/permissions/resources/get/'+id,
    method: 'get',
  })
}


export function apiUpdate(data : any): any {
  return request({
    url: '/permissions/resources/update',
    method: 'put',
    data: data
  })
}


export function apiCreate(data : any): any {
  return request({
    url: '/permissions/resources/add',
    method: 'post',
    data: data
  })
}


export function apiDeleteByIds(id : any): any {
  if (!(id instanceof Array)) {
    id = [id]
  }
  return request({
    url: '/permissions/resources/delete?ids=' + id.join(','),
    method: 'delete'
  })
}
