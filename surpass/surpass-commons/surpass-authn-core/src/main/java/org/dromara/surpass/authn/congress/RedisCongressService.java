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






package org.dromara.surpass.authn.congress;


import org.dromara.surpass.authn.jwt.AuthJwt;
import org.dromara.surpass.persistence.redis.IRedisStatement;
import org.dromara.surpass.persistence.redis.connection.RedisConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * congress Redis缓存服务
 *
 * @author Crystal.Sea
 *
 */
public class RedisCongressService implements CongressService {
    private static final Logger logger = LoggerFactory.getLogger(RedisCongressService.class);

    /**
     * 默认有效时间3分钟
     */
	protected int validitySeconds = 60 * 3; //default 3 minutes.

	RedisConnectionFactory connectionFactory;

	public static final String PREFIX = "mxk:congress:%s";

	/**
	 * @param connectionFactory
	 */
	public RedisCongressService(
			RedisConnectionFactory connectionFactory) {
		super();
		this.connectionFactory = connectionFactory;
	}

	/**
	 *
	 */
	public RedisCongressService() {

	}

	public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	/**
	 * 存储
	 */
	@Override
	public void store(String congress, AuthJwt authJwt) {
		IRedisStatement stmt = connectionFactory.createStatement();
		stmt.setexObject(formatKey(congress), validitySeconds, authJwt);
		stmt.close();
		logger.debug("store congress {} , {}",congress,authJwt);
	}

	/**
	 * 删除
	 */
	@Override
	public AuthJwt remove(String congress) {
		IRedisStatement stmt=connectionFactory.createStatement();
		AuthJwt authJwt = stmt.getObject(formatKey(congress));
		stmt.del(formatKey(congress));
		stmt.close();
		logger.debug("remove {}",congress);
		return authJwt;
	}

	/**
	 * 读取
	 */
    @Override
    public AuthJwt get(String congress) {
    	IRedisStatement stmt = connectionFactory.createStatement();
        AuthJwt authJwt = stmt.getObject(formatKey(congress));
        stmt.close();
        return authJwt;
    }

    /**
     * 读取
     */
	@Override
	public AuthJwt consume(String congress) {
		IRedisStatement stmt=connectionFactory.createStatement();
		AuthJwt authJwt = stmt.getObject(formatKey(congress));
		stmt.del(formatKey(congress));
		stmt.close();
		logger.debug("consume {}",congress);
		return authJwt;
	}

	public String formatKey(String congress) {
		return PREFIX.formatted(congress) ;
	}


}
