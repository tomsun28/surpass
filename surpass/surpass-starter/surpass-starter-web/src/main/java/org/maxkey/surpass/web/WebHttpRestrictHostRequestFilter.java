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




package org.maxkey.surpass.web;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


import org.apache.commons.lang3.StringUtils;
import org.maxkey.surpass.constants.ConstsHttpHeader;
import org.maxkey.surpass.web.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 请求域名限定Filter
 */
public class WebHttpRestrictHostRequestFilter  extends GenericFilterBean {
	static final  Logger logger = LoggerFactory.getLogger(WebHttpRestrictHostRequestFilter.class);

	ConcurrentMap<String,String> restrictHostMap ;

	boolean isRestrict;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		logger.trace("WebHttpRestrictHostRequestFilter");
		HttpServletRequest request= ((HttpServletRequest)servletRequest);
		if(logger.isTraceEnabled()) {WebContext.printRequest(request);}

		String host = request.getHeader(ConstsHttpHeader.HEADER_HOST);
		if(StringUtils.isBlank(host)) {
			host = request.getHeader(ConstsHttpHeader.HEADER_HOSTNAME);
		}
		logger.trace("host {}",host);

		if(host.indexOf(":")> -1 ) {
			host = host.split(":")[0];
			logger.trace("host split {}",host);
		}

		//限制条件true and host不为空 and 不在限制的host请求 需要过滤
		if(isRestrict && StringUtils.isNotBlank(host) && !restrictHostMap.containsKey(host)) {
			logger.error("host {} is restrict",host);
			return;
		}

        chain.doFilter(servletRequest, servletResponse);
	}

	public WebHttpRestrictHostRequestFilter(List<String> restrictHosts) {
		restrictHostMap = new ConcurrentHashMap<>();
		for(String restrictHost : restrictHosts) {
			if(StringUtils.isNotBlank(restrictHost)) {
				restrictHostMap.put(restrictHost, restrictHost);
				isRestrict = true;
			}
		}
		logger.debug("isRestrict {}",isRestrict);
		if(isRestrict) {
			logger.debug("Restrict Host {}",restrictHostMap);
		}
	}

}
