import request from '@/utils/Request'

// 查询在线用户列表
export function list(query : any): any {
  return request({
    url: '/monitor/online/list',
    method: 'get',
    params: query
  })
}

// 强退用户
export function forceLogout(tokenId : any): any {
  return request({
    url: '/monitor/online/' + tokenId,
    method: 'delete'
  })
}
