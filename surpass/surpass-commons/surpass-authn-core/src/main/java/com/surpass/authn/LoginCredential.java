/*
 * Copyright [2025] [Surpass of copyright http://www.surpass.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */






package com.surpass.authn;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.surpass.authn.session.Session;
import com.surpass.web.WebConstants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录提交的信息属性
 * @author Crystal.Sea
 *
 */

@Data
@NoArgsConstructor
public class LoginCredential  implements Authentication {
    private static final long serialVersionUID = 3125709257481600320L;

    @Schema(name = "style", description = "登录方式")
    String style =Session.STYLE.WEB;
    @Schema(name = "congress", description = "信任令牌")
    String congress;
    @Schema(name = "username", description = "登录名")
    String username;
    @Schema(name = "password", description = "登录密码")
    String password;
    @Schema(name = "captcha", description = "验证码")
    String captcha;
    @Schema(name = "mobile", description = "手机号码")
    String mobile;
    @Schema(name = "otpCaptcha", description = "一次性口令")
    String otpCaptcha;
    @Schema(name = "remeberMe", description = "记住我")
    String remeberMe;
    @Schema(name = "authType", description = "认证类型")
    String authType;
    @Schema(name = "secretKey", description = "密钥编码")
    String secretKey;
    @Schema(name = "deviceId", description = "设备识别码")
    String deviceId;
    @Schema(name = "state", description = "服务器签名")
    String state;
    @Schema(name = "jwtToken", description = "JWT令牌")
    String jwtToken;
    String onlineTicket;
    String provider;
    String code;
    String message = WebConstants.LOGIN_RESULT.SUCCESS;
    String instId;


    List<GrantedAuthority> grantedAuthority;
    boolean authenticated;
    boolean roleAdministrators;

    /**
     * BasicAuthentication.
     */
    public LoginCredential(String username,String password,String authType) {
        this.username = username;
        this.password = password;
        this.authType = authType;
    }


	@Override
    public String getName() {
        return "Login Credential";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthority;
    }

    @Override
    public Object getCredentials() {
        return this.getPassword();
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.getUsername();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        this.authenticated = authenticated;

    }

}
