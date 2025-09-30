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
 





package com.jinbooks.password.onetimepwd.impl;

import com.jinbooks.password.onetimepwd.AbstractOtpAuthn;
import com.jinbooks.password.onetimepwd.OneTimePassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.util.DateUtils;
import com.jinbooks.web.WebContext;

public class MessageQueueOtpAuthn extends AbstractOtpAuthn {
    private static final Logger logger = LoggerFactory.getLogger(MessageQueueOtpAuthn.class);


    String subject = "One Time PassWord";

    String messageTemplate = "{0} You Token is {1} , it validity in {2}  minutes.";


    public MessageQueueOtpAuthn() {
        this.otpType = OtpTypes.MQ_OTP;
        this.interval = 300;
    }


	@Override
    public boolean produce(UserInfo userInfo,String otpMsgType) {
        try {
            String token = this.genToken(userInfo);

            OneTimePassword otp = new OneTimePassword(WebContext.genId());

            otp.setToken(token);
            otp.setValidity((interval / 60));
            otp.setType(otpMsgType);

            otp.setUserId(userInfo.getId());
            otp.setUsername(userInfo.getUsername());
            otp.setEmail(userInfo.getEmail());
            otp.setMobile(userInfo.getMobile());
            otp.setCreateTime(DateUtils.getCurrentDateTimeAsString());
            logger.debug("prepear otp message {}" , otp);


            //store token
            this.optTokenStore.store(
                    userInfo,
                    token,
                    userInfo.getMobile(),
                    OtpTypes.MQ_OTP);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean validate(UserInfo userInfo, String token) {
        return this.optTokenStore.validate(userInfo, token, OtpTypes.MQ_OTP, interval);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }

    public void setMessageTemplate(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }


}
