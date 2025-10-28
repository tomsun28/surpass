package com.surpass.entity.expense.dto;

import com.surpass.entity.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 简介说明:
 *
 * @author wuyan
 * {@code @date} 2025/07/15 21:36:59
 * {@code @version} 1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ExpenseApprovalPageDto extends PageQuery {
    private static final long serialVersionUID = 1L;

    private String reportId;

    private String approverId;

    private String action;
}
