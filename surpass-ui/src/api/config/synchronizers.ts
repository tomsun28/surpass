import request from '@/utils/Request'

// 查询岗位列表
export function listSynchronizers(query : any): any {
  return request({
    url: '/config/synchronizers/fetch',
    method: 'get',
    params: query
  })
}

// 查询岗位详细
export function getSynchronizers(id : any): any {
  return request({
    url: '/config/synchronizers/get/' + id,
    method: 'get'
  })
}

// 查询岗位详细
export function setSynchronizers(id : any): any {
  return request({
    url: '/config/synchronizers/synchr',
    method: 'get',
    params: {
      id: id
    }
  })
}

// 新增岗位
export function addSynchronizers(data : any): any {
  return request({
    url: '/config/synchronizers/add',
    method: 'post',
    data: data
  })
}

// 修改岗位
export function updateSynchronizers(data : any): any {
  return request({
    url: '/config/synchronizers/update',
    method: 'put',
    data: data
  })
}

// 删除岗位
export function delSynchronizers(id : any): any {
  if (!(id instanceof Array)) {
    id = [id]
  }
  return request({
    url: '/config/synchronizers/delete?ids=' + id.join(','),
    method: 'delete'
  })
}
