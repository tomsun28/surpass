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





package com.surpass.persistence.redis.connection;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.surpass.persistence.redis.IRedisConnection;
import com.surpass.persistence.redis.IRedisStatement;
import com.surpass.persistence.redis.RedisConfig;
import com.surpass.persistence.redis.RedisConfigConsts;

public class RedisConnectionFactory  implements IRedisConnection{
	private static final  Logger logger = LoggerFactory.getLogger(RedisConnectionFactory.class);

	RedisConfig config;

	IRedisConnection 		connection 		= null;

    public RedisConnectionFactory(RedisConfig config) {
    	if (config.getPort() <= 0) {
    		config.setPort(RedisConfigConsts.DEFAULT_PORT);
        }
        if (config.getTimeOut() <= 0 ) {
        	config.setTimeOut(RedisConfigConsts.DEFAULT_TIMEOUT);
        }
        if(StringUtils.isBlank(config.getPassword())) {
        	config.setPassword(null);
        }
        if (StringUtils.isBlank(config.getHostName())) {
	 			config.setHostName(RedisConfigConsts.DEFAULT_ADDRESS);
	 	}
        logger.debug("RedisConfig {}",config);
        logger.debug("init redis AUTH {}",config.getPassword());
        this.config = config;
    }

    @Override
    public void init() {
    	if(config.getNodes() == null || config.getNodes().size() < 3) {
    		logger.debug("init redis Connection.");
    		connection = new RedisPoolConnection(config);
    	}else {
    		logger.debug("init redis Cluster Connection.");
    		connection = new RedisClusterConnection(config);
    	}
    	connection.init();
    }

	@Override
	public void open() {
		connection.open();
	}

    @Override
    public synchronized IRedisStatement createStatement() {
    	init();
    	open();
        return connection.createStatement();
    }

}
