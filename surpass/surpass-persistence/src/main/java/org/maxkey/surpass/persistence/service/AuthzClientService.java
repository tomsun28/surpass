package org.maxkey.surpass.persistence.service;

import org.maxkey.surpass.entity.ApiRequestUri;
import org.maxkey.surpass.entity.history.HistoryOpenapi;

public interface AuthzClientService{
    /**
     * 客户端权限判断
     * @param apiRequestUri
     * @param clientId
     * @return
     */
    public boolean enforce(ApiRequestUri apiRequestUri ,String clientId,HistoryOpenapi history);
}
