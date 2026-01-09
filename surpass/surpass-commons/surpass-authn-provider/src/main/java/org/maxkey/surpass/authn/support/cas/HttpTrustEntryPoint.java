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





package org.maxkey.surpass.authn.support.cas;

import org.maxkey.surpass.authn.jwt.AuthJwt;
import org.maxkey.surpass.authn.support.cas.service.CasTrustLoginService;
import org.maxkey.surpass.entity.*;
import org.maxkey.surpass.web.WebConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * trust信任登录
 * <p>
 * /login/trust 后台登录验证
 * </p>
 * @author shimi
 *
 */
@RestController
@RequestMapping("/login")
public class HttpTrustEntryPoint {
	static final Logger logger = LoggerFactory.getLogger(HttpTrustEntryPoint.class);

    @Autowired
	CasTrustLoginService casTrustLoginService;

	@GetMapping("/trust")
	public Message<AuthJwt> trust(@RequestParam(value = WebConstants.CAS_TICKET_PARAMETER, required = true) String ticket) {
		logger.debug("ticket : {}" , ticket);
		return casTrustLoginService.authenticate(ticket);
	}

}
