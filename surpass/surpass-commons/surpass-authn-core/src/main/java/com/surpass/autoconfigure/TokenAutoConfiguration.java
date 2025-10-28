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






package com.surpass.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import com.surpass.authn.congress.CongressService;
import com.surpass.authn.congress.InMemoryCongressService;
import com.surpass.authn.congress.RedisCongressService;
import com.surpass.authn.jwt.service.AuthRefreshTokenService;
import com.surpass.authn.jwt.service.AuthTokenService;
import com.surpass.configuration.ApplicationConfig;
import com.surpass.configuration.AuthJwkConfig;
import com.surpass.entity.config.ConfigLoginPolicy;
import com.surpass.persistence.cache.MemCacheService;
import com.surpass.persistence.redis.connection.RedisConnectionFactory;
import com.surpass.persistence.service.ConfigLoginPolicyService;
import com.nimbusds.jose.JOSEException;

/**
 * Token令牌自动配置
 *
 * @author Crystal.Sea
 *
 */
@AutoConfiguration
public class TokenAutoConfiguration{
    private static final  Logger logger = LoggerFactory.getLogger(TokenAutoConfiguration.class);

    /**
     * @Description: 认证令牌
     * @Param: [authJwkConfig, redisConnFactory, memCacheService, configLoginPolicyService, refreshTokenService, applicationConfig]
     * @return: org.maxkey.authn.jwt.AuthTokenService
     * @Author: xZen
     * @Date: 2024/11/25 15:58
     */
    @Bean
    AuthTokenService authTokenService(
            AuthJwkConfig authJwkConfig,
            RedisConnectionFactory redisConnFactory,
            MemCacheService  memCacheService,
            ConfigLoginPolicyService configLoginPolicyService,
            AuthRefreshTokenService refreshTokenService,
            ApplicationConfig applicationConfig) throws JOSEException {
    	ConfigLoginPolicy configLoginPolicy = configLoginPolicyService.getConfigLoginPolicy();
    	if(configLoginPolicy != null) {
    		authJwkConfig.setExpires(configLoginPolicy.getTokenValidity() * 3600 );
    		authJwkConfig.setRefreshExpires(configLoginPolicy.getSessionValidity() * 3600);
    	}

    	CongressService congressService;
    	if (applicationConfig.isCachedRedis()) {
    		congressService = new RedisCongressService(redisConnFactory);
    		logger.debug("RedisCongressService");
    	}else {
    		congressService = new InMemoryCongressService();
    		logger.debug("InMemoryCongressService");
    	}

    	return	new AuthTokenService(authJwkConfig,congressService,memCacheService,refreshTokenService);
    }

    /**
     * 刷新令牌
     * @param authJwkConfig
     * @return
     * @throws JOSEException
     */
    @Bean
    AuthRefreshTokenService refreshTokenService(
            AuthJwkConfig authJwkConfig,
            ConfigLoginPolicyService configLoginPolicyService) throws JOSEException {
    	ConfigLoginPolicy configLoginPolicy = configLoginPolicyService.getConfigLoginPolicy();
    	if(configLoginPolicy != null) {
    		authJwkConfig.setExpires(configLoginPolicy.getTokenValidity() * 3600 );
    		authJwkConfig.setRefreshExpires(configLoginPolicy.getSessionValidity() * 3600);
    	}
    	logger.debug("authJwkConfig {}",authJwkConfig);
    	return new AuthRefreshTokenService(authJwkConfig);
    }

}
