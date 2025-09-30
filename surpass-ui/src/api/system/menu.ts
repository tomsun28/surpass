import request from '@/utils/Request'

// 查询菜单列表
export function listMenu(query : any): any {
  return request({
    url: '/resources/fetch',
    method: 'get',
    params: query
  })
}

// 查询菜单详细
export function getMenu(menuId : any): any {
  return request({
    url: '/resources/get/' + menuId,
    method: 'get'
  })
}

// 查询菜单下拉树结构
export function treeselect(): any {
  return request({
    url: '/system/menu/treeselect',
    method: 'get'
  })
}

// 根据角色ID查询菜单下拉树结构
export function roleMenuTreeselect(roleId : any): any {
  return request({
    url: '/system/menu/roleMenuTreeselect/' + roleId,
    method: 'get'
  })
}

// 新增菜单
export function addMenu(data : any): any {
  return request({
    url: '/resources/add',
    method: 'post',
    data: data
  })
}

// 修改菜单
export function updateMenu(data : any): any {
  return request({
    url: '/resources/edit',
    method: 'put',
    data: data
  })
}

// 删除菜单
export function delMenu(menuId : any): any {
  return request({
    url: '/resources/delete/' + menuId,
    method: 'delete'
  })
}

// 系统资源树
export function systemTree(): any {
  return request({
    url: '/resources/systemTree',
    method: 'get'
  })
}
