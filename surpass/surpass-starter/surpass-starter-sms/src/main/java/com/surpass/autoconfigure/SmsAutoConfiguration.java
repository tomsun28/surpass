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

import com.surpass.password.onetimepwd.token.RedisOtpTokenStore;
import com.surpass.password.sms.SmsOtpAuthnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import com.surpass.configuration.ApplicationConfig;
import com.surpass.persistence.redis.connection.RedisConnectionFactory;
import com.surpass.persistence.service.ConfigEmailSendersService;
import com.surpass.persistence.service.ConfigSmsProviderService;

@AutoConfiguration
public class SmsAutoConfiguration {
    private static final  Logger logger = LoggerFactory.getLogger(SmsAutoConfiguration.class);

    @Bean(name = "smsOtpAuthnService")
    SmsOtpAuthnService smsOtpAuthnService(
    		ApplicationConfig applicationConfig,
            ConfigSmsProviderService configSmsProviderService,
            ConfigEmailSendersService configEmailSendersService,
            RedisConnectionFactory redisConnFactory) {
        SmsOtpAuthnService smsOtpAuthnService =
        		new SmsOtpAuthnService(
                        configSmsProviderService,
                        configEmailSendersService);

        if (applicationConfig.isCachedRedis()) {
            RedisOtpTokenStore redisOptTokenStore = new RedisOtpTokenStore(redisConnFactory);
            smsOtpAuthnService.setRedisOptTokenStore(redisOptTokenStore);
            logger.debug("SmsOtpAuthnService Redis inited.");
        }


        return smsOtpAuthnService;
    }
}
