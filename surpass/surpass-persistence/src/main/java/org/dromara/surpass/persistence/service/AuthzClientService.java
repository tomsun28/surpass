package org.dromara.surpass.persistence.service;

import org.dromara.surpass.entity.ApiRequestUri;
import org.dromara.surpass.entity.history.HistoryOpenapi;

public interface AuthzClientService{
    /**
     * 客户端权限判断
     * @param apiRequestUri
     * @param clientId
     * @return
     */
    public boolean enforce(ApiRequestUri apiRequestUri ,String clientId,HistoryOpenapi history);
}
