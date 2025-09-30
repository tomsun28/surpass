import request from '@/utils/Request'

// 获取资产负债配置明细
export function getConfigBalanceSheet(standardId:string,itemCode: string): any {
    return request({
        url: '/standard/balance-sheet/get' ,
        method: 'get',
        params: {
            standardId:standardId,
            itemCode: itemCode
        }
    })
}

// 获取资产负债配置
export function listConfigBalanceSheet(standardId: string): any {
    return request({
        url: '/standard/balance-sheet/fetch',
        method: 'get',
        params: {
            standardId: standardId
        }
    })
}

// 设置资产负债配置
export function saveConfigBalanceSheet(data: any): any {
    return request({
        url: '/standard/balance-sheet/save',
        method: 'post',
        data: data
    })
}

// 删除资产负债配置
export function delConfigBalanceSheet(id: string): any {
    return request({
        url: '/standard/balance-sheet/delete/' + id,
        method: 'delete'
    })
}
