import {ValueName} from "@/utils/enums/ValueName";

// 创建性别的 ValueName 数组
export const Genders: ValueName[] = [
    new ValueName(0, '其他'),
    new ValueName(1, '男'),
    new ValueName(2, '女')
];

// 根据 value 查询对应的 name
export function getGenderName(value: number): string {
    const gender = Genders.find(g => g.value === value);
    return gender ? gender.name : '其他';
}

// 获取所有性别数组
export function getAllGenders(): ValueName[] {
    return Genders;
}