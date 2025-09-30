import {ValueName} from "@/utils/enums/ValueName";

// 创建学历的 ValueName 数组
export const Status: ValueName[] = [
    new ValueName(1, '启用'),
    new ValueName(0, '禁用')
];

// 根据 value 查询对应的 name
export function getStatusName(value: number): string {
    const education = Status.find(e => e.value === value);
    return education ? education.name : '无';
}

// 获取所有学历数组
export function getAllStatus(): ValueName[] {
    return Status;
}
