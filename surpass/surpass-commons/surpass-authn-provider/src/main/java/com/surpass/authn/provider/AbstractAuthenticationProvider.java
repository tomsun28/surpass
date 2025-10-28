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






package com.surpass.authn.provider;

import java.util.List;

import com.surpass.ip2location.IpLocationParser;
import com.surpass.ip2location.Region;
import com.surpass.password.onetimepwd.AbstractOtpAuthn;
import com.surpass.password.onetimepwd.MailOtpAuthnService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.surpass.authn.LoginCredential;
import com.surpass.authn.SignedPrincipal;
import com.surpass.authn.jwt.service.AuthTokenService;
import com.surpass.authn.realm.AbstractAuthenticationRealm;
import com.surpass.authn.session.Session;
import com.surpass.authn.session.SessionManager;
import com.surpass.authn.web.AuthorizationUtils;
import com.surpass.constants.ConstsLoginType;
import com.surpass.constants.ConstsRoles;
import com.surpass.constants.ConstsStatus;
import com.surpass.entity.client.ClientResolve;
import com.surpass.entity.client.ClientUserAgent;
import com.surpass.entity.client.UserAgentParser;
import com.surpass.entity.idm.UserInfo;
import com.surpass.persistence.service.LoginService;
import com.surpass.web.WebConstants;
import com.surpass.web.WebContext;
/**
 * login Authentication abstract class.登录认证提供者抽象类
 *
 * @author Crystal.Sea
 *
 */
public abstract class AbstractAuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(AbstractAuthenticationProvider.class);

    public static final String PROVIDER_SUFFIX = "AuthenticationProvider";

    /**
     * 认证类型
     *
     */
    public class AuthType{
    	//用户名和密码登录
    	public static final  String NORMAL 		= "normal";
    	//双因素认证
    	public static final  String TFA 		= "tfa";
    	//手机号码登录
    	public static final  String MOBILE 		= "mobile";
    	//信任JWT登录
    	public static final  String TRUSTED 	= "trusted";
    }

    //认证域
    protected AbstractAuthenticationRealm authenticationRealm;
    //双因素OTP生成和验证服务
    protected AbstractOtpAuthn tfaOtpAuthn;
    //one time password 认证服务
    protected MailOtpAuthnService otpAuthnService;
    //会话管理服务
    protected SessionManager sessionManager;
    //认证令牌服务
    protected AuthTokenService authTokenService;
    //登录校验服务
    protected LoginService loginService;
    //IP地址转换服务
    protected IpLocationParser ipLocationParser;

    protected boolean supported = true;

    public abstract String getProviderName();

    public abstract Authentication doAuthenticate(LoginCredential credential);

    @SuppressWarnings("rawtypes")
    public boolean supports(Class authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    public Authentication authenticate(LoginCredential credential){
    	logger.debug("credential {}",credential);
    	return null;
    }

    public Authentication authenticate(LoginCredential credential,boolean trusted) {
    	logger.debug("trusted {} , credential {}",trusted,credential);
    	return null;
    }



    /**
     * createOnlineSession 认证成功后签发token及会话
     * @param credential
     * @param userInfo
     * @return
     */
    public UsernamePasswordAuthenticationToken createOnlineTicket(LoginCredential credential,UserInfo userInfo,ClientResolve client) {
        //create session/创建新用户会话
        Session session = new Session();

        session.setStyle(credential.getStyle());

        //set session with principal。设置认证当事人
        SignedPrincipal principal = new SignedPrincipal(userInfo,session);
        //读取用户授权角色
        List<GrantedAuthority> grantedAuthoritys = authenticationRealm.grantAuthority(userInfo);
        principal.setAuthenticated(true);
        principal.setStyle(session.getStyle());
        //判断管理员角色
        for(GrantedAuthority adminAuthority : ConstsRoles.grantedAdminAuthoritys) {
            if(grantedAuthoritys.contains(adminAuthority)) {
            	principal.setRoleAdministrators(true);
                logger.trace("ROLE ADMINISTRATORS Authentication .");
            }
        }
        logger.debug("Granted Authority {}" , grantedAuthoritys);

        //创建认证token
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                		principal,
                        "PASSWORD",
                        grantedAuthoritys
                );

        authenticationToken.setDetails(
                new WebAuthenticationDetails(WebContext.getRequest()));

        /*
         *  put Authentication to current session context，设置会话的认证token
         */
        session.setAuthentication(authenticationToken);

        //create session。会话管理服务管理当前新会话。
        this.sessionManager.create(session.getId(), session);

        //set Authentication to http session，设置当前认证token
        AuthorizationUtils.setAuthentication(authenticationToken);

        return authenticationToken;
    }

    /**
     * login user by username ， userinfo is null,query user from system.
     *
     * @param username String
     * @param password String
     * @return
     */
    public UserInfo loadUserInfo(String username, String password) {
    	return authenticationRealm.loadUserInfo(username, password);
    }

    /**
     * check input password empty.
     *
     * @param password String
     * @return
     */
    protected boolean emptyPasswordValid(String password) {
        if (null == password || "".equals(password)) {
            throw new BadCredentialsException(WebContext.getI18nValue("login.error.password.null"));
        }
        return true;
    }

    /**
     * check input username or password empty.
     *
     * @param email String
     * @return
     */
    protected boolean emptyEmailValid(String email) {
        if (null == email || "".equals(email)) {
            throw new BadCredentialsException("login.error.email.null");
        }
        return true;
    }

    /**
     * check input username empty.
     *
     * @param username String
     * @return
     */
    protected boolean emptyUsernameValid(String username) {
        if (null == username || "".equals(username)) {
            throw new BadCredentialsException(WebContext.getI18nValue("login.error.username.null"));
        }
        return true;
    }

    protected boolean statusValid(LoginCredential loginCredential , UserInfo userInfo,ClientResolve client) {
        if (null == userInfo) {
            String i18nMessage = WebContext.getI18nValue("login.error.username");
            logger.debug("login user {} not in this System {}." ,loginCredential.getUsername(), i18nMessage);
            UserInfo loginUser = new UserInfo(loginCredential.getUsername());
            loginUser.setId(WebContext.genId());
            loginUser.setUsername(loginCredential.getUsername());
            loginUser.setDisplayName("not exist");
            loginUser.setLoginCount(0);
            authenticationRealm.insertLoginHistory(
            			loginUser,
            			client,
            			ConstsLoginType.NORMAL,
            			"",
            			i18nMessage,
            			WebConstants.LOGIN_RESULT.USER_NOT_EXIST);
            throw new BadCredentialsException(i18nMessage);
        }else {
        	if(userInfo.getIsLocked()==ConstsStatus.LOCK) {
        		authenticationRealm.insertLoginHistory(
        				userInfo,
        				client,
                        loginCredential.getAuthType(),
                        loginCredential.getProvider(),
                        loginCredential.getCode(),
                        WebConstants.LOGIN_RESULT.USER_LOCKED
                    );
        	}else if(userInfo.getStatus()!=ConstsStatus.ACTIVE) {
        		authenticationRealm.insertLoginHistory(
        				userInfo,
        				client,
                        loginCredential.getAuthType(),
                        loginCredential.getProvider(),
                        loginCredential.getCode(),
                        WebConstants.LOGIN_RESULT.USER_INACTIVE
                    );
        	}
        }
        return true;
    }

    public ClientResolve parserClientResolve() {
    	//客户端浏览器和系统平台解析
    	ClientUserAgent  clientUserAgent  = UserAgentParser.resolveUserAgent(WebContext.getRequest());
    	//ip address to region
    	String requestIpAddress = WebContext.getRequestIpAddress();
        Region  region = ipLocationParser.region(requestIpAddress);
        ClientResolve clientResolve = new ClientResolve(clientUserAgent);
        clientResolve.setIpAddr(requestIpAddress);
        clientResolve.setCountry(region.getCountry());
        clientResolve.setProvince(region.getProvince());
        clientResolve.setCity(region.getCity());
        clientResolve.setLocation(region.getAddr());
    	return clientResolve;
    }

	public boolean isSupported() {
		return supported;
	}

	public void setSupported(boolean supported) {
		this.supported = supported;
	}

}
