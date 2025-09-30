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
 





package com.jinbooks.password.sms;

import java.util.concurrent.TimeUnit;

import com.jinbooks.password.onetimepwd.AbstractOtpAuthn;
import com.jinbooks.password.onetimepwd.impl.MailOtpAuthn;
import com.jinbooks.password.onetimepwd.token.RedisOtpTokenStore;
import com.jinbooks.password.sms.impl.SmsOtpAuthnYunxin;
import com.jinbooks.password.sms.impl.SmsOtpAuthnAliyun;
import com.jinbooks.password.sms.impl.SmsOtpAuthnTencentCloud;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.jinbooks.configuration.EmailConfig;
import com.jinbooks.constants.ConstsBoolean;
import com.jinbooks.constants.ConstsConfig;
import com.jinbooks.constants.ConstsStatus;
import com.jinbooks.crypto.password.PasswordReciprocal;
import com.jinbooks.entity.config.ConfigEmailSenders;
import com.jinbooks.entity.config.ConfigSmsProvider;
import com.jinbooks.persistence.service.ConfigEmailSendersService;
import com.jinbooks.persistence.service.ConfigSmsProviderService;

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

    		LambdaQueryWrapper<ConfigSmsProvider> queryWrapper = new LambdaQueryWrapper<>();
			queryWrapper.eq(ConfigSmsProvider::getStatus, ConstsStatus.ACTIVE)
					.orderByDesc(ConfigSmsProvider::getModifiedDate);
    		ConfigSmsProvider configSmsProvider = smsProviderService.getOne(queryWrapper, false);

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

    		LambdaQueryWrapper<ConfigEmailSenders> queryWrapper = new LambdaQueryWrapper<>();
    		queryWrapper.eq(ConfigEmailSenders::getStatus, ConstsStatus.ACTIVE)
					.orderByDesc(ConfigEmailSenders::getModifiedDate);
			ConfigEmailSenders configEmailSender = emailSendersService.getOne(queryWrapper, false);
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
