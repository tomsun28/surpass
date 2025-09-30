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

import com.jinbooks.password.onetimepwd.MailOtpAuthnService;
import com.jinbooks.password.onetimepwd.token.RedisOtpTokenStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import com.jinbooks.configuration.ApplicationConfig;
import com.jinbooks.persistence.redis.connection.RedisConnectionFactory;
import com.jinbooks.persistence.service.ConfigEmailSendersService;
import com.jinbooks.persistence.service.ConfigSmsProviderService;

@AutoConfiguration
public class OneTimePasswordAutoConfiguration {
    private static final  Logger logger = LoggerFactory.getLogger(OneTimePasswordAutoConfiguration.class);


    @Bean(name = "mailOtpAuthnService")
    MailOtpAuthnService mailOtpAuthnService(
    		ApplicationConfig applicationConfig,
            ConfigSmsProviderService configSmsProviderService,
            ConfigEmailSendersService configEmailSendersService,
            RedisConnectionFactory redisConnFactory) {
        MailOtpAuthnService otpAuthnService =
        		new MailOtpAuthnService(
                        configSmsProviderService,
                        configEmailSendersService);

        if (applicationConfig.isCachedRedis()) {
            RedisOtpTokenStore redisOptTokenStore = new RedisOtpTokenStore(redisConnFactory);
            otpAuthnService.setRedisOptTokenStore(redisOptTokenStore);
            logger.debug("MailOtpAuthnService Redis inited." );
        }

        return otpAuthnService;
    }

}
