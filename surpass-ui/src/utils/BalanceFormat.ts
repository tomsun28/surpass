/**
 * 格式化余额显示
 * @param value 数值
 */
export function formatBalance(value: number | string): string {
    if (value === undefined || value === null || value === '' || value === 0) return '';

    const num = parseFloat(String(value));
    if (isNaN(num)) return '';

    // 格式化为带千分位和两位小数的格式
    return num.toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
        useGrouping: true
    });
}

/**
 * 验证输入的是否为有效数字（允许负号和小数点）
 * @param row 表格行数据
 */
export function validateNumber(row: any) {
    // 只允许数字、负号和小数点
    if (typeof row.inputBalance === 'string') {
        // 允许的格式: -123.45 或 123.45 或 123 或 -123
        // 保留有效的数字、最多一个负号（只能在开头）、最多一个小数点
        const validInput = row.inputBalance.replace(/[^0-9.-]/g, '');

        // 处理多个小数点的情况
        const parts = validInput.split('.');
        if (parts.length > 2) {
            row.inputBalance = parts[0] + '.' + parts.slice(1).join('');
        }

        // 处理多个负号的情况，只保留开头的负号
        if (validInput.lastIndexOf('-') > 0) {
            const firstPart = validInput.charAt(0) === '-' ? '-' : '';
            row.inputBalance = firstPart + validInput.replace(/-/g, '');
        } else {
            row.inputBalance = validInput;
        }
    }
}
