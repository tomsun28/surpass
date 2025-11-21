import request from "../../utils/Request.js";

export function getPublishHistory(apiId : any): any {
    return request({
        url: `/publish/api/${apiId}/history`,
        method: 'get'
    })
}

export function getLatestPublishRecord(apiId : any): any {
    return request({
        url: `/publish/api/${apiId}/latest`,
        method: 'get'
    })
}

// 发布版本
export function publishVersion(apiId : any, versionId : any, operator = 'admin', description = ''): any {
    return request({
        url: `/publish/api/${apiId}/version/${versionId}/publish?operator=${operator}&description=${description}`,
        method: 'post'
    })
}

// 下线版本
export function offlineVersion(apiId : any, versionId : any, operator = 'admin', description = ''): any {
    return request({
        url: `/publish/api/${apiId}/version/${versionId}/offline?operator=${operator}&description=${description}`,
        method: 'post'
    })
}

// 提交审核
export function submitForReview(apiId : any, versionId : any): any {
    return request({
        url: `/publish/api/${apiId}/version/${versionId}/submit`,
        method: 'post'
    })
}

// 驳回审核
export function rejectVersion(apiId : any, versionId : any, reason : any): any {
    return request({
        url: `/publish/api/${apiId}/version/${versionId}/reject?reason=${reason}`,
        method: 'post'
    })
}

// 重新上线版本
export function onlineVersion(apiId : any, versionId : any, operator = 'admin', description = ''): any {
    return request({
        url: `/publish/api/${apiId}/version/${versionId}/online?operator=${operator}&description=${description}`,
        method: 'post'
    })
}


