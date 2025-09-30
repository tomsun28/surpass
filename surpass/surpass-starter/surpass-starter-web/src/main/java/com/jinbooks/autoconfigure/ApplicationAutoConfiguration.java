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
 





package com.jinbooks.autoconfigure;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import com.jinbooks.configuration.ApplicationConfig;
import com.jinbooks.configuration.IdStrategyConfig;
import com.jinbooks.crypto.password.MessageDigestPasswordEncoder;
import com.jinbooks.crypto.password.NoOpPasswordEncoder;
import com.jinbooks.crypto.password.PasswordReciprocal;
import com.jinbooks.crypto.password.SM3PasswordEncoder;
import com.jinbooks.persistence.cache.InMemoryCacheService;
import com.jinbooks.persistence.cache.MemCacheService;
import com.jinbooks.persistence.cache.RedisCacheService;
import com.jinbooks.persistence.redis.connection.RedisConnectionFactory;
import com.jinbooks.persistence.service.ConfigPasswordPolicyService;
import com.jinbooks.persistence.service.PasswordPolicyValidatorService;
import com.jinbooks.persistence.service.impl.PasswordPolicyValidatorServiceImpl;
import com.jinbooks.util.IdGenerator;
import com.jinbooks.util.SnowFlakeId;
import com.jinbooks.web.WebContext;

@AutoConfiguration
public class ApplicationAutoConfiguration {
	static final Logger logger = LoggerFactory.getLogger(ApplicationAutoConfiguration.class);

    @Bean
    PasswordReciprocal passwordReciprocal() {
        return new PasswordReciprocal();
    }

    @Bean
    DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * Authentication Password Encoder .
     * @return
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        Map<String ,PasswordEncoder > encoders = new HashMap<>();
        String idForEncode = "bcrypt";
        encoders.put("plain", new NoOpPasswordEncoder());
    	encoders.put("bcrypt", new BCryptPasswordEncoder());
    	encoders.put("sm3", new SM3PasswordEncoder());
    	encoders.put("md5", new MessageDigestPasswordEncoder("MD5"));
    	
    	if(logger.isDebugEnabled()) {
    		logger.debug("PasswordEncoder {} ", encoders);
    	}
    	
        //idForEncode is default for encoder
        PasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(idForEncode, encoders);
        logger.info("{} is default encoder" , idForEncode);
        return passwordEncoder;
    }


    @Bean
    PasswordPolicyValidatorService passwordPolicyValidatorService(ConfigPasswordPolicyService configPasswordPolicyService,MessageSource messageSource) {
        return new PasswordPolicyValidatorServiceImpl(configPasswordPolicyService,messageSource);
    }

    /**
     * Id Generator .
     * @return
     */
    @Bean
    IdGenerator idGenerator(IdStrategyConfig idStrategyConfig) {
    	IdGenerator idGenerator = new IdGenerator(idStrategyConfig.getStrategy());
    	SnowFlakeId snowFlakeId = new SnowFlakeId(idStrategyConfig.getDatacenterId(),idStrategyConfig.getMachineId());
    	idGenerator.setSnowFlakeId(snowFlakeId);
    	WebContext.setIdGenerator(idGenerator);
        return idGenerator;
    }

    @Bean
    MemCacheService memCacheService(
    		RedisConnectionFactory redisConnFactory,
    		ApplicationConfig applicationConfig) {
    	MemCacheService memCacheService;
    	if (applicationConfig.isCachedRedis()) {
    		memCacheService = new RedisCacheService(redisConnFactory);
    	}else {
    		memCacheService = new InMemoryCacheService();
    	}
    	return memCacheService;
    }

}
