import request from '@/utils/Request'

// 获取组织
export function listCategory(): any {
  return request({
    url: '/apps/category/findList',
    method: 'get'
  })
}
