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

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;


public class InMemoryCacheService implements MemCacheService{
    private static final Logger logger = LoggerFactory.getLogger(InMemoryCacheService.class);

    static final 	long 	CACHE_MAXIMUM_SIZE 	= 2000000;

	static  Cache<String, Object> momentaryStore =
        	        Caffeine.newBuilder()
        	            .expireAfterWrite(5, TimeUnit.MINUTES)
        	            .maximumSize(CACHE_MAXIMUM_SIZE)
        	            .build();

	public InMemoryCacheService() {
        super();
    }

    @Override
    public  void put(String sessionId , String name, Object value){
    	put(getSessionKey(sessionId,name), value);
	}

	@Override
	public Object remove(String sessionId , String name) {
		return remove(getSessionKey(sessionId,name));
	}

    @Override
    public Object get(String sessionId , String name) {
    	return get(getSessionKey(sessionId,name));
    }


    private String getSessionKey(String sessionId , String name) {
    	return sessionId + "_" + name;
    }

	@Override
	public void put(String key, Object value) {
		momentaryStore.put(key, value);
	}

	@Override
	public Object get(String key) {
		 logger.trace("key {}" , key);
		return momentaryStore.getIfPresent(key);
	}

	@Override
	public Object remove(String key) {
		Object value = momentaryStore.getIfPresent(key);
		momentaryStore.invalidate(key);
		logger.trace("key {}, value {}",key , value);
		return value;
	}
}
