// 定义索引签名接口
export interface DictItem {
    label?: string;
    value?: any;
    options?: any;
    id?: any;
    name?: any;
    type?: any;
}

export interface DistData extends Record<string, DictItem[]> {
}
