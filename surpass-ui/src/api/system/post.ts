import request from '@/utils/Request'

// 查询岗位列表
export function listPost(query : any): any {
  return request({
    url: '/posts/fetch',
    method: 'get',
    params: query
  })
}

export function getPost(id : any): any {
  return request({
    url: `/posts/get/${id}`,
    method: 'get',
  })
}

export function addPost(data : any): any {
  return request({
    url: `/posts/add`,
    method: 'post',
    data: data
  })
}

export function updatePost(data : any): any {
  return request({
    url: `/posts/update`,
    method: 'put',
    data: data
  })
}

export function deleteBatch(id : any): any {
  return request({
    url: '/posts/delete?ids=' + id,
    method: 'delete'
  })
}
