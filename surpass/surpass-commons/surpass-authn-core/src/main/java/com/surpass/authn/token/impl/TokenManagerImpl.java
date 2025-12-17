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





package com.surpass.authn.token.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.surpass.authn.token.AccessToken;
import com.surpass.authn.token.TokenManager;
import com.surpass.constants.ConstsCached;
import com.surpass.crypto.password.PasswordReciprocal;
import com.surpass.persistence.redis.connection.RedisConnectionFactory;
import com.surpass.web.WebContext;

/**
 * TokenManager/令牌管理实现
 * persistence 0 in memory,store in Caffeine/存储内存中，采用Caffeine实现
 * persistence 2 in Redis 缓存存储Redis中
 * user Token status in database/用户会话状态存储在数据库中
 *
 * @author Crystal.Sea
 *
 */
public class TokenManagerImpl implements TokenManager{
	private static final  Logger logger = LoggerFactory.getLogger(TokenManagerImpl.class);

	InMemoryTokenManager 	inMemoryTokenManager;

	RedisTokenManager 	redisTokenManager;

	boolean isRedis = false;

	public TokenManagerImpl(
				int persistence,
				RedisConnectionFactory redisConnFactory
	            ) {
		 if (persistence == ConstsCached.REDIS) {
			isRedis = true;
			this.redisTokenManager = new RedisTokenManager(redisConnFactory);
			logger.debug("RedisTokenManager");
		 }else {
			 this.inMemoryTokenManager = new InMemoryTokenManager();
			 logger.debug("InMemoryTokenManager");
		 }
	}

	@Override
	public AccessToken generate(String clientId) {
		AccessToken accessToken = new AccessToken();
		accessToken.setClientId(clientId);
		String accessTokenId = WebContext.genId();
		String refreshTokenId =  WebContext.genId();
		accessToken.setAccessToken(accessTokenId);
		accessToken.setRefreshToken(refreshTokenId);
		create(accessToken);
		AccessToken token = new AccessToken();
		token.setClientId(clientId);
		token.setAccessToken(PasswordReciprocal.getInstance().encode(accessTokenId));
		token.setRefreshToken(PasswordReciprocal.getInstance().encode(refreshTokenId));
		return token;
	}
	
	/**
	 * 创建
	 */
	public void create(AccessToken accessToken) {
		if(isRedis) {
			redisTokenManager.create(accessToken);
		}else {
			inMemoryTokenManager.create(accessToken);
		}
	}

	/**
	 * 删除
	 */
	public AccessToken remove(String token) {
		AccessToken accessToken = null;
		if(isRedis) {
			accessToken = redisTokenManager.remove(token);
		}else {
			accessToken = inMemoryTokenManager.remove(token);
		}
		return accessToken;
	}

	/**
	 * 读取，先读取本地缓存再读取Redis
	 */
	public AccessToken get(String token) {
		AccessToken accessToken = null;
		if(isRedis) {
			accessToken = redisTokenManager.get(token);
		}else {
			accessToken = inMemoryTokenManager.get(token);
		}
		return accessToken;
	}

	@Override
	public AccessToken getRefreshToken(String refreshToken) {
		AccessToken accessToken = null;
		if(isRedis) {
			accessToken = redisTokenManager.getRefreshToken(refreshToken);
		}else {
			accessToken = inMemoryTokenManager.getRefreshToken(refreshToken);
		}
		return accessToken;
	}
	
	/**
	 * 根据id刷新，先刷新Redis，再刷新本地缓存
	 */
	public AccessToken refresh(String refreshToken) {
		AccessToken refresh = getRefreshToken(refreshToken);
		if(refresh != null) {
			AccessToken accessToken = generate(refresh.getClientId());
			remove(refreshToken);
			return accessToken;
		}
		return null;
	}

	@Override
	public boolean isRedis() {
		return isRedis;
	}

	@Override
	public List<AccessToken> tokenList() {
		// TODO Auto-generated method stub
		return null;
	}

}
