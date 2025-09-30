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
 





package com.jinbooks.password.onetimepwd.token;

import com.jinbooks.password.onetimepwd.OneTimePassword;
import org.joda.time.DateTime;

import com.jinbooks.constants.ConstsTimeInterval;
import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.persistence.redis.IRedisStatement;
import com.jinbooks.persistence.redis.connection.RedisConnectionFactory;

public class RedisOtpTokenStore  extends AbstractOtpTokenStore {

    protected int validitySeconds = ConstsTimeInterval.ONE_MINUTE * 5;

    RedisConnectionFactory connectionFactory;

    public RedisOtpTokenStore(RedisConnectionFactory connectionFactory) {
        super();
        this.connectionFactory = connectionFactory;
    }

    public static final String PREFIX = "mxk:otp:%s:%s:%s";

    @Override
    public void store(UserInfo userInfo, String token, String receiver, String type) {
        DateTime currentDateTime = new DateTime();
        OneTimePassword otp = new OneTimePassword();
        otp.setId(formatKey(userInfo.getUsername() ,type , token));
        otp.setType(type);
        otp.setUsername(userInfo.getUsername());
        otp.setToken(token);
        otp.setReceiver(receiver);
        otp.setCreateTime(currentDateTime.toString("yyyy-MM-dd HH:mm:ss"));
        IRedisStatement stmt = connectionFactory.createStatement();
        stmt.setexObject(formatKey(userInfo.getUsername() ,type , token), validitySeconds, otp);
        stmt.close();
    }

    @Override
    public boolean validate(UserInfo userInfo, String token, String type, int interval) {
    	IRedisStatement stmt = connectionFactory.createStatement();
        OneTimePassword otp = (OneTimePassword)stmt.getObject(formatKey(userInfo.getUsername() ,type , token));
        stmt.del(formatKey(userInfo.getUsername() ,type , token));
        stmt.close();
        return (otp != null);
    }

    private String formatKey(String username,String type,String token) {
    	return PREFIX.formatted(username,type,token);
    }

}
