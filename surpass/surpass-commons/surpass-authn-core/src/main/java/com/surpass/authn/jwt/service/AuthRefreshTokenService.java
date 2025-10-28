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






package com.surpass.authn.jwt.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import com.surpass.configuration.AuthJwkConfig;
import com.surpass.crypto.jwt.Hmac512Service;
import com.nimbusds.jose.JOSEException;

/**
 * 刷新令牌服务
 *
 * @author Crystal.Sea
 *
 */
public class AuthRefreshTokenService extends AuthJwtService{
	private static final  Logger logger = LoggerFactory.getLogger(AuthRefreshTokenService.class);

	/**
	 * 前端JWT生成配置信息
	 */
	AuthJwkConfig authJwkConfig;

	public AuthRefreshTokenService(AuthJwkConfig authJwkConfig) throws JOSEException {
		this.authJwkConfig = authJwkConfig;
		/**
		 * HMAC512签名服务
		 */
		this.hmac512Service = new Hmac512Service(authJwkConfig.getRefreshSecret());
	}

	/**
	 * JWT Refresh Token with Authentication/根据认证信息生成JWT刷新令牌
	 * @param authentication
	 * @return String
	 */
	public String genRefreshToken(Authentication authentication) {
		logger.trace("generate Refresh JWT Token");
		return genJwt(
				 authentication,
				 authJwkConfig.getIssuer(),
				 authJwkConfig.getRefreshExpires());
	}
}
