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



package org.dromara.surpass.persistence.cache.diplex;

import org.dromara.surpass.persistence.redis.connection.RedisConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DiplexCacheManager  implements DiplexCache{

    private static final Logger logger = LoggerFactory.getLogger(DiplexCacheManager.class);

    InMemoryDiplexCache inMemoryDiplexCache;

    RedisDiplexCache redisDiplexCache;

    boolean redisCache;

    protected int validitySeconds 	= 60 * 30; //default 30 minutes.

	/**
	 * Just in Memory Cache
	 */
	public DiplexCacheManager() {
		this.redisCache = false;
		inMemoryDiplexCache = new InMemoryDiplexCache();
	}

	/**
	 * Just in Memory Cache  with validity Seconds
	 */
	public DiplexCacheManager(int validitySeconds) {
		this.redisCache = false;
		inMemoryDiplexCache = new InMemoryDiplexCache(validitySeconds);
	}

	/**
	 * Diplex Cache with default validity Seconds
	 * @param connectionFactory
	 */
	public DiplexCacheManager(RedisConnectionFactory connectionFactory) {
		this.redisCache = true;
		this.inMemoryDiplexCache = new InMemoryDiplexCache();
		this.redisDiplexCache = new RedisDiplexCache(connectionFactory,validitySeconds);

	}

	/**
	 * Diplex Cache with validity Seconds
	 * @param connectionFactory
	 */
	public DiplexCacheManager(RedisConnectionFactory connectionFactory,int validitySeconds) {
		this.redisCache = true;
		this.inMemoryDiplexCache = new InMemoryDiplexCache(validitySeconds);
		this.redisDiplexCache = new RedisDiplexCache(connectionFactory,validitySeconds);
	}

	@Override
	public void putObject(String key, Object value) {
		if(redisCache) {
			redisDiplexCache.putObject(key, key);
		}
		inMemoryDiplexCache.putObject(key, key);
	}

	@Override
	public Object getObject(String key) {
		if(key == null) {
    		logger.debug("key can't been null .");
    		return null;
    	}
		Object value  = inMemoryDiplexCache.getObject(key);
		if(redisCache && value == null) {
			value = redisDiplexCache.getObject(key);
			//TODO:Dirty Read
			if( value != null) {
				inMemoryDiplexCache.putObject(key, value);
			}
		}
		return value;
	}

	@Override
	public Object removeObject(String key) {
		Object value  = inMemoryDiplexCache.removeObject(key);
		if(redisCache ) {
			value = redisDiplexCache.removeObject(key);
		}
		return value;
	}

	@Override
	public void put(String key, String value) {
		if(redisCache) {
			redisDiplexCache.put(key, key);
		}
		inMemoryDiplexCache.put(key, key);
	}

	@Override
	public String get(String key) {
		if(key == null) {
    		logger.debug("key can't been null .");
    		return null;
    	}
		String value  = inMemoryDiplexCache.get(key);
		if(redisCache && value == null) {
			value = redisDiplexCache.get(key);
			//TODO:Dirty Read
			if( value != null) {
				inMemoryDiplexCache.put(key, value);
			}
		}
		return value;
	}

	@Override
	public String remove(String key) {
		String value  = inMemoryDiplexCache.get(key);
		inMemoryDiplexCache.remove(key);
		if(redisCache) {
			value = redisDiplexCache.get(key);
			redisDiplexCache.remove(key);
		}
		return value;
	}

}
