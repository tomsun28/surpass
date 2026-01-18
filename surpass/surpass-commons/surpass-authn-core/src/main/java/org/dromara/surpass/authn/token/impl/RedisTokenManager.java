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

import org.dromara.surpass.authn.token.AccessToken;
import org.dromara.surpass.authn.token.TokenManager;
import org.dromara.surpass.persistence.redis.IRedisStatement;
import org.dromara.surpass.persistence.redis.connection.RedisConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 会话管理 Redis存储
 *
 * @author Crystal.Sea
 *
 */
public class RedisTokenManager implements TokenManager {
    private static final Logger logger = LoggerFactory.getLogger(RedisTokenManager.class);

	public static final String PREFIX_TOKEN = "mxk:token:%s";
	
	public static final String PREFIX_REFRESH_TOKEN = "mxk:token:refresh:%s";
	
    int tokenValidity 			= 60 * 60 * 2; //default 7200.
    
    int refreshTokenValidity 	= 60 * 60 * 12;

	RedisConnectionFactory connectionFactory;



	public String formatKey(String token) {
		return PREFIX_TOKEN.formatted(token) ;
	}
	
	public String formatRefreshKey(String token) {
		return PREFIX_REFRESH_TOKEN.formatted(token) ;
	}

	/**
	 * @param connectionFactory
	 */
	public RedisTokenManager(
			RedisConnectionFactory connectionFactory) {
		super();
		this.connectionFactory = connectionFactory;
	}

	/**
	 *
	 */
	public RedisTokenManager() {

	}

	public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	@Override
	public AccessToken generate(String clientId) {
		return null;
	}
	
	@Override
	public void create( AccessToken accessToken) {
		IRedisStatement stmt = connectionFactory.createStatement();
		stmt.setexObject(formatKey(accessToken.getAccessToken()), tokenValidity, accessToken);
		stmt.setexObject(formatRefreshKey(accessToken.getRefreshToken()), refreshTokenValidity, accessToken);
		stmt.close();
	}

	@Override
	public AccessToken remove(String token) {
		IRedisStatement stmt = connectionFactory.createStatement();
		//清除token
		AccessToken accessToken = stmt.getObject(formatKey(token));
		if(accessToken != null) {
			stmt.del(formatKey(token));
			stmt.del(formatRefreshKey(accessToken.getRefreshToken()));
			logger.trace("remove Token {}",token);
		}
		//清除refreshToken
		AccessToken refreshToken = stmt.getObject(formatRefreshKey(token));
		if(refreshToken != null  ) {
			stmt.del(formatKey(refreshToken.getAccessToken()));
			stmt.del(formatRefreshKey(refreshToken.getRefreshToken()));
			logger.trace("remove refreshToken {}",token);
		}
		stmt.close();
		return accessToken;
	}

    @Override
    public AccessToken get(String token) {
    	IRedisStatement stmt = connectionFactory.createStatement();
    	AccessToken accessToken = stmt.getObject(formatKey(token));
        stmt.close();
        return accessToken;
    }

	@Override
	public AccessToken getRefreshToken(String refreshToken) {
		IRedisStatement stmt=connectionFactory.createStatement();
    	AccessToken accessToken = stmt.getObject(formatRefreshKey(refreshToken));
        stmt.close();
        return accessToken;
	}

    @Override
    public AccessToken refresh(String refreshToken) {
        return null;
    }

	@Override
	public boolean isRedis() {
		return true;
	}

	@Override
	public List<AccessToken> tokenList() {
		// TODO Auto-generated method stub
		return null;
	}

}
