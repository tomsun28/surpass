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






package org.dromara.surpass.authn.support.cas.service.impl;

import org.dromara.surpass.authn.LoginCredential;
import org.dromara.surpass.authn.jwt.AuthJwt;
import org.dromara.surpass.authn.jwt.service.AuthTokenService;
import org.dromara.surpass.authn.provider.AbstractAuthenticationProvider;
import org.dromara.surpass.authn.session.Session;
import org.dromara.surpass.authn.support.cas.service.CasTrustLoginService;
import org.dromara.surpass.constants.ConstsLoginType;
import org.dromara.surpass.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;


public class CasTrustLoginServiceImpl implements CasTrustLoginService {
    static final Logger logger = LoggerFactory.getLogger(CasTrustLoginServiceImpl.class);

    String service;

    Cas20ServiceTicketValidator cas20ServiceTicketValidator;

    AbstractAuthenticationProvider authenticationProvider ;

	AuthTokenService authTokenService;

    public CasTrustLoginServiceImpl(
    		String service,
    		Cas20ServiceTicketValidator cas20ServiceTicketValidator,
    		AbstractAuthenticationProvider authenticationProvider,
    		AuthTokenService authTokenService) {
    	this.service = service;
        this.cas20ServiceTicketValidator = cas20ServiceTicketValidator;
        this.authenticationProvider =authenticationProvider;
        this.authTokenService = authTokenService;
    }

    public Message<AuthJwt> authenticate(String ticket) {
    	String username = validateLoginUser(ticket);

		 if(username != null) {
			 LoginCredential loginCredential =new LoginCredential(username,"", ConstsLoginType.CAS);
			 loginCredential.setStyle(Session.STYLE.MGMT);
			 Authentication  authentication = authenticationProvider.authenticate(loginCredential,true);
			 logger.debug("CAS Logined in , username {}" , username);
			 AuthJwt authJwt = authTokenService.genAuthJwt(authentication);
	 		 return new Message<>(authJwt);
		 }
		 return new Message<>(Message.FAIL);
    }

    public String validateLoginUser(String ticket) {
        logger.debug("build Login User .");
        String user = null;
        Assertion assertion;
		try {
			assertion = cas20ServiceTicketValidator.validate(ticket, service);
			 if(assertion != null) {
		        	user = assertion.getPrincipal().getName();
		     }
		} catch (TicketValidationException e) {
			logger.error("cas TicketValidationException" , e);
			e.printStackTrace();
		}

        logger.debug("cas user : {}" , user);
        return user;
    }

}
