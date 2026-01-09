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




package org.maxkey.surpass.authn.web;

import org.maxkey.surpass.authn.LoginSecretKey;
import org.maxkey.surpass.authn.dto.LoginConfigDto;
import org.maxkey.surpass.authn.jwt.service.AuthTokenService;
import org.maxkey.surpass.authn.secretkey.SecretKeyManager;
import org.maxkey.surpass.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/secretKey")
public class SecretKeyEndpoint {
	private static final Logger logger = LoggerFactory.getLogger(SecretKeyEndpoint.class);

	@Autowired
	AuthTokenService authTokenService;

	@Autowired
	SecretKeyManager secretKeyManager;

	@GetMapping("/get")
	public Message<LoginConfigDto> get() {
		logger.debug("/secretKey.");
		LoginSecretKey  loginSecretKey = secretKeyManager.getSecretKey();
		LoginConfigDto conf = new LoginConfigDto();
		conf.setState(authTokenService.genRandomJwt());
		conf.setSecretKey(loginSecretKey.getSecretKey());
		conf.setSecretPublicKey(loginSecretKey.getPublicKey());
		return new Message<>(conf);
	}

}
