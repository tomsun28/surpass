import {createI18n} from "vue-i18n";

// 从语言包文件中导入语言包对象
import zhCN from './locales/zh-CN.js'
import elZhCN from 'element-plus/es/locale/lang/zh-cn'
import elEnUS from 'element-plus/es/locale/lang/en'

const langs: any = [
    {
        label: '简体中文',
        value: 'zh-CN'
    }
]

const messages: any = {
    "zh-CN": {
        ...zhCN, ...elZhCN
    }
}

const LangName: any = 'Admin-Lang'

export const getLocale: any = () => {
    // 获取缓存
    const storLanguage: any = localStorage.getItem(LangName)
    // 存在返回当前语言
    if (storLanguage) return storLanguage
    // 不存在 获取系统语言
    const language: any = (navigator.language || navigator?.browserLanguage)?.toLowerCase()
    const locales: any = Object.keys(messages)
    for (const locale of locales) {
        if (language.indexOf(locale) > -1) {
            return locale
        }
    }
    // 默认返回简体中文
    return 'zh-CN'
}

export function getLang() {
    return localStorage.getItem(LangName)
}

export function setLang(land : any): any {
    return localStorage.setItem(LangName, land)
}

export function removeLang(): any {
    return localStorage.removeItem(LangName)
}

export function getLangList(): any {
    return langs
}


const i18n: any = createI18n({
    legacy: false, // 使用Composition API，这里必须设置为false
    locale: getLocale(), // 默认显示语言
    globalInjection: true, // 全局注册$t方法
    messages: messages,
    missing: function (locale, key) {
        // 可以在这里决定如何处理缺失的键
        // console.warn(`Missing translation for key ${key} in locale ${locale}`);
        return key; // 或者返回某个默认字符串
    }
});

export default i18n;
