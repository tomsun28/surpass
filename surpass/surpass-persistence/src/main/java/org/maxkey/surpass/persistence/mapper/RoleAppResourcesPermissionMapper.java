package org.maxkey.surpass.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.mybatis.jpa.IJpaMapper;
import org.maxkey.surpass.entity.ClientPermission;
import org.maxkey.surpass.entity.RoleAppResourcesPermission;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/29 14:53
 */

@Mapper
public interface RoleAppResourcesPermissionMapper extends IJpaMapper<RoleAppResourcesPermission> {
}
