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






package org.dromara.surpass.web;

import java.io.IOException;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

/**
 * XSS请求Filter，包括忽略地址、忽略请求参数、风险字符等
 */
public class WebHttpXssRequestFilter  extends GenericFilterBean {
	static final  Logger logger = LoggerFactory.getLogger(WebHttpXssRequestFilter.class);

	static final ConcurrentHashMap <String,String> ignoreUrlMap = new  ConcurrentHashMap <>();

	static final ConcurrentHashMap <String,String> ignoreParameterName = new  ConcurrentHashMap <>();

	/**
	 * 特殊字符 ' -- #
	 */
	public final static Pattern specialCharacterRegex = Pattern.compile(".*((\\%27)|(')|(\\')|(--)|(\\-\\-)|(\\%23)|(#)).*", Pattern.CASE_INSENSITIVE);

	static {
		//add or update
		//ignoreUrlMap.put("/apps/updateExtendAttr",			"/apps/updateExtendAttr");

		ignoreParameterName.put("relatedPassword", 			"relatedPassword");
		ignoreParameterName.put("oldPassword", 				"oldPassword");
		ignoreParameterName.put("password", 				"password");
		ignoreParameterName.put("confirmpassword", 			"confirmpassword");
		ignoreParameterName.put("credentials", 				"credentials");
		ignoreParameterName.put("clientSecret", 			"clientSecret");
		ignoreParameterName.put("appSecret", 				"appSecret");
		ignoreParameterName.put("sharedSecret", 			"sharedSecret");
		ignoreParameterName.put("secret", 					"secret");
		ignoreParameterName.put("token", 					"token");
		ignoreParameterName.put("access_token", 			"access_token");
		ignoreParameterName.put("refresh_token", 			"refresh_token");


	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		logger.trace("WebHttpXssRequestFilter");
		boolean isWebXss = false;
		HttpServletRequest request= ((HttpServletRequest)servletRequest);
		if(logger.isTraceEnabled()) {WebContext.printRequest(request);}
		if(ignoreUrlMap.containsKey(request.getRequestURI().substring(request.getContextPath().length()))) {
			//url ignore , do nothing
		}else {
	        Enumeration<String> parameterNames = request.getParameterNames();
	        while (parameterNames.hasMoreElements()) {
	          String key = parameterNames.nextElement();
	          if(!ignoreParameterName.containsKey(key)) {
		          String value = request.getParameter(key);
		          logger.trace("parameter name {} , value {}" ,key, value);
		          String tempValue = value;
		          if(!StringEscapeUtils.escapeHtml4(tempValue).equals(value)
		        		  ||specialCharacterMatches(value)
		        		  ||tempValue.toLowerCase().indexOf("script")>-1
		        		  ||tempValue.toLowerCase().replace(" ", "").indexOf("eval(")>-1) {
		        	  isWebXss = true;
		        	  logger.error("parameter name {} , value {}, contains dangerous content ! ", key , value);
		        	  break;
		          }
	          }
	        }
		}
        if(!isWebXss) {
        	chain.doFilter(request, response);
        }
	}

	/**
	 * 特殊字符匹配
	 * @param text
	 * @return
	 */
	static boolean specialCharacterMatches(String text) {
		return specialCharacterRegex.matcher(text).matches();
	}

}
