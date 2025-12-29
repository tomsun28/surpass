package com.surpass.persistence.service;

import com.surpass.entity.ApiRequestUri;

public interface AuthzClientService{
    /**
     * 客户端权限判断
     * @param apiRequestUri
     * @param clientId
     * @return
     */
    public boolean enforce(ApiRequestUri apiRequestUri ,String clientId);
}
