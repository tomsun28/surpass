package org.dromara.surpass.entity.app.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/23 15:13
 */

@Data
public class ClientAuthzDto {

    @NotEmpty(message = "客户端Id不能为空")
    private String clientId;

    @NotEmpty(message = "应用Id不能为空")
    private String appId;

    @Valid
    @NotEmpty(message = "资源集合不能为空")
    private List<String> resourceIds;
}
