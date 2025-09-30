package org.dromara.surpass.service;

import org.dromara.mybatis.jpa.service.IJpaService;
import org.dromara.surpass.pojo.entity.access.SessionList;

import java.util.List;

public interface SessionListService extends IJpaService<SessionList> {
    public void insertOnline(SessionList sessionList);

    public List<SessionList> list(String style);

    public void updateLastLogoffTime(String userId);

    public SessionList getBySessionId(String sessionId);
}
