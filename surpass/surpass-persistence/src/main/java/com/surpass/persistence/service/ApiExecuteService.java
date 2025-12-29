package com.surpass.persistence.service;

import com.surpass.entity.ApiRequestUri;
import java.util.Map;

/**
 * API执行服务
 */
public interface ApiExecuteService{

    public Object execute(ApiRequestUri apiRequestUri, String method,  Map<String, Object> params);
    
}
