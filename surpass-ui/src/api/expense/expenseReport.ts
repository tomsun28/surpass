import request from '@/utils/Request'

// 获取
export function getExpenseReport(id: string): any {
    return request({
        url: '/expenseReport/get',
        method: 'get',
        params: {id}
    })
}

// 列表
export function listExpenseReport(params: any): any {
    return request({
        url: '/expenseReport/fetch',
        method: 'get',
        params
    })
}

// 保存
export function saveExpenseReport(data: any): any {
    return request({
        url: '/expenseReport/save',
        method: 'post',
        data
    })
}

// 更新
export function updateExpenseReport(data: any): any {
    return request({
        url: '/expenseReport/update',
        method: 'put',
        data
    })
}

// 删除
export function deleteExpenseReport(id: string): any {
    return request({
        url: '/expenseReport/delete',
        method: 'delete',
        data: {
            listIds: [id]
        }
    })
}
