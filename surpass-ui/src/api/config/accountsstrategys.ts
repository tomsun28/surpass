import request from '@/utils/Request'

// 查询岗位列表
export function listAccountsstrategys(query : any): any {
  return request({
    url: '/config/accountsstrategy/fetch',
    method: 'get',
    params: query
  })
}

// 查询岗位详细
export function getAccountsstrategys(id : any): any {
  return request({
    url: '/config/accountsstrategy/get/' + id,
    method: 'get'
  })
}

// 新增岗位
export function addAccountsstrategys(data : any): any {
  return request({
    url: '/config/accountsstrategy/add',
    method: 'post',
    data: data
  })
}

// 修改岗位
export function updateAccountsstrategys(data : any): any {
  return request({
    url: '/config/accountsstrategy/update',
    method: 'put',
    data: data
  })
}

// 删除岗位
export function delAccountsstrategys(id : any): any {
  if (!(id instanceof Array)) {
    id = [id]
  }
  return request({
    url: '/config/accountsstrategy/delete?ids=' + id.join(','),
    method: 'delete'
  })
}
