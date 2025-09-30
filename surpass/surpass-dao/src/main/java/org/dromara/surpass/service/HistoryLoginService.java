package org.dromara.surpass.service;

import org.dromara.mybatis.jpa.service.IJpaService;
import org.dromara.surpass.pojo.entity.history.HistoryLogin;

public interface HistoryLoginService extends IJpaService<HistoryLogin> {
    void insertHistory(HistoryLogin historyLogin) ;
}
