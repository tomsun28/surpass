package com.surpass.entity.expense.dto;

import com.surpass.validate.EditGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * 简介说明:
 *
 * @author wuyan
 * {@code @date} 2025/07/15 21:40:17
 * {@code @version} 1.0
 */

@Data
public class ExpenseApprovalChangeDto {

    @NotNull(message = "编辑对象不能为空", groups = {EditGroup.class})
    private String id;

    @NotBlank(message = "报销单ID不能为空")
    private String reportId;

    @NotBlank(message = "审批人ID不能为空")
    private String approverId;

    @NotNull(message = "审批级别不能为空")
    private Integer approveLevel;

    @NotBlank(message = "审批动作不能为空") // agree, reject, return
    private String action;

    private String comments;

    private Date approveTime;
}
