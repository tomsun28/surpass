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





package com.surpass.authn.session.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.surpass.authn.session.Session;
import com.surpass.authn.session.SessionManager;
import com.surpass.constants.ConstsCached;
import com.surpass.entity.access.SessionList;
import com.surpass.entity.history.HistoryLogin;
import com.surpass.entity.history.HistoryLogin.CATEGORY;
import com.surpass.persistence.redis.connection.RedisConnectionFactory;
import com.surpass.persistence.service.HistoryLoginService;
import com.surpass.persistence.service.SessionListService;
import com.surpass.util.DateUtils;

/**
 * SessionManager/会话管理实现
 * persistence 0 in memory,store in Caffeine/存储内存中，采用Caffeine实现
 * persistence 2 in Redis 缓存存储Redis中
 * user session status in database/用户会话状态存储在数据库中
 * sessionstatus 1有效 2踢出 7无效
 *
 * @author Crystal.Sea
 *
 */
public class SessionManagerImpl implements SessionManager{
	private static final  Logger logger = LoggerFactory.getLogger(SessionManagerImpl.class);

	InMemorySessionManager 	inMemorySessionManager;

	RedisSessionManager 	redisSessionManager;

	SessionListService 		sessionListService;

	HistoryLoginService historyLoginService;

	boolean isRedis = false;

	int validitySeconds ;

	public SessionManagerImpl(
				int persistence,
				int validitySeconds,
				RedisConnectionFactory redisConnFactory,
				SessionListService sessionListService,
			 	HistoryLoginService historyLoginService
	            ) {
		 this.validitySeconds = validitySeconds;
		 this.sessionListService = sessionListService;
		 this.historyLoginService = historyLoginService;
		 if (persistence == ConstsCached.REDIS) {
			isRedis = true;
			this.redisSessionManager = new RedisSessionManager(redisConnFactory,validitySeconds);
			logger.debug("RedisSessionManager");
		 }else {
			 this.inMemorySessionManager = new InMemorySessionManager(validitySeconds);
			 logger.debug("InMemorySessionManager");
		 }
	}

	/**
	 * 创建
	 */
	public void create(String sessionId, Session session) {
		if(isRedis) {
			redisSessionManager.create(sessionId, session);
		}else {
			inMemorySessionManager.create(sessionId, session);
		}
	}

	/**
	 * 删除
	 */
	public Session remove(String sessionId) {
		Session session = null;
		if(isRedis) {
			session = redisSessionManager.remove(sessionId);
		}else {
			session = inMemorySessionManager.remove(sessionId);
		}
		return session;
	}

	/**
	 * 读取，先读取本地缓存再读取Redis
	 */
	public Session get(String sessionId) {
		Session session = null;
		if(isRedis) {
			session = redisSessionManager.get(sessionId);
		}else {
			session = inMemorySessionManager.get(sessionId);
		}
		return session;
	}

	/**
	 * 根据会话id和刷新时间刷新，先刷新Redis，再刷新本地缓存
	 */
	public Session refresh(String sessionId, LocalDateTime refreshTime) {
		Session session = null;
		if(isRedis) {
			session = redisSessionManager.refresh(sessionId,refreshTime);
		}else {
			session = inMemorySessionManager.refresh(sessionId,refreshTime);
		}
		return session;
	}

	/**
	 * 根据会话id刷新，先刷新Redis，再刷新本地缓存
	 */
	public Session refresh(String sessionId) {
		Session session = null;
		if(isRedis) {
			session = redisSessionManager.refresh(sessionId);
		}else {
			session = inMemorySessionManager.refresh(sessionId);
		}

		return session;
	}

	/**
	 * 查询数据库会话状态
	 */
	public List<SessionList> sessionList(String style) {
		//trace memory sessions/内存会话打印
		if(!isRedis) {
			inMemorySessionManager.sessionList(style);
		}
		//query on line session/查询在线会话
		return sessionListService.list(style);
	}



    /**
     * 终止会话
     */
	public void terminate(String sessionId, String userId, String username) {
		String lastLogoffTime = DateUtils.formatDateTime(new Date());
	   	 logger.trace("{} user {} terminate session {} ." ,lastLogoffTime,username, sessionId);
	   	sessionListService.updateLastLogoffTime(userId);
	   	SessionList  sessionList  = sessionListService.getBySessionId(sessionId);
		//判断是否存在在线会话，否则直接移除
		if (Objects.nonNull(sessionList)) {
			HistoryLogin historyLogin = new HistoryLogin();
			BeanUtils.copyProperties(sessionList,historyLogin );
			historyLogin.setCategory(CATEGORY.LOGOUT);
			historyLoginService.insertHistory(historyLogin);
			//移除会话
			this.remove(sessionId);
		}
		//删除数据库记录
        sessionListService.removeById(sessionList.getId());

	}

	public int getValiditySeconds() {
		return validitySeconds;
	}

	@Override
	public void setLimit(int sessionLimit) {
		//

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
		return isRedis;
	}

}
