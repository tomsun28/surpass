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






package org.maxkey.mail;

import java.util.Properties;

import org.junit.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.surpass.uuid.UUID;

public class MailTest {

	@Test
	public void test() throws Exception {
	String username="test@maxsso.net";
	String password="test";
	String smtpHost="smtp.exmail.qq.com";
	int port=465;
	boolean ssl=true;
	String senderMail=username;


	 JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
     javaMailSender.setUsername(username);
     javaMailSender.setPassword(password);
     Properties properties = new Properties();
     properties.put("mail.smtp.auth","true");
     javaMailSender.setJavaMailProperties(properties);
     javaMailSender.setHost(smtpHost);
     javaMailSender.setPort(port);

     //MailMessage
     SimpleMailMessage mailMessage = new SimpleMailMessage();
     mailMessage.setFrom(senderMail);
     mailMessage.setTo("shimingxy@163.com");
     mailMessage.setSubject("One Time PassWord");
     mailMessage.setText("You Token is "+UUID.generate()+" , it validity in "+5 +" minutes");

     javaMailSender.send(mailMessage);


	}
}
