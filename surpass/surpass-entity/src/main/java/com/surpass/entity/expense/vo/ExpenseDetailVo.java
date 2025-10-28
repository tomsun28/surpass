package com.surpass.entity.expense.vo;

import com.surpass.entity.expense.ExpenseDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 简介说明:
 *
 * @author wuyan
 * {@code @date} 2025/07/15 21:29:34
 * {@code @version} 1.0
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class ExpenseDetailVo extends ExpenseDetail {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 费用类型名称（可由枚举或字典转换）
     */
    private String expenseTypeName;

    /**
     * 成本中心名称
     */
    private String costCenterName;

    /**
     * 会计科目名称
     */
    private String accountName;
}
