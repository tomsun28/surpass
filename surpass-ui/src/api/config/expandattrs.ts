import request from '@/utils/Request'

// 查询岗位列表
export function listExpandattrs(query : any): any {
  return request({
    url: '/config/expandattrs/fetch',
    method: 'get',
    params: query
  })
}

// 查询岗位详细
export function getExpandattrs(id : any): any {
  return request({
    url: '/config/expandattrs/get/' + id,
    method: 'get'
  })
}

// 新增岗位
export function addExpandattrs(data : any): any {
  return request({
    url: '/config/expandattrs/add',
    method: 'post',
    data: data
  })
}

// 修改岗位
export function updateExpandattrs(data : any): any {
  return request({
    url: '/config/expandattrs/update',
    method: 'put',
    data: data
  })
}

// 删除岗位
export function delExpandattrs(id : any): any {
  if (!(id instanceof Array)) {
    id = [id]
  }
  return request({
    url: '/config/expandattrs/delete?ids=' + id.join(','),
    method: 'delete'
  })
}
