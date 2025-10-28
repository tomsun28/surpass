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






package com.surpass.persistence.cache.diplex;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;


public class InMemoryDiplexCache implements DiplexCache{
    private static final Logger logger = LoggerFactory.getLogger(InMemoryDiplexCache.class);

    static final 	long 	CACHE_MAXIMUM_SIZE 	= 2000000;
    protected int validitySeconds 				= 60 * 30; //default 30 minutes.

	protected  static  Cache<String, Object> diplexCacheStore =
        	        Caffeine.newBuilder()
        	            .expireAfterWrite(10, TimeUnit.MINUTES)
        	            .maximumSize(CACHE_MAXIMUM_SIZE)
        	            .build();

	public InMemoryDiplexCache() {
		super();
	}

	public InMemoryDiplexCache(int validitySeconds) {
        super();
        this.validitySeconds = validitySeconds;
        diplexCacheStore =
                Caffeine.newBuilder()
                    .expireAfterWrite(validitySeconds, TimeUnit.SECONDS)
                    .maximumSize(CACHE_MAXIMUM_SIZE)
                    .build();

    }

	@Override
	public Object removeObject(String key) {
	    Object value = diplexCacheStore.getIfPresent(key);
	    diplexCacheStore.invalidate(key);
		return value;
	}

	@Override
    public Object getObject(String key) {
    	if(key == null) {
    		logger.debug("key can't been null .");
    		return null;
    	}
        return diplexCacheStore.getIfPresent(key);
    }

	@Override
	public void putObject(String key, Object value) {
		diplexCacheStore.put(key, value);

	}

	@Override
	public void put(String key, String value) {
		diplexCacheStore.put(key, value);

	}

	@Override
	public String get(String key) {
		if(key == null) {
			logger.debug("key can't been null .");
    		return null;
    	}
		return (String)diplexCacheStore.getIfPresent(key);
	}

	@Override
	public String remove(String key) {
		String value = (String)diplexCacheStore.getIfPresent(key);
		diplexCacheStore.invalidate(key);
		return value;
	}

	public void setValiditySeconds(int validitySeconds) {
		this.validitySeconds = validitySeconds;
	}

}
