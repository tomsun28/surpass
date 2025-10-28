package com.surpass.entity.expense.dto;

import com.surpass.entity.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 简介说明:
 *
 * @author wuyan
 * {@code @date} 2025/07/15 21:36:35
 * {@code @version} 1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ExpenseDetailPageDto extends PageQuery {
    private static final long serialVersionUID = 1L;

    /**
     * 所属报销单
     */
    private String reportId;

    /**
     * 费用类型
     */
    private String expenseType;
}
