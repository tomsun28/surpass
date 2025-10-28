package com.surpass.entity.idm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/7/18 16:41
 */

@Data
public class RegisterUserDto {

    @NotBlank(message = "手机号不能为空")
    @Pattern(
            regexp = "^1[3-9]\\d{9}$",
            message = "请输入正确的11位手机号"
    )
    private String username;

    @NotEmpty(message = "密码不能为空")
    private String password;

    @NotEmpty(message = "确认密码不能为空")
    private String confirmPassword;

//    @NotEmpty(message = "验证码不能为空")
    private String code;

    @NotNull(message = "机构类型不能为空")
    private Integer instType;

    @NotEmpty(message = "机构名称不能为空")
    private String instName;

    @NotEmpty(message = "邮箱地址不能为空")
    private String email;

    @NotEmpty(message = "邮箱验证码不能为空")
    private String emailOtp;

    private String tempUsername;

//    private String state;
}
