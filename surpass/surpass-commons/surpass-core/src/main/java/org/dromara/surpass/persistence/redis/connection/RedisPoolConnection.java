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






package org.dromara.surpass.persistence.redis.connection;

import java.time.Duration;
import org.apache.commons.lang3.StringUtils;
import org.dromara.surpass.persistence.redis.IRedisConnection;
import org.dromara.surpass.persistence.redis.IRedisStatement;
import org.dromara.surpass.persistence.redis.RedisConfig;
import org.dromara.surpass.persistence.redis.statement.RedisPoolStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPoolConnection  implements IRedisConnection {
	private static final  Logger logger = LoggerFactory.getLogger(RedisPoolConnection.class);

	RedisConfig config;
    JedisPool 		jedisPool 		= null;
    JedisPoolConfig poolConfig 		= null;

    public RedisPoolConnection(RedisConfig config) {
        this.config = config;
    }

    @Override
    public void init() {
    	if(poolConfig == null) {
	    	poolConfig = new JedisPoolConfig();
	   	 	poolConfig.setMaxIdle(config.getMaxIdle());
	   	 	poolConfig.setMinIdle(config.getMinIdle());
	   	 	poolConfig.setMaxTotal(config.getMaxActive());
	   	 	poolConfig.setMaxWait(Duration.ofMillis(config.getMaxWait()));
    	}
    }

	@Override
	public void open() {
    	if (jedisPool == null) {
    		logger.debug("redis host {} : {} ." , config.getHostName() , config.getPort());
   	 		if(StringUtils.isBlank(config.getPassword())) {
   	 			logger.debug("init redis Connection no password");
   	 			jedisPool = new JedisPool(poolConfig, config.getHostName() , config.getPort(), config.getTimeOut());
   	 		}else {
   	 			logger.debug("init redis Connection with password");
   	 			jedisPool = new JedisPool(poolConfig, config.getHostName() , config.getPort(), config.getTimeOut(), config.getPassword());
   	 		}
       }
	}

    @Override
    public IRedisStatement createStatement() {
        return new RedisPoolStatement(jedisPool);
    }

}
