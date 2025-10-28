package com.surpass.entity.expense.dto;

import com.surpass.validate.EditGroup;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 简介说明:
 *
 * @author wuyan
 * {@code @date} 2025/07/15 21:41:02
 * {@code @version} 1.0
 */

@Data
public class ExpensePaymentChangeDto {

    @NotNull(message = "编辑对象不能为空", groups = {EditGroup.class})
    private String id;

    @NotBlank(message = "报销单ID不能为空")
    private String reportId;

    @NotBlank(message = "付款方式不能为空")
    private String paymentMethod;

    @NotBlank(message = "银行账户ID不能为空")
    private String bankAccountId;

    @NotNull(message = "付款金额不能为空")
    @DecimalMin(value = "0.01", message = "付款金额必须大于0")
    private BigDecimal paidAmount;

    private Date paidTime;

    @NotBlank(message = "操作人不能为空")
    private String operatorId;
}
