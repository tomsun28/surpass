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






package org.dromara.surpass.autoconfigure;

import com.nimbusds.jose.JOSEException;
import org.dromara.surpass.authn.congress.CongressService;
import org.dromara.surpass.authn.congress.InMemoryCongressService;
import org.dromara.surpass.authn.congress.RedisCongressService;
import org.dromara.surpass.authn.jwt.service.AuthRefreshTokenService;
import org.dromara.surpass.authn.jwt.service.AuthTokenService;
import org.dromara.surpass.configuration.ApplicationConfig;
import org.dromara.surpass.configuration.AuthJwkConfig;
import org.dromara.surpass.persistence.cache.MemCacheService;
import org.dromara.surpass.persistence.redis.connection.RedisConnectionFactory;
import org.dromara.surpass.pojo.entity.config.ConfigLoginPolicy;
import org.dromara.surpass.service.ConfigLoginPolicyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

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
            MemCacheService memCacheService,
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
