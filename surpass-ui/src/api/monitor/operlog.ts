import request from '@/utils/Request'

// 查询操作日志列表
export function list(query : any): any {
  return request({
    url: '/monitor/operlog/list',
    method: 'get',
    params: query
  })
}

// 删除操作日志
export function delOperlog(operId : any): any {
  return request({
    url: '/monitor/operlog/' + operId,
    method: 'delete'
  })
}

// 清空操作日志
export function cleanOperlog(): any {
  return request({
    url: '/monitor/operlog/clean',
    method: 'delete'
  })
}
