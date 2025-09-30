import request from '@/utils/Request'

// 查询调度日志列表
export function listJobLog(query : any): any {
  return request({
    url: '/monitor/jobLog/list',
    method: 'get',
    params: query
  })
}

// 删除调度日志
export function delJobLog(jobLogId : any): any {
  return request({
    url: '/monitor/jobLog/' + jobLogId,
    method: 'delete'
  })
}

// 清空调度日志
export function cleanJobLog(): any {
  return request({
    url: '/monitor/jobLog/clean',
    method: 'delete'
  })
}
