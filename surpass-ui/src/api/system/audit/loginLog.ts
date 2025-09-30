import request from '@/utils/Request';

//分页查询
export function list(query : any): any {
    return request({
      url: '/audit/login/list',
      method: 'get',
      params: query
    })
  }