import request from '@/utils/Request'



export function listGroup(query : any): any {
  return request({
    url: '/idm/groups/fetch',
    method: 'get',
    params: query
  })
}
