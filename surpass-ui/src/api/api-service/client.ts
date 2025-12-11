import request from "../../utils/Request.js";

// 客户端列表
export function list(params: any) {
    return request({
        url: '/client/list',
        method: 'get',
        params: params
    })
}

// 获取客户端详情
export function getClient(id: any) {
    return request({
        url: `/client/get/${id}`,
        method: 'get'
    })
}

// 新增客户端
export function addClient(data: any) {
    return request({
        url: '/client/add',
        method: 'post',
        data: data
    })
}

// 更新客户端
export function updateClient(data: any) {
    return request({
        url: '/client/update',
        method: 'put',
        data: data
    })
}

// 批量删除
export function deleteBatch(ids: any) {
    return request({
        url: '/client/delete?ids=' + ids,
        method: 'delete'
    })
}

// 获取客户端已授权的应用
export function getClientApps(clientId: any) {
    return request({
        url: `/client/relate-app/${clientId}`,
        method: 'get'
    })
}

// 保存客户端应用授权
export function saveClientApps(data: any) {
    return request({
        url: `/client/save-relate`,
        method: 'post',
        data: data
    })
}
