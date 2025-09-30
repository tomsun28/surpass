enum Status {
    DRAFT = "draft",            // 暂存
    UNDER_REVIEW = "reviewing", // 审核中
    COMPLETED = "completed",    // 已完成
    REJECTED = "rejected",      // 被拒绝
    CANCELLED = "cancelled"     // 已取消
}

// 中文描述的映射对象
const statusDescriptions: { [key in Status]: string } = {
    [Status.DRAFT]: "暂存",
    [Status.UNDER_REVIEW]: "审核中",
    [Status.COMPLETED]: "已完成",
    [Status.REJECTED]: "被拒绝",
    [Status.CANCELLED]: "已取消"
};

// 对应状态的颜色映射
const statusColors: { [key in Status]: string } = {
    [Status.DRAFT]: "#909399",        // 暂存
    [Status.UNDER_REVIEW]: "#E6A23C", // 审核中
    [Status.COMPLETED]: "#67C23A",    // 已完成
    [Status.REJECTED]: "#F56C6C",     // 被拒绝
    [Status.CANCELLED]: "#909399"     // 已取消
};

// 根据输入值获取中文描述，并添加颜色
export function getVoucherStatusDesc(value: string): string {
    // 使用枚举值反向查找映射
    const status = Object.values(Status).find(status => status === value);
    if (status) {
        // 获取对应状态的颜色
        const color = statusColors[status];
        // 返回带有颜色的 span 标签
        return `<span style="color: ${color}">${statusDescriptions[status]}</span>`;
    }
    return value;
}
