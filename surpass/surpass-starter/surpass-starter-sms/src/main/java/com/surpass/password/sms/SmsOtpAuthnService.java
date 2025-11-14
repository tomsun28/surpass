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






package com.surpass.password.sms;

import java.util.concurrent.TimeUnit;

import com.surpass.password.onetimepwd.AbstractOtpAuthn;
import com.surpass.password.onetimepwd.impl.MailOtpAuthn;
import com.surpass.password.onetimepwd.token.RedisOtpTokenStore;
import com.surpass.password.sms.impl.SmsOtpAuthnYunxin;
import com.surpass.password.sms.impl.SmsOtpAuthnAliyun;
import com.surpass.password.sms.impl.SmsOtpAuthnTencentCloud;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.surpass.configuration.EmailConfig;
import com.surpass.constants.ConstsBoolean;
import com.surpass.constants.ConstsConfig;
import com.surpass.constants.ConstsStatus;
import com.surpass.crypto.password.PasswordReciprocal;
import com.surpass.entity.config.ConfigEmailSenders;
import com.surpass.entity.config.ConfigSmsProvider;
import com.surpass.persistence.service.ConfigEmailSendersService;
import com.surpass.persistence.service.ConfigSmsProviderService;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.query.OrderBy;
import org.dromara.mybatis.jpa.query.OrderType;

public class SmsOtpAuthnService {

    protected static final Cache<String, AbstractOtpAuthn> smsOtpAuthnStore =
            Caffeine.newBuilder()
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .build();

    ConfigSmsProviderService smsProviderService;

    ConfigEmailSendersService emailSendersService;

    RedisOtpTokenStore redisOptTokenStore;

    public SmsOtpAuthnService(ConfigSmsProviderService smsProviderService, ConfigEmailSendersService emailSendersService) {
		this.smsProviderService  = smsProviderService;
		this.emailSendersService = emailSendersService;
	}

	public SmsOtpAuthnService(ConfigSmsProviderService smsProviderService,RedisOtpTokenStore redisOptTokenStore) {
		this.smsProviderService = smsProviderService;
		this.redisOptTokenStore = redisOptTokenStore;
	}

	public AbstractOtpAuthn getOneSmsAuthn() {
    	AbstractOtpAuthn smsOtpAuthn = smsOtpAuthnStore.getIfPresent(ConstsConfig.CONSOLE_CONFIG_SMS_SENDERS);
    	if(smsOtpAuthn == null) {

    		LambdaQuery<ConfigSmsProvider> queryWrapper = new LambdaQuery<>();
			queryWrapper.eq(ConfigSmsProvider::getStatus, ConstsStatus.ACTIVE)
					.orderBy(ConfigSmsProvider::getModifiedDate, OrderBy.DESC.getOrder());
    		ConfigSmsProvider configSmsProvider = smsProviderService.get(queryWrapper);
    		if(configSmsProvider != null ) {
    			if(configSmsProvider.getProvider().equalsIgnoreCase("aliyun")) {
    				smsOtpAuthn = new SmsOtpAuthnAliyun(
    						configSmsProvider.getAppKey(),
    						configSmsProvider.getAppSecret(),
    						configSmsProvider.getTemplateId(),
    						configSmsProvider.getSignName());
    			}else if(configSmsProvider.getProvider().equalsIgnoreCase("tencentcloud")) {
    				smsOtpAuthn = new SmsOtpAuthnTencentCloud(
    						configSmsProvider.getAppKey(),
    						configSmsProvider.getAppSecret(),
    						configSmsProvider.getSmsSdkAppId(),
    						configSmsProvider.getTemplateId(),
    						configSmsProvider.getSignName());
    			}else if(configSmsProvider.getProvider().equalsIgnoreCase("neteasesms")) {
    				smsOtpAuthn = new SmsOtpAuthnYunxin(
    						configSmsProvider.getAppKey(),
    						configSmsProvider.getAppSecret(),
    						configSmsProvider.getTemplateId());
    			}else if(configSmsProvider.getProvider().equalsIgnoreCase("email")) {
    				smsOtpAuthn = getMailOtpAuthn();
    			}else if(configSmsProvider.getProvider().equalsIgnoreCase("mq")) {
    				//smsOtpAuthn = new MessageQueueOtpAuthn(provisionService);
    			}
    			//set redis
    			if(redisOptTokenStore != null) {
    				smsOtpAuthn.setOptTokenStore(redisOptTokenStore);
				}
    			smsOtpAuthnStore.put(ConstsConfig.CONSOLE_CONFIG_SMS_SENDERS, smsOtpAuthn);
    		}
    	}
    	return smsOtpAuthn;
    }

	private AbstractOtpAuthn getMailOtpAuthn() {
		AbstractOtpAuthn smsOtpAuthn = smsOtpAuthnStore.getIfPresent(ConstsConfig.CONSOLE_CONFIG_SMS_SENDERS);
    	if(smsOtpAuthn == null) {

    		LambdaQuery<ConfigEmailSenders> queryWrapper = new LambdaQuery<>();
    		queryWrapper.eq(ConfigEmailSenders::getStatus, ConstsStatus.ACTIVE)
					.orderBy(ConfigEmailSenders::getModifiedDate, OrderBy.DESC.getOrder());
			ConfigEmailSenders configEmailSender = emailSendersService.get(queryWrapper);
			String credentials = PasswordReciprocal.getInstance().decoder(configEmailSender.getCredentials());
			EmailConfig emailConfig =
							new EmailConfig(
									configEmailSender.getAccount(),
									credentials,
									configEmailSender.getSmtpHost(),
									configEmailSender.getPort(),
									ConstsBoolean.isTrue(configEmailSender.getSslSwitch()),
									configEmailSender.getSender());
			smsOtpAuthn = new MailOtpAuthn(emailConfig);
			//5 minute
			smsOtpAuthn.setInterval(60 * 5);
    	}
		return smsOtpAuthn;
	}

	public void setRedisOptTokenStore(RedisOtpTokenStore redisOptTokenStore) {
		this.redisOptTokenStore = redisOptTokenStore;
	}


}
