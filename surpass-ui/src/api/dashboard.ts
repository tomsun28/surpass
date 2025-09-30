import request from '@/utils/Request'

// 仪表盘
export function getDashboard(): any {
    return request({
        url: '/dashboard',
        method: 'get'
    })
}

export function statisticsFundBalance(params: any) {
    return request({
        url: '/statistics/fund-balance',
        method: 'get',
        params: params
    })
}

export function statisticsAccountsReceivable(params: any) {
    return request({
        url: '/statistics/accounts-receivable',
        method: 'get',
        params: params
    })
}
export function statisticsAccountsPayable(params: any) {
    return request({
        url: '/statistics/accounts-payable',
        method: 'get',
        params: params
    })
}
export function statisticsAbleCash(params: any) {
    return request({
        url: '/statistics/able-cash',
        method: 'get',
        params: params
    })
}
export function statisticsOtherSubjects(params: any) {
    return request({
        url: '/statistics/other-subjects',
        method: 'get',
        params: params
    })
}
export function statisticsNetProfit(params: any) {
    return request({
        url: '/statistics/net-profit',
        method: 'get',
        params: params
    })
}
export function statisticsRevenueCost(params: any) {
    return request({
        url: '/statistics/revenue-cost',
        method: 'get',
        params: params
    })
}
export function statisticsExpense(params: any) {
    return request({
        url: '/statistics/expense',
        method: 'get',
        params: params
    })
}
export function statisticsAddedTax(params: any) {
    return request({
        url: '/statistics/added-tax',
        method: 'get',
        params: params
    })
}