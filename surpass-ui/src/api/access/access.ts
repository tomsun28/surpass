import request from '@/utils/Request'

export function apiAuthGroupApps(query : any): any {
  return request({
    url: '/access/access/appsInGroup',
    method: 'get',
    params: query
  })
}

/**
 * 查询用户授权应用
 * @param query
 * @returns {*}
 */
export function apiUserAccess(query : any): any {
  return request({
    url: '/access/accessuser/userAccess',
    method: 'get',
    params: query
  })
}


/**
 * 查询用户组未授权应用列表
 * @param query
 * @returns {*}
 */
export function apiAppsNotInGroup(query : any): any {
  return request({
    url: '/access/access/appsNotInGroup',
    method: 'get',
    params: query
  })
}



/**
 * 查询用户未授权应用列表
 * @param query
 * @returns {*}
 */
export function apiAppsNotInUsers(query : any): any {
  return request({
    url: '/access/accessuser/userNotAccess',
    method: 'get',
    params: query
  })
}




/**
 * 删除授权
 * @param query
 * @returns {*}
 */
export function apiAppDeleteByIds(id : any): any {
  if (!(id instanceof Array)) {
    id = [id]
  }
  return request({
    url: '/access/access/delete?ids=' + id.join(','),
    method: 'delete'
  })
}

/**
 * 删除授权
 * @param query
 * @returns {*}
 */
export function apiDelUserAccess(id : any): any {
  if (!(id instanceof Array)) {
    id = [id]
  }
  return request({
    url: '/access/accessuser/delete?ids=' + id.join(','),
    method: 'delete'
  })
}




/**
 * 授权用户组和应用
 * @param query
 * @returns {*}
 */
export function apiGroupAuthzApp(data : any): any {
  return request({
    url: '/access/access/add',
    method: 'post',
    data: data
  })
}

/**
 * 授权用户和应用
 * @param query
 * @returns {*}
 */
export function apiAddUserAccess(data : any): any {
  return request({
    url: '/access/accessuser/add',
    method: 'post',
    data: data
  })
}


/**
 * 修改应用显示
 * @param query
 * @returns {*}
 */
export function apiUpdateVisibleAccess(query : any): any {
  return request({
    url: '/access/access/updateVisible',
    method: 'get',
    params: query
  })
}




/**
 * 修改应用显示
 * @param query
 * @returns {*}
 */
export function apiUpdateVisibleUsers(query : any): any {
  return request({
    url: '/access/accessuser/updateVisible',
    method: 'get',
    params: query
  })
}
