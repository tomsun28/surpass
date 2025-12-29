import request from "@/utils/Request";

export function saveRoleAppRelation(data: any) {
    return request({
        url: '/role-app-resources/authz',
        method: 'post',
        data: data
    })
}

export function getRoleAuthz(query: any): any {
    return request({
        url: '/role-app-resources/getRoleAuthz',
        method: 'get',
        params: query
    })
}
