/** 通用的 label/value 项 */
export interface LabeledValue {
    label: string;
    value: string | number;
}

/** 分组选项（如 el-option-group） */
export interface GroupedOptions {
    label: string;
    options: LabeledValue[];
}

export interface TreeNode {
    id: string | number; // 根据你的数据实际情况调整类型
    children?: TreeNode[]; // 子节点可能不存在
}
