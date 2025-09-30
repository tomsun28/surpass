import request from '@/utils/Request'

export function getExpenseDetail(id: string): any {
    return request({
        url: '/expenseDetail/get',
        method: 'get',
        params: { id }
    })
}

export function listExpenseDetail(params: any): any {
    return request({
        url: '/expenseDetail/fetch',
        method: 'get',
        params
    })
}

export function saveExpenseDetail(data: any): any {
    return request({
        url: '/expenseDetail/save',
        method: 'post',
        data
    })
}

export function updateExpenseDetail(data: any): any {
    return request({
        url: '/expenseDetail/update',
        method: 'put',
        data
    })
}

export function deleteExpenseDetail(id: string): any {
    return request({
        url: '/expenseDetail/delete',
        method: 'delete',
        data: {
            listIds: [id]
        }
    })
}
