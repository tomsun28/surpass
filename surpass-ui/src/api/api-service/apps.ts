import request from "@/utils/Request";

export function list(query : any): any {
    return request({
        url: '/app/list',
        method: 'get',
        params: query
    })
}

export function deleteBatch(id : any): any {
    return request({
        url: '/app/delete?ids=' + id,
        method: 'delete'
    })
}

export function addApp(data : any): any {
    return request({
        url: '/app/add',
        method: 'post',
        data: data
    })
}

export function updateApp(data : any): any {
    return request({
        url: '/app/update',
        method: 'put',
        data: data
    })
}

export function getApp(id : any): any {
    return request({
        url: `/app/get/${id}`,
        method: 'get'
    })
}

// 获取应用已绑定的API列表
export function getAppApis(appId: any) {
    return request({
        url: `/apps/${appId}/apis`,
        method: 'get'
    })
}

// 保存应用API绑定
export function saveAppApis(data: any): any {
    return request({
        url: '/apps/bind-apis',
        method: 'post',
        data: data
    })
}
