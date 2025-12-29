package com.surpass.persistence.service;

import com.surpass.entity.RoleAppResourcesPermission;
import com.surpass.entity.Message;
import com.surpass.entity.app.dto.AppResourcesPageDto;
import com.surpass.entity.app.dto.RoleAppResourcesAuthzDto;
import org.dromara.mybatis.jpa.service.IJpaService;

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
