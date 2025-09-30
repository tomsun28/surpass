import request from '@/utils/Request'

export function getExpensePayment(id: string): any {
    return request({
        url: '/expensePayment/get',
        method: 'get',
        params: { id }
    })
}

export function listExpensePayment(params: any): any {
    return request({
        url: '/expensePayment/fetch',
        method: 'get',
        params
    })
}

export function saveExpensePayment(data: any): any {
    return request({
        url: '/expensePayment/save',
        method: 'post',
        data
    })
}

export function updateExpensePayment(data: any): any {
    return request({
        url: '/expensePayment/update',
        method: 'put',
        data
    })
}

export function deleteExpensePayment(id: string): any {
    return request({
        url: '/expensePayment/delete',
        method: 'delete',
        data: {
            listIds: [id]
        }
    })
}
