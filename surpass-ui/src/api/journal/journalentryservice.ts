import request from '@/utils/Request'

export function fetch(query : any): any {
  return request({
    url: '/journal/entry/fetch',
    method: 'get',
    params: query
  })
}

export function get(id : any): any {
  return request({
    url: '/journal/entry/get/' + id,
    method: 'get'
  })
}

export function add(data : any): any {
  return request({
    url: '/journal/entry/add',
    method: 'post',
    data: data
  })
}

export function update(data : any): any {
  return request({
    url: '/journal/entry/update',
    method: 'put',
    data: data
  })
}

export function del(id : any): any {
  if (!(id instanceof Array)) {
    id = [id]
  }
  return request({
    url: '/journal/entry/delete?listIds=' + id.join(','),
    method: 'delete'
  })
}

export function generateVoucherSubmit(data: any) {
  return request({
      url: '/journal/entry/generate-voucher',
      method: 'post',
      data: data
  })
}
