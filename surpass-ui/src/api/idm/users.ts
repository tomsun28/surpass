import request from '@/utils/Request'



export function userList(query : any): any {
  return request({
    url: '/users/fetch',
    method: 'get',
    params: query
  })
}
