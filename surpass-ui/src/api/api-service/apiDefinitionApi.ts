import request from "../../utils/Request.js";

export function list(): any {
    return request({
        url: '/apis',
        method: 'get'
    })
}

export function getById(id : any): any {
    return request({
        url: `/apis/${id}`,
        method: 'get'
    })
}

export function getByDatasourceId(datasourceId : any): any {
    return request({
        url: `/apis/datasource/${datasourceId}`,
        method: 'get'
    })
}

export function create(data : any): any {
    return request({
        url: '/apis',
        method: 'post',
        data: data
    })
}

export function update(id : any, data: any): any {
    return request({
        url: `/apis/${id}`,
        method: 'put',
        data: data
    })
}

export function deleteData(id : any): any {
    return request({
        url: `/apis/${id}`,
        method: 'delete'
    })
}
