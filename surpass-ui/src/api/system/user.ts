import request from '@/utils/Request'

export function exportUser(query : any): any {
  return request({
    url: `/users/export/${query}`,
    method: 'get',
    responseType: 'blob'
  })
}

// 查询用户详细
export function getUser(userId : any): any {
  return request({
    url: `/users/get/${userId}`,
    method: 'get'
  })
}

// 新增用户
export function addUser(data : any): any {
  return request({
    url: '/users/add',
    method: 'post',
    data: data
  })
}

//上传头像
export function uploadImage(data : any): any {
  return request({
    url: '/filestorage/upload/',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  })
}

// 修改用户
export function updateUser(data : any): any {
  return request({
    url: '/users/update',
    method: 'put',
    data: data
  })
}

export function deleteBatch(id : any): any {
  return request({
    url: '/users/delete?ids=' + id,
    method: 'delete'
  })
}

export function listUsers(query : any): any {
  return request({
    url: '/users/fetch',
    method: 'get',
    params: query
  })
}

export function getImage(id : any): any {
  return request({
    url: '/filestorage/image/' + id,
    method: 'get',
  })
}

export function importUsers(data : any): any {
  return request({
    url: '/users/import',
    method: 'post',
    data: data
  })
}

export function generateOnePas(): any {
  return request({
    url: '/users/randomPassword',
    method: 'get'
  })
}

//修改密码
export function changePass(data : any): any {
  return request({
    url: '/users/changePassword',
    method: 'put',
    data: data
  })
}

//获取用户岗位信息
export function getUsersPosts(query : any): any {
  return request({
    url: '/userposts/fetch',
    method: 'get',
    params: query
  })
}


//删除用户岗位信息
export function deletePost(id : any): any {
  return request({
    url: '/userposts/delete?ids=' + id,
    method: 'get'
  })
}

//新增用户岗位信息
export function addPost(data : any): any {
  return request({
    url: '/userposts/add',
    method: 'post',
    data: data
  })
}

//查询兼职信息
export function listHats(query : any): any {
  return request({
    url: '/organizationUser/fetch',
    method: 'get',
    params: query
  })
}

//查询兼职详细信息
export function getHatsDetail(id : any): any {
  return request({
    url: `/organizationUser/get/${id}`,
    method: 'get',
  })
}

//新增用户兼职信息
export function addOneUserHat(data : any): any {
  return request({
    url: '/organizationUser/add',
    method: 'post',
    data: data
  })
}

//更新用户兼职信息
export function updateOneUserHat(data : any): any {
  return request({
    url: '/organizationUser/update',
    method: 'put',
    data: data
  })
}

//删除用户兼职信息
export function deleteUserHat(id : any): any {
  return request({
    url: '/organizationUser/delete?ids=' + id,
    method: 'delete'
  })
}

//修改状态
export function changeStatus(query : any): any {
  return request({
    url: '/users/updateStatus',
    method: 'get',
    params: query
  })
}


export function randomPassword(): any {
  return request({
    url: '/users/randomPassword',
    method: 'get'
  })
}

export function switchBook(bookId : any): any {
  return request({
    url: '/users/switchBook/'+bookId,
    method: 'get'
  })
}


// 查询用户账套列表
export function userAccessBook(query : any): any {
  return request({
    url: '/permissions/permissionBook/userAccessBook',
    method: 'get',
    params: query
  })
}

// 查询用户无权账套列表
export function userNotAccessBook(query : any): any {
  return request({
    url: '/permissions/permissionBook/userNotAccessBook',
    method: 'get',
    params: query
  })
}

// 添加未关联的账套
export function addAccessBook(data : any): any {
  return request({
    url: '/permissions/permissionBook/add',
    method: 'post',
    data: data
  })
}

// 删除关联的账套
export function deleteAccessBook(query : any): any {
  return request({
    url: '/permissions/permissionBook/delete',
    method: 'DELETE',
    params: query
  })
}
