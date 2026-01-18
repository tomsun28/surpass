package org.dromara.surpass.persistence.service;

import org.dromara.mybatis.jpa.service.IJpaService;
import org.dromara.surpass.entity.Message;
import org.dromara.surpass.entity.RoleAppResourcesPermission;
import org.dromara.surpass.entity.app.dto.AppResourcesPageDto;
import org.dromara.surpass.entity.app.dto.RoleAppResourcesAuthzDto;

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
