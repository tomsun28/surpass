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






package org.maxkey.surpass.authn.web;

import java.text.ParseException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.maxkey.surpass.authn.SignedPrincipal;
import org.maxkey.surpass.authn.jwt.service.AuthTokenService;
import org.maxkey.surpass.authn.session.Session;
import org.maxkey.surpass.authn.session.SessionManager;
import org.maxkey.surpass.entity.idm.UserInfo;
import org.maxkey.surpass.util.AuthorizationHeaderUtils;
import org.maxkey.surpass.web.WebConstants;
import org.maxkey.surpass.web.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import com.nimbusds.jwt.SignedJWT;

/**
 * 认证信息工具类
 *
 * @author Crystal.Sea
 *
 */
public class AuthorizationUtils {
	private static final Logger logger = LoggerFactory.getLogger(AuthorizationUtils.class);

	public class BearerType{
		/**
		 * cookie name congress
		 */
		public static final String CONGRESS 			= "congress";

		public static final String CONGRESS_TYPE 		= "cookie";
		/**
		 * parameter name congress
		 */
		public static final String PARAMETER 			= "congress";

		public static final String PARAMETER_TYPE 		= "parameter";
		/**
		 * header name Authorization
		 */
		public static final String AUTHORIZATION 		= "Authorization";

		public static final String AUTHORIZATION_TYPE 	= "Authorization";
	}

	/**
	 * 根据
	 * <p>
	 * 1. header authorization
	 * </p>
	 * <p>
	 * 2. parameter congress
	 * </p>
	 * <p>
	 * 3. cookie congress  Authorization
	 * </p>  认证
	 * @param request
	 * @param authTokenService
	 * @param sessionManager
	 * @throws ParseException
	 */
	public static  void authenticate(
			HttpServletRequest request,
			AuthTokenService authTokenService,
			SessionManager sessionManager
			) throws ParseException{
		if(isNotAuthenticated()) { //判断是否已经认证通过
			String bearerType = BearerType.AUTHORIZATION_TYPE;
			String  authorization = AuthorizationHeaderUtils.resolveBearer(request);
			logger.trace("bearerType {} ,  authorization {}" , bearerType ,authorization);

			if(StringUtils.isBlank(authorization)) {
				authorization = request.getParameter(BearerType.CONGRESS);
				bearerType = BearerType.PARAMETER_TYPE;
				logger.trace("get congress from parameter ,  value {}" , authorization);
			}

			if(StringUtils.isBlank(authorization)) {
				Cookie authCookie = WebContext.getCookie(request, BearerType.CONGRESS);
				if(authCookie != null ) {//cookie不为空，解析cookie并认证
			    	authorization =  authCookie.getValue();
			    	bearerType = BearerType.CONGRESS_TYPE;
			    	logger.trace("get congress from cookie ,  value {}" , authorization);
				}
			}
			if(StringUtils.isNotBlank(authorization) && !authorization.equalsIgnoreCase("undefined")) {
				logger.trace("Try Authorization authenticate .");
				doJwtAuthenticate(request,bearerType,authorization,authTokenService,sessionManager);
			}
		}
	}

	/**
	 * 根据JWT认证
	 * @param bearerType
	 * @param authorization
	 * @param authTokenService
	 * @param sessionManager
	 * @throws ParseException
	 */
	public static void doJwtAuthenticate(
			HttpServletRequest request,
			String  bearerType,
			String  authorization,
			AuthTokenService authTokenService,
			SessionManager sessionManager) throws ParseException {
		//验证JWT令牌的有效性
		if(authTokenService.validateJwtToken(authorization)) {
			//解析jwt jid即会话id
			String sessionId = authTokenService.resolveJWTID(authorization);
			//读取会话
			Session session = sessionManager.get(sessionId);
			if(session != null) {//会话不为空，设置认证信息
				setAuthentication(request,session.getAuthentication());
				logger.debug("Automatic authenticated by Type {}",bearerType);
			}else {
				//time out/会话超时
				logger.debug("Session timeout .");
				clearAuthentication();
			}
		}else {
			//token invalidate/验证不通过，会话过期，需要清除认证信息
			logger.debug("Token invalidate .");
			clearAuthentication();
		}
	}

    /**
     * 读取Authentication by WebContext.getRequest
     * @return
     */
    public static Authentication getAuthentication() {
    	HttpServletRequest request = WebContext.getRequest();
    	return request == null ? null : getAuthentication(request);
    }

    /**
     * HttpServletRequest 中读取认证信息
     * @param request
     * @return
     */
    public static Authentication getAuthentication(HttpServletRequest request) {
    	return (Authentication) request.getSession().getAttribute(WebConstants.AUTHENTICATION);
    }

    /**
     * set Authentication to http session/认证会话信息
     * @param authentication
     */
    public static void setAuthentication(HttpServletRequest request,Authentication authentication) {
    	if(request != null ) {
    		request.getSession().setAttribute(WebConstants.AUTHENTICATION, authentication);
    	}else {
    		setAuthentication(authentication);
    	}
    }

    public static void setAuthentication(Authentication authentication) {
    	WebContext.setAttribute(WebConstants.AUTHENTICATION, authentication);
    }

    /**
     * 清除当前认证
     */
    public static void clearAuthentication() {
    	WebContext.removeAttribute(WebConstants.AUTHENTICATION);
    }

    public static  boolean isAuthenticated() {
    	return getAuthentication() != null;
    }

    public static  boolean isNotAuthenticated() {
    	return ! isAuthenticated();
    }

    public static SignedPrincipal getPrincipal() {
    	Authentication authentication =  getAuthentication();
    	return authentication == null ? null : getPrincipal(authentication);
    }

    public static SignedPrincipal getPrincipal(Authentication authentication) {
    	return authentication == null ? null : (SignedPrincipal) authentication.getPrincipal();
   }

    /**
     * 读取认证的用户信息
     * @param authentication
     * @return UserInfo
     */
    public static UserInfo getUserInfo(Authentication authentication) {
    	UserInfo userInfo = null;
    	SignedPrincipal principal = getPrincipal(authentication);
    	if(principal != null ) {
        	userInfo = principal.getUserInfo();
        }
    	return userInfo;
    }

    public static Session getSession(SessionManager sessionManager,String authorization) throws ParseException {
    	//解析accessToken，转换会话id
    	logger.debug("get session by authorization {}" , authorization);
		SignedJWT signedJWT = SignedJWT.parse(authorization);
		String sessionId = signedJWT.getJWTClaimsSet().getJWTID();
    	return sessionManager.get(sessionId);
    }

    public static UserInfo getUserInfo() {
    	return getUserInfo(getAuthentication());
    }

    public static User getUser() {
    	return (User)getAuthentication().getPrincipal();
    }

}
