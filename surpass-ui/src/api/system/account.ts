import request from '@/utils/Request'

export function listAccounts(query : any): any {
    return request({
        url: '/accounts/fetch',
        method: 'get',
        params: query
    })
}

export function updateStatus(query : any): any {
    return request({
        url: '/accounts/updateStatus',
        method: 'get',
        params: query
    })
}

export function generateStrategy(query : any): any {
    return request({
        url: '/accounts/generate',
        method: 'get',
        params: query
    })
}

export function addAccounts(data : any): any {
    return request({
        url: '/accounts/add',
        method: 'post',
        data: data
    })
}

export function updateAccounts(data : any): any {
    return request({
        url: '/accounts/update',
        method: 'put',
        data: data
    })
}

export function deleteAccounts(id : any): any {
    return request({
        url: '/accounts/delete?ids=' + id,
        method: 'delete'
    })
}

export function getOneAccount(id : any): any {
    return request({
        url: `/accounts/get/${id}`,
        method: 'get'
    })
}

export function accountsUnitList(query : any): any {
    return request({
        url: `/accountsUnited/fetch`,
        method: 'get',
        params: query
    })
}
