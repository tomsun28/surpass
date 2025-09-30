import request from '@/utils/Request'

// 查询结账月份
export function fetch(query : any): any {
    return request({
        url: '/settlement/fetch',
        method: 'get',
        params: query
    })
}

export function checkout(query : any): any {
    return request({
        url: '/settlement/checkout',
        method: 'get',
        params: query
    })
}

export function verify(): any {
    return request({
        url: '/settlement/verify',
        method: 'get'
    })
}


export function saveOne(data : any): any {
    return request({
        url: '/settlement/save',
        method: 'post',
        data: data
    })
}

export function updateOne(data : any): any {
    return request({
        url: '/settlement/update',
        method: 'put',
        data: data
    })
}

export function getOne(id : any): any {
    return request({
        url: `/settlement/get/${id}`,
        method: 'get',
    })
}

export function deleteBatch(data : any): any {
    return request({
        url: '/settlement/delete',
        method: 'delete',
        data: data
    })
}

export function fetchcarry(query : any): any {
    return request({
        url: '/settlementcarry/fetchcarry',
        method: 'get',
        params: query
    })
}

export function generateVoucherSubmit(data: any) {
    return request({
        url: '/settlementcarry/generate-voucher',
        method: 'post',
        data: data
    })
  }

export function deleteVoucherSubmit(data: any) {
    return request({
        url: '/settlementcarry/generate-voucher',
        method: 'post',
        data: data
    })
  }


  export function deleteOne(id : any): any {
    return request({
        url: `/settlementcarry/delete/${id}`,
        method: 'delete',
    })
}