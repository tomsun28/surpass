import request from '@/utils/Request'

export function fetchPage(query: any) {
    return request({
        url: '/fa/category/fetch',
        method: 'get',
        params: query
    })
}

export function getOne(id: string) {
    return request({
        url: `/fa/category/get/${id}`,
        method: 'get',
    })
}


export function delLead(data: any) {
    return request({
        url: '/fa/category/delete',
        method: 'delete',
        data: data
    })
}

export function saveOne(data: any) {
    return request({
        url: '/fa/category/save',
        method: 'post',
        data: data
    })
}

export function updateOne(data: any) {
    return request({
        url: '/fa/category/update',
        method: 'put',
        data: data
    })
}

