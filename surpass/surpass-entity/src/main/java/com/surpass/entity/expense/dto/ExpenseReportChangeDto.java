package com.surpass.entity.expense.dto;

import com.surpass.validate.EditGroup;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 简介说明:
 *
 * @author wuyan
 * {@code @date} 2025/07/15 21:36:00
 * {@code @version} 1.0
 */

@Data
public class ExpenseReportChangeDto {

    @NotNull(message = "编辑对象不能为空", groups = {EditGroup.class})
    private String id;

    private String bookId;

    @NotBlank(message = "报销人ID不能为空")
    private String employeeId;

    @NotBlank(message = "部门ID不能为空")
    private String departmentId;

    @NotBlank(message = "报销标题不能为空")
    private String title;

    @NotNull(message = "报销金额不能为空")
    @DecimalMin(value = "0.01", message = "报销金额必须大于0")
    private BigDecimal totalAmount;

    @NotBlank(message = "状态不能为空")
    private String status;

    private Date submitTime;

    private Date approveTime;

    private Date accountingTime;

    private Date paymentTime;

    private String remarks;

    /**
     * 明细
     */
    @NotEmpty(message = "报销明细不能为空")
    private List<ExpenseDetailChangeDto> items;
}
