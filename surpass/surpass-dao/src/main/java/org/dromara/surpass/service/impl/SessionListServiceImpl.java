package org.dromara.surpass.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.dromara.surpass.dao.SessionListDao;
import org.dromara.surpass.pojo.entity.access.SessionList;
import org.dromara.surpass.pojo.entity.idm.UserInfo;
import org.dromara.surpass.service.SessionListService;
import org.dromara.surpass.web.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/9/30 10:27
 */

@Service
@RequiredArgsConstructor
public class SessionListServiceImpl extends JpaServiceImpl<SessionListDao, SessionList> implements SessionListService {
    private static Logger logger = LoggerFactory.getLogger(SessionListServiceImpl.class);


    private final SessionListDao sessionListDao;


    @Override
    public void insertOnline(SessionList sessionList) {
        sessionList.setId(WebContext.genId());

        new Thread(new SessionInsertRunnable(this,sessionList)).start();
    }

    @Override
    public SessionList getBySessionId(String sessionId) {
        return sessionListDao.getBySessionId(sessionId);
    }

    public void removeById(String sessionId) {
        sessionListDao.removeById(sessionId);
    }

    @Override
    public void updateLastLogoffTime(String userId) {
        UserInfo user = new UserInfo();
        user.setId(userId);
        user.setLastLogoffTime(new Date());
        sessionListDao.updateLastLogoffTime(user);
    }

    @Override
    public List<SessionList> list(String style) {
        if(StringUtils.isBlank(style)){
            return sessionListDao.listAll();
        }else {
            return sessionListDao.listByStyle(style);
        }
    }

    public class SessionInsertRunnable implements Runnable{

        SessionListServiceImpl service;

        SessionList sessionList;

        public SessionInsertRunnable(SessionListServiceImpl sessionListService, SessionList sessionList) {
            super();
            this.service = sessionListService;
            this.sessionList = sessionList;
        }

        @Override
        public void run() {
            logger.debug(" sessionList {}" , sessionList);
            sessionList.setOperateTime(new Date());
            service.insert(sessionList);
        }
    }

}
