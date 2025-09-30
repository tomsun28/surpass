import request from '@/utils/Request'

// 查询岗位列表
export function listPost(query : any): any {
  return request({
    url: '/posts/fetch',
    method: 'get',
    params: query
  })
}

// 查询岗位详细
export function getPost(id : any): any {
  return request({
    url: '/posts/get/' + id,
    method: 'get'
  })
}

// 新增岗位
export function addPost(data : any): any {
  return request({
    url: '/posts/add',
    method: 'post',
    data: data
  })
}

// 修改岗位
export function updatePost(data : any): any {
  return request({
    url: '/posts/update',
    method: 'put',
    data: data
  })
}

// 删除岗位
export function delPost(id : any): any {
  if (!(id instanceof Array)) {
    id = [id]
  }
  return request({
    url: '/posts/delete?ids=' + id.join(','),
    method: 'delete'
  })
}
