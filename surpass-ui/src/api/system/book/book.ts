import request from '@/utils/Request'

// 查询会计科目列表
export function listBooksSets(query : any): any {
    return request({
        url: '/book/fetch',
        method: 'get',
        params: query
    })
}

export function saveOne(data : any): any {
    return request({
        url: '/book/save',
        method: 'post',
        data: data
    })
}

export function updateOne(data : any): any {
    return request({
        url: '/book/update',
        method: 'put',
        data: data
    })
}

export function getOne(id : any): any {
    return request({
        url: `/book/get/${id}`,
        method: 'get',
    })
}

export function deleteBatch(data : any): any {
    return request({
        url: '/book/delete',
        method: 'delete',
        data: data
    })
}

export function listStore(): any {
    return request({
        url: '/book/fetchAll',
        method: 'get'
    })
}


