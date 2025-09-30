import request from '@/utils/Request'

// 查询列表
export function fetch(query : any): any {
  return request({
    url: '/security/configPasswordEncrypt/fetch',
    method: 'get',
    params: query
  })
}

// 查询详细
export function get(id : any): any {
  return request({
    url: '/security/configPasswordEncrypt/get/' + id,
    method: 'get'
  })
}

// 新增
export function create(data : any): any {
  return request({
    url: '/security/configPasswordEncrypt/add',
    method: 'post',
    data: data
  })
}

// 修改
export function update(data : any): any {
  return request({
    url: '/security/configPasswordEncrypt/update',
    method: 'put',
    data: data
  })
}

// 删除
export function deleteByIds(id : any): any {
  if (!(id instanceof Array)) {
    id = [id]
  }
  return request({
    url: '/security/configPasswordEncrypt/delete?ids=' + id.join(','),
    method: 'delete'
  })
}
