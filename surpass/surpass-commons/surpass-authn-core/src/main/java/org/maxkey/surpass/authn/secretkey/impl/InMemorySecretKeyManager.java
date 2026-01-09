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




package org.maxkey.surpass.authn.secretkey.impl;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.maxkey.surpass.authn.LoginCredential;
import org.maxkey.surpass.authn.LoginSecretKey;
import org.maxkey.surpass.authn.secretkey.SecretKeyManager;
import org.maxkey.surpass.authn.secretkey.SecretKeyProvider;
import org.maxkey.surpass.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class InMemorySecretKeyManager implements SecretKeyManager{
	private static final Logger logger = LoggerFactory.getLogger(InMemorySecretKeyManager.class);

	static final 	long 	 CACHE_MAXIMUM_SIZE 		= 2000000;//200W
	//旧key缓存24小时后过期
	public static final  int CACHED_DELAY_EXPIRES = 24;

	SecretKeyProvider secretKeyProvider;

	//实际key有效时间48小时
	public static final  int CACHED_EXPIRES = CACHED_DELAY_EXPIRES + 24;

	static Cache<String, LoginSecretKey> secretKeyStore= Caffeine.newBuilder()
										        	.expireAfterWrite(CACHED_EXPIRES, TimeUnit.HOURS)
										        	.maximumSize(CACHE_MAXIMUM_SIZE)
										        	.build();

	public InMemorySecretKeyManager() {
		secretKeyProvider = new SecretKeyProvider(SecretKeyProvider.AlgorithmType.SM2,CACHED_EXPIRES);
	}

	@Override
	public  LoginSecretKey generate() {
		try {
			LoginSecretKey loginSecretKey = secretKeyStore.getIfPresent(CACHED_KEY_NAME);
			if(loginSecretKey != null) {
				//旧key设置过期时间
				secretKeyStore.invalidate(CACHED_KEY_LAST_NAME.formatted(loginSecretKey.getSecretKey()));
			}
			loginSecretKey =  secretKeyProvider.generator();
			logger.info("login SecretKey {}",loginSecretKey);
			//取默认数据
			secretKeyStore.put(CACHED_KEY_NAME, loginSecretKey);
			secretKeyStore.put(CACHED_KEY_LAST_NAME.formatted(loginSecretKey.getSecretKey()), loginSecretKey);
			return loginSecretKey;
    	 } catch (Exception e) {
    		 logger.error("Exception login SecretKey",e);
         }
		return null;
	}

	@Override
	public  LoginSecretKey getSecretKey() {
		LoginSecretKey loginSecretKey = secretKeyStore.getIfPresent(CACHED_KEY_NAME);
		if(loginSecretKey == null) {
			loginSecretKey = generate();
		}
		return loginSecretKey;
	}

	@Override
	public  String decrypt(String secretKey,String cipherText ) throws Exception {
		LoginSecretKey loginSecretKey = secretKeyStore.getIfPresent(CACHED_KEY_LAST_NAME.formatted(secretKey));
		if(loginSecretKey == null) {
			throw new BusinessException(12112,"SecretKey not Present");
		}
		logger.trace("loginSecretKey {}",loginSecretKey);
		String text = null;
		//使用私钥解密
		byte[] decrypt = secretKeyProvider.decrypt(cipherText, loginSecretKey.getPrivateKey());
		if(decrypt != null) {
			text = new String (decrypt);
		}
		return text;
	}

	@Override
	public void decrypt(LoginCredential credential) {
		if(StringUtils.isNotBlank(credential.getSecretKey())) {
    		try {
				String password = decrypt(credential.getSecretKey(), credential.getPassword());
				credential.setPassword(password);
			} catch (Exception e) {
				logger.error("decrypt Exception", e);
			}
    	}
	}
}
