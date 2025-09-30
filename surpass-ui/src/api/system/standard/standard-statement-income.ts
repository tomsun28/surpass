import request from '@/utils/Request'

// 获取利润配置明细
export function getConfigIncome(standardId:string,itemCode: string): any {
    return request({
        url: '/standardstatementincome/get',
        method: 'get',
        params: {
            standardId:standardId,
            itemCode: itemCode
        }
    })
}

// 获取利润配置
export function listConfigIncome(standardId: string): any {
    return request({
        url: '/standardstatementincome/fetch',
        method: 'get',
        params: {
            standardId: standardId
        }
    })
}

// 设置利润配置
export function saveConfigIncome(data: any): any {
    return request({
        url: '/standardstatementincome/save',
        method: 'post',
        data: data
    })
}

// 删除利润配置
export function delConfigIncome(id: string): any {
    return request({
        url: '/standardstatementincome/delete/' + id,
        method: 'delete'
    })
}
