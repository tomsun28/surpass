import booksSetStore from "@/store/modules/bookStore";
import {h, VNode} from "vue";
import {formatAmount} from "@/utils/index";
import {TableColumnCtx} from "element-plus";
import Decimal from 'decimal.js'

export interface SummaryMethodProps<T = any> {
    columns: TableColumnCtx<T>[]
    data: T[]
}

export const cascaderSubjectProps = {
    expandTrigger: 'hover',
    label: 'name',
    value: 'code',
    children: 'children',
    checkStrictly: false,
    emitPath: false,
    defaultExpandAll: true,
    showAllLevels: false,
    clearable: false,
    filterable: true,
    filterMethod: (item: any, keyword: any) => {
        if (!keyword) return true
        return item.data.name.toLowerCase().includes(keyword.toLowerCase())
            || item.data.code.toLowerCase().includes(keyword.toLowerCase())
            || (item.data.pinyinCode)?.toLowerCase().includes(keyword.toLowerCase())
            || (item.data.pinyinDisplayCode)?.toLowerCase().includes(keyword.toLowerCase())
    }
}


/**
 * 获取科目缩进
 * @param subjectCode
 */
export function getSubjectIndent(subjectCode: string) {
    const booksSet = booksSetStore()
    let num = 0
    let start = 0
    for (let i = 0; i < booksSet.subjectCodeLen.length; i++) {
        const len = booksSet.subjectCodeLen[i]
        if (subjectCode.length > start + len) {
            start = start + len
            num += 1
        } else {
            break
        }
    }

    return num.toFixed(0)
}


/**
 * 获取科目余额的所有节点id
 * @param data
 */
export function getSubjectAllNodeIds(data: any) {
    let ids: any = [];

    function recurse(nodes: any) {
        nodes.forEach((node: any) => {
            ids.push(node.sourceId);
            if (node.children) {
                recurse(node.children);
            }
        });
    }

    recurse(data);
    return ids;
}

/**
 * 合计行
 * @param param 列表参数
 * @param recordsAll 自定义数据列
 * @param summaryTextIdx 显示文本的列索引
 * @param summaryValueIdx 显示数值的列索引
 */
export function handleSummaryMethod(param: SummaryMethodProps, recordsAll?: any, summaryTextIdx: number = 0, summaryValueIdx: Array<number> = []) {
    const {columns, data} = param
    const sums: (string | VNode)[] = []
    columns.forEach((column, index) => {
        if (index === summaryTextIdx) {
            sums[index] = h('div', {style: {}}, ['合计'])
            return
        }
        if (summaryValueIdx.length > 0 && !summaryValueIdx.includes(index)) {
            return;
        }

        const values = (recordsAll || data).filter((item: any) => {
            return true
        }).map((item: any) => Number(item[column.property]))
        if (!values.every((value: any) => Number.isNaN(value))) {
            sums[index] = formatAmount(values.reduce((total: Decimal, curr: any) => {
                if (!total) {
                    return new Decimal(curr || 0)
                }
                return total.plus(new Decimal(curr || 0));
            }, 0), '￥ 0')
        } else {
            sums[index] = ''
        }
    })

    return sums
}

