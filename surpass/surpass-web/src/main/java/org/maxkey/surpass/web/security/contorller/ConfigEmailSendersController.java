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






package org.maxkey.surpass.web.security.contorller;

import org.apache.commons.lang3.StringUtils;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.maxkey.surpass.authn.annotation.CurrentUser;
import org.maxkey.surpass.crypto.password.PasswordReciprocal;
import org.maxkey.surpass.entity.Message;
import org.maxkey.surpass.entity.config.ConfigEmailSenders;
import org.maxkey.surpass.entity.idm.UserInfo;
import org.maxkey.surpass.persistence.service.ConfigEmailSendersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/security/emailsenders"})
public class ConfigEmailSendersController {
	static final Logger logger = LoggerFactory.getLogger(ConfigEmailSendersController.class);

	@Autowired
	ConfigEmailSendersService configEmailSendersService;

	@GetMapping(value={"/get"})
	public Message<ConfigEmailSenders> get(@CurrentUser UserInfo currentUser){
		LambdaQuery<ConfigEmailSenders> wrapper = new LambdaQuery<>();
		wrapper.notNull(ConfigEmailSenders::getId);
		ConfigEmailSenders emailSenders = configEmailSendersService.get(wrapper);
		if(emailSenders != null && StringUtils.isNotBlank(emailSenders.getCredentials())) {
			emailSenders.setCredentials(PasswordReciprocal.getInstance().
					decoder(emailSenders.getCredentials()));
		}else {
			emailSenders =new ConfigEmailSenders();
			emailSenders.setProtocol("smtp");
			emailSenders.setEncoding("utf-8");
		}
		return new Message<>(emailSenders);
	}

	@PutMapping(value={"/update"})
	public Message<ConfigEmailSenders> update( @RequestBody ConfigEmailSenders emailSenders,@CurrentUser UserInfo currentUser,BindingResult result) {
		logger.debug("update emailSenders : {}",emailSenders);
		emailSenders.setCredentials(PasswordReciprocal.getInstance().encode(emailSenders.getCredentials()));
		if(StringUtils.isBlank(emailSenders.getId())) {
			if(configEmailSendersService.insert(emailSenders)) {
				return new Message<>(Message.SUCCESS);
			}else {
				return new Message<>(Message.ERROR);
			}
		}else {
			if(configEmailSendersService.update(emailSenders)) {
				return new Message<>(Message.SUCCESS);
			}else {
				return new Message<>(Message.ERROR);
			}
		}

	}
}
