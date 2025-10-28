package com.surpass.entity.expense;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.surpass.util.DateUtils;
import lombok.*;
import com.surpass.entity.BaseEntity;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 简介说明:
 *
 * @author wuyan
 * {@code @date} 2025/07/15 21:25:42
 * {@code @version} 1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("jbx_expense_payment")
public class ExpensePayment extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String reportId;

    private String paymentMethod;

    private String bankAccountId;

    private BigDecimal paidAmount;

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_YYYY_MM_DD_HH_MM_SS, timezone = "GMT+8")
    private Date paidTime;

    private String operatorId;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;
}
