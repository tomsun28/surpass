import request from "../../utils/Request.js";

export function list(): any {
    return request({
        url: '/app-resources/list',
        method: 'get'
    })
}

export function getById(id : any): any {
    return request({
        url: `/app-resources/get/${id}`,
        method: 'get'
    })
}
