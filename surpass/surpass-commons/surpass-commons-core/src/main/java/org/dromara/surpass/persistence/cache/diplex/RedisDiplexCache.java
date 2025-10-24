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






package org.dromara.surpass.persistence.cache.diplex;


import org.dromara.surpass.persistence.redis.IRedisStatement;
import org.dromara.surpass.persistence.redis.connection.RedisConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RedisDiplexCache implements DiplexCache{
    private static final Logger logger = LoggerFactory.getLogger(RedisDiplexCache.class);

    protected int validitySeconds 	= 60 * 30; //default 30 minutes.

	RedisConnectionFactory connectionFactory;

	public static final String PREFIX = "MXK_DIPLEX_";

	public String getKey(String key) {
		return PREFIX + key;
	}

	/**
	 * @param connectionFactory
	 */
	public RedisDiplexCache(
			RedisConnectionFactory connectionFactory,
			int validitySeconds) {
		super();
		this.connectionFactory = connectionFactory;
		this.validitySeconds = validitySeconds;
	}

	/**
	 *
	 */
	public RedisDiplexCache() {

	}

	public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}


	@Override
	public void putObject(String key, Object value) {
		IRedisStatement stmt = connectionFactory.createStatement();
		stmt.setexObject( getKey(key), validitySeconds, value);
		stmt.close();
	}

    @Override
    public Object getObject(String key) {
    	if(key == null) {
    		logger.debug("key can't been null .");
    		return null;
    	}
    	IRedisStatement stmt=connectionFactory.createStatement();
    	Object value = stmt.getObject(getKey(key));
    	stmt.close();
        return value;
    }

	@Override
	public Object removeObject(String key) {
		IRedisStatement stmt=connectionFactory.createStatement();
		Object value = stmt.getObject(getKey(key));
		stmt.del(getKey(key));
		stmt.close();
		return value;
	}

	@Override
	public void put(String key, String value) {
		IRedisStatement stmt = connectionFactory.createStatement();
		stmt.setex( getKey(key), validitySeconds, value);
		stmt.close();

	}

	@Override
	public String get(String key) {
		if(key == null) {
    		logger.debug("key can't been null .");
    		return null;
    	}
		IRedisStatement stmt=connectionFactory.createStatement();
    	String value = stmt.get(getKey(key));
    	stmt.close();
        return value;
	}

	@Override
	public String remove(String key) {
		IRedisStatement stmt=connectionFactory.createStatement();
		String value = stmt.get(getKey(key));
		stmt.del(getKey(key));
		stmt.close();
		return value;
	}

	public void setValiditySeconds(int validitySeconds) {
		this.validitySeconds = validitySeconds;
	}


}
