import request from '@/utils/Request'

// 登录方法
export function login(username: any, password: any, captcha: any, state: any, authType: any): any {
    const data: any = {
        username,
        password,
        captcha,
        state,
        authType
    }
    return request({
        url: '/login/signin?_allow_anonymous=true',
        headers: {
            isToken: false,
            repeatSubmit: false
        },
        method: 'post',
        data: data
    })
}

export function getThirdById(id: any): any {
    return request({
        url: '/login/authorize/' + id,
        headers: {
            isToken: false,
            repeatSubmit: false
        },
        method: 'get'
    })
}

// 注册方法
export function register(data: any): any {
    return request({
        url: '/users/register',
        headers: {
            isToken: false
        },
        method: 'post',
        data: data
    })
}

//忘记密码-发送短信验证码
export function sendSmsCode(query: any): any {
    return request({
        url: '/users/sendCode',
        headers: {
            isToken: false
        },
        method: 'get',
        params: query
    })
}

//注册-发送邮箱验证码
export function sendEmailCode(query: any): any {
    return request({
        url: '/users/register/produceEmailOtp',
        headers: {
            isToken: false
        },
        method: 'get',
        params: query
    })
}

export function validateCaptcha(query: any): any {
    return request({
        url: `/password/validate`,
        method: 'get',
        params: query,
        headers: {
            isToken: false,
        }
    })
}

//获取所有的策略
export function getPwdPolicy(): any {
    return request({
        url: `/policy/password`,
        method: 'get',
        headers: {
            isToken: false,
        },
    })
}

//设置新密码
export function setNewPassword(data: any): any {
    return request({
        url: `/users/setPwd`,
        method: 'put',
        data: data,
        headers: {
            isToken: false,
        }
    })
}

// 获取用户详细信息
export function currentUser(): any {
    return request({
        url: '/users/currentUser',
        method: 'get'
    })
}

// 退出方法
export function logout(): any {
    return request({
        url: '/logout',
        method: 'post'
    })
}

// 获取验证码
export function getCodeImg(state?: any, captcha?: any): any {
    return request({
        url: '/captcha?_allow_anonymous=true&state=' + state + '&captcha=' + captcha,
        headers: {
            isToken: false
        },
        method: 'get',
        timeout: 20000
    })
}

//
export function loginPreGet(): any {
    return request({
        url: '/login/get?_allow_anonymous=true',
        method: 'get'
    })
}

export function openFuncList(): any {
    return request({
        url: '/open/func/list?_allow_anonymous=true&appId=1',
        method: 'get'
    })
}

// 方法
export function callbackLogin(data: any): any {
    return request({
        url: '/login/callback',
        headers: {
            isToken: false,
            repeatSubmit: false
        },
        method: 'post',
        data: data
    })
}

// 退出方法
export function logoutApi(): any {
    return request({
        url: '/logout',
        method: 'get'
    })
}
