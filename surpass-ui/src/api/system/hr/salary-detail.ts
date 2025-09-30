import request from '@/utils/Request'

//查询员工明细表
export function fetchPage(query: any) {
    return request({
        url: '/salary/detail/fetch',
        method: 'get',
        params: query
    })
}

//生成员工明细中间表
export function createDetailTemp(data: any) {
    return request({
        url: '/salary/detail/createTable',
        method: 'post',
        data: data
    })
}

export function getCurrentDetail(id: any): any {
    return request({
        url: `/salary/detail/get/${id}`,
        method: 'get'
    })
}

export function updateDetailTemp(data: any) {
    return request({
        url: '/salary/detail/update',
        method: 'put',
        data: data
    })
}

export function createFinalDetail() {
    return request({
        url: '/salary/detail/submit-detail',
        method: 'post'
    })
}

export function reCalculateSalary(query: any) {
    return request({
        url: '/salary/detail/re-calculate',
        method: 'get',
        params: query
    })
}

export function batchDelete(data: any) {
    return request({
        url: '/salary/detail/delete',
        method: 'delete',
        data: data
    })
}
