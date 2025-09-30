/*
 * Copyright [2025] [JinBooks of copyright http://www.jinbooks.com]
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




package org.dromara.surpass.authn.secretkey.impl;

import org.apache.commons.lang3.StringUtils;
import org.dromara.surpass.authn.LoginCredential;
import org.dromara.surpass.authn.LoginSecretKey;
import org.dromara.surpass.authn.secretkey.SecretKeyManager;
import org.dromara.surpass.authn.secretkey.SecretKeyProvider;
import org.dromara.surpass.exception.BusinessException;
import org.dromara.surpass.persistence.redis.IRedisStatement;
import org.dromara.surpass.persistence.redis.connection.RedisConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisSecretKeyManager  implements SecretKeyManager {
	private static final Logger logger = LoggerFactory.getLogger(RedisSecretKeyManager.class);

	//旧key缓存24小时后过期
	public static final  int CACHED_DELAY_EXPIRES = 24;

	public static final  int REDIS_CACHED_DELAY_EXPIRES = CACHED_DELAY_EXPIRES * 3600;

	//实际key有效时间48小时
	public static final  int CACHED_EXPIRES = CACHED_DELAY_EXPIRES  * 2;

	public static final  int REDIS_CACHED_EXPIRES = CACHED_EXPIRES  * 2 * 3600;

	SecretKeyProvider secretKeyProvider;

	RedisConnectionFactory connectionFactory;

	public RedisSecretKeyManager(RedisConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
		secretKeyProvider = new SecretKeyProvider(SecretKeyProvider.AlgorithmType.SM2,CACHED_EXPIRES);
	}

	@Override
	public LoginSecretKey generate() {
		IRedisStatement stmt = connectionFactory.createStatement();
		try {
			LoginSecretKey loginSecretKey = stmt.getObject(CACHED_KEY_NAME);
			if(loginSecretKey != null) {
				//旧key设置过期时间
				stmt.setexObject(CACHED_KEY_LAST_NAME.formatted(loginSecretKey.getSecretKey()),REDIS_CACHED_EXPIRES,loginSecretKey);
			}
			loginSecretKey =  secretKeyProvider.generator();
			logger.info("login SecretKey {}",loginSecretKey);
			//取默认数据
			stmt.setexObject(CACHED_KEY_NAME, REDIS_CACHED_DELAY_EXPIRES ,loginSecretKey);
			stmt.setexObject(CACHED_KEY_LAST_NAME.formatted(loginSecretKey.getSecretKey()),REDIS_CACHED_EXPIRES , loginSecretKey);
			return loginSecretKey;
    	 } catch (Exception e) {
    		 logger.error("Exception login SecretKey",e);
         }finally {
        	 stmt.close();
         }
		return null;
	}

	@Override
	public  LoginSecretKey getSecretKey() {
		IRedisStatement conn = connectionFactory.createStatement();
		LoginSecretKey loginSecretKey = conn.getObject(CACHED_KEY_NAME);
		conn.close();
		if(loginSecretKey == null) {
			loginSecretKey = generate();
		}
		return loginSecretKey;
	}

	@Override
	public  String decrypt(String secretKey,String cipherText ) throws Exception {
		IRedisStatement stmt = connectionFactory.createStatement();
		LoginSecretKey loginSecretKey = stmt.getObject(CACHED_KEY_LAST_NAME.formatted(secretKey));
		stmt.close();
		if(loginSecretKey == null) {
			throw new BusinessException(12112,"SecretKey not Present");
		}
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
