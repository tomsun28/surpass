import request from '@/utils/Request'

export function fetchPage(query: any) {
    return request({
        url: '/invoice/fetch',
        method: 'get',
        params: query
    })
}

export function getOne(id: string) {
    return request({
        url: `/invoice/get/${id}`,
        method: 'get',
    })
}


export function delInvoice(data: any) {
    return request({
        url: '/invoice/delete',
        method: 'delete',
        data: data
    })
}

export function saveOne(data: any) {
    return request({
        url: '/invoice/save',
        method: 'post',
        data: data
    })
}

export function updateOne(data: any) {
    return request({
        url: '/invoice/update',
        method: 'put',
        data: data
    })
}

export function generateVoucher(data: any) {
    return request({
        url: '/invoice/generate-voucher',
        method: 'post',
        data: data
    })
}

export function deleteVoucherSubmit(data: any) {
    return request({
        url: '/invoice/delete-voucher',
        method: 'delete',
        data: data
    })
}
