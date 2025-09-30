import request from '@/utils/Request'

export function pageList(query: any) {
    return request({
        url: '/employee/salary-voucher-rule/fetch',
        method: 'get',
        params: query
    })
}

export function getById(id: string): any {
    return request({
        url: `/employee/salary-voucher-rule/get/${id}`,
        method: 'get'
    })
}

export function addRule(data: any) {
    return request({
        url: '/employee/salary-voucher-rule/save',
        method: 'post',
        data: data
    })
}

export function updateRule(data: any) {
    return request({
        url: '/employee/salary-voucher-rule/update',
        method: 'put',
        data: data
    })
}

export function delRule(data: any) {
    return request({
        url: '/employee/salary-voucher-rule/delete',
        method: 'delete',
        data: data
    })
}

export function changeStatus(data: any) {
    return request({
        url: '/employee/salary-voucher-rule/change-status',
        method: 'put',
        data: data
    })
}

