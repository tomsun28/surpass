import request from '@/utils/Request'

// 查询部门列表
export function listDept(query: any): any {
    return request({
        url: '/orgs/fetch',
        method: 'get',
        params: query
    })
}

// 查询部门详细
export function getDept(deptId: any): any {
    return request({
        url: `/orgs/get/${deptId}`,
        method: 'get'
    })
}

// 新增部门
export function addDept(data: any): any {
    return request({
        url: '/orgs/add',
        method: 'post',
        data: data
    })
}

// 修改部门
export function updateDept(data: any): any {
    return request({
        url: '/orgs/update',
        method: 'put',
        data: data
    })
}


//获取组织部门树
export function getTree(query?: any): any {
    return request({
        url: '/orgs/tree',
        method: 'get',
        params: query
    })
}

//禁用组织机构
export function disableBatch(data: any): any {
    return request({
        url: '/organizations/disable',
        method: 'put',
        data: data
    })
}

//启用组织机构
export function activeBatch(data: any): any {
    return request({
        url: '/organizations/active',
        method: 'put',
        data: data
    })
}

export function deleteBatch(id: any): any {
    return request({
        url: '/orgs/delete?ids=' + id,
        method: 'delete'
    })
}

//查询自定义字段
export function findList(query: any): any {
    return request({
        url: '/config/expandattrs/findList',
        method: 'get',
        params: query
    })
}

export function exportOrgs(query: any): any {
    return request({
        url: `/orgs/export/${query}`,
        method: 'get',
        responseType: 'blob'
    })
}

export function importOrgs(data: any): any {
    return request({
        url: '/orgs/import',
        method: 'post',
        data: data
    })
}
