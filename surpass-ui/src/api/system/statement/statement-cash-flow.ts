import request from '@/utils/Request'

export function saveItemCodes(data: any) {
    return request({
        url: '/statement/cash-flow/specify',
        method: 'post',
        data: data
    })
}

export function getCashFlowItems(query: any) {
    return request({
        url: '/statement/cash-flow/get',
        method: 'get',
        params: query
    })
}

export function saveCashFlowItem(data: any) {
    return request({
        url: '/statement/cash-flow/save',
        method: 'post',
        data: data
    })
}

