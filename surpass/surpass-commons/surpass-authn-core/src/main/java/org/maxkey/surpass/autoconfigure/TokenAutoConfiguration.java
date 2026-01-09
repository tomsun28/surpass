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

import org.maxkey.surpass.authn.congress.CongressService;
import org.maxkey.surpass.authn.congress.InMemoryCongressService;
import org.maxkey.surpass.authn.congress.RedisCongressService;
import org.maxkey.surpass.authn.jwt.service.AuthRefreshTokenService;
import org.maxkey.surpass.authn.jwt.service.AuthTokenService;
import org.maxkey.surpass.authn.token.TokenManager;
import org.maxkey.surpass.authn.token.impl.TokenManagerImpl;
import org.maxkey.surpass.configuration.ApplicationConfig;
import org.maxkey.surpass.configuration.AuthJwkConfig;
import org.maxkey.surpass.entity.config.ConfigLoginPolicy;
import org.maxkey.surpass.persistence.cache.MemCacheService;
import org.maxkey.surpass.persistence.redis.connection.RedisConnectionFactory;
import org.maxkey.surpass.persistence.service.ConfigLoginPolicyService;
import org.maxkey.surpass.persistence.service.HistoryLoginService;
import org.maxkey.surpass.persistence.service.SessionListService;
import org.maxkey.surpass.security.TokenStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

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
			ApplicationConfig applicationConfig,
			TokenStore tokenStore) throws JOSEException {
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

    	return	new AuthTokenService(authJwkConfig,congressService,memCacheService,refreshTokenService,tokenStore);
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
