import request from "../../utils/Request.js";

export function list(): any {
    return request({
        url: '/datasources',
        method: 'get'
    })
}

export function getById(id : any): any {
    return request({
        url: `/datasources/${id}`,
        method: 'get'
    })
}

export function create(data : any): any {
    return request({
        url: '/datasources',
        method: 'post',
        data: data
    })
}

export function update(id : any, data: any): any {
    return request({
        url: `/datasources/${id}`,
        method: 'put',
        data: data
    })
}

export function deleteData(id : any): any {
    return request({
        url: `/datasources/${id}`,
        method: 'delete'
    })
}

export function testConnection(id : any): any {
    return request({
        url: `/datasources/${id}/test`,
        method: 'post'
    })
}

export function testConnectionWithData(data : any): any {
    return request({
        url: '/datasources/test',
        method: 'post',
        data: data
    })
}

export function updateStatus(id : any, status : any): any {
    return request({
        url: `/datasources/${id}/status?status=${status}`,
        method: 'put'
    })
}
