import request from '@/utils/Request'

/**
 * 明细账
 * @param query
 */
export function listVoucherSubLedger(query: any): any {
    return request({
        url: '/voucher/items/fetch',
        method: 'get',
        params: query
    })
}


/**
 * 现金流量凭证明细
 * @param query
 */
export function fetchByCashFlow(query: any): any {
    return request({
        url: '/voucher/items/fetch-by-cash-flow',
        method: 'get',
        params: query
    })
}

export function listVouchers(query: any): any {
    return request({
        url: '/voucher/fetch',
        method: 'get',
        params: query
    })
}

export function exportVouchers(query: any): any {
    return request({
        url: `/voucher/export`,
        method: 'get',
        params: query,
        responseType: 'blob'
    })
}

export function deleteBatch(ids: any): any {
    return request({
        url: '/voucher/delete/' + ids,
        method: 'delete'
    })
}

export function submitVoucher(data: any): any {
    return request({
        url: '/voucher/submit',
        method: 'post',
        data: data
    })
}

export function draftVoucher(data: any): any {
    return request({
        url: '/voucher/draft',
        method: 'post',
        data: data
    })
}

export function updateVoucher(data: any): any {
    return request({
        url: '/voucher/update',
        method: 'put',
        data: data
    })
}

export function getOneVoucher(id: any): any {
    return request({
        url: `/voucher/get/${id}`,
        method: 'get',
    })
}

/**
 * 获取最新可用凭证子号
 * @param head
 * @param year
 * @param month
 */
export function getVoucherAbleWordNum(head: any, year: any, month: any): any {
    return request({
        url: `/voucher/able-word-num`,
        method: 'get',
        params: {
            head: head,
            year: year,
            month: month,
        }
    })
}

/**
 * 取消申请
 * @param ids
 */
export function cancelVoucherByIds(ids: any): any {
    return request({
        url: `/voucher/cancel/` + ids,
        method: 'put'
    })
}

/**
 * 凭证连续性验证
 */
export function getVoucherSuccessiveList(params: any): any {
    return request({
        url: `/voucher/successive`,
        method: 'get',
        params: params
    })
}

/**
 * 更新凭证连续性
 */
export function updateVoucherSuccessive(data: any): any {
    return request({
        url: `/voucher/successive`,
        method: 'put',
        data: data
    })
}

/**
 * 批量提交凭证
 * @param ids 凭证ID串
 */
export function submitBatch(ids: any): any {
    return request({
        url: '/voucher/submit/' + ids,
        method: 'post'
    })
}

/**
 * 审核凭证
 * @param ids
 */
export function auditBatch(ids: any): any {
    return request({
        url: '/voucher/audit/' + ids,
        method: 'put'
    })
}

/**
 * 过账凭证
 * @param ids
 */
export function senderBatch(ids: any): any {
    return request({
        url: '/voucher/sender/' + ids,
        method: 'put'
    })
}

/**
 * 主管审核凭证
 * @param ids
 */
export function manageBatch(ids: any): any {
    return request({
        url: '/voucher/manage-audit/' + ids,
        method: 'put'
    })
}
