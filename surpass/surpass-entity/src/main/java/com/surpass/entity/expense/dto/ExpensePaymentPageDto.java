package com.surpass.entity.expense.dto;

import com.surpass.entity.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 简介说明:
 *
 * @author wuyan
 * {@code @date} 2025/07/15 21:37:42
 * {@code @version} 1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ExpensePaymentPageDto extends PageQuery {
    @Serial
    private static final long serialVersionUID = 1L;

    private String reportId;

    private String paymentMethod;

    private String operatorId;
}
