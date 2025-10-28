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
 * {@code @date} 2025/07/15 21:23:47
 * {@code @version} 1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("jbx_expense_report")
public class ExpenseReport extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String employeeId;

    private String departmentId;

    private String title;

    private BigDecimal totalAmount;

    private String status; // 可定义枚举：draft, submitted, approved, rejected, posted, paid

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_YYYY_MM_DD_HH_MM_SS, timezone = "GMT+8")
    private Date submitTime;

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_YYYY_MM_DD_HH_MM_SS, timezone = "GMT+8")
    private Date approveTime;

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_YYYY_MM_DD_HH_MM_SS, timezone = "GMT+8")
    private Date accountingTime;

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_YYYY_MM_DD_HH_MM_SS, timezone = "GMT+8")
    private Date paymentTime;

    private String remarks;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;
}
