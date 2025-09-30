import request from '@/utils/Request'


// 获取ID
export function generateId(): any {
  return request({
    url: '/socials-provider/generate',
    method: 'get'
  })
}


// 查询角色列表
export function listPage(query : any): any {
  return request({
    url: '/socials-provider/fetch',
    method: 'get',
    params: query
  })
}

// 查询分组详细
export function get(groupId : any): any {
  return request({
    url: '/socials-provider/get/' + groupId,
    method: 'get'
  })
}

// 新增分组
export function add(data : any): any {
  return request({
    url: '/socials-provider/add',
    method: 'post',
    data: data
  })
}

// 修改分组
export function update(data : any): any {
  return request({
    url: '/socials-provider/edit',
    method: 'put',
    data: data
  })
}

// 删除分组
export function deleteByIds(ids : any): any {
  return request({
    url: '/socials-provider/delete/' + ids,
    method: 'delete'
  })
}
