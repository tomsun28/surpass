package com.surpass.entity.expense.vo;

import com.surpass.entity.expense.ExpensePayment;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 简介说明:
 *
 * @author wuyan
 * {@code @date} 2025/07/15 21:30:51
 * {@code @version} 1.0
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class ExpensePaymentVo extends ExpensePayment {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 付款账户名称
     */
    private String bankAccountName;
    /**
     * 付款账户编号
     */
    private String bankAccountNumber;

    /**
     * 支付方式名称（如：转账、现金）
     */
    private String paymentMethodName;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 报销单标题
     */
    private String reportTitle;
}
