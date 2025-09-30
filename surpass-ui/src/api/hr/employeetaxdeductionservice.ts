import request from '@/utils/Request'

export function fetch(query : any): any {
  return request({
    url: '/employee/taxdeduction/fetch',
    method: 'get',
    params: query
  })
}

export function findAll(query : any): any {
  return request({
    url: '/employee/taxdeduction/findAll',
    method: 'get',
    params: query
  })
}

export function get(id : any): any {
  return request({
    url: '/employee/taxdeduction/get/' + id,
    method: 'get'
  })
}

export function add(data : any): any {
  return request({
    url: '/employee/taxdeduction/add',
    method: 'post',
    data: data
  })
}

export function update(data : any): any {
  return request({
    url: '/employee/taxdeduction/update',
    method: 'put',
    data: data
  })
}

export function importExcel(data: any): any {
  return request({
      url: '/employee/taxdeduction/import',
      method: 'post',
      data: data,
      headers: {
        'Content-Type': 'multipart/form-data' // 不需要手动设置，axios会自动设置正确类型
      }
  })
}

export function del(data : any): any {
  // if (!(id instanceof Array)) {
  //   id = [id]
  // }
  // return request({
  //   url: '/employee/taxdeduction/delete?listIds=' + id.join(','),
  //   method: 'delete'
  // })
  return request({
    url: '/employee/taxdeduction/delete',
    method: 'delete',
    data: data
  })
}
