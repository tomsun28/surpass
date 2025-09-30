import request from '@/utils/Request'

// 获取组织
export function orgsTree(): any {
  return request({
    url: '/orgs/tree',
    method: 'get'
  })
}
