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






package com.surpass.web.security.contorller;

import org.apache.commons.lang3.StringUtils;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.surpass.authn.annotation.CurrentUser;
import com.surpass.crypto.password.PasswordReciprocal;
import com.surpass.entity.Message;
import com.surpass.entity.config.ConfigSmsProvider;
import com.surpass.entity.idm.UserInfo;
import com.surpass.persistence.service.ConfigSmsProviderService;

@RestController
@RequestMapping(value={"/security/smsprovider"})
public class ConfigSmsProviderController {
	static final  Logger logger = LoggerFactory.getLogger(ConfigSmsProviderController.class);

	@Autowired
	ConfigSmsProviderService configSmsProviderService;

	@GetMapping(value={"/get"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<ConfigSmsProvider> get(@CurrentUser UserInfo currentUser){
		LambdaQuery<ConfigSmsProvider> wrapper = new LambdaQuery<>();
		wrapper.notNull(ConfigSmsProvider::getId);
		ConfigSmsProvider smsProvider = configSmsProviderService.get(wrapper);
		if(smsProvider != null && StringUtils.isNoneBlank(smsProvider.getId())) {
			smsProvider.setAppSecret(PasswordReciprocal.getInstance().decoder(smsProvider.getAppSecret()));
		}
		return new Message<>(smsProvider);
	}

	@PutMapping(value={"/update"})
	public Message<ConfigSmsProvider> update( @RequestBody ConfigSmsProvider smsProvider,@CurrentUser UserInfo currentUser,BindingResult result) {
		logger.debug("update smsProvider : {}" ,smsProvider);
		smsProvider.setAppSecret(PasswordReciprocal.getInstance().encode(smsProvider.getAppSecret()));
		boolean updateResult = false;
		if(StringUtils.isBlank(smsProvider.getId())) {
			updateResult = configSmsProviderService.insert(smsProvider);
		}else {
			updateResult = configSmsProviderService.update(smsProvider);
		}
		if(updateResult) {
			return new Message<>(Message.SUCCESS);
		} else {
			return new Message<>(Message.FAIL);
		}
	}
}
