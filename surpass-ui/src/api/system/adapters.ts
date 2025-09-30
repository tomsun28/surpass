import request from '@/utils/Request'

// 查询岗位列表
export function listAdapters(query : any): any {
  return request({
    url: '/config/adapters/fetch',
    method: 'get',
    params: query
  })
}

// 查询岗位详细
export function getAdapters(id : any): any {
  return request({
    url: '/config/adapters/get/' + id,
    method: 'get'
  })
}

// 新增岗位
export function addAdapters(data : any): any {
  return request({
    url: '/config/adapters/add',
    method: 'post',
    data: data
  })
}

// 修改岗位
export function updateAdapters(data : any): any {
  return request({
    url: '/config/adapters/update',
    method: 'put',
    data: data
  })
}

// 删除岗位
export function delAdapters(id : any): any {
  if (!(id instanceof Array)) {
    id = [id]
  }
  return request({
    url: '/config/adapters/delete?ids=' + id.join(','),
    method: 'delete'
  })
}
