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
package org.dromara.surpass.controller;

import org.apache.tomcat.util.descriptor.web.LoginConfig;
import org.aspectj.bridge.Message;
import org.dromara.surpass.pojo.dto.LoginConfigDto;
import org.dromara.surpass.pojo.entity.Institutions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


/**
 * 登录
 * <p>
 * 登录界面初始化/login/get
 * </p>
 *
 * <p>
 * 登录入口/login/signin
 * </p>
 *
 * @author Crystal.Sea
 *
 */
@RestController
@RequestMapping(value = "/login")
public class LoginEndpoint {
	private static final Logger logger = LoggerFactory.getLogger(LoginEndpoint.class);

	@Autowired
	AuthTokenService authTokenService;

	@Autowired
  	ApplicationConfig applicationConfig;

	@Autowired
	LoginConfig loginConfig;

	@Autowired
	AbstractAuthenticationProvider authenticationProvider ;

	@Autowired
	LoginService loginService;

	@Autowired
	SecretKeyManager secretKeyManager;

	/**
	 * init login。登录界面初始化信息
	 * @return
	 */
 	@GetMapping(value={"/get"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<LoginConfigDto> get() {
		logger.debug("/login.");
		LoginConfigDto conf = new LoginConfigDto();
		ConfigLoginPolicy loginPolicy = loginService.getConfigLoginPolicy();
		Institutions inst = (Institutions)WebContext.getAttribute(WebConstants.CURRENT_INST);
		conf.setInst(inst);
		conf.setCaptcha(loginPolicy.getCaptchaMgt().toUpperCase());
		conf.setState(authTokenService.genRandomJwt());
		LoginSecretKey  loginSecretKey = secretKeyManager.getSecretKey();
		conf.setSecretKey(loginSecretKey.getSecretKey());
		conf.setSecretPublicKey(loginSecretKey.getPublicKey());
		return new Message<>(conf);
	}

 	/**
 	 * 常规用户名和密码登录
 	 * @param credential
 	 * @return
 	 */
 	@PostMapping(value={"/signin"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<AuthJwt> signin( @RequestBody LoginCredential credential) {
 		Message<AuthJwt> authJwtMessage = new Message<>(Message.FAIL);
 		secretKeyManager.decrypt(credential);
 		if(authTokenService.validateJwtToken(credential.getState())){
	 		Authentication authentication  = authenticationProvider.authenticate(credential);
	 		if(authentication != null) {//success
	 			AuthJwt authJwt = authTokenService.genAuthJwt(authentication);
	 			authJwtMessage.setData(Message.SUCCESS,authJwt);
	 		}else {//fail
	 			String errorMsg = WebContext.getAttribute(WebConstants.LOGIN_ERROR_SESSION_MESSAGE) == null ?
							      "" : WebContext.getAttribute(WebConstants.LOGIN_ERROR_SESSION_MESSAGE).toString();
	 			authJwtMessage.setMessage(errorMsg);
	 			logger.debug("login fail , message {}",errorMsg);
	 		}
 		}
 		return authJwtMessage;
 	}

}
