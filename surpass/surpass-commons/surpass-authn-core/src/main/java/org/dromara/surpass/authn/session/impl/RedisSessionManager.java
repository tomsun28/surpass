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

import org.dromara.surpass.authn.session.Session;
import org.dromara.surpass.authn.session.SessionManager;
import org.dromara.surpass.entity.access.SessionList;
import org.dromara.surpass.persistence.redis.IRedisStatement;
import org.dromara.surpass.persistence.redis.connection.RedisConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 会话管理 Redis存储
 *
 * @author Crystal.Sea
 *
 */
public class RedisSessionManager implements SessionManager {
    private static final Logger logger = LoggerFactory.getLogger(RedisSessionManager.class);

    int validitySeconds 	= 60 * 30; //default 30 minutes.

	RedisConnectionFactory connectionFactory;

	public static final String PREFIX = "mxk:session:%s";

	public static final String PREFIX_TWOFACTOR = "mxk:session:twofactor:%s";

    int twoFactorValidity 	= 10 * 60; //default 10 minutes.

    int sessionLimit		= 5;

	//存储当前用户的会话
	public static final String USER_SESSION 		= 	"mxk:session:user:%s";

	//租户端每个用户最多登录5个浏览器
	public static final String WEB_SESSION 			=	"mxk:session:web:%s:%s";

	//平台关联端会话和租户端一致
	public static final String MGMT_SESSION 		=	"mxk:session:mgmt:%s:%s";

	//APP端会话只有一个
	public static final String APP_SESSION 			=	"mxk:session:app:%s";


	public String getKey(String style, String userId,String sessionId) {
		if(style.equalsIgnoreCase(Session.STYLE.WEB)) {
			return WEB_SESSION.formatted(userId,sessionId);
		}else if(style.equalsIgnoreCase(Session.STYLE.MGMT)) {
			return MGMT_SESSION.formatted(userId,sessionId);
		}else if(style.equalsIgnoreCase(Session.STYLE.APP)) {
			return APP_SESSION.formatted(userId);
		}
		return WEB_SESSION.formatted(userId,sessionId);
	}

	public String formatKey(String sessionId) {
		return PREFIX.formatted(sessionId) ;
	}

	public String formatTwoFactorKey(String sessionId) {
		return PREFIX_TWOFACTOR.formatted(sessionId) ;
	}

	/**
	 * @param connectionFactory
	 */
	public RedisSessionManager(
			RedisConnectionFactory connectionFactory,
			int validitySeconds) {
		super();
		this.connectionFactory = connectionFactory;
		this.validitySeconds = validitySeconds;
	}

	/**
	 *
	 */
	public RedisSessionManager() {

	}

	public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	@Override
	public void create(String sessionId, Session session) {
		session.setExpiredTime(session.getLastAccessTime().plusSeconds(validitySeconds));
		IRedisStatement stmt = connectionFactory.createStatement();
		stmt.setexObject( formatKey(sessionId), validitySeconds, session);
		stmt.close();
	}

	@Override
	public Session remove(String sessionId) {
		IRedisStatement stmt=connectionFactory.createStatement();
		Session ticket = stmt.getObject(formatKey(sessionId));
		stmt.del(formatKey(sessionId));
		stmt.close();
		return ticket;
	}

    @Override
    public Session get(String sessionId) {
    	IRedisStatement stmt=connectionFactory.createStatement();
        Session session = stmt.getObject(formatKey(sessionId));
        stmt.close();
        return session;
    }


    public int getValiditySeconds() {
		return validitySeconds;
	}

	public void setValiditySeconds(int validitySeconds) {
		this.validitySeconds = validitySeconds;
	}

	@Override
    public Session refresh(String sessionId,LocalDateTime refreshTime) {
        Session session = get(sessionId);
        if(session != null) {
        	logger.debug("refresh session Id {} at {}",sessionId,refreshTime);
	        session.setLastAccessTime(refreshTime);
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
        	create(sessionId , session);
        }
        return session;
    }

	@Override
	public List<SessionList> sessionList(String style) {
		// TODO Auto-generated method stub
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
/*
	@Override
	public void put(String style,String userId, String sessionKey) {
		IRedisConnection conn=connectionFactory.getConnection();
		UserSessions userSession = conn.getObject(USER_SESSION.formatted(userId));
		if(userSession == null) {
			userSession = new UserSessions();
		}else if(style.equalsIgnoreCase(Session.STYLE.APP)
				&&!userSession.getMobileSessionIdQueue().isEmpty()
				&& userSession.getMobileSessionIdQueue().size() >= sessionLimit){//APP会话
			//判断限制
			boolean isNew = true;
			Iterator<String> listOfNames = userSession.getMobileSessionIdQueue().iterator();
			while (listOfNames.hasNext()) {
				String redisKey = listOfNames.next();
				logger.debug("new app sessionKey {} , redisKey {}",sessionKey,redisKey);
				if(sessionKey.equals(redisKey)) {
					isNew = false;
				}
			}
			if(isNew) {
				//删除最早会话记录
				for(int i = userSession.getMobileSessionIdQueue().size(); i >= sessionLimit ; i--) {
					String lastLoginSessionId = userSession.getMobileSessionIdQueue().poll();
					conn.del(lastLoginSessionId);
				}
			}
		}else if( !userSession.getSessionIdQueue().isEmpty()
				&& userSession.getSessionIdQueue().size() >= sessionLimit) {//web端会话
			//判断限制
			boolean isNew = true;
			Iterator<String> listOfNames = userSession.getSessionIdQueue().iterator();
			while (listOfNames.hasNext()) {
				String redisKey = listOfNames.next();
				logger.debug("new sessionKey {} , redisKey {}",sessionKey,redisKey);
				if(sessionKey.equals(redisKey)) {
					isNew = false;
				}
			}
			if(isNew) {
				//删除最早会话记录
				for(int i = userSession.getSessionIdQueue().size(); i >= sessionLimit ; i--) {
		    		String lastLoginSessionId = userSession.getSessionIdQueue().poll();
		    		conn.del(lastLoginSessionId);
				}
			}
		}

		addUserSession(userSession,style,sessionKey);
		conn.setObject(USER_SESSION.formatted(userId), userSession);
		conn.close();
	}

	private void addUserSession(UserSessions userSession,String style, String sessionKey) {
		if(style.equalsIgnoreCase(Session.STYLE.APP)) {
			userSession.getMobileSessionIdQueue().add(sessionKey);
		}else {
			userSession.getSessionIdQueue().add(sessionKey);
		}
	}

	public List<SessionDto> getSessionList(String userId){
		List<SessionDto> sessionList = new ArrayList<>();
		UserSessions userSession = get(userId);
		if(userSession != null) {
			//web端会话
			if(!userSession.getSessionIdQueue().isEmpty()) {
				Iterator<String> listOfNames = userSession.getSessionIdQueue().iterator();
				while (listOfNames.hasNext()) {
					String redisKey =listOfNames.next();
					logger.debug("redisKey {}",redisKey);
					SessionDto  dto = buildSessionDto(redisKey);
					if(dto != null) {
						sessionList.add(dto);
					}
				}
			}
			//手机APP会话
			if(!userSession.getMobileSessionIdQueue().isEmpty()) {
				Iterator<String> listOfNames = userSession.getMobileSessionIdQueue().iterator();
				while (listOfNames.hasNext()) {
					String redisKey =listOfNames.next();
					logger.debug("redisKey {}",redisKey);
					SessionDto  dto = buildSessionDto(redisKey);
					if(dto != null) {
						sessionList.add(dto);
					}
				}
			}
		}
		Collections.reverse(sessionList);
		return sessionList;
	}


	SessionDto buildSessionDto(String redisKey){
		IRedisConnection conn=connectionFactory.getConnection();
		String value = conn.getObject(redisKey);
		Session  session = StringUtils.isBlank(value) ? null : ObjectTransformer.deserialize(value);
		SessionDto dto = null;
		if(session != null) {
			dto = new SessionDto();
			dto.setCacheKey(HexUtils.bytes2HexString(redisKey.getBytes()));
			dto.setId(session.getId());
			dto.setStartTimestamp(session.getStartTimestamp());
			SignedPrincipal principal = (SignedPrincipal)session.getAuthentication().getPrincipal();
			dto.setUserId(principal.getUserId());
			dto.setUsername(principal.getUsername());
			dto.setDisplayName(principal.getDisplayName());
			dto.setBrowser(principal.getBrowser());
			dto.setOsPlatform(principal.getOsPlatform());
			dto.setIpAddr(principal.getIpAddr());
			dto.setIpLocation(principal.getIpLocation());
		}
		conn.close();
		return dto;
	}
	//终止用户所有登录会话
	@Override
	public void terminateByUserId(String userId) {
		UserSessions userSession = get(userId);
		if(userSession != null) {
			if(!userSession.getSessionIdQueue().isEmpty()) {
				Iterator<String> listOfNames = userSession.getSessionIdQueue().iterator();
				while (listOfNames.hasNext()) {
					String redisKey =listOfNames.next();
					logger.debug("terminate Web/Mgmt redis Key {}",redisKey);
					terminate(redisKey);
				}
			}
			//手机APP会话
			if(!userSession.getMobileSessionIdQueue().isEmpty()) {
				Iterator<String> listOfNames = userSession.getMobileSessionIdQueue().iterator();
				while (listOfNames.hasNext()) {
					String redisKey =listOfNames.next();
					logger.debug("terminate Mobile redis Key {}",redisKey);
					terminate(redisKey);
				}
			}
		}
	}
	*/

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
		return true;
	}

}
