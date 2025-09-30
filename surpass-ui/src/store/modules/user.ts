import {login, logout, currentUser} from '@/api/login'
import {defineStore} from "pinia"
import {
    getToken,
    setToken,
    removeToken,
    setRefreshToken,
    setUserInfo,
    setTokenInfo,
    removeUserInfo,
    removeRefreshToken,
    removeTokenInfo, getTokenInfo
} from '@/utils/Auth'
import defAva from '@/assets/images/profile.png'

const useUserStore: any = defineStore(
    'user',
    {
        state: () => ({
            token: getToken(),
            id: '',
            name: '',
            username: '',
            avatar: '',
            roles: [],
            permissions: [],
            headerType: 'Bearer',
            bookId: '',
        }),
        actions: {
            // 登录
            login(loginCredential: any) {
                const username: any = loginCredential.username.trim()
                const password: any = loginCredential.password
                const captcha: any = loginCredential.captcha
                const state: any = loginCredential.state
                const authType: any = loginCredential.authType
                return new Promise((resolve: any, reject: any) => {
                    login(username, password, captcha, state, authType).then((res: any) => {
                        setToken(res.data.token)
                        setRefreshToken(res.data.refresh_token)
                        setTokenInfo(res.data)
                        this.token = res.data.token
                        this.name = res.data.displayName
                        this.username = res.data.username
                        this.id = res.data.id
                        this.headerType = res.data.type
                        resolve(res)
                    }).catch((error: any) => {
                        reject(error)
                    })
                })
            },
            setCredential(loginCredential: any) {
                setToken(loginCredential.token)
                setRefreshToken(loginCredential.refresh_token)
                setTokenInfo(loginCredential.data)
                // this.roles = res.data.authorities
                this.token = loginCredential.token
            },
            // 获取用户信息
            currentUser() {
                return new Promise((resolve: any, reject: any) => {
                    currentUser().then((res: any) => {
                        const user: any = res.data
                        const avatar: any = (user.avatar && user.avatar.length > 0)
                            ? import.meta.env.VITE_APP_BASE_API + user.avatar
                            : defAva
                        const tokenInfo: any = getTokenInfo() || {}
                        this.roles = tokenInfo.authorities || ['ROLE_DEFAULT']
                        this.id = user.id
                        this.name = user.displayName
                        this.username = user.username
                        this.avatar = avatar
                        this.bookId = user.bookId
                        setUserInfo(user)
                        resolve(res)
                    }).catch((error: any) => {
                        reject(error)
                    })

                    // const user: any = {}
                    // const avatar: any = (user.avatar === "" || user.avatar == null)
                    //     ? defAva : import.meta.env.VITE_APP_BASE_API + user.avatar;
                    // const tokenInfo: any = getTokenInfo()
                    // this.roles = tokenInfo.authorities || ['ROLE_DEFAULT']
                    // this.id = tokenInfo.id
                    // this.name = tokenInfo.displayName
                    // this.avatar = avatar
                    // setUserInfo(tokenInfo)
                    // resolve(tokenInfo)
                })
            },
            // 退出系统
            logOut() {
                return new Promise((resolve: any, reject: any) => {
                    this.token = ''
                    this.roles = []
                    this.permissions = []
                    removeToken()
                    removeUserInfo()
                    removeRefreshToken()
                    removeTokenInfo()
                    resolve()
                })
            }
        }
    })

export default useUserStore
