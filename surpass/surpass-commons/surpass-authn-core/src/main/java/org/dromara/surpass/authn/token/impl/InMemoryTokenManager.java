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






package org.dromara.surpass.authn.token.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.dromara.surpass.authn.token.AccessToken;
import org.dromara.surpass.authn.token.TokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * 令牌管理 内存存储
 *
 * @author Crystal.Sea
 *
 */
public class InMemoryTokenManager implements TokenManager{
    private static final Logger logger = LoggerFactory.getLogger(InMemoryTokenManager.class);

    static final 	long 	CACHE_MAXIMUM_SIZE 		= 2000000;//200W

	Cache<String, AccessToken> accessTokenStore;

	Cache<String, AccessToken> refreshTokenStore;

	public InMemoryTokenManager() {
        super();
        accessTokenStore = Caffeine.newBuilder()
                    	.expireAfterWrite(2, TimeUnit.HOURS)
                    	.maximumSize(CACHE_MAXIMUM_SIZE)
                    	.build();

        refreshTokenStore = Caffeine.newBuilder()
		            	.expireAfterWrite(12, TimeUnit.HOURS)
		            	.maximumSize(CACHE_MAXIMUM_SIZE)
		            	.build();

    }
	
	@Override
	public AccessToken generate(String clientId) {
		return null;
	}
	
    @Override
	public void create(AccessToken accessToken) {
    	accessTokenStore.put(accessToken.getAccessToken(), accessToken);
    	refreshTokenStore.put(accessToken.getRefreshToken(), accessToken);
	}

	@Override
	public AccessToken remove(String token) {
		AccessToken accessToken = accessTokenStore.getIfPresent(token);
		if(accessToken != null) {
			accessTokenStore.invalidate(token);
			refreshTokenStore.invalidate(accessToken.getRefreshToken());
			logger.trace("remove Token {}",token);
		}
		//清除refreshToken
		AccessToken refreshToken = refreshTokenStore.getIfPresent(token);
		if(refreshToken != null  ) {
			accessTokenStore.invalidate(refreshToken.getAccessToken());
			refreshTokenStore.invalidate(refreshToken.getRefreshToken());
			logger.trace("remove refreshToken {}",token);
		}
		
		return accessToken;
	}

    @Override
    public AccessToken get(String token) {
    	return accessTokenStore.getIfPresent(token);
    }

	@Override
	public AccessToken getRefreshToken(String refreshToken) {
		return refreshTokenStore.getIfPresent(refreshToken);
	}

    @Override
    public AccessToken refresh(String refreshToken) {
        return null;
    }

	@Override
	public boolean isRedis() {
		return false;
	}

	@Override
	public List<AccessToken> tokenList() {
		// TODO Auto-generated method stub
		return null;
	}

}
