import request from '@/utils/Request'


export function apiFetch(params : any): any {
  return request({
    url: '/config/accountsstrategy/fetch',
    method: 'get',
    params: params
  })
}

export function apiGet(id : any): any {
  return request({
    url: '/config/accountsstrategy/get/' + id,
    method: 'get'
  })
}

export function apiCreate(data : any): any {
  return request({
    url: '/config/accountsstrategy/add',
    method: 'post',
    data: data
  })
}

export function apiUpdate(data : any): any {
  return request({
    url: '/config/accountsstrategy/update',
    method: 'put',
    data: data
  })
}

export function apiDeleteById(ids : any): any {
  if (!(ids instanceof Array)) {
    ids = [ids]
  }
  return request({
    url: '/config/accountsstrategy/delete?ids=' + ids.join(','),
    method: 'delete'
  })
}

/**
 * 查询组织树
 * @param query
 * @returns {*}
 */
export function apiOrgTree(): any {
  return request({
    url: '/orgs/tree',
    method: 'get'
  })
}
