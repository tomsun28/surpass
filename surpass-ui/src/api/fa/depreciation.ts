import request from '@/utils/Request'

export function fetchPage(query: any) {
    return request({
        url: '/fa/depreciation/fetch',
        method: 'get',
        params: query
    })
}

export function generateProvision(data: any) {
    return request({
        url: '/fa/depreciation/provision',
        method: 'post',
        data: data
    })
}


