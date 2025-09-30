import type {DirectiveBinding, ObjectDirective} from 'vue'
import useUserStore from '@/store/modules/user'

const superAdminRole = 'admin'

const hasRoleDirective: ObjectDirective = {
    mounted(el: HTMLElement, binding: DirectiveBinding<string[]>) {
        const {value} = binding
        const userStore = useUserStore()
        const roles: string[] = userStore.roles

        if (Array.isArray(value) && value.length > 0) {
            const requiredRoles = value

            const hasRole = roles.some((role: string) =>
                role === superAdminRole || requiredRoles.includes(role)
            )

            if (!hasRole && el.parentNode) {
                el.parentNode.removeChild(el)
            }
        } else {
            console.error('v-hasRole：请设置角色值，例如 v-hasRole="[\'admin\', \'editor\']"')
            throw new Error('v-hasRole 缺少角色数组')
        }
    }
}

export default hasRoleDirective
