import request from '@/utils/Request'

// 查询辅助核算项目列表
export function listAssistAcc(query: any) {
    return request({
        url: '/base/assist-acc/fetch',
        method: 'get',
        params: query
    })
}

// 查询辅助核算项目详细
export function getAssistAcc(id: any) {
    return request({
        url: '/base/assist-acc/get/' + id,
        method: 'get'
    })
}

// 新增辅助核算项目
export function addAssistAcc(data: any) {
    return request({
        url: '/base/assist-acc/save',
        method: 'post',
        data: data
    })
}

// 修改辅助核算项目
export function updateAssistAcc(data: any) {
    return request({
        url: '/base/assist-acc/update',
        method: 'put',
        data: data
    })
}

// 删除辅助核算项目
export function delAssistAcc(data: any) {
    return request({
        url: '/base/assist-acc/delete',
        method: 'delete',
        data: data
    })
}
