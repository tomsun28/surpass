import request from '@/utils/Request'

export function apiSessionList(query : any): any {
  return request({
    url: '/access/session/fetch',
    method: 'get',
    params: query
  })
}



export function apiDelSession(id : any): any {
  if (!(id instanceof Array)) {
    id = [id]
  }
  return request({
    url: '/access/session/terminate?ids=' + id.join(','),
    method: 'delete'
  })
}
