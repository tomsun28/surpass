import {ValueName} from "@/utils/enums/ValueName";

// 创建性别的 ValueName 数组
export const IdTypes: ValueName[] = [
    new ValueName(0, '未知'),
    new ValueName(1, '身份证'),
    new ValueName(2, '护照'),
    new ValueName(3, '学生证'),
    new ValueName(4, '军人证')
];

// 根据 value 查询对应的 name
export function getTypeName(value: number): string {
    const type = IdTypes.find(g => g.value === value);
    return type ? type.name : '未知';
}

// 获取所有性别数组
export function getAllTypes(): ValueName[] {
    return IdTypes;
}
