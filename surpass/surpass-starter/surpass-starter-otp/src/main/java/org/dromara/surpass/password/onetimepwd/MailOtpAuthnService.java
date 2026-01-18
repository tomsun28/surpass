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






package org.dromara.surpass.password.onetimepwd;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.query.OrderBy;
import org.dromara.mybatis.jpa.query.OrderType;
import org.dromara.surpass.configuration.EmailConfig;
import org.dromara.surpass.constants.ConstsBoolean;
import org.dromara.surpass.constants.ConstsConfig;
import org.dromara.surpass.constants.ConstsStatus;
import org.dromara.surpass.crypto.password.PasswordReciprocal;
import org.dromara.surpass.entity.config.ConfigEmailSenders;
import org.dromara.surpass.password.onetimepwd.impl.MailOtpAuthn;
import org.dromara.surpass.password.onetimepwd.token.RedisOtpTokenStore;
import org.dromara.surpass.persistence.service.ConfigEmailSendersService;
import org.dromara.surpass.persistence.service.ConfigSmsProviderService;

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
		LambdaQuery<ConfigEmailSenders> queryWrapper = new LambdaQuery<>();
		queryWrapper.eq(ConfigEmailSenders::getStatus, ConstsStatus.ACTIVE)
				.orderBy(ConfigEmailSenders::getModifiedDate, OrderBy.DESC.getOrder());
		ConfigEmailSenders configEmailSender = emailSendersService.get(queryWrapper);

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
