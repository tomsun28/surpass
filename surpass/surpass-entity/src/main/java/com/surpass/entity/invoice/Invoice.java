package com.surpass.entity.invoice;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.surpass.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/8/21 17:40
 */

@EqualsAndHashCode(callSuper = true)
@TableName("jbx_invoice")
@Data
public class Invoice extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 8068544283219609776L;

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
     * 发票号码
     */
    private String invoiceNo;

    /**
     * 发票代码
     */
    private String invoiceCode;

    /**
     * 发票类型（增值税专票、普票、电子发票等）
     */
    private String invoiceType;

    /**
     * 开票日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date invoiceDate;

    /**
     * 购方名称
     */
    private String buyerName;

    /**
     * 购方税号
     */
    private String buyerTaxNo;

    /**
     * 销方名称
     */
    private String sellerName;

    /**
     * 销方税号
     */
    private String sellerTaxNo;


    /**
     * 金额合计（不含税）
     */
    private BigDecimal totalAmount;


    /**
     * 税额
     */
    private BigDecimal taxAmount;

    /**
     * 价税合计
     */
    private BigDecimal totalWithTax;

    /**
     * 币种
     */
    private String currency;

    /**
     * 发票状态（NEW/VERIFIED/CANCELED/PAID等）
     */
    private String status;

    /**
     * 方向（INPUT / OUTPUT）
     */
    private String direction;

    /**
     * 发票凭证ID
     */
    private String invoiceVoucherId;
}
