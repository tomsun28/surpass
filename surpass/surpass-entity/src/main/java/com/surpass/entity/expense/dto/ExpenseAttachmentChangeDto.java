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
 * {@code @date} 2025/07/15 21:40:38
 * {@code @version} 1.0
 */

@Data
public class ExpenseAttachmentChangeDto {

    @NotNull(message = "编辑对象不能为空", groups = {EditGroup.class})
    private String id;

    @NotBlank(message = "报销单ID不能为空")
    private String reportId;

    @NotBlank(message = "文件名不能为空")
    private String fileName;

    @NotBlank(message = "文件路径不能为空")
    private String filePath;

    @NotBlank(message = "上传人不能为空")
    private String uploadedBy;

    private Date uploadedAt;
}
