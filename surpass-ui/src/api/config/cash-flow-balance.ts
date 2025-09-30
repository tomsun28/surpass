import request from "@/utils/Request";

export function fetchPage(query: any): any {
    return request({
        url: '/config/cash-flow-balance/fetch',
        method: 'get',
        params: query
    })
}

// 添加保存数据的函数
export function saveBalanceData(data: any) {
    return request({
        url: '/config/cash-flow-balance/save',
        method: 'post',
        data: data
    });
}

// 获取选择项
export function getSelectItem(query: any) {
    return request({
        url: '/config/cash-flow-balance/get-select-item',
        method: 'get',
        params: query
    });
}

//新增科目和现金流量默认关系
export function saveSelectItem(data: any) {
    return request({
        url: '/config/subject-cash-flow/save',
        method: 'post',
        data: data
    });
}

//查询科目和现金流量默认关系
export function fetchSelectItem(query: any) {
    return request({
        url: '/config/subject-cash-flow/fetch-relationships',
        method: 'get',
        params: query
    });
}
