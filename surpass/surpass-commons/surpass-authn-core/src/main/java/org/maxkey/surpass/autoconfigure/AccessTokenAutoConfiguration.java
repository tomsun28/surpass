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






package org.maxkey.surpass.autoconfigure;

import org.maxkey.surpass.authn.token.TokenManager;
import org.maxkey.surpass.authn.token.impl.TokenManagerImpl;
import org.maxkey.surpass.configuration.ApplicationConfig;
import org.maxkey.surpass.persistence.redis.connection.RedisConnectionFactory;
import org.maxkey.surpass.persistence.service.ConfigLoginPolicyService;
import org.maxkey.surpass.persistence.service.HistoryLoginService;
import org.maxkey.surpass.persistence.service.SessionListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 令牌自动配置
 *
 * @author Crystal.Sea
 *
 */
@AutoConfiguration
public class AccessTokenAutoConfiguration {
    private static final  Logger logger = LoggerFactory.getLogger(AccessTokenAutoConfiguration.class);


    @Bean(name = "tokenManager")
    TokenManager tokenManager(
    		ApplicationConfig applicationConfig,
            ConfigLoginPolicyService configLoginPolicyService,
            RedisConnectionFactory redisConnFactory,
            SessionListService sessionListService,
            HistoryLoginService historyLoginService
    ) {
    	logger.debug("AccessToken persistence {} " ,applicationConfig.getCached());
    	return  new TokenManagerImpl(applicationConfig.getCached(),redisConnFactory);
    }

}
