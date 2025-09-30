import useUserStore from '@/store/modules/user'

/**
 * 字符权限校验
 * @param {Array} value 校验值
 * @returns {Boolean}
 */
export function checkPermi(value : any): any {
  if (value && value instanceof Array && value.length > 0) {
    const permissions: any = useUserStore().permissions
    const permissionDatas: any = value
    const all_permission: any = "*:*:*";

    const hasPermission: any = permissions.some((permission: any) =>  {
      return all_permission === permission || permissionDatas.includes(permission)
    })

    if (!hasPermission) {
      return false
    }
    return true
  } else {
    console.error(`need roles! Like checkPermi="['system:user:add','system:user:edit']"`)
    return false
  }
}

/**
 * 角色权限校验
 * @param {Array} value 校验值
 * @returns {Boolean}
 */
export function checkRole(value : any): any {
  if (value && value instanceof Array && value.length > 0) {
    const roles: any = useUserStore().roles
    const permissionRoles: any = value
    const super_admin: any = "admin";

    const hasRole: any = roles.some((role: any) =>  {
      return super_admin === role || permissionRoles.includes(role)
    })

    if (!hasRole) {
      return false
    }
    return true
  } else {
    console.error(`need roles! Like checkRole="['admin','editor']"`)
    return false
  }
}