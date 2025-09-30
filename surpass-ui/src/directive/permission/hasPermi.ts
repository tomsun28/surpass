import type {DirectiveBinding, ObjectDirective} from 'vue'
import useUserStore from '@/store/modules/user'

const allPermission = '*:*:*'

const hasPermiDirective: ObjectDirective = {
    mounted(el: HTMLElement, binding: DirectiveBinding<string[]>) {
        const {value} = binding
        const userStore = useUserStore()
        const permissions: string[] = userStore.permissions

        if (Array.isArray(value) && value.length > 0) {
            const requiredPermissions = value

            const hasPermission = permissions.some((perm: string) =>
                perm === allPermission || requiredPermissions.includes(perm)
            )

            if (!hasPermission) {
                if (el.parentNode) {
                    el.parentNode.removeChild(el)
                }
            }
        } else {
            console.error('v-hasPermi：请设置权限值，例如 v-hasPermi="[\'sys:user:edit\']"')
            throw new Error('v-hasPermi 缺少权限数组')
        }
    }
}

export default hasPermiDirective
