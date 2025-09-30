import request from '@/utils/Request'


export function listRoles(query : any): any {
  return request({
    url: '/permissions/roles/fetch',
    method: 'get',
    params: query
  })
}

export function getRoles(id : any): any {
  return request({
    url: '/permissions/roles/get/'+id,
    method: 'get',
  })
}


export function update(data : any): any {
  return request({
    url: '/permissions/roles/update',
    method: 'put',
    data: data
  })
}


export function create(data : any): any {
  return request({
    url: '/permissions/roles/add',
    method: 'post',
    data: data
  })
}


export function deleteByIds(id : any): any {
  if (!(id instanceof Array)) {
    id = [id]
  }
  return request({
    url: '/permissions/roles/delete?ids=' + id.join(','),
    method: 'delete'
  })
}
