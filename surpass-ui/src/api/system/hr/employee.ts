import request from '@/utils/Request'

// 查询员工信息列表
export function listEmployee(query: any) {
    return request({
        url: '/salary/employee/fetch',
        method: 'get',
        params: query
    })
}

// 查询员工信息详细
export function getEmployee(id: any) {
    return request({
        url: '/salary/employee/get/' + id,
        method: 'get'
    })
}

// 新增员工信息
export function addEmployee(data: any) {
    return request({
        url: '/salary/employee/save',
        method: 'post',
        data: data
    })
}

// 修改员工信息
export function updateEmployee(data: any) {
    return request({
        url: '/salary/employee/update',
        method: 'put',
        data: data
    })
}

// 删除员工信息
export function delEmployee(data: any) {
    return request({
        url: '/salary/employee/delete',
        method: 'delete',
        data: data
    })
}
