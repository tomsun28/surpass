import request from '@/utils/Request'


/**
 * 查询分组已授权的资源集合
 * @param query
 * @returns {*}
 */
export function getResourceByGroup(query : any): any {
  return request({
    url: '/permissions/permission/get',
    method: 'get',
    params: query
  })
}

/**
 * 查询用户已授权的资源集合
 * @param query
 * @returns {*}
 */
export function getResourceByUser(query : any): any {
  return request({
    url: '/permissions/permissionUser/get',
    method: 'get',
    params: query
  })
}

/**
 * 查询角色已授权的资源集合
 * @param query
 * @returns {*}
 */
export function getResourceByRole(query : any): any {
  return request({
    url: '/permissions/permissionRole/get',
    method: 'get',
    params: query
  })
}


export function tree(query : any): any {
  return request({
    url: '/permissions/resources/tree',
    method: 'get',
    params: query
  })
}

/**
 * 保存授权
 * @param data
 * @returns {*}
 */
export function saveGroupAuthz(data : any): any {
  return request({
    url: '/permissions/permission/update',
    method: 'put',
    data: data
  })
}



/**
 * 保存授权
 * @param data
 * @returns {*}
 */
export function saveUsersAuthz(data : any): any {
  return request({
    url: '/permissions/permissionUser/update',
    method: 'put',
    data: data
  })
}


/**
 * 保存授权
 * @param data
 * @returns {*}
 */
export function saveRoleAuthz(data : any): any {
  return request({
    url: '/permissions/permissionRole/update',
    method: 'put',
    data: data
  })
}
