import request from '@/utils/Request'

// 机构配置
export function configGetCurrent(): any {
    return request({
        url: '/config/insurance_fund/getCurrent',
        method: 'get'
    })
}

export function configUpdateCurrent(data : any): any {
    return request({
        url: '/config/insurance_fund/updateCurrent',
        method: 'put',
        data: data
    })
}

