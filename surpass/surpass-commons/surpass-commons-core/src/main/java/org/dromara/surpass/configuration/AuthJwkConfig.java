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






package org.dromara.surpass.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 令牌JWT参数基本配置，
 *
 * @author Crystal.Sea
 *
 */

@Data
@NoArgsConstructor
@Component
@Configuration
public class AuthJwkConfig {
	/**
	 * 令牌有效时间，默认8小时(43200)
	 */
	@Value("${jinbooks.auth.jwt.expires:43200}")
	int 	expires;
	/**
	 * 令牌密钥
	 */
	@Value("${jinbooks.auth.jwt.secret}")
	String 	secret;
	/**
	 * 刷新令牌有效时间,默认值24小时(86400)，refreshExpires > expires
	 */
	@Value("${jinbooks.auth.jwt.refresh.expires:86400}")
	int 	refreshExpires;
	/**
	 * 刷新令牌密钥
	 */
	@Value("${jinbooks.auth.jwt.refresh.secret}")
	String 	refreshSecret;
	/**
	 * 令牌签发者
	 */
	@Value("${jinbooks.auth.jwt.issuer:https://www.jinbooks.com/}")
	String 	issuer;

}
