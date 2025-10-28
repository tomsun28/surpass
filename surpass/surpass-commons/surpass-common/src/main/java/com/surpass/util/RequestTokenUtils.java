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




package com.surpass.util;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class RequestTokenUtils {

	public static final String TOKEN 			= "token";

	public static final String ACCESS_TOKEN 	= "access_token";
	/**
	 * 从请求中获取token令牌信息,优先级顺序如下
	 * <p>
	 *  1) 参数 access_token <br/>
	 *  2) 参数 token <br/>
	 *  3) header 的Authorization或者authorization <br/>
	 * </p>
	 *
	 * @param request
	 * @return access_token
	 */
	public static String resolveAccessToken(HttpServletRequest request) {
		String accessToken = request.getParameter(ACCESS_TOKEN);

		if(StringUtils.isBlank(accessToken)) {
			accessToken = request.getParameter(TOKEN);
		}

		if(StringUtils.isBlank(accessToken)) {
	    	//for header authorization bearer
			accessToken = AuthorizationHeaderUtils.resolveBearer(request);
	    }

		return accessToken;
	}

}
