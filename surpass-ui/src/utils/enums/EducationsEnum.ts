import {ValueName} from "@/utils/enums/ValueName";

// 创建学历的 ValueName 数组
export const Educations: ValueName[] = [
    new ValueName('小学', '小学'),
    new ValueName('初中', '初中'),
    new ValueName('高中', '高中'),
    new ValueName('中专', '中专'),
    new ValueName('大专', '大专'),
    new ValueName('本科', '本科'),
    new ValueName('硕士研究生', '硕士研究生'),
    new ValueName('博士研究生', '博士研究生'),
    new ValueName('无', '无')
];

// 根据 value 查询对应的 name
export function getEducationName(value: number): string {
    const education = Educations.find(e => e.value === value);
    return education ? education.name : '无';
}

// 获取所有学历数组
export function getAllEducations(): ValueName[] {
    return Educations;
}
