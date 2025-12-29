package com.surpass.persistence.service;

import com.surpass.entity.Message;
import com.surpass.entity.ApiRequestUri;
import com.surpass.entity.ClientPermission;
import com.surpass.entity.app.AppResources;
import com.surpass.entity.app.dto.AppResourcesPageDto;
import com.surpass.entity.app.dto.ClientAuthzDto;

import org.dromara.mybatis.jpa.service.IJpaService;

import java.util.List;

public interface ClientPermissionService extends IJpaService<ClientPermission> {
    List<ClientPermission> getClientApps(String clientId);

    Message<String> saveClientAppRelation(ClientAuthzDto dto);

    Message<List<ClientPermission>> getClientAuthz(AppResourcesPageDto dto);
    
    public List<AppResources> queryPermissions(ClientPermission clientPermission);
    
    public boolean enforce(ApiRequestUri apiRequestUri ,String clientId);
}
