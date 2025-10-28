package com.surpass.entity.invoice;

import com.baomidou.mybatisplus.annotation.*;
import com.surpass.entity.BaseEntity;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/8/21 17:57
 */

@EqualsAndHashCode(callSuper = true)
@TableName("jbx_invoice_item")
@Data
public class InvoiceItem extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1598271170253146457L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 关联发票ID
     */
    private String invoiceId;

    /**
     * 商品名称
     */
    @NotEmpty(message = "商品名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String itemName;

    /**
     * 规格型号
     */
    private String specModel;

    /**
     * 单位
     */
    private String unit;

    /**
     * 数量
     */
    private BigDecimal quantity;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 金额（不含税）
     */
    private BigDecimal amount;

    /**
     * 税率（%）
     */
    private String taxRate;

    /**
     * 税额
     */
    private String taxAmount;

    /**
     * 价税合计
     */
    private String totalWithTax;

    /**
     * 删除标记
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;
}
