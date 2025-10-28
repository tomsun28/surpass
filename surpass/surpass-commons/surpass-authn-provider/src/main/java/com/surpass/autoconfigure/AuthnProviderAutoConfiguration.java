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






package com.surpass.autoconfigure;

import com.surpass.ip2location.IpLocationParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.surpass.authn.jwt.service.AuthTokenService;
import com.surpass.authn.provider.AbstractAuthenticationProvider;
import com.surpass.authn.provider.AuthenticationProviderFactory;
import com.surpass.authn.provider.impl.NormalAuthenticationProvider;
import com.surpass.authn.provider.impl.TrustedAuthenticationProvider;
import com.surpass.authn.realm.AbstractAuthenticationRealm;
import com.surpass.authn.session.SessionManager;
import com.surpass.persistence.service.LoginService;

/**
 * 认证提供者自动配置，可根据需要增加新的提供者
 *
 * @author Crystal.Sea
 *
 */
@AutoConfiguration
public class AuthnProviderAutoConfiguration {
    static final  Logger logger = LoggerFactory.getLogger(AuthnProviderAutoConfiguration.class);

    @Bean
	@Primary
    AbstractAuthenticationProvider authenticationProvider(
    		NormalAuthenticationProvider normalAuthenticationProvider,
            TrustedAuthenticationProvider trustedAuthenticationProvider) {
    	AuthenticationProviderFactory provider = new AuthenticationProviderFactory();
    	//常规提供者
    	provider.addAuthenticationProvider(normalAuthenticationProvider);
    	//信任提供者
    	provider.addAuthenticationProvider(trustedAuthenticationProvider);

    	return provider;
    }

    @Bean
    NormalAuthenticationProvider normalAuthenticationProvider(
            AbstractAuthenticationRealm authenticationRealm,
            IpLocationParser ipLocationParser,
            SessionManager sessionManager,
            LoginService loginService,
            AuthTokenService authTokenService) {
    	logger.debug("init Normal authentication Provider .");
    	return new NormalAuthenticationProvider(
        		authenticationRealm,
        		sessionManager,
        		loginService,
        		authTokenService,
        		ipLocationParser
        	);
    }

    @Bean
    TrustedAuthenticationProvider trustedAuthenticationProvider(
            AbstractAuthenticationRealm authenticationRealm,
            SessionManager sessionManager,
            IpLocationParser ipLocationParser) {
    	logger.debug("init Trusted authentication Provider .");
    	return new TrustedAuthenticationProvider(
        		authenticationRealm,
        		sessionManager,
        		ipLocationParser
        	);
    }

}
