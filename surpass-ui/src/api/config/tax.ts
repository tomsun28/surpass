import request from '@/utils/Request'

export function fetchPage(query: any): any {
    return request({
        url: '/config/tax/fetch',
        method: 'get',
        params: query
    })
}

export function getOne(id: any) {
    return request({
        url: '/config/tax/get/' + id,
        method: 'get'
    })
}

export function addTax(data : any): any {
    return request({
        url: '/config/tax/save',
        method: 'post',
        data: data
    })
}

export function updateTax(data : any): any {
    return request({
        url: '/config/tax/update',
        method: 'put',
        data: data
    })
}

export function deleteOne(data: any) {
    return request({
        url: '/config/tax/delete',
        method: 'delete',
        data: data
    })
}
