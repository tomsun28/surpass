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






package org.dromara.surpass.authn.provider.impl;


import org.dromara.surpass.authn.LoginCredential;
import org.dromara.surpass.authn.jwt.service.AuthTokenService;
import org.dromara.surpass.authn.provider.AbstractAuthenticationProvider;
import org.dromara.surpass.authn.realm.AbstractAuthenticationRealm;
import org.dromara.surpass.authn.session.Session;
import org.dromara.surpass.authn.session.SessionManager;
import org.dromara.surpass.constants.ConstsCaptchaType;
import org.dromara.surpass.constants.ConstsLoginType;
import org.dromara.surpass.entity.client.ClientResolve;
import org.dromara.surpass.ip2location.IpLocationParser;
import org.dromara.surpass.pojo.entity.config.ConfigLoginPolicy;
import org.dromara.surpass.pojo.entity.idm.UserInfo;
import org.dromara.surpass.service.LoginService;
import org.dromara.surpass.web.WebConstants;
import org.dromara.surpass.web.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.text.ParseException;

/**
 * Normal Authentication provider.正常用户名密码认证提供者
 *
 * @author Crystal.Sea
 *
 */
public class NormalAuthenticationProvider extends AbstractAuthenticationProvider {
    private static final Logger logger =LoggerFactory.getLogger(NormalAuthenticationProvider.class);

    public String getProviderName() {
        return "normal" + PROVIDER_SUFFIX;
    }


    public NormalAuthenticationProvider() {
		super();
	}

    public NormalAuthenticationProvider(
    		AbstractAuthenticationRealm authenticationRealm,
    	    SessionManager sessionManager,
    	    LoginService loginService,
    	    AuthTokenService authTokenService,
    	    IpLocationParser ipLocationParser) {
		this.authenticationRealm = authenticationRealm;
		this.sessionManager = sessionManager;
		this.loginService = loginService;
		this.authTokenService = authTokenService;
		this.ipLocationParser = ipLocationParser;
	}

    @Override
	public Authentication doAuthenticate(LoginCredential loginCredential) {
		UsernamePasswordAuthenticationToken authenticationToken = null;
		loginCredential.setStyle(Session.STYLE.MGMT);
		logger.debug("Trying to authenticate user {} via {}", loginCredential.getPrincipal(), getProviderName());
		logger.debug("loginCredential {}" , loginCredential);
        try {
	        //判断图片验证码并验证
	        if(!this.loginService.getConfigLoginPolicy().getCaptchaMgt().equalsIgnoreCase(ConstsCaptchaType.NONE)) {
	        	captchaValid(loginCredential.getState(),loginCredential.getCaptcha());
	        }

	        emptyPasswordValid(loginCredential.getPassword());
	        emptyUsernameValid(loginCredential.getUsername());

	        //查询用户
	        UserInfo userInfo =  loadUserInfo(loginCredential.getUsername(),loginCredential.getPassword());
	        //获取登录终端信息
	        ClientResolve client = parserClientResolve();
	        statusValid(loginCredential , userInfo , client);
	        //Validate PasswordPolicy
	        authenticationRealm.applyLoginPolicy(userInfo);

	        //Match password 验证密码
	        boolean passwordMatches = authenticationRealm.passwordMatches(userInfo, loginCredential.getPassword());

	        if (!passwordMatches) {
	        	ConfigLoginPolicy configLoginPolicy = loginService.getConfigLoginPolicy();
	        	loginService.updateBadPasswordCount(userInfo);
	        	authenticationRealm.insertLoginHistory(userInfo,client, ConstsLoginType.NORMAL, "", "xe00000004", WebConstants.LOGIN_RESULT.PASSWORD_ERROE);
	            if(userInfo.getBadPasswordCount()>=(configLoginPolicy.getPasswordAttempts())) {
	                throw new BadCredentialsException(
	                        WebContext.getI18nValue("login.error.password.attempts",
	                                new Object[]{
	                                        userInfo.getBadPasswordCount() ,
	                                        configLoginPolicy.getLoginAttempts(),
	                                        configLoginPolicy.getLockInterval()}));
	            }else {
	                throw new BadCredentialsException(WebContext.getI18nValue("login.error.password"));
	            }
	        }

	        authenticationToken = createOnlineTicket(loginCredential,userInfo,client);
	        // user authenticated
	        logger.debug("'{}' authenticated successfully by {}.",
	        		loginCredential.getPrincipal(), getProviderName());

	        authenticationRealm.insertLoginHistory(userInfo,
	        										client,
							        				ConstsLoginType.NORMAL,
									                "",
									                "xe00000004",
									                WebConstants.LOGIN_RESULT.SUCCESS);
        } catch (AuthenticationException e) {
            logger.error("Failed to authenticate user {} via {}: {}",
                    				loginCredential.getPrincipal(),
                                    getProviderName(),
                                    e.getMessage() );
            WebContext.setAttribute(
                    WebConstants.LOGIN_ERROR_SESSION_MESSAGE, e.getMessage());
        } catch (Exception e) {
            logger.error("Login error Unexpected exception in {} authentication:{}" ,
                            getProviderName(), e.getMessage());
        }

        return  authenticationToken;
    }

    /**
     * captcha validate .图片验证码校验
     *
     * @param state String
     * @param captcha String
     * @throws ParseException
     */
    protected void captchaValid(String state ,String captcha) {
        // for basic
    	if(!authTokenService.validateCaptcha(state,captcha)) {
    		throw new BadCredentialsException(WebContext.getI18nValue("login.error.captcha"));
    	}
    }
}
