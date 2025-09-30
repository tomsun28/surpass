import request from '@/utils/Request'


/**
 * 查询open权限树
 * @param query
 * @returns {*}
 */
export function apiTree(): any {
  return request({
    url: '/openapi/resources/tree',
    method: 'get'
  })
}
/**
 * 查询列表
 * @param query
 * @returns {*}
 */
export function apiFetch(query : any): any {
  return request({
    url: '/openapi/resources/fetch',
    method: 'get',
    params: query
  })
}



/**
 * 查询已授权资源
 * @param query
 * @returns {*}
 */
export function apiGet(query : any): any {
  return request({
    url: '/openapi/privileges/get',
    method: 'get',
    params: query
  })
}


/**
 * 查询已授权资源
 * @param query
 * @returns {*}
 */
export function apiGetOrg(query : any): any {
  return request({
    url: '/openapi/orgs/get',
    method: 'get',
    params: query
  })
}

/**
 * 查询open权限树
 * @param query
 * @returns {*}
 */
export function apiOrgTree(): any {
  return request({
    url: '/orgs/tree',
    method: 'get'
  })
}

/**
 * 授权open权限
 * @param data
 * @returns {*}
 */
export function apiUpdate(data : any): any {
  return request({
    url: '/openapi/privileges/update',
    method: 'put',
    data: data
  })
}
/**
 * 授权open权限
 * @param data
 * @returns {*}
 */
export function apiOrgsUpdate(data : any): any {
  return request({
    url: '/openapi/orgs/update',
    method: 'put',
    data: data
  })
}



/**
 * 授权open权限
 * @param data
 * @returns {*}
 */
export function apiOpenUpdate(data : any): any {
  return request({
    url: '/openapi/resources/update',
    method: 'put',
    data: data
  })
}

/**
 * 授权open权限
 * @param data
 * @returns {*}
 */
export function apiOpenCreate(data : any): any {
  return request({
    url: '/openapi/resources/add',
    method: 'post',
    data: data
  })
}



export function apiOpenDeleteByIds(id : any): any {
  if (!(id instanceof Array)) {
    id = [id]
  }
  return request({
    url: '/openapi/resources/delete?ids=' + id.join(','),
    method: 'delete'
  })
}


export function apiOpenGet(id : any): any {
  return request({
    url: '/openapi/resources/get/'+id,
    method: 'get',
  })
}
