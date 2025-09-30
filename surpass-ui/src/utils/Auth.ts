import Cookies from 'js-cookie'

const TokenKey: any = 'Admin-Token'
const RefreshTokenKey: any = 'refresh_token'
const TokenInfoKey: any = '_token'
const UserInfoKey: any = 'user'
const SetsKey: any = 'books'

export function getToken(): any {
    return Cookies.get(TokenKey)
}

export function setToken(token: any): any {
    return Cookies.set(TokenKey, token)
}

export function removeToken(): any {
    return Cookies.remove(TokenKey)
}

export function getRefreshToken(): any {
    return window.localStorage.getItem(RefreshTokenKey)
}

export function setRefreshToken(token: any): any {
    return window.localStorage.setItem(RefreshTokenKey, token)
}

export function removeRefreshToken(): any {
    return window.localStorage.removeItem(RefreshTokenKey)
}


export function getTokenInfo(): any {
    return JSON.parse(window.localStorage.getItem(TokenInfoKey) || "");
}

export function setTokenInfo(info: any): any {
    return window.localStorage.setItem(TokenInfoKey, JSON.stringify(info || {}))
}

export function removeTokenInfo(): any {
    return window.localStorage.removeItem(TokenInfoKey)
}

export function getUserInfo(): any {
    return JSON.parse(window.localStorage.getItem(UserInfoKey) || "")
}

export function setUserInfo(info: any): any {
    return window.localStorage.setItem(UserInfoKey, JSON.stringify(info || {}))
}

export function setSetList(list: any): any {
    return window.localStorage.setItem(SetsKey, JSON.stringify(list || []))
}

export function removeUserInfo(): any {
    return window.localStorage.removeItem(UserInfoKey)
}
