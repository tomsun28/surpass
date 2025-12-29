import request from '@/utils/Request'
import {handleTree} from '@/utils/Surpass'
import useUserStore from '@/store/modules/user'
import i18n from '@/languages'

const {t} = i18n.global;

const mapMenu: any = {}
const mapParentIds: any = {}
const mapChildren: any = {}
const additionalMenu: any = {}

function setChildrenMenu(menus: any): any {
    menus.forEach((t: any) => {
        if (t.meta.activeMenu && !t.meta.activeMenu.startsWith('/')) {
            t.meta.activeMenu = '/' + t.meta.activeMenu
        }
        if (t.children && t.children.length > 0) {
            setChildrenMenu(t.children)
        }
    })
}

/**
 * 转换成router可用的格式
 * @param menu
 */
function formatMenu(menu: any): any {
    const icon: any = (menu.resStyle || "list").replace("anticon-", '')
    let requestUrl: any = menu.requestUrl;
    // 斜杠开头去掉
    if (requestUrl && menu.requestUrl.startsWith('/')) {
        requestUrl = menu.requestUrl.substring(1);
    }

    return {
        "id": menu.id,
        "parentId": menu.parentId,
        "name": menu.permission,
        "path": menu.requestUrl || menu.permission,
        "query": menu.params || null,
        "hidden": menu.isVisible !== 'y',
        "redirect": "noRedirect",
        "component": mapParentIds[menu.id] && mapChildren[menu.id].filter((t: any) => t.isVisible === 'y').length > 0
            ? "ParentView"
            : requestUrl,
        "alwaysShow": !!mapParentIds[menu.id] && mapChildren[menu.id].filter((t: any) => t.isVisible === 'y').length > 0,
        "permissions": [menu.permission],
        "meta": {
            "title": menu.i18n && t(menu.i18n).indexOf('.') < 0
                ? t(menu.i18n)
                : menu.resName,
            "icon": icon,
            "noCache": menu.isCache === 'n',
            "link": menu.isFrame === 'y' ? menu.requestUrl : null,
            "activeMenu": mapMenu[menu.parentId]
                ? (mapMenu[menu.parentId].requestUrl || menu.requestUrl)
                : null
        },
        "raw": menu
    }
}

// 获取路由
export const getRouters: any = () => {
    return new Promise((resolve: any, reject: any) => {
        request({
            url: '/open/func/list?_allow_anonymous=true&appId=1',
            method: 'get'
        }).then((res: any) => {
            useUserStore().permissions = res.data.functions.map((t: any) => {
                return t.permission
            })
            const functions: any = res.data.functions.filter((t: any) => t.status === "1" && t.classify === 'MENU')
                .sort((a: any, b: any) => a.sortIndex - b.sortIndex)
            const menus: any = functions.filter((t: any) => t.status === "1" && t.classify === 'MENU').map(menu => {
                if(menu.requestUrl && menu.requestUrl === '/'){
                    menu.requestUrl = ""
                }
                return menu
            })
            menus.forEach((item: any) => {
                mapMenu[item.id] = item
                mapParentIds[item.parentId] = item
                if (!mapChildren[item.parentId]) {
                    mapChildren[item.parentId] = []
                }
                mapChildren[item.parentId].push(item)
            })
            // 预处理空菜单,过滤出不需要显示在菜单中，但需要定义路由的菜单（附件的隐藏菜单）
            for (let key in mapChildren) {
                const children: any = mapChildren[key]
                children.filter((t: any) => t.isVisible !== 'y').forEach((t: any) => {
                    additionalMenu[t.id] = formatMenu(t)
                })
            }

            let tree: any = menus
                // 此处同时需要过滤additionalMenu中的隐藏菜单，后续单独处理
                .filter((t: any) => {
                    return t.status === "1" && t.classify === 'MENU'
                        && !additionalMenu[t.id]
                        && !(t.requestUrl && t.requestUrl.startsWith('/redirect'))
                })
                .map((menu: any) => {
                    return formatMenu(menu)
                })

            tree = handleTree(tree || [])
            for (let key in additionalMenu) {
                tree.push(additionalMenu[key])
            }
            tree = tree.map((t: any) => {
                if (t.path && !t.path.startsWith('/')) {
                    t.path = '/' + t.path
                }
                if (t.meta.activeMenu && !t.meta.activeMenu.startsWith('/')) {
                    t.meta.activeMenu = '/' + t.meta.activeMenu
                }
                if (!t.children || t.children.length < 1) {
                    const r: any = JSON.parse(JSON.stringify(t))
                    r.children = [t]
                    r.component = 'Layout'
                    r.path = ''
                    r.name = null
                    r.permissions = null
                    return r
                } else {
                    t.component = 'Layout'
                    setChildrenMenu(t.children)
                    return t
                }
            })

            // 将首页重定向到第一个页面
            if (tree.length > 0) {
                const index: any = tree[0].children[0]
                tree[0].redirect = index.path
            }
            console.log(tree)
            resolve({
                code: 200,
                data: tree
            })
        }).catch((err: any) => {
            reject(err)
        })
    })
}
