import Decimal from 'decimal.js'

/**
 * Merges two objects, giving the last one precedence
 * @param {Object} target
 * @param {(Object|Array)} source
 * @returns {Object}
 */
export function objectMerge(target: any, source: any): any {
    if (typeof target !== 'object') {
        target = {}
    }
    if (Array.isArray(source)) {
        return source.slice()
    }
    Object.keys(source).forEach((property: any) => {
        const sourceProperty: any = source[property]
        if (typeof sourceProperty === 'object') {
            target[property] = objectMerge(target[property], sourceProperty)
        } else {
            target[property] = sourceProperty
        }
    })
    return target
}

/**
 * @param {Function} func
 * @param {number} wait
 * @param {boolean} immediate
 * @return {*}
 */
export function debounce(func: any, wait: any, immediate: any): any {
    let timeout: any, args: any, context: any, timestamp: any, result: any
    const later: any = function () {
        // 据上一次触发时间间隔
        const last: any = +new Date() - timestamp

        // 上次被包装函数被调用时间间隔 last 小于设定时间间隔 wait
        if (last < wait && last > 0) {
            timeout = setTimeout(later, wait - last)
        } else {
            timeout = null
            // 如果设定为immediate===true，因为开始边界已经调用过了此处无需调用
            if (!immediate) {
                result = func.apply(context, args)
                if (!timeout) context = args = null
            }
        }
    }

    return function (...args: any) {
        context = this
        timestamp = +new Date()
        const callNow: any = immediate && !timeout
        // 如果延时不存在，重新设定延时
        if (!timeout) timeout = setTimeout(later, wait)
        if (callNow) {
            result = func.apply(context, args)
            context = args = null
        }

        return result
    }
}


/**
 * This is just a simple version of deep copy
 * Has a lot of edge cases bug
 * If you want to use a perfect deep copy, use lodash's _.cloneDeep
 * @param {Object} source
 * @returns {Object}
 */
export function deepClone(source: any): any {
    if (!source && typeof source !== 'object') {
        throw new Error('error arguments deepClone')
    }
    const targetObj: any = source.constructor === Array ? [] : {}
    Object.keys(source).forEach((keys: any) => {
        if (source[keys] && typeof source[keys] === 'object') {
            targetObj[keys] = deepClone(source[keys])
        } else {
            targetObj[keys] = source[keys]
        }
    })
    return targetObj
}

/**
 * 生成uuid
 * @returns {string}
 */
export function createUniqueString(): any {
    const timestamp: any = +new Date() + ''
    const randomNum: any = parseInt(String((1 + Math.random()) * 65536)) + ''
    return (+(randomNum + timestamp)).toString(32)
}

/**
 * Check if an element has a class
 * @param ele e
 * @param {string} cls
 * @returns {boolean}
 */
export function hasClass(ele: any, cls: any): any {
    return !!ele.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'))
}

/**
 * Add class to element
 * @param ele e
 * @param {string} cls
 */
export function addClass(ele: any, cls: any): any {
    if (!hasClass(ele, cls)) ele.className += ' ' + cls
}

/**
 * Remove class from element
 * @param ele e
 * @param {string} cls
 */
export function removeClass(ele: any, cls: any): any {
    if (hasClass(ele, cls)) {
        const reg: any = new RegExp('(\\s|^)' + cls + '(\\s|$)')
        ele.className = ele.className.replace(reg, ' ')
    }
}

/**
 *
 * @param str 字符串
 */
export function titleCase(str: any): any {
    return str.replace(/( |^)[a-z]/g, (L: any) => L.toUpperCase())
}

/**
 * 下划线转驼峰
 * @param str 字符串
 */
export function camelCase(str: any): any {
    return str.replace(/_[a-z]/g, (str1: any) => str1.substr(-1).toUpperCase())
}

/**
 * 判断是否为数字
 * @param str 字符串
 */
export function isNumberStr(str: any): any {
    return /^[+-]?(0|([1-9]\d*))(\.\d+)?$/g.test(str)
}

// 自定义校验函数
export function validateForm(formData: any, rules: any) {
    let isValid: boolean = true;
    const errorFields: any = {};

    for (const field in rules) {
        if (formData.hasOwnProperty(field)) {
            const value = formData[field];
            const fieldRules = Array.isArray(rules[field]) ? rules[field] : [rules[field]];
            for (const rule of fieldRules) {
                if (rule.required && (!value || String(value).trim() === '')) {
                    errorFields[field] = rule.message || field + '此项为必填项';
                    isValid = false;
                    break;
                }
                // 最小长度检查
                if ('min' in rule && value.length < rule.min) {
                    errorFields[field] = rule.message || field + `长度至少为 ${rule.min}`;
                    isValid = false;
                    continue;
                }
                // 最大长度检查
                if ('max' in rule && value.length > rule.max) {
                    errorFields[field] = rule.message || field + `长度最多为 ${rule.max}`;
                    isValid = false;
                    continue;
                }
                // 正则表达式模式检查
                if ('pattern' in rule) {
                    const regex = new RegExp(rule.pattern);
                    if (!regex.test(value)) {
                        errorFields[field] = rule.message || field + '格式不正确';
                        isValid = false;
                        continue;
                    }
                }
                if (rule.validator) {
                    const result = rule.validator(value);
                    if (!result) {
                        errorFields[field] = rule.message || field + '验证失败';
                        isValid = false;
                        break;
                    }
                }
                // 可以添加更多的内置验证逻辑，如 min, max, pattern 等等...
            }
        }
    }

    return new Promise((resolve, reject) => {
        if (isValid) {
            resolve(true);
        } else {
            reject(errorFields);
        }
    });
}

/**
 * 格式化金额到千分位
 * @param value
 * @param defaultValue
 */
export const formatAmount = (value: number | null | string | Decimal, defaultValue?: any) => {
    if (!value || value === 0) return defaultValue === undefined ? '0.00' : defaultValue
    if (value instanceof Decimal) {
        value = value.toNumber()
        if (value == 0) {
            return defaultValue || ""
        }
    }
    return value.toLocaleString('en-US', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    })
}

/**
 * 格式化金额到千分位
 * @param value
 * @param defaultValue
 */
export const formatAmountEmpty = (value: number | null | string | Decimal, defaultValue?: any) => {
    if (!value || value === 0) return defaultValue === undefined ? '' : defaultValue
    if (value instanceof Decimal) {
        value = value.toNumber()
        if (value == 0) {
            return defaultValue || ""
        }
    }
    return value.toLocaleString('en-US', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    })
}

export function downloadData(data: any, filename: any) {
    const blob = new Blob([data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8'});
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename
    a.click();
    URL.revokeObjectURL(a.href);
    a.remove();
}

/**
 * Set集合转字符串
 * @param {Set} set 集合
 */
export function set2String(set: Set<any>): string {
    return [...set].join(',');
}
