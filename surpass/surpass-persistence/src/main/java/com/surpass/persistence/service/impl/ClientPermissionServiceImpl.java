package com.surpass.persistence.service.impl;

import com.surpass.entity.Message;
import com.surpass.entity.ClientPermission;
import com.surpass.entity.app.dto.AppResourcesPageDto;
import com.surpass.entity.app.dto.ClientAuthzDto;
import com.surpass.persistence.mapper.ClientPermissionMapper;
import com.surpass.persistence.service.ClientPermissionService;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
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
 * @time: 2025/12/11 10:17
 */

@Service
public class ClientPermissionServiceImpl extends JpaServiceImpl<ClientPermissionMapper, ClientPermission> implements ClientPermissionService {

    @Override
    public List<ClientPermission> getClientApps(String clientId) {
        LambdaQuery<ClientPermission> wrapper = new LambdaQuery<>();
        wrapper.eq(ClientPermission::getClientId, clientId);
        return super.query(wrapper);
    }

    @Override
    @Transactional
    public Message<String> saveClientAppRelation(ClientAuthzDto dto) {
        String clientId = dto.getClientId();
        String appId = dto.getAppId();
        List<String> resourceIds = dto.getResourceIds();

        // 保护：传入为 null 时视为空集合
        if (resourceIds == null) {
            resourceIds = Collections.emptyList();
        }

        // 查询数据库中已经存在的关联
        LambdaQuery<ClientPermission> wrapper = new LambdaQuery<>();
        wrapper.eq(ClientPermission::getClientId, clientId);
        wrapper.eq(ClientPermission::getAppId, appId);
        List<ClientPermission> relationsInDb = super.query(wrapper);

        // 已存在的 appId 集合（用 Set 提升 contains 性能）
        Set<String> existAppIdSet = relationsInDb.stream()
                .map(ClientPermission::getResourceId)
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
            List<ClientPermission> insertList = toAdd.stream()
                    .map(id -> {
                        ClientPermission r = new ClientPermission();
                        r.setClientId(clientId);
                        r.setAppId(appId);
                        r.setResourceId(id);
                        return r;
                    })
                    .toList();
            super.insertBatch(insertList);
        }

        // 执行删除
        if (!toDelete.isEmpty()) {
            LambdaQuery<ClientPermission> deleteWrapper = new LambdaQuery<>();
            deleteWrapper.eq(ClientPermission::getClientId, clientId)
                    .eq(ClientPermission::getAppId, appId)
                    .in(ClientPermission::getResourceId, toDelete);
            super.delete(deleteWrapper);
        }

        return Message.ok("保存成功");
    }

    @Override
    public Message<List<ClientPermission>> getClientAuthz(AppResourcesPageDto dto) {
        String appId = dto.getAppId();
        String clientId = dto.getClientId();
        LambdaQuery<ClientPermission> wrapper = new LambdaQuery<>();
        wrapper.eq(ClientPermission::getAppId, appId);
        wrapper.eq(ClientPermission::getClientId, clientId);

        List<ClientPermission> query = super.query(wrapper);

        return Message.ok(query);
    }

}
