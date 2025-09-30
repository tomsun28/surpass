/**
 * 判断科目是否可以设置现金流量项目
 * @param {Object} subject 科目对象
 * @returns {Object} 包含是否可设置以及可设置范围的结果
 */
export function checkCashFlowSettingPermission(subject: any): {
    canSetMainTable: boolean; // 是否可设置主表项目
    canSetSupplementary: boolean; // 是否可设置补充资料项目
    reason: string; // 限制原因(如果有)
} {
    // 获取科目信息
    // const {code, displayName} = subject;
    const {isCash} = subject;

    // 结果对象
    let result = {
        canSetMainTable: false,       // 是否可设置主表项目
        canSetSupplementary: false,   // 是否可设置补充资料项目
        reason: ""                    // 限制原因(如果有)
    };

    //排除现金类科目
    if (isCash === 1) {
        result.reason = "现金类科目无需手动指定现金流量项目，系统将自动处理";
        return result;
    }
   /* //排除现金类科目
    if (displayName.includes("库存现金") ||
        displayName.includes("银行存款") ||
        displayName.includes("其他货币资金") ||
        displayName.includes("货币资金") ||
        code.startsWith("1001") ||
        code.startsWith("1002") ||
        code.startsWith("1003") ||
        displayName.includes("现金")) {
        result.reason = "现金类科目无需手动指定现金流量项目，系统将自动处理";
        return result;
    }*/

    // 6. 默认情况 - 保守处理，允许设置两种项目
    // 注意：实际应用中可能需要更严格的默认规则
    result.canSetMainTable = true;
    result.canSetSupplementary = true;
    result.reason = "默认允许设置，请根据实际业务判断";

    return result;
}
