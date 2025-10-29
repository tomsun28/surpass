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

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.Message;
import com.surpass.entity.config.ConfigLoginPolicy;
import com.surpass.entity.idm.UserInfo;
import com.surpass.persistence.service.ConfigLoginPolicyService;

@RestController
@RequestMapping(value={"/security/configLoginPolicy"})
public class ConfigLoginPolicyController {
	static final  Logger logger = LoggerFactory.getLogger(ConfigLoginPolicyController.class);

	@Autowired
	ConfigLoginPolicyService configLoginPolicyService;

	@GetMapping(value={"/get"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<ConfigLoginPolicy> get(@CurrentUser UserInfo currentUser){
		ConfigLoginPolicy configLoginPolicy = configLoginPolicyService.getConfigLoginPolicy();
		return new Message<>(configLoginPolicy);
	}

	@PutMapping(value={"/update"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<ConfigLoginPolicy> update(@Valid @RequestBody ConfigLoginPolicy configLoginPolicy,@CurrentUser UserInfo currentUser) {
		logger.debug("updateRole configLoginPolicy : {}" ,configLoginPolicy);
		if(configLoginPolicyService.updateById(configLoginPolicy)) {
			return new Message<>(Message.SUCCESS);
		} else {
			return new Message<>(Message.ERROR);
		}
	}

}
