package com.surpass.entity.invoice;

import com.baomidou.mybatisplus.annotation.*;
import com.surpass.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/8/28 10:14
 */

@EqualsAndHashCode(callSuper = true)
@TableName("jbx_invoice_voucher_template")
@Data
public class InvoiceVoucherTemplate extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 6301487677182179460L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 账套ID
     */
    private String bookId;

    /**
     * 删除标记
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 发票方向
     */
    private String direction;

    /**
     * 凭证字
     */
    private String wordHead;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 是否默认模板 0-否；1-是
     */
    private Integer isDefault;
}
