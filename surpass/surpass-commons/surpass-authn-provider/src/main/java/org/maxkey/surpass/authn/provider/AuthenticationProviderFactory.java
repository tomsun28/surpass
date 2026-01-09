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




package org.maxkey.surpass.authn.provider;

import java.util.concurrent.ConcurrentHashMap;

import org.maxkey.surpass.authn.LoginCredential;
import org.springframework.security.core.Authentication;

/**
 * 认证提供者工厂
 *
 * @author Crystal.Sea
 *
 */
public class AuthenticationProviderFactory extends AbstractAuthenticationProvider {

    static ConcurrentHashMap<String,AbstractAuthenticationProvider> providers = new ConcurrentHashMap<>();

    /**
     * 登录传入类型AuthType，读取认证提供者，进行登录认证
     */
    @Override
    public Authentication authenticate(LoginCredential credential){
    	if(credential.getAuthType().equalsIgnoreCase("trusted")) {
    		//risk remove 前端不能使用trusted认证提供者登录
    		return null;
    	}
    	AbstractAuthenticationProvider provider = providers.get(credential.getAuthType() + PROVIDER_SUFFIX);

    	return provider == null ? null : provider.doAuthenticate(credential);
    }

    /**
     * trusted 认证提供者
     */
    @Override
    public Authentication authenticate(LoginCredential credential,boolean trusted){
    	AbstractAuthenticationProvider provider = providers.get(AuthType.TRUSTED + PROVIDER_SUFFIX);
    	return provider.doAuthenticate(credential);
    }

    /**
     * 增加认证提供者
     * @param provider
     */
    public void addAuthenticationProvider(AbstractAuthenticationProvider provider) {
    	if(provider != null && provider.isSupported()) {
    		providers.put(provider.getProviderName(), provider);
    	}
    }

	@Override
	public String getProviderName() {
		return "AuthenticationProviderFactory";
	}

	@Override
	public Authentication doAuthenticate(LoginCredential credential) {
		//AuthenticationProvider Factory do nothing
		return null;
	}
}
