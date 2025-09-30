import request from '@/utils/Request'

// 应用分类
export function getCategoryFetch(params : any): any {
  return request({
    url: '/apps/category/fetch',
    method: 'get',
    params: params
  })
}

export function getCategoryFetchById(id : any): any {
  return request({
    url: '/apps/category/get/' + id,
    method: 'get'
  })
}

export function addCategoryFetch(data : any): any {
  return request({
    url: '/apps/category/add',
    method: 'post',
    data: data
  })
}

export function editCategoryFetch(data : any): any {
  return request({
    url: '/apps/category/update',
    method: 'put',
    data: data
  })
}

export function delCategoryFetch(ids : any): any {
  if (!(ids instanceof Array)) {
    ids = [ids]
  }
  return request({
    url: '/apps/category/delete?ids=' + ids.join(','),
    method: 'delete'
  })
}
