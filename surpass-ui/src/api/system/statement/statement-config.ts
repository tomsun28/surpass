import request from '@/utils/Request'

// 获取资产负债配置明细
export function getConfigBalanceSheet(id: string): any {
    return request({
        url: '/statement/config/balance-sheet/' + id,
        method: 'get'
    })
}

// 获取资产负债配置
export function listConfigBalanceSheet(yearPeriod: string): any {
    return request({
        url: '/statement/config/balance-sheet/fetch',
        method: 'get',
        params: {
            yearPeriod: yearPeriod
        }
    })
}

// 设置资产负债配置
export function saveConfigBalanceSheet(data: any): any {
    return request({
        url: '/statement/config/balance-sheet',
        method: 'post',
        data: data
    })
}

// 删除资产负债配置
export function delConfigBalanceSheet(id: string): any {
    return request({
        url: '/statement/config/balance-sheet/' + id,
        method: 'delete'
    })
}

// 获取报表项统计规则配置
export function listConfigRules(reportItemId: string): any {
    return request({
        url: '/statement/config/rules',
        method: 'get',
        params: {
            reportItemId: reportItemId
        }
    })
}

// 报表项统计规则配置
export function saveConfigRules(data: any): any {
    return request({
        url: '/statement/config/rules',
        method: 'post',
        data: data
    })
}
