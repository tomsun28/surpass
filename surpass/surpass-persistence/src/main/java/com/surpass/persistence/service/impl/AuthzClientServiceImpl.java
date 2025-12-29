package com.surpass.persistence.service.impl;

import com.surpass.entity.RegisteredClient;
import com.surpass.constants.ConstsBoolean;
import com.surpass.entity.ApiRequestUri;
import com.surpass.entity.ClientPermission;
import com.surpass.entity.app.App;
import com.surpass.entity.app.AppResources;
import com.surpass.entity.history.HistoryOpenapi;
import com.surpass.persistence.service.AppService;
import com.surpass.persistence.service.AuthzClientService;
import com.surpass.persistence.service.ClientPermissionService;
import com.surpass.persistence.service.RegisteredClientService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/11 10:17
 */

@Slf4j
@Service
public class AuthzClientServiceImpl implements AuthzClientService {

    @Autowired
    AppService appService ;
    
    @Autowired
    RegisteredClientService clientService;
    
    @Autowired
    ClientPermissionService clientPermissionService;
    
    PathMatcher pathMatcher = new AntPathMatcher();
   
	@Override
	public boolean enforce(ApiRequestUri apiRequestUri,String clientId,HistoryOpenapi history) {
		App app = appService.findByContextPath(apiRequestUri.getContextPath());
        if(app != null) {
        	history.setAppId(app.getId());
        	history.setAppName(app.getAppName());
        	RegisteredClient client = clientService.findByClientId(clientId);
            ClientPermission clientPermission = new ClientPermission();
            clientPermission.setClientId(client.getId());
            clientPermission.setAppId(app.getId());
            List<AppResources> resourcesList = clientPermissionService.queryPermissions(clientPermission);
            if(CollectionUtils.isNotEmpty( resourcesList )) {
            	log.debug("resourcesList {}",resourcesList);
            	List<AppResources> permsList = 
            			resourcesList.stream().filter(item -> {
            					if(StringUtils.isNotBlank(item.getPath()) 
            							&& apiRequestUri.getHttpMethod().equalsIgnoreCase(item.getMethod())) {
	                				String itemResourcePath = item.getPath();
		                            if (!item.getPath().startsWith("/")) {
		                            	itemResourcePath = "/" + item.getPath();
		                            }
			                        return pathMatcher.match(itemResourcePath, apiRequestUri.getResourcePath());
            					}
		                        return false;
		                    }).collect(Collectors.toList());
            	log.debug("permsList {}",permsList);
            	if(CollectionUtils.isNotEmpty(permsList)) {
            		history.setAccess(ConstsBoolean.YES);
            		history.setResourceId(permsList.get(0).getId());
            		history.setResourceName(permsList.get(0).getName());
            		return true;
            	}
            }
        }
		return false;
	}

}
