package com.surpass.entity.expense.dto;

import com.surpass.entity.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 简介说明:
 *
 * @author wuyan
 * {@code @date} 2025/07/15 21:34:02
 * {@code @version} 1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ExpenseReportPageDto extends PageQuery {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 报销人ID
     */
    private String employeeId;

    /**
     * 部门ID
     */
    private String departmentId;

    /**
     * 状态
     */
    private String status;

    /**
     * 提交时间范围
     */
    private String startDate;
    private String endDate;
}
