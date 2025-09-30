import request from '@/utils/Request'

// 查询角色列表
export function listProviders(query : any): any {
    return request({
        url: '/provider/list',
        method: 'get',
        params: query
    })
}

export function getProvider(id : any): any {
    return request({
        url: `/provider/get/${id}`,
        method: 'get'
    })
}

export function addProvider(data : any): any {
    return request({
        url: `/provider/add`,
        method: 'post',
        data: data
    })
}

export function editProvider(data : any): any {
    return request({
        url: `/provider/edit`,
        method: 'put',
        data: data
    })
}

export function deleteBatch(data : any): any {
    return request({
        url: `/provider/delete`,
        method: 'delete',
        data: data
    })
}

export function disableProvider(id : any): any {
    return request({
        url: `/provider/disable/${id}`,
        method: 'put',
    })
}

export function activeProvider(id : any): any {
    return request({
        url: `/provider/active/${id}`,
        method: 'put',
    })
}
