package com.surpass.entity.expense.vo;

import com.surpass.entity.expense.ExpenseAttachment;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 简介说明:
 *
 * @author wuyan
 * {@code @date} 2025/07/15 21:30:31
 * {@code @version} 1.0
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class ExpenseAttachmentVo extends ExpenseAttachment {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 上传人姓名 */
    private String uploadedByName;
}
