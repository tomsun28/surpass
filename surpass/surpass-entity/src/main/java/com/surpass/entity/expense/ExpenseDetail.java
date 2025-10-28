package com.surpass.entity.expense;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import com.surpass.entity.BaseEntity;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 简介说明:
 *
 * @author wuyan
 * {@code @date} 2025/07/15 21:24:34
 * {@code @version} 1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("jbx_expense_detail")
public class ExpenseDetail extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String reportId;

    private String expenseType;

    private String description;

    private BigDecimal amount;

    private String costCenterId;

    private String accountCode;

    private String invoiceNo;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;
}
