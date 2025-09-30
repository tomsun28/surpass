import request from '@/utils/Request'


/**
 * 角色成员列表
 * @param query
 * @returns {*}
 */
export function memberInRole(query : any): any {
  return request({
    url: '/permissions/rolemembers/memberInRole',
    method: 'get',
    params: query
  })
}



/**
 * 查询不存在当前角色的用户
 * @param query
 * @returns {*}
 */
export function memberNotInRole(query : any): any {
  return request({
    url: '/permissions/rolemembers/memberNotInRole',
    method: 'get',
    params: query
  })
}

/**
 * 查询不存在当前角色的岗位
 * @param query
 * @returns {*}
 */
export function memberPostNotInRole(query : any): any {
  return request({
    url: '/permissions/rolemembers/memberPostNotInRole',
    method: 'get',
    params: query
  })
}


export function addMember(data : any): any {
  return request({
    url: '/permissions/rolemembers/add',
    method: 'post',
    data: data
  })
}


export function removeMember(id : any): any {
  if (!(id instanceof Array)) {
    id = [id]
  }
  return request({
    url: '/permissions/rolemembers/delete?ids=' + id.join(','),
    method: 'delete'
  })
}
