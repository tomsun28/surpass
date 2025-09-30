/*
 * Copyright [2025] [JinBooks of copyright http://www.jinbooks.com]
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




package org.dromara.surpass.authn.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.surpass.authn.jwt.AuthJwt;
import org.dromara.surpass.pojo.entity.Institutions;
import org.dromara.surpass.pojo.entity.SocialsProviderLogin;

@Data
@NoArgsConstructor
public class LoginConfigDto{
	/**
	 * 机构信息
	 */
	Institutions inst;
	/**
	 * 验证码
	 */
	String captcha;
	/**
	 * 状态码
	 */
	String state;
	/**
	 * 社交账号登录
	 */
	boolean isSocial;
	/**
	 * 是否首次登录修改密码
	 */
	String isFirstPasswordModify;
	/**
	 * 社交登录提供者
	 */
	SocialsProviderLogin socials;

	/**
	 * jwt令牌
	 */
	AuthJwt authJwt;

	/**
	 * 默认跳转地址
	 */
	String redirectUri;

	String secretKey;

	String secretPublicKey;

}
