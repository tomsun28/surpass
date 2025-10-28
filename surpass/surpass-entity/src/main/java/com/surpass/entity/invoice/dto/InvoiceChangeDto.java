package com.surpass.entity.invoice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.surpass.entity.invoice.InvoiceItem;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/8/26 17:46
 */

@Data
public class InvoiceChangeDto {
    /**
     * 主键
     */
    @NotEmpty(message = "ID不能为空", groups = {EditGroup.class})
    private String id;

    /**
     * 账套ID
     */
    private String bookId;

    /**
     * 发票号码
     */
    @NotEmpty(message = "发票号码不能为空", groups = {AddGroup.class, EditGroup.class})
    private String invoiceNo;

    /**
     * 发票代码
     */
    @NotEmpty(message = "发票代码不能为空", groups = {AddGroup.class, EditGroup.class})
    private String invoiceCode;

    /**
     * 发票类型（增值税专票、普票、电子发票等）
     */
    @NotEmpty(message = "发票类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private String invoiceType;

    /**
     * 开票日期
     */
    @NotNull(message = "开票日期不能为空", groups = {AddGroup.class, EditGroup.class})
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
    @NotEmpty(message = "发票状态不能为空", groups = {AddGroup.class, EditGroup.class})
    private String status;

    /**
     * 方向（INPUT / OUTPUT）
     */
    @NotEmpty(message = "发票方向不能为空", groups = {AddGroup.class, EditGroup.class})
    private String direction;

    @Valid
    private List<InvoiceItem> items;
}
