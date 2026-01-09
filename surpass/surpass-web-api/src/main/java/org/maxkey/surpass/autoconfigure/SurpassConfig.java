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






package org.maxkey.surpass.autoconfigure;

import org.maxkey.surpass.authn.realm.jdbc.JdbcAuthenticationRealm;
import org.maxkey.surpass.persistence.service.LoginService;
import org.maxkey.surpass.persistence.service.PasswordPolicyValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@AutoConfiguration
public class SurpassConfig {
    private static final  Logger logger = LoggerFactory.getLogger(SurpassConfig.class);

    //authenticationRealm 
    @Bean
    JdbcAuthenticationRealm authenticationRealm(
            @Qualifier("passwordEncoder") PasswordEncoder passwordEncoder,
            PasswordPolicyValidatorService passwordPolicyValidator,
            LoginService loginService) {

        JdbcAuthenticationRealm authenticationRealm = new JdbcAuthenticationRealm(
        		passwordEncoder,
        		passwordPolicyValidator,
        		loginService);

        logger.debug("JdbcAuthenticationRealm inited.");
        return authenticationRealm;
    }

}
