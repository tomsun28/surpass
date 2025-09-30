import request from '@/utils/Request'

// 查询角色列表
export function listGroup(query : any): any {
  return request({
    url: '/idm/groups/fetch',
    method: 'get',
    params: query
  })
}

// 查询角色列表
export function listUserGroup(query : any): any {
  return request({
    url: '/idm/groupmembers/memberInGroup',
    method: 'get',
    params: query
  })
}

// 查询未关联的用户组
export function listNoUserGroup(query : any): any {
  return request({
    url: '/idm/groupmembers/groupsNoMember',
    method: 'get',
    params: query
  })
}

// 添加未关联的用户组
export function addNoUserGroup(data : any): any {
  return request({
    url: '/idm/groupmembers/addMember2Groups',
    method: 'post',
    data: data
  })
}

// 删除关联的用户组
export function deleteUserGroup(query : any): any {
  return request({
    url: '/idm/groupmembers/delete',
    method: 'DELETE',
    params: query
  })
}


export function getOneGroup(id : any): any {
  return request({
    url: `/idm/groups/get/${id}`,
    method: 'get',
  })
}

export function updateGroup(data : any): any {
  return request({
    url: `/idm/groups/update`,
    method: 'put',
    data: data
  })
}

export function addGroup(data : any): any {
  return request({
    url: `/idm/groups/add`,
    method: 'post',
    data: data
  })
}

export function deleteBatch(id : any): any {
  return request({
    url: '/idm/groups/delete?ids=' + id,
    method: 'delete'
  })
}

export function memberNotInGroup(query : any): any {
  return request({
    url: '/idm/groupmembers/memberNotInGroup',
    method: 'get',
    params: query
  })
}

export function memberPostNotInGroup(query : any): any {
  return request({
    url: '/idm/groupmembers/memberPostNotInGroup',
    method: 'get',
    params: query
  })
}

// 删除关联的用户组
export function addMemberGroup(data : any): any {
  return request({
    url: '/idm/groupmembers/add',
    method: 'post',
    data: data
  })
}
