import request from '@/utils/Request'

// 查询角色列表
export function fetchAuth(query : any): any {
  return request({
    url: '/groups/apps/fetch',
    method: 'get',
    params: query
  })
}


// 新增分组
export function authData(data : any): any {
  return request({
    url: '/groups/apps/auth',
    method: 'post',
    data: data
  })
}
// 删除分组
export function delAuth(ids : any): any {
  return request({
    url: '/groups/apps/delete/' + ids,
    method: 'delete'
  })
}
