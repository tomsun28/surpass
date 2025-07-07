package org.dromara.surpass.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author tomsun28
 * @date 20:36 2019-08-01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    /**
     * 登录类型  password github phone email ..
     */
    @NotNull(message = "authType can not null")
    @Range(min = 1, max = 4, message = "1.password 2.github 3.email 4.phone")
    private Integer authType;

    /**
     * 用户标识
     */
    @NotBlank(message = "identifier can not null")
    private String identifier;

    /**
     * 密钥
     */
    @NotBlank(message = "credential can not null")
    private String credential;

    /**
     * 用户请求标识随机串
     */
    private String userKey;

}
