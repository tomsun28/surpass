import request from '@/utils/Request'

export function fetchPage(query: any) {
    return request({
        url: '/fa/info/fetch',
        method: 'get',
        params: query
    })
}

export function getOne(id: string) {
    return request({
        url: `/fa/info/get/${id}`,
        method: 'get',
    })
}


export function delLead(data: any) {
    return request({
        url: '/fa/info/delete',
        method: 'delete',
        data: data
    })
}

export function saveOne(data: any) {
    return request({
        url: '/fa/info/save',
        method: 'post',
        data: data
    })
}

export function updateOne(data: any) {
    return request({
        url: '/fa/info/update',
        method: 'put',
        data: data
    })
}

export function generateVoucher(data: any) {
    return request({
        url: '/fa/info/generate-voucher',
        method: 'post',
        data: data
    })
}

export function deleteVoucherSubmit(data: any) {
    return request({
        url: '/fa/info/delete-voucher',
        method: 'delete',
        data: data
    })
}

