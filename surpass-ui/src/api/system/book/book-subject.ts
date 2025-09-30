import request from '@/utils/Request'

// 查询账套下会计科目列表
export function listSubjectsBySet(query : any): any {
    return request({
        url: '/booksubject/fetch',
        method: 'get',
        params: query
    })
}

export function getSetSubject(query: any): any {
    return request({
        url: `/booksubject/get`,
        method: 'get',
        params: query
    })
}

export function saveSubject(data : any): any {
    return request({
        url: '/booksubject/save',
        method: 'post',
        data: data
    })
}

export function updateSubject(data : any): any {
    return request({
        url: '/booksubject/update',
        method: 'put',
        data: data
    })
}

export function deleteBatch(data : any): any {
    return request({
        url: '/booksubject/delete',
        method: 'delete',
        data: data
    })
}

export function reorgDisplayName(query : any): any {
    return request({
        url: '/booksubject/reorgDisplayName',
        method: 'get',
        params: query
    })
}
