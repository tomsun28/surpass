import request from '@/utils/Request'

// 机构配置
export function institutionsGetCurrent(): any {
  return request({
    url: '/config/institutions/getCurrent',
    method: 'get'
  })
}

export function institutionsUpdateCurrent(data : any): any {
  return request({
    url: '/config/institutions/updateCurrent',
    method: 'put',
    data: data
  })
}

export function fetch(query : any): any {
  return request({
    url: '/config/institutions/fetch',
    method: 'get',
    params: query
  })
}

export function bookFetch(query : any): any {
  return request({
    url: '/config/institutions/books',
    method: 'get',
    params: query
  })
}

export function getOne(id : any): any {
  return request({
    url: `/config/institutions/get/${id}`,
    method: 'get'
  })
}

export function saveOne(data : any): any {
  return request({
    url: '/config/institutions/add',
    method: 'post',
    data: data
  })
}

export function updateOne(data : any): any {
  return request({
    url: '/config/institutions/update',
    method: 'put',
    data: data
  })
}

export function deleteBatch(data : any): any {
  return request({
    url: '/config/institutions/delete',
    method: 'delete',
    data: data
  })
}




