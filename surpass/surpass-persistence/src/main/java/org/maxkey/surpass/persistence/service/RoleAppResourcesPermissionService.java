package org.maxkey.surpass.persistence.service;

import org.dromara.mybatis.jpa.service.IJpaService;
import org.maxkey.surpass.entity.Message;
import org.maxkey.surpass.entity.RoleAppResourcesPermission;
import org.maxkey.surpass.entity.app.dto.AppResourcesPageDto;
import org.maxkey.surpass.entity.app.dto.RoleAppResourcesAuthzDto;

import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/29 15:00
 */
public interface RoleAppResourcesPermissionService extends IJpaService<RoleAppResourcesPermission> {

    Message<String> saveRoleAppRelation(RoleAppResourcesAuthzDto dto);

    Message<List<RoleAppResourcesPermission>> getRoleAuthz(AppResourcesPageDto dto);
}
