import request from '@/utils/Request'

export function fetchPage(query: any) {
    return request({
        url: '/invoice-voucher/fetch',
        method: 'get',
        params: query
    })
}

export function getOne(id: string) {
    return request({
        url: `/invoice-voucher/get/${id}`,
        method: 'get',
    })
}


export function delTemplate(data: any) {
    return request({
        url: '/invoice-voucher/delete',
        method: 'delete',
        data: data
    })
}

export function saveOne(data: any) {
    return request({
        url: '/invoice-voucher/save',
        method: 'post',
        data: data
    })
}

export function updateOne(data: any) {
    return request({
        url: '/invoice-voucher/update',
        method: 'put',
        data: data
    })
}

export function setTemplateAsDefault(data: any) {
    return request({
        url: `/invoice-voucher/set-default`,
        method: 'put',
        data: data
    })
}
