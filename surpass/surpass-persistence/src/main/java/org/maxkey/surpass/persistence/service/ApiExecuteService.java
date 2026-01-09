package org.maxkey.surpass.persistence.service;

import java.util.Map;

import org.maxkey.surpass.entity.ApiRequestUri;

/**
 * API执行服务
 */
public interface ApiExecuteService{

    public Object execute(ApiRequestUri apiRequestUri, String method,  Map<String, Object> params);
    
}
