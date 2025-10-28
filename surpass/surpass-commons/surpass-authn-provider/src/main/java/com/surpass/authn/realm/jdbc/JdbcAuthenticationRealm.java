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






package com.surpass.authn.realm.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.surpass.authn.realm.AbstractAuthenticationRealm;
import com.surpass.entity.idm.UserInfo;
import com.surpass.persistence.service.LoginService;
import com.surpass.persistence.service.PasswordPolicyValidatorService;

/**
 * JdbcAuthenticationRealm.数据认证域
 *
 * @author Crystal.Sea
 *
 */
public class JdbcAuthenticationRealm extends AbstractAuthenticationRealm {
    private static final Logger logger = LoggerFactory.getLogger(JdbcAuthenticationRealm.class);

    protected PasswordEncoder passwordEncoder;

    public JdbcAuthenticationRealm() {
        logger.debug("init . ");
    }


    public JdbcAuthenticationRealm(
    		PasswordEncoder passwordEncoder,
    		PasswordPolicyValidatorService passwordPolicyValidator,
    		LoginService loginService) {

    	this.passwordEncoder = passwordEncoder;
    	this.passwordPolicyValidator = passwordPolicyValidator;
    	this.loginService = loginService;
    }


    /**
     * passwordMatches.
     */
    public boolean passwordMatches(UserInfo userInfo, String password) {
        boolean passwordMatches = false;
        //jdbc password check
        passwordMatches = passwordEncoder.matches(password,userInfo.getPassword());

        logger.debug("passwordvalid : {}" , passwordMatches);

        return passwordMatches;
    }

}
