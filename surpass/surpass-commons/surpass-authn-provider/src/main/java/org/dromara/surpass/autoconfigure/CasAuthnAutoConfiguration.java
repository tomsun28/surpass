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






package org.dromara.surpass.autoconfigure;

import org.apereo.cas.client.validation.Cas20ServiceTicketValidator;
import org.dromara.surpass.authn.jwt.service.AuthTokenService;
import org.dromara.surpass.authn.provider.AbstractAuthenticationProvider;
import org.dromara.surpass.authn.support.cas.service.CasTrustLoginService;
import org.dromara.surpass.authn.support.cas.service.impl.CasTrustLoginServiceImpl;
import org.dromara.surpass.configuration.LoginConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * CAS配置，验证自动配置
 *
 * @author Crystal.Sea
 *
 */
@AutoConfiguration
public class CasAuthnAutoConfiguration {
    static final  Logger logger = LoggerFactory.getLogger(CasAuthnAutoConfiguration.class);

    @Bean
    Cas20ServiceTicketValidator cas20ServiceTicketValidator(LoginConfig loginConfig) {
    	return	new Cas20ServiceTicketValidator(loginConfig.getCasServerUrlPrefix());
	}

    /**
     * CAS TrustLoginService.
     * @return
     */
    @Bean
    CasTrustLoginService casTrustLoginService(
    		LoginConfig loginConfig,
    		Cas20ServiceTicketValidator cas20ServiceTicketValidator,
    		AuthTokenService authTokenService,
    		AbstractAuthenticationProvider authenticationProvider) {
        CasTrustLoginService casTrustLoginService = new CasTrustLoginServiceImpl(
        		loginConfig.getCasService(),
        		cas20ServiceTicketValidator,
        		authenticationProvider,
        		authTokenService);
        logger.debug("CAS Login Service init.");
        return casTrustLoginService;
    }

}
