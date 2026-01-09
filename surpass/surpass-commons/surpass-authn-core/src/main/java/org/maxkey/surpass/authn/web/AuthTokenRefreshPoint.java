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

import jakarta.servlet.http.HttpServletRequest;

import org.maxkey.surpass.authn.jwt.AuthJwt;
import org.maxkey.surpass.authn.jwt.service.AuthRefreshTokenService;
import org.maxkey.surpass.authn.jwt.service.AuthTokenService;
import org.maxkey.surpass.authn.session.Session;
import org.maxkey.surpass.authn.session.SessionManager;
import org.maxkey.surpass.entity.Message;
import org.maxkey.surpass.web.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 前端认证令牌刷新/auth/token/refresh
 *
 * @author Crystal.Sea
 *
 */
@Controller
@RequestMapping(value = "/auth")
public class AuthTokenRefreshPoint {
	private static final  Logger logger = LoggerFactory.getLogger(AuthTokenRefreshPoint.class);
	/**
	 * 认证令牌服务
	 */
	@Autowired
	AuthTokenService authTokenService;
	/**
	 * 刷新令牌服务
	 */
	@Autowired
	AuthRefreshTokenService refreshTokenService;
	/**
	 * 会话管理
	 */
	@Autowired
	SessionManager sessionManager;

 	/**
 	 * 根据refreshToken刷新认证令牌
 	 * @param request
 	 * @param refreshToken
 	 * @return
 	 */
 	@GetMapping(value={"/token/refresh"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> refreshGet(HttpServletRequest request,
			@RequestParam(name = "refresh_token", required = false) String refreshToken) {
 		return refresh(request ,refreshToken);
 	}

 	@PostMapping(value={"/token/refresh"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> refresh(HttpServletRequest request,
			@RequestParam(name = "refresh_token", required = false) String refreshToken) {
 		logger.debug("try to refresh token " );
 		logger.trace("refresh token {} " , refreshToken);
 		//日志打印trace
 		if(logger.isTraceEnabled()) {WebContext.printRequest(request);}
 		try {
 			//判断refreshToken的有效性
	 		if(refreshTokenService.validateJwtToken(refreshToken)) {
	 			//解析refreshToken，转换会话id
	 			String sessionId = refreshTokenService.resolveJWTID(refreshToken);
	 			//尝试刷新会话
	 			logger.trace("Try to  refresh sessionId [{}]" , sessionId);
		 		Session session = sessionManager.refresh(sessionId);
		 		if(session != null) {//有会话
		 			//重新生成新令牌
		 			AuthJwt authJwt = authTokenService.genAuthJwt(session.getAuthentication());
		 			logger.trace("Grant new token {}" , authJwt);
		 			return new Message<AuthJwt>(authJwt).buildResponse();
		 		}else {//无会话
		 			logger.debug("Session is timeout , sessionId [{}]" , sessionId);
		 		}
	 		}else {//验证失效
	 			logger.debug("refresh token is not validate .");
	 		}
 		}catch(Exception e) {
 			logger.error("Refresh Exception !",e);
 		}
 		return new ResponseEntity<>("Refresh Token Fail !", HttpStatus.UNAUTHORIZED);
 	}
}
