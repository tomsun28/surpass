import request from "../../utils/Request.js";

export function getById(id : any): any {
    return request({
        url: `/versions/${id}`,
        method: 'get'
    })
}

export function getByApiId(apiId : any): any {
    return request({
        url: `/versions/api/${apiId}`,
        method: 'get'
    })
}

export function getPublishedVersion(apiId : any): any {
    return request({
        url: `/versions/api/${apiId}/published`,
        method: 'get'
    })
}

export function create(data : any): any {
    return request({
        url: '/versions',
        method: 'post',
        data: data
    })
}

export function update(id : any, data: any): any {
    return request({
        url: `/versions/${id}`,
        method: 'put',
        data: data
    })
}

export function deleteData(id : any): any {
    return request({
        url: `/versions/${id}`,
        method: 'delete'
    })
}

export function updateStatus(id: any, status: any): any {
    return request({
        url: `/versions/${id}/status?status=${status}`,
        method: 'put'
    })
}

export function getDiff(oldVersionId: any, newVersionId: any): any {
    return request({
        url: `/versions/diff?oldVersionId=${oldVersionId}&newVersionId=${newVersionId}`,
        method: 'get'
    })
}

export function getVersionStatistics(apiId: any): any {
    return request({
        url: `/versions/api/${apiId}/statistics`,
        method: 'get'
    })
}
