import request from '@/utils/Request'

export function getExpenseApproval(id: string): any {
    return request({
        url: '/expenseApproval/get',
        method: 'get',
        params: { id }
    })
}

export function listExpenseApproval(params: any): any {
    return request({
        url: '/expenseApproval/fetch',
        method: 'get',
        params
    })
}

export function saveExpenseApproval(data: any): any {
    return request({
        url: '/expenseApproval/save',
        method: 'post',
        data
    })
}

export function updateExpenseApproval(data: any): any {
    return request({
        url: '/expenseApproval/update',
        method: 'put',
        data
    })
}

export function deleteExpenseApproval(id: string): any {
    return request({
        url: '/expenseApproval/delete',
        method: 'delete',
        data: {
            listIds: [id]
        }
    })
}
