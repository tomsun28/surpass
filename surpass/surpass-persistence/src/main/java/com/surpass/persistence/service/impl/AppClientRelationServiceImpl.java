package com.surpass.persistence.service.impl;

import com.surpass.entity.Message;
import com.surpass.entity.app.AppClientRelation;
import com.surpass.entity.app.dto.AppClientChangeDto;
import com.surpass.entity.app.dto.AppClientRelationDto;
import com.surpass.persistence.mapper.AppClientRelationMapper;
import com.surpass.persistence.service.AppClientRelationService;
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
public class AppClientRelationServiceImpl extends JpaServiceImpl<AppClientRelationMapper, AppClientRelation> implements AppClientRelationService {

    @Override
    public List<AppClientRelation> getClientApps(String clientId) {
        LambdaQuery<AppClientRelation> wrapper = new LambdaQuery<>();
        wrapper.eq(AppClientRelation::getClientId, clientId);
        return super.query(wrapper);
    }

    @Override
    @Transactional
    public Message<String> saveClientAppRelation(AppClientRelationDto dto) {
        String clientId = dto.getClientId();
        List<String> appIds = dto.getAppIds();

        // 保护：传入为 null 时视为空集合
        if (appIds == null) {
            appIds = Collections.emptyList();
        }

        // 查询数据库中已经存在的关联
        LambdaQuery<AppClientRelation> wrapper = new LambdaQuery<>();
        wrapper.eq(AppClientRelation::getClientId, clientId);
        List<AppClientRelation> relationsInDb = super.query(wrapper);

        // 已存在的 appId 集合（用 Set 提升 contains 性能）
        Set<String> existAppIdSet = relationsInDb.stream()
                .map(AppClientRelation::getAppId)
                .collect(Collectors.toSet());

        // 去重并做快速查询
        Set<String> newAppIdSet = new HashSet<>(appIds);

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
            List<AppClientRelation> insertList = toAdd.stream()
                    .map(id -> {
                        AppClientRelation r = new AppClientRelation();
                        r.setClientId(clientId);
                        r.setAppId(id);
                        return r;
                    })
                    .toList();
            super.insertBatch(insertList);
        }

        // 执行删除
        if (!toDelete.isEmpty()) {
            LambdaQuery<AppClientRelation> deleteWrapper = new LambdaQuery<>();
            deleteWrapper.eq(AppClientRelation::getClientId, clientId)
                    .in(AppClientRelation::getAppId, toDelete);
            super.delete(deleteWrapper);
        }

        return Message.ok("保存成功");
    }

}
