import Cookies from 'js-cookie'
import logo from '@/assets/logo/logo.png'
import {defineStore} from "pinia"

const useAppStore: any = defineStore(
    'app',
    {
        state: () => ({
            sidebar: {
                opened: Cookies.get('sidebarStatus') ? !!+(Cookies.get('sidebarStatus') || "") : true,
                withoutAnimation: false,
                hide: false
            },
            device: 'desktop',
            size: Cookies.get('size') || 'default',
            appTitle: import.meta.env.VITE_APP_TITLE,
            logo: logo,
        }),
        actions: {
            toggleSideBar(withoutAnimation: any) {
                if (this.sidebar.hide) {
                    return false;
                }
                this.sidebar.opened = !this.sidebar.opened
                this.sidebar.withoutAnimation = withoutAnimation
                if (this.sidebar.opened) {
                    Cookies.set('sidebarStatus', "1")
                } else {
                    Cookies.set('sidebarStatus', "0")
                }
            },
            closeSideBar({withoutAnimation}: any) {
                Cookies.set('sidebarStatus', "0")
                this.sidebar.opened = false
                this.sidebar.withoutAnimation = withoutAnimation
            },
            toggleDevice(device: any) {
                this.device = device
            },
            setSize(size: any) {
                this.size = size;
                Cookies.set('size', size)
            },
            setAppInfo(info: any) {
                this.appTitle = info.consoleTitle || import.meta.env.VITE_APP_TITLE
                this.logo = info.logo || logo
                setTimeout(() => {
                    document.title = this.appTitle
                }, 1000)
            },
            toggleSideBarHide(status: any) {
                this.sidebar.hide = status
            }
        }
    })

export default useAppStore
