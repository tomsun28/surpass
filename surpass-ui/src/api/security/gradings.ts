import request from '@/utils/Request'



export function apiOrgTree(query : any): any {
  return request({
    url: '/orgs/tree',
    method: 'get',
    params: query
  })
}



/**
 * 查询已授权资源
 * @param query
 * @returns {*}
 */
export function apiOrgGet(query : any): any {
  return request({
    url: '/security/grading/orgs/get',
    method: 'get',
    params: query
  })
}


/**
 * 授权open权限
 * @param data
 * @returns {*}
 */
export function apiOrgUpdate(data : any): any {
  return request({
    url: '/security/grading/orgs/update',
    method: 'put',
    data: data
  })
}

/**
 * 查询已授权资源
 * @param query
 * @returns {*}
 */
export function apiAppGet(query : any): any {
  return request({
    url: '/security/grading/apps/member',
    method: 'get',
    params: query
  })
}

/**
 * 查询已授权资源
 * @param query
 * @returns {*}
 */
export function apiGroupsGet(query : any): any {
  return request({
    url: '/security/grading/groups/member',
    method: 'get',
    params: query
  })
}


/**
 * 删除授权应用
 * @param query
 * @returns {*}
 */
export function apiAppDeleteByIds(id : any): any {
  if (!(id instanceof Array)) {
    id = [id]
  }
  return request({
    url: '/security/grading/apps/delete?ids=' + id.join(','),
    method: 'delete'
  })
}
/**
 * 删除授权用户组
 * @param query
 * @returns {*}
 */
export function apiGroupsDeleteByIds(id : any): any {
  if (!(id instanceof Array)) {
    id = [id]
  }
  return request({
    url: '/security/grading/groups/delete?ids=' + id.join(','),
    method: 'delete'
  })
}



/**
 * 查询未授权应用
 * @param query
 * @returns {*}
 */
export function apiAppGetOut(query : any): any {
  return request({
    url: '/security/grading/apps/memberOut',
    method: 'get',
    params: query
  })
}



/**
 * 查询未授权用户组
 * @param query
 * @returns {*}
 */
export function apiGroupsGetOut(query : any): any {
  return request({
    url: '/security/grading/groups/memberOut',
    method: 'get',
    params: query
  })
}


/**
 * 授权应用 - 组
 * @param data
 * @returns {*}
 */
export function apiGroupAuthzApp(data : any): any {
  return request({
    url: '/security/grading/apps/add',
    method: 'post',
    data: data
  })
}
/**
 * 授权组 - 组
 * @param data
 * @returns {*}
 */
export function apiGroupAuthzGroups(data : any): any {
  return request({
    url: '/security/grading/groups/add',
    method: 'post',
    data: data
  })
}




