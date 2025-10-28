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






package com.surpass.authn.provider.impl;

import com.surpass.ip2location.IpLocationParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import com.surpass.authn.LoginCredential;
import com.surpass.authn.provider.AbstractAuthenticationProvider;
import com.surpass.authn.realm.AbstractAuthenticationRealm;
import com.surpass.authn.session.SessionManager;
import com.surpass.entity.client.ClientResolve;
import com.surpass.entity.idm.UserInfo;
import com.surpass.web.WebContext;

/**
 * Trusted Authentication provider.信任登录提供者
 *
 * @author Crystal.Sea
 *
 */
public class TrustedAuthenticationProvider extends AbstractAuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(TrustedAuthenticationProvider.class);

    public String getProviderName() {
        return "trusted" + PROVIDER_SUFFIX;
    }

    public TrustedAuthenticationProvider(
    		AbstractAuthenticationRealm authenticationRealm,
    	    SessionManager sessionManager,
    	    IpLocationParser ipLocationParser) {
		this.authenticationRealm = authenticationRealm;
		this.sessionManager = sessionManager;
		this.ipLocationParser = ipLocationParser;
	}

    @Override
	public Authentication doAuthenticate(LoginCredential loginCredential) {
    	//获取登录终端信息
        ClientResolve client = parserClientResolve();
        UserInfo loadeduserInfo = loadUserInfo(loginCredential.getUsername(), "");
        statusValid(loginCredential , loadeduserInfo,client);
        if (loadeduserInfo != null) {
            //Validate PasswordPolicy
        	authenticationRealm.applyLoginPolicy(loadeduserInfo);
            //apply PasswordSetType and resetBadPasswordCount
            Authentication authentication = createOnlineTicket(loginCredential,loadeduserInfo,client);

            authenticationRealm.insertLoginHistory( loadeduserInfo,
            										client,
                                                    loginCredential.getAuthType(),
                                                    loginCredential.getProvider(),
                                                    loginCredential.getCode(),
                                                    loginCredential.getMessage()
                                                );

            return authentication;
        }else {
            String i18nMessage = WebContext.getI18nValue("login.error.username");
            logger.debug("login user {} not in this System . {}" ,
                            loginCredential.getUsername(),i18nMessage);
            throw new BadCredentialsException(WebContext.getI18nValue("login.error.username"));
        }
    }

}
