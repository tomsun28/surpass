package org.dromara.surpass.entity.app.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/29 15:01
 */

@Data
public class RoleAppResourcesAuthzDto {

    @NotEmpty(message = "角色Id不能为空")
    private String roleId;

    @NotEmpty(message = "应用Id不能为空")
    private String appId;

    @Valid
    @NotEmpty(message = "资源集合不能为空")
    private List<String> resourceIds;
}
