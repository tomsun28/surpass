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






package org.dromara.surpass.authn.web;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.dromara.surpass.configuration.ApplicationConfig;
import org.dromara.surpass.entity.Message;
import org.dromara.surpass.pojo.entity.Institutions;
import org.dromara.surpass.service.InstitutionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 机构查询
 *
 * @author Crystal.Sea
 *
 */
@RestController
@RequestMapping(value = "/inst")
public class InstitutionEndpoint {
	private static final  Logger logger = LoggerFactory.getLogger(InstitutionEndpoint.class);

	public static final String  HEADER_HOST 		= "host";

	public static final String  HEADER_HOSTNAME 	= "hostname";

	@Autowired
	InstitutionsService institutionsService;

	@Autowired
	ApplicationConfig applicationConfig;

	/**
	 * 根据header参数读取机构信息
	 * @param request
	 * @param originURL
	 * @param headerHostName
	 * @param headerHost
	 * @return inst
	 */
 	@GetMapping(value={"/get"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Institutions> get(
			HttpServletRequest request,
			@RequestHeader(value = "Origin",required=false) String originURL,
			@RequestHeader(value = HEADER_HOSTNAME,required=false) String headerHostName,
			@RequestHeader(value = HEADER_HOST,required=false) String headerHost) {
 		logger.debug("get Institution" );

		String host = headerHostName;
		logger.trace("hostname {}",host);
		if(StringUtils.isEmpty(host)) {
			host = headerHost;
			logger.trace("host {}",host);
		}

		if(host.indexOf(":")> -1 ) {
			host = host.split(":")[0];
			logger.trace("domain split {}",host);
		}

		Institutions inst = institutionsService.getByInstIdOrDomain(host);
		if(inst != null) {
			logger.debug("inst {}",inst);
			return new Message<>(inst);
		}else {
			Institutions defaultInst = institutionsService.getByInstIdOrDomain("1");
			logger.debug("default inst {}",inst);
			return new Message<>(defaultInst);
		}
 	}
}
