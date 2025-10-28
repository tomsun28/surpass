package com.surpass.entity.expense;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.surpass.util.DateUtils;
import lombok.*;
import com.surpass.entity.BaseEntity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 简介说明:
 *
 * @author wuyan
 * {@code @date} 2025/07/15 21:24:59
 * {@code @version} 1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("jbx_expense_approval")
public class ExpenseApproval extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String reportId;

    private String approverId;

    private Integer approveLevel;

    private String action; // agree, reject, return

    private String comments;

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_YYYY_MM_DD_HH_MM_SS, timezone = "GMT+8")
    private Date approveTime;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;
}
