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






package org.dromara.surpass.authn.session.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.dromara.surpass.authn.session.Session;
import org.dromara.surpass.authn.session.SessionManager;
import org.dromara.surpass.authn.session.UserSessions;
import org.dromara.surpass.entity.access.SessionList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * 会话管理 内存存储
 *
 * @author Crystal.Sea
 *
 */
public class InMemorySessionManager implements SessionManager{
    private static final Logger logger = LoggerFactory.getLogger(InMemorySessionManager.class);

    static final 	long 	CACHE_MAXIMUM_SIZE 		= 2000000;//200W
    static final 	int 	CACHE_DEFAULT_SECONDS 	= 60 * 30; //default 30 minutes.
    int validitySeconds 							= CACHE_DEFAULT_SECONDS;

	Cache<String, Session> sessionStore;

	Cache<String, Session> sessionTwoFactorStore;

	Cache<String, UserSessions> userSessionsStore;

	public InMemorySessionManager(int validitySeconds) {
        super();
        this.validitySeconds = validitySeconds;
        sessionStore = Caffeine.newBuilder()
                    	.expireAfterWrite(validitySeconds, TimeUnit.SECONDS)
                    	.maximumSize(CACHE_MAXIMUM_SIZE)
                    	.build();

        sessionTwoFactorStore = Caffeine.newBuilder()
		            	.expireAfterWrite(10, TimeUnit.MINUTES)
		            	.maximumSize(CACHE_MAXIMUM_SIZE)
		            	.build();

        userSessionsStore =
		        Caffeine.newBuilder()
		            .expireAfterWrite(CACHE_DEFAULT_SECONDS, TimeUnit.SECONDS)
		            .maximumSize(CACHE_MAXIMUM_SIZE)
		            .build();

    }

    @Override
	public void create(String sessionId, Session session) {
    	session.setExpiredTime(session.getLastAccessTime().plusSeconds(validitySeconds));
    	sessionStore.put(sessionId, session);
	}

	@Override
	public Session remove(String sessionId) {
	    Session session = sessionStore.getIfPresent(sessionId);
	    sessionStore.invalidate(sessionId);
		return session;
	}

    @Override
    public Session get(String sessionId) {
    	return sessionStore.getIfPresent(sessionId);
    }

    @Override
    public Session refresh(String sessionId,LocalDateTime refreshTime) {
        Session session = get(sessionId);
        if(session != null) {
        	logger.debug("refresh session Id {} at refreshTime {}",sessionId,refreshTime);
	        session.setLastAccessTime(refreshTime);
	        //renew one
	        create(sessionId , session);
        }
        return session;
    }

    @Override
    public Session refresh(String sessionId) {
        Session session = get(sessionId);

        if(session != null) {
        	LocalDateTime currentTime = LocalDateTime.now();
        	logger.debug("refresh session Id {} at time {}",sessionId,currentTime);
        	session.setLastAccessTime(currentTime);
        	//put renew session
	        create(sessionId , session);
        }
        return session;
    }

	public int getValiditySeconds() {
		return validitySeconds;
	}

	@Override
	public List<SessionList> sessionList(String style) {
		ConcurrentMap<String, Session> sessions = sessionStore.asMap();
		for (Map.Entry<String, Session> entry: sessions.entrySet()) {
			logger.debug("session id = {}, Value = {} Start at {} , Expired at {}" ,
					entry.getKey(),entry.getValue(), entry.getValue().getStartTimestamp(),entry.getValue().getExpiredTime());
		}
		return null;
	}

	@Override
	public void terminate(String sessionId, String userId, String username) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLimit(int sessionLimit) {
		// TODO Auto-generated method stub

	}

	@Override
	public void terminateByUserId(String userId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void put(String style, String userId, String sessionKey) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isRedis() {
		return false;
	}

}
