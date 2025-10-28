package com.surpass.entity.expense.dto;

import com.surpass.validate.EditGroup;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 简介说明:
 *
 * @author wuyan
 * {@code @date} 2025/07/15 21:39:54
 * {@code @version} 1.0
 */

@Data
public class ExpenseDetailChangeDto {

    @NotNull(message = "编辑对象不能为空", groups = {EditGroup.class})
    private String id;

    private String bookId;

    @NotBlank(message = "所属报销单ID不能为空")
    private String reportId;

    @NotBlank(message = "费用类型不能为空")
    private String expenseType;

    private String description;

    @NotNull(message = "金额不能为空")
    @DecimalMin(value = "0.01", message = "金额必须大于0")
    private BigDecimal amount;

    @NotBlank(message = "成本中心不能为空")
    private String costCenterId;

    @NotBlank(message = "会计科目不能为空")
    private String accountCode;

    private String invoiceNo;
}
