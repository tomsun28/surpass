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

import java.text.MessageFormat;
import java.util.Properties;

import com.jinbooks.password.onetimepwd.AbstractOtpAuthn;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.jinbooks.configuration.EmailConfig;
import com.jinbooks.entity.idm.UserInfo;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailOtpAuthn extends AbstractOtpAuthn {
    private static final Logger logger = LoggerFactory.getLogger(MailOtpAuthn.class);

    EmailConfig emailConfig;
    String subject = "One Time PassWord";

    String messageTemplate = "{0} You Token is {1} , it validity in {2}  minutes.";

    String registerTemplate = "<h3>您好，{0}：</h3>"
            + "<p>您正在注册我们的账号，请使用以下验证码完成注册：</p>"
            + "<h2 style=\"color: blue;\">{1}</h2>"
            + "<p>该验证码有效期为 {2} 分钟，请尽快完成验证。</p>"
            + "<p>如果不是您本人操作，请忽略此邮件。</p>";


    public MailOtpAuthn() {
        otpType = OtpTypes.EMAIL;
    }

    public MailOtpAuthn(EmailConfig emailConfig) {
    	otpType = OtpTypes.EMAIL;
		this.emailConfig = emailConfig;
	}

	public MailOtpAuthn(EmailConfig emailConfig, String subject, String messageTemplate) {
		otpType = OtpTypes.EMAIL;
		this.emailConfig = emailConfig;
		this.subject = subject;
		this.messageTemplate = messageTemplate;
	}



	@Override
    public boolean produce(UserInfo userInfo,String otpMsgType) {
        try {
            String token = this.genToken(userInfo);

            logger.debug("token {} send to user {}, email {}",token, userInfo.getUsername(), userInfo.getEmail());
            //成功返回
            this.optTokenStore.store(
                    userInfo,
                    token,
                    userInfo.getEmail(),
                    OtpTypes.EMAIL);

            //Sender
            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
            javaMailSender.setUsername(emailConfig.getUsername());
            javaMailSender.setPassword(emailConfig.getPassword());
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.ssl.enable", "true");
            javaMailSender.setJavaMailProperties(properties);
            javaMailSender.setHost(emailConfig.getSmtpHost());
            javaMailSender.setPort(emailConfig.getPort());

            // 创建 MimeMessage
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // 设置邮件信息
            // 发件人地址（建议用 setUsername 的邮箱）
            helper.setTo(userInfo.getEmail());
            helper.setFrom(emailConfig.getUsername());
            helper.setSubject(subject);

            // 设置 HTML 格式内容
            String content;
            if (AbstractOtpAuthn.OtpMsgTypes.REGISTER.equals(otpMsgType)) {
                helper.setSubject("JinBooks用户注册验证");
                content = MessageFormat.format(registerTemplate, userInfo.getEmail(), token, (interval / 60));
            } else {
                content = MessageFormat.format(messageTemplate, userInfo.getUsername(), token, (interval / 60));
            }
            // 👈 第二个参数 true 表示启用 HTML
            helper.setText(content, true);

            // 发送
            javaMailSender.send(mimeMessage);

            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean validate(UserInfo userInfo, String token) {
        return this.optTokenStore.validate(userInfo, token, OtpTypes.EMAIL, interval);
    }

    public void setEmailConfig(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
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
