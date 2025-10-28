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


package com.surpass.web.idm.contorller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.surpass.authn.jwt.service.AuthTokenService;
import com.surpass.constants.ConstsRegex;
import com.surpass.entity.Message;
import com.surpass.entity.idm.UserInfo;
import com.surpass.entity.idm.dto.RegisterUserDto;
import com.surpass.password.onetimepwd.AbstractOtpAuthn;
import com.surpass.password.onetimepwd.MailOtpAuthnService;
import com.surpass.persistence.service.UserInfoService;

import cn.hutool.core.lang.UUID;

@RestController
@RequestMapping(value = { "/users" })
public class UserRegisterController {
	static final Logger logger = LoggerFactory.getLogger(UserRegisterController.class);

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	AuthTokenService authTokenService;

	@Autowired
	MailOtpAuthnService mailOtpAuthnService;

	@PostMapping("/register")
	public Message<String> handleRegister(@Validated @RequestBody RegisterUserDto dto) {
		String tempUsername = dto.getTempUsername();
		String email = dto.getEmail();
		String emailOtp = dto.getEmailOtp();
		UserInfo userInfo = new UserInfo();
		userInfo.setUsername(tempUsername);
		userInfo.setEmail(email);
		AbstractOtpAuthn mailOtpAuthn = mailOtpAuthnService.getMailOtpAuthn();
		boolean validate = mailOtpAuthn.validate(userInfo, emailOtp);
		if (!validate) {
			return new Message<>(Message.FAIL, "验证码错误");
		}
		return userInfoService.handleRegister(dto);
	}

	@GetMapping("/register/produceEmailOtp")
	public Message<String> produceEmailOtp(  @RequestParam(name = "email") String email,
													 @RequestParam(name = "state") String state,
													 @RequestParam(name = "captcha") String captcha) {
		if (!authTokenService.validateCaptcha(state, captcha)) {
			return new Message<>(Message.WARNING);
		}

		if (StringUtils.isNotBlank(email) && ConstsRegex.EMAIL_PATTERN.matcher(email).matches()) {
			AbstractOtpAuthn mailOtpAuthn = mailOtpAuthnService.getMailOtpAuthn();

			UserInfo userInfo = new UserInfo();
			userInfo.setUsername(UUID.randomUUID().toString() + email);
			userInfo.setEmail(email);
			userInfo.setMobile(null);
			boolean produce = mailOtpAuthn.produce(userInfo, AbstractOtpAuthn.OtpMsgTypes.REGISTER);
			if (produce) {
				return new Message<>(Message.SUCCESS, userInfo.getUsername());
			} else {
				return new Message<>(Message.FAIL);
			}
		}
		return new Message<>(Message.FAIL);
	}
}
