import request from '@/utils/Request'

export function getExpenseAttachment(id: string): any {
    return request({
        url: '/expenseAttachment/get',
        method: 'get',
        params: { id }
    })
}

export function listExpenseAttachment(params: any): any {
    return request({
        url: '/expenseAttachment/fetch',
        method: 'get',
        params
    })
}

export function saveExpenseAttachment(data: any): any {
    return request({
        url: '/expenseAttachment/save',
        method: 'post',
        data
    })
}

export function updateExpenseAttachment(data: any): any {
    return request({
        url: '/expenseAttachment/update',
        method: 'put',
        data
    })
}

export function deleteExpenseAttachment(id: string): any {
    return request({
        url: '/expenseAttachment/delete',
        method: 'delete',
        data: {
            listIds: [id]
        }
    })
}
