import router from './router'
import {ElMessage} from 'element-plus'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import {getToken} from '@/utils/Auth'
import {isHttp} from '@/utils/Validate'
import useUserStore from '@/store/modules/user'
import useSettingsStore from '@/store/modules/settings'
import usePermissionStore from '@/store/modules/permission'
import {loginPreGet} from "@/api/login.js";
import logoUrl from "@/assets/logo/logo.png";
import appStore from "@/store/modules/app.js";

NProgress.configure({showSpinner: false});

const whiteList: any = ['/login', '/register', '/callback', '/forgot'];

router.beforeEach(async (to: any, from: any, next: any) => {
    NProgress.start();
    const token = getToken();

    if (token) {
        to.meta.title && useSettingsStore().setTitle(to.meta.title);

        if (to.path === '/login') {
            next({path: '/'});
            NProgress.done();
        } else if (whiteList.includes(to.path)) {
            next();
        } else {
            const userStore = useUserStore();

            if (userStore.roles.length === 0) {
                try {
                    const res = await loginPreGet();
                    if (res.code === 0) {
                        const staticAppInfo: any = res.data.inst;
                        staticAppInfo.logo = logoUrl;
                        appStore().setAppInfo(staticAppInfo);
                    }
                    await userStore.currentUser();
                    const accessRoutes = await usePermissionStore().generateRoutes();

                    accessRoutes.forEach((route: any) => {
                        if (!isHttp(route.path)) {
                            router.addRoute(route);
                        }
                    });

                    next({...to, replace: true});
                } catch (err: any) {
                    console.error(err);
                    await userStore.logOut();
                    ElMessage.error(err);
                    next({path: '/'});
                }
            } else {
                next();
            }
        }
    } else {
        if (whiteList.includes(to.path)) {
            next();
        } else {
            next(`/login?redirect=${to.fullPath}`);
            NProgress.done();
        }
    }
});

router.afterEach(() => {
    NProgress.done()
})
