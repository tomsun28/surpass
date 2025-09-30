import request from '@/utils/Request'

// 查询会计科目列表
export function listSubjects(query : any): any {
    return request({
        url: '/standardsubject/fetch',
        method: 'get',
        params: query
    })
}

export function reorgDisplayName(query : any): any {
    return request({
        url: '/standardsubject/reorgDisplayName',
        method: 'get',
        params: query
    })
}

export function deleteBatch(data : any): any {
    return request({
        url: '/standardsubject/delete',
        method: 'delete',
        data: data
    })
}

export function getTree(query?: any): any {
    //读取准则的科目
    if(query.standardId){
        return request({
            url: '/standardsubject/tree',
            method: 'get',
            params: query
        })
    }else if(query.bookId){
        //读取账套的科目
        return request({
            url: '/booksubject/tree/'+query.bookId,
            method: 'get'
        })
    }
}

export function saveSubject(data : any): any {
    return request({
        url: '/standardsubject/save',
        method: 'post',
        data: data
    })
}

export function updateSubject(data : any): any {
    return request({
        url: '/standardsubject/update',
        method: 'put',
        data: data
    })
}

export function getOneSubject(id : any): any {
    return request({
        url: `/standardsubject/get/${id}`,
        method: 'get',
    })
}
