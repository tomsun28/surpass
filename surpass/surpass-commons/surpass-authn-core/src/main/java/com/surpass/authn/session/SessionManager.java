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






package com.surpass.authn.session;

import java.time.LocalDateTime;
import java.util.List;

import com.surpass.entity.access.SessionList;

/**
 * 会话管理接口
 *
 * @author Crystal.Sea
 *
 */
public interface SessionManager {

	public  void create(String sessionId, Session session);

    public  Session remove(String sessionId);

    public  Session get(String sessionId);

    public Session refresh(String sessionId ,LocalDateTime refreshTime);

    public Session refresh(String sessionId);

    public List<SessionList> sessionList(String style);

    public int getValiditySeconds();

    public void terminate(String sessionId,String userId,String username);

    public void terminateByUserId(String userId);

    public void setLimit(int sessionLimit);

    public void put(String style,String userId, String sessionKey) ;

    public boolean isRedis();

}
