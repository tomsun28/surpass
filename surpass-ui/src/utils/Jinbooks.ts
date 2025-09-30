/**
 * 显示图片
 */
export function privateImage(path: any): any {
    if (path.startsWith("/file")) {
        return import.meta.env.VITE_APP_BASE_API + path;
    }
    return path;
}


/**
 * 通用js方法封装处理
 * Copyright (c) 2019 ruoyi
 */

// 日期格式化
export function parseTime(time: any, pattern?: any): any {
    if (arguments.length === 0 || !time) {
        return null
    }
    const format: any = pattern || '{y}-{m}-{d} {h}:{i}:{s}'
    let date
    if (typeof time === 'object') {
        date = time
    } else {
        if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
            time = parseInt(time)
        } else if (typeof time === 'string') {
            time = time.replace(new RegExp(/-/gm), '/').replace('T', ' ').replace(new RegExp(/\.[\d]{3}/gm), '');
        }
        if ((typeof time === 'number') && (time.toString().length === 10)) {
            time = time * 1000
        }
        date = new Date(time)
    }
    const formatObj: any = {
        y: date.getFullYear(),
        m: date.getMonth() + 1,
        d: date.getDate(),
        h: date.getHours(),
        i: date.getMinutes(),
        s: date.getSeconds(),
        a: date.getDay()
    }
    const time_str: any = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result: any, key: any) => {
        let value: any = formatObj[key]
        // Note: getDay() returns 0 on Sunday
        if (key === 'a') {
            return ['日', '一', '二', '三', '四', '五', '六'][value]
        }
        if (result.length > 0 && value < 10) {
            value = '0' + value
        }
        return value || 0
    })
    return time_str
}

/**
 * 获取当前所属季度
 */
export function getCurrentQuarter() {
    const month = new Date().getMonth() + 1;  // 获取当前月份（注意：月份从0开始，所以需要加1）

    if (month >= 1 && month <= 3) {
        return 'Q1';
    } else if (month >= 4 && month <= 6) {
        return 'Q2';
    } else if (month >= 7 && month <= 9) {
        return 'Q3';
    } else {
        return 'Q4';
    }
}

// 表单重置
export function resetForm(refName: any): any {
    if (this.$refs[refName]) {
        this.$refs[refName].resetFields();
    }
}

// 添加日期范围
export function addDateRange(params: any, dateRange: any, propName: any): any {
    let search: any = params;
    search.params = typeof (search.params) === 'object' && search.params !== null && !Array.isArray(search.params) ? search.params : {};
    dateRange = Array.isArray(dateRange) ? dateRange : [];
    if (typeof (propName) === 'undefined') {
        search.params['beginTime'] = dateRange[0];
        search.params['endTime'] = dateRange[1];
    } else {
        search.params['begin' + propName] = dateRange[0];
        search.params['end' + propName] = dateRange[1];
    }
    return search;
}

// 回显数据字典
export function selectDictLabel(datas: any, value: any): any {
    if (value === undefined) {
        return "";
    }
    var actions: any = [];
    Object.keys(datas).some((key: any) => {
        if (datas[key].value == ('' + value)) {
            actions.push(datas[key].label);
            return true;
        }
    })
    if (actions.length === 0) {
        actions.push(value);
    }
    return actions.join('');
}

// 回显数据字典（字符串数组）
export function selectDictLabels(datas: any, value: any, separator: any): any {
    if (value === undefined || value.length === 0) {
        return "";
    }
    if (Array.isArray(value)) {
        value = value.join(",");
    }
    var actions: any = [];
    var currentSeparator: any = undefined === separator ? "," : separator;
    var temp: any = value.split(currentSeparator);
    Object.keys(value.split(currentSeparator)).some((val: any) => {
        var match: any = false;
        Object.keys(datas).some((key: any) => {
            if (datas[key].value == ('' + temp[val])) {
                actions.push(datas[key].label + currentSeparator);
                match = true;
            }
        })
        if (!match) {
            actions.push(temp[val] + currentSeparator);
        }
    })
    return actions.join('').substring(0, actions.join('').length - 1);
}

/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 */
export function handleTree(data?: any, id?: any, parentId?: any, children?: any): any {
    let config: any = {
        id: id || 'id',
        parentId: parentId || 'parentId',
        childrenList: children || 'children'
    };
    var childrenListMap: any = {};
    var nodeIds: any = {};
    var tree: any = [];

    for (let d of data) {
        let parentId: any = d[config.parentId];
        if (childrenListMap[parentId] == null) {
            childrenListMap[parentId] = [];
        }
        nodeIds[d[config.id]] = d;
        childrenListMap[parentId].push(d);
    }
    for (let d of data) {
        let parentId: any = d[config.parentId];
        if (!nodeIds[parentId]) {
            tree.push(d);
        }
    }

    for (let t of tree) {
        adaptToChildrenList(t);
    }

    function adaptToChildrenList(o: any): any {
        if (childrenListMap[o[config.id]] !== null) {
            o[config.childrenList] = childrenListMap[o[config.id]];
        }
        if (o[config.childrenList]) {
            for (let c of o[config.childrenList]) {
                adaptToChildrenList(c);
            }
        }
    }

    return tree;
}

/**
 * 树转换为列表
 * @param data tree
 * @param children 孩子节点字段
 */
export function handleTreeToList(data?: any, children?: any): any {
    children = children || 'children'
    const list: any = [];
    data = JSON.parse(JSON.stringify(data))
    for (let d of data) {
        adaptToListChildren(d)
    }

    function adaptToListChildren(o: any): any {
        list.push(o);
        if (o[children]) {
            for (let c of o[children]) {
                adaptToListChildren(c);
            }
            o[children] = null
        }
    }

    return list;
}

/**
 * 参数处理
 * @param {*} params  参数
 */
export function tansParams(params: any): any {
    let result: any = ''
    for (const propName of Object.keys(params)) {
        const value: any = params[propName];
        var part: any = encodeURIComponent(propName) + "=";
        if (value !== null && value !== "" && typeof (value) !== "undefined") {
            if (typeof value === 'object') {
                for (const key of Object.keys(value)) {
                    if (value[key] !== null && value[key] !== "" && typeof (value[key]) !== 'undefined') {
                        let params: any = propName + '[' + key + ']';
                        var subPart: any = encodeURIComponent(params) + "=";
                        result += subPart + encodeURIComponent(value[key]) + "&";
                    }
                }
            } else {
                result += part + encodeURIComponent(value) + "&";
            }
        }
    }
    return result
}


// 返回项目路径
export function getNormalPath(p: any): any {
    if (p.length === 0 || !p || p === 'undefined') {
        return p
    }
    let res: any = p.replace('//', '/')
    if (res[res.length - 1] === '/') {
        return res.slice(0, res.length - 1)
    }
    return res;
}

// 验证是否为blob格式
export function blobValidate(data: any): any {
    return data.type !== 'application/json'
}

/**
 * 账期选择器，参数更新
 * @param queryParams 查询参数实体
 * @param currBookStore 账簿信息
 */
export function getAccountPeriod(queryParams: any, currBookStore: any): string {
    let date = new Date(currBookStore.termCurrent + "-01")
    if (queryParams.value.accountPeriod === "currentPeriod") {
        queryParams.value.reportDate = parseTime(date, "{y}-{m}")
        queryParams.value.periodType = "month"
        return parseTime(date, "{y}年{m}期")
    } else if (queryParams.value.accountPeriod === "lastPeriod") {
        date.setMonth(date.getMonth() - 1)
        queryParams.value.reportDate = parseTime(date, "{y}-{m}")
        queryParams.value.periodType = "month"
        return parseTime(date, "{y}年{m}期")
    } else if (queryParams.value.accountPeriod === "currentYear") {
        queryParams.value.reportDate = parseTime(date, "{y}")
        queryParams.value.periodType = "year"
        return parseTime(date, "{y}年")
    } else if (queryParams.value.accountPeriod === "lastYear") {
        date.setMonth(date.getMonth() - 12)
        queryParams.value.reportDate = parseTime(date, "{y}")
        queryParams.value.periodType = "year"
        return parseTime(date, "{y}年")
    }
    return ""
}
