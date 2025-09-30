import request from '@/utils/Request'

//查询员工明细表
export function fetchPage(query: any) {
    return request({
        url: '/employee/salary/fetch',
        method: 'get',
        params: query
    })
}

export function getCurrentDetail(id: any): any {
    return request({
        url: `/employee/salary/get/${id}`,
        method: 'get'
    })
}

export function salarySummary(query: any): any {
    return request({
        url: `/employee/salary/summary`,
        method: 'get',
        params: query
    })
}

export function updateDetail(data: any) {
    return request({
        url: '/employee/salary/update',
        method: 'put',
        data: data
    })
}

export function saveDetail(data: any) {
    return request({
        url: '/employee/salary/save',
        method: 'post',
        data: data
    })
}

export function delSalary(data: any) {
    return request({
        url: '/employee/salary/delete',
        method: 'delete',
        data: data
    })
}

export function exportSalary(query: any): any {
    return request({
        url: '/employee/salary/export',
        method: 'get',
        params: query,
        responseType: 'blob'
    })
}

export function generateVoucherSubmit(data: any) {
    return request({
        url: '/employee/salary/generate-voucher',
        method: 'post',
        data: data
    })
}

export function deleteVoucherSubmit(data: any) {
    return request({
        url: '/employee/salary/delete-voucher',
        method: 'post',
        data: data
    })
}
