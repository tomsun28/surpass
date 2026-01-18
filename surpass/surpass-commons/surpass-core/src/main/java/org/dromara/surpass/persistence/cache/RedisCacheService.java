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
 





package org.dromara.surpass.persistence.cache;

import org.dromara.surpass.persistence.redis.IRedisStatement;
import org.dromara.surpass.persistence.redis.connection.RedisConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RedisCacheService implements MemCacheService {
    private static final Logger logger = LoggerFactory.getLogger(RedisCacheService.class);

	protected int validitySeconds 	= 60 * 5; //default 5 minutes.

	public static final String PREFIX 	= "mxk:momentary:%s:%s";

	RedisConnectionFactory connectionFactory;

	/**
	 * @param connectionFactory
	 */
	public RedisCacheService(
			RedisConnectionFactory connectionFactory) {
		super();
		this.connectionFactory = connectionFactory;
	}

	/**
	 *
	 */
	public RedisCacheService() {

	}

	public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	@Override
	public  void put(String sessionId , String name, Object value){
		put(formatKey(sessionId , name), value);
	}

    @Override
    public Object get(String sessionId , String name) {
    	return get(formatKey(sessionId , name));
    }

	@Override
	public Object remove(String sessionId, String name) {
		return remove(formatKey(sessionId , name));
	}

    private String formatKey(String sessionId , String name) {
    	return PREFIX.formatted(sessionId,name);
    }

	@Override
	public void put(String key, Object value) {
		IRedisStatement stmt = connectionFactory.createStatement();
		stmt.setexObject(key, validitySeconds, value);
		logger.trace("key {}, validitySeconds {}, value {}",key,validitySeconds,value);
		stmt.close();
	}

	@Override
	public Object get(String key) {
		IRedisStatement stmt = connectionFactory.createStatement();
        Object value = stmt.getObject(key);
        logger.trace("key {}, value {}",key,value);
        stmt.close();
        return value;
	}

	@Override
	public Object remove(String key) {
		IRedisStatement stmt = connectionFactory.createStatement();
        Object value = stmt.getObject(key);
        stmt.del(key);
        stmt.close();
        logger.trace("key {}, value {}",key,value);
        return value;
	}

}
