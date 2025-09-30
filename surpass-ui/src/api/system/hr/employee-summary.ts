import request from '@/utils/Request'

export function saveSummary(data: any) {
    return request({
        url: '/employee/salary-summary/save',
        method: 'post',
        data: data
    })
}

export function fetchPage(query: any) {
    return request({
        url: '/employee/salary-summary/fetch',
        method: 'get',
        params: query
    })
}

export function salarySummary(query: any) {
    return request({
        url: '/employee/salary-summary/summary',
        method: 'get',
        params: query
    })
}
export function generateVoucherSubmit(data: any) {
    return request({
        url: '/employee/salary-summary/generate-voucher',
        method: 'post',
        data: data
    })
}

