package com.surpass.entity.expense.vo;

import com.surpass.entity.expense.ExpenseApproval;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 简介说明:
 *
 * @author wuyan
 * {@code @date} 2025/07/15 21:30:08
 * {@code @version} 1.0
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class ExpenseApprovalVo extends ExpenseApproval {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 审批人姓名 */
    private String approverName;

    /** 审批动作名（如：通过、驳回） */
    private String actionName;
}
