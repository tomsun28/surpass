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

import org.maxkey.surpass.configuration.ApplicationConfig;
import org.maxkey.surpass.password.onetimepwd.MailOtpAuthnService;
import org.maxkey.surpass.password.onetimepwd.token.RedisOtpTokenStore;
import org.maxkey.surpass.persistence.redis.connection.RedisConnectionFactory;
import org.maxkey.surpass.persistence.service.ConfigEmailSendersService;
import org.maxkey.surpass.persistence.service.ConfigSmsProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

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
