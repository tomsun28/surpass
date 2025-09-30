import request from '@/utils/Request'

// 获取利润配置明细
export function getConfigIncome(id: string): any {
    return request({
        url: '/statement/config/income/' + id,
        method: 'get'
    })
}

// 获取利润配置
export function listConfigIncome(yearPeriod: string): any {
    return request({
        url: '/statement/config/income/fetch',
        method: 'get',
        params: {
            yearPeriod: yearPeriod
        }
    })
}

// 设置利润配置
export function saveConfigIncome(data: any): any {
    return request({
        url: '/statement/config/income',
        method: 'post',
        data: data
    })
}

// 删除利润配置
export function delConfigIncome(id: string): any {
    return request({
        url: '/statement/config/income/' + id,
        method: 'delete'
    })
}
