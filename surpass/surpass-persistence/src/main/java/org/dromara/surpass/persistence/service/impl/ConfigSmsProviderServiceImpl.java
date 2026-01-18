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






package org.dromara.surpass.persistence.service.impl;

import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.dromara.surpass.entity.config.ConfigSmsProvider;
import org.dromara.surpass.persistence.mapper.ConfigSmsProviderMapper;
import org.dromara.surpass.persistence.service.ConfigSmsProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ConfigSmsProviderServiceImpl extends JpaServiceImpl<ConfigSmsProviderMapper, ConfigSmsProvider> implements ConfigSmsProviderService {

	@Autowired
	ConfigSmsProviderMapper configSmsProviderMapper;

	public ConfigSmsProviderMapper getMapper() {
		return configSmsProviderMapper;
	}


}
