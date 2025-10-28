package com.surpass.entity.expense.vo;

import com.surpass.entity.expense.ExpenseReport;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

/**
 * 简介说明:
 *
 * @author wuyan
 * {@code @date} 2025/07/15 21:29:05
 * {@code @version} 1.0
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class ExpenseReportVo extends ExpenseReport {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 报销人姓名
     */
    private String employeeName;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 状态名称（可由枚举转换）
     */
    private String statusName;

    private List<ExpenseDetailVo> items;
}
