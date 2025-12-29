package com.surpass.persistence.mapper;

import com.surpass.entity.ClientPermission;
import com.surpass.entity.RoleAppResourcesPermission;
import org.apache.ibatis.annotations.Mapper;
import org.dromara.mybatis.jpa.IJpaMapper;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/29 14:53
 */

@Mapper
public interface RoleAppResourcesPermissionMapper extends IJpaMapper<RoleAppResourcesPermission> {
}
