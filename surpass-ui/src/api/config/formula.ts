import request from '@/utils/Request'

export function fetchPage(query: any): any {
    return request({
        url: '/config/salary/formula/fetch',
        method: 'get',
        params: query
    })
}

export function getOne(id: any) {
    return request({
        url: '/config/salary/formula/get/' + id,
        method: 'get'
    })
}

export function addOne(data : any): any {
    return request({
        url: '/config/salary/formula/save',
        method: 'post',
        data: data
    })
}

export function updateOne(data : any): any {
    return request({
        url: '/config/salary/formula/update',
        method: 'put',
        data: data
    })
}

export function deleteBatch(data: any) {
    return request({
        url: '/config/salary/formula/delete',
        method: 'delete',
        data: data
    })
}
