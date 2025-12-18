import request from "../../utils/Request.js";

export function create(data : any): any {
    return request({
        url: '/app-resources/add',
        method: 'post',
        data: data
    })
}
