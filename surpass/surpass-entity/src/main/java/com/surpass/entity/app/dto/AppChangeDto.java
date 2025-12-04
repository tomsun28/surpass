package com.surpass.entity.app.dto;

import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/4 16:19
 */

@Data
public class AppChangeDto {

    @NotEmpty(message = "ID不能为空", groups = EditGroup.class)
    private String id;

    @NotEmpty(message = "应用名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String appName;

    private Integer status;
}
