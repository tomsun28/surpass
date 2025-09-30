import request from '@/utils/Request'

// 查询会计科目列表分页
export function listStandards(query : any): any {
    return request({
        url: '/standard/fetch',
        method: 'get',
        params: query
    })
}

export function listStandardsAll(query: any): any {
    return request({
        url: '/standard/fetchAll',
        method: 'get',
        params: query
    })
}

export function saveOne(data : any): any {
    return request({
        url: '/standard/save',
        method: 'post',
        data: data
    })
}

export function updateOne(data : any): any {
    return request({
        url: '/standard/update',
        method: 'put',
        data: data
    })
}

export function deleteBatch(data : any): any {
    return request({
        url: '/standard/delete',
        method: 'delete',
        data: data
    })
}

export function getOne(id : any): any {
    return request({
        url: `/standard/get/${id}`,
        method: 'get',
    })
}


