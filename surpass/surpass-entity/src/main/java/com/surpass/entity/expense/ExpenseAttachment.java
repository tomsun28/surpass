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
 * {@code @date} 2025/07/15 21:25:18
 * {@code @version} 1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("jbx_expense_attachment")
public class ExpenseAttachment extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String reportId;

    private String fileName;

    private String filePath;

    private String uploadedBy;

    @JsonFormat(pattern = DateUtils.FORMAT_DATE_YYYY_MM_DD_HH_MM_SS, timezone = "GMT+8")
    private Date uploadedAt;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;
}
