package org.dromara.surpass.persistence.service.impl;

import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.dromara.surpass.entity.Message;
import org.dromara.surpass.entity.RoleAppResourcesPermission;
import org.dromara.surpass.entity.app.dto.AppResourcesPageDto;
import org.dromara.surpass.entity.app.dto.RoleAppResourcesAuthzDto;
import org.dromara.surpass.persistence.mapper.RoleAppResourcesPermissionMapper;
import org.dromara.surpass.persistence.service.RoleAppResourcesPermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/29 15:07
 */

@Service
public class RoleAppResourcesPermissionServiceImpl extends JpaServiceImpl<RoleAppResourcesPermissionMapper, RoleAppResourcesPermission> implements RoleAppResourcesPermissionService {

    @Override
    @Transactional
    public Message<String> saveRoleAppRelation(RoleAppResourcesAuthzDto dto) {
        String roleId = dto.getRoleId();
        String appId = dto.getAppId();
        List<String> resourceIds = dto.getResourceIds();

        // 保护：传入为 null 时视为空集合
        if (resourceIds == null) {
            resourceIds = Collections.emptyList();
        }

        // 查询数据库中已经存在的关联
        LambdaQuery<RoleAppResourcesPermission> wrapper = new LambdaQuery<>();
        wrapper.eq(RoleAppResourcesPermission::getRoleId, roleId);
        wrapper.eq(RoleAppResourcesPermission::getAppId, appId);
        List<RoleAppResourcesPermission> relationsInDb = super.query(wrapper);

        // 已存在的 appId 集合（用 Set 提升 contains 性能）
        Set<String> existAppIdSet = relationsInDb.stream()
                .map(RoleAppResourcesPermission::getResourceId)
                .collect(Collectors.toSet());

        // 去重并做快速查询
        Set<String> newAppIdSet = new HashSet<>(resourceIds);

        // 需要新增的 appId = 传入的 - 已存在的
        List<String> toAdd = newAppIdSet.stream()
                .filter(id -> !existAppIdSet.contains(id))
                .toList();

        // 需要删除的 appId = 已存在的 - 传入的
        List<String> toDelete = existAppIdSet.stream()
                .filter(id -> !newAppIdSet.contains(id))
                .toList();

        // 执行新增
        if (!toAdd.isEmpty()) {
            List<RoleAppResourcesPermission> insertList = toAdd.stream()
                    .map(id -> {
                        RoleAppResourcesPermission r = new RoleAppResourcesPermission();
                        r.setRoleId(roleId);
                        r.setAppId(appId);
                        r.setResourceId(id);
                        return r;
                    })
                    .toList();
            super.insertBatch(insertList);
        }

        // 执行删除
        if (!toDelete.isEmpty()) {
            LambdaQuery<RoleAppResourcesPermission> deleteWrapper = new LambdaQuery<>();
            deleteWrapper.eq(RoleAppResourcesPermission::getRoleId, roleId)
                    .eq(RoleAppResourcesPermission::getAppId, appId)
                    .in(RoleAppResourcesPermission::getResourceId, toDelete);
            super.delete(deleteWrapper);
        }

        return Message.ok("保存成功");
    }

    @Override
    public Message<List<RoleAppResourcesPermission>> getRoleAuthz(AppResourcesPageDto dto) {
        String appId = dto.getAppId();
        String roleId = dto.getRoleId();
        LambdaQuery<RoleAppResourcesPermission> wrapper = new LambdaQuery<>();
        wrapper.eq(RoleAppResourcesPermission::getAppId, appId);
        wrapper.eq(RoleAppResourcesPermission::getRoleId, roleId);

        List<RoleAppResourcesPermission> query = super.query(wrapper);

        return Message.ok(query);
    }
}
