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






package com.surpass.password.onetimepwd;

import java.util.concurrent.TimeUnit;

import com.surpass.password.onetimepwd.impl.MailOtpAuthn;
import com.surpass.password.onetimepwd.token.RedisOtpTokenStore;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.surpass.configuration.EmailConfig;
import com.surpass.constants.ConstsBoolean;
import com.surpass.constants.ConstsConfig;
import com.surpass.constants.ConstsStatus;
import com.surpass.crypto.password.PasswordReciprocal;
import com.surpass.entity.config.ConfigEmailSenders;
import com.surpass.persistence.service.ConfigEmailSendersService;
import com.surpass.persistence.service.ConfigSmsProviderService;

public class MailOtpAuthnService {

    protected static final Cache<String, AbstractOtpAuthn> mailOtpAuthnStore =
            Caffeine.newBuilder()
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .build();

    ConfigSmsProviderService smsProviderService;

    ConfigEmailSendersService emailSendersService;

    RedisOtpTokenStore redisOptTokenStore;


    public MailOtpAuthnService(ConfigSmsProviderService smsProviderService, ConfigEmailSendersService emailSendersService) {
		this.smsProviderService  = smsProviderService;
		this.emailSendersService = emailSendersService;
	}

	public MailOtpAuthnService(ConfigSmsProviderService smsProviderService,RedisOtpTokenStore redisOptTokenStore) {
		this.smsProviderService = smsProviderService;
		this.redisOptTokenStore = redisOptTokenStore;
	}

	public AbstractOtpAuthn getMailOtpAuthn() {
		AbstractOtpAuthn otpAuthn = mailOtpAuthnStore.getIfPresent(ConstsConfig.CONSOLE_CONFIG_EMAIL_SENDERS);
    	if(otpAuthn == null) {
    		otpAuthn = builderMailOtpAuthn();
    	}
    	mailOtpAuthnStore.put(ConstsConfig.CONSOLE_CONFIG_EMAIL_SENDERS, otpAuthn);
		return otpAuthn;
	}

	MailOtpAuthn builderMailOtpAuthn(){
		LambdaQueryWrapper<ConfigEmailSenders> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(ConfigEmailSenders::getStatus, ConstsStatus.ACTIVE)
				.orderByDesc(ConfigEmailSenders::getModifiedDate);
		ConfigEmailSenders configEmailSender = emailSendersService.getOne(queryWrapper, false);

		String credentials = PasswordReciprocal.getInstance().decoder(configEmailSender.getCredentials());
		EmailConfig emailConfig = new EmailConfig(
								configEmailSender.getAccount(),
								credentials,
								configEmailSender.getSmtpHost(),
								configEmailSender.getPort(),
								ConstsBoolean.isTrue(configEmailSender.getSslSwitch()),
								configEmailSender.getSender());
		MailOtpAuthn mailOtpAuthn = new MailOtpAuthn(emailConfig);
		//5 minutes
		mailOtpAuthn.setInterval(60 * 5);
		if(redisOptTokenStore != null) {
			mailOtpAuthn.setOptTokenStore(redisOptTokenStore);
		}
		return mailOtpAuthn;
	}

	public void setRedisOptTokenStore(RedisOtpTokenStore redisOptTokenStore) {
		this.redisOptTokenStore = redisOptTokenStore;
	}

}
