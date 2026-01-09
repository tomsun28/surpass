package org.maxkey.surpass.persistence.service;

import org.dromara.mybatis.jpa.service.IJpaService;
import org.maxkey.surpass.entity.ApiRequestUri;
import org.maxkey.surpass.entity.ClientPermission;
import org.maxkey.surpass.entity.Message;
import org.maxkey.surpass.entity.app.AppResources;
import org.maxkey.surpass.entity.app.dto.AppResourcesPageDto;
import org.maxkey.surpass.entity.app.dto.ClientAuthzDto;

import java.util.List;

public interface ClientPermissionService extends IJpaService<ClientPermission> {
    List<ClientPermission> getClientApps(String clientId);

    Message<String> saveClientAppRelation(ClientAuthzDto dto);

    Message<List<ClientPermission>> getClientAuthz(AppResourcesPageDto dto);
    /**
     * 读取客户端对应应用资源得权限列表
     * @param clientPermission
     * @return
     */
    public List<AppResources> queryPermissions(ClientPermission clientPermission);
}
