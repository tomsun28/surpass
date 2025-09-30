package org.dromara.surpass.service.impl;

import lombok.RequiredArgsConstructor;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.dromara.surpass.dao.HistoryLoginDao;
import org.dromara.surpass.pojo.entity.history.HistoryLogin;
import org.dromara.surpass.service.HistoryLoginService;
import org.dromara.surpass.web.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/9/30 10:52
 */

@Service
@RequiredArgsConstructor
public class HistoryLoginServiceImpl extends JpaServiceImpl<HistoryLoginDao, HistoryLogin> implements HistoryLoginService {
    private static Logger logger = LoggerFactory.getLogger(HistoryLoginServiceImpl.class);

    private final HistoryLoginDao historyLoginDao;

    @Override
    public void insertHistory(HistoryLogin historyLogin) {
        historyLogin.setId(WebContext.genId());
        //Thread insert HistoryLogin
        new Thread(new HistoryLoginRunnable(this,historyLogin)).start();
    }

    public class HistoryLoginRunnable implements Runnable{

        HistoryLoginServiceImpl service;

        HistoryLogin historyLogin;

        public HistoryLoginRunnable(HistoryLoginServiceImpl historyLoginService, HistoryLogin historyLogin) {
            super();
            this.service = historyLoginService;
            this.historyLogin = historyLogin;
        }

        @Override
        public void run() {
            logger.debug(" historyLogin {}" , historyLogin);
            historyLogin.setOperateTime(new Date());
            service.insert(historyLogin);
        }
    }

}
