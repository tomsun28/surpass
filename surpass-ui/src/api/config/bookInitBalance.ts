import request from '@/utils/Request'

// 查询初始余额列表
export function listBookInitBalance(query: any) {
    return request({
        url: '/base/init-balance/list',
        method: 'get',
        params: query
    })
}

// 保存初始余额
export function saveBookInitBalance(data: any) {
    return request({
        url: '/base/init-balance/save',
        method: 'post',
        data: data
    })
}

