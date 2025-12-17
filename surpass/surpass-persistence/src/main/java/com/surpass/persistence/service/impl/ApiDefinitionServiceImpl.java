package com.surpass.persistence.service.impl;

import com.surpass.entity.Message;
import com.surpass.entity.api.ApiDefinition;
import com.surpass.entity.api.DataSource;
import com.surpass.entity.api.dto.ApiPageDto;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.ApiDefinitionMapper;
import com.surpass.persistence.service.ApiDefinitionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.query.OrderBy;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.dromara.mybatis.jpa.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/11/19 9:59
 */

@Service
@RequiredArgsConstructor
public class ApiDefinitionServiceImpl extends JpaServiceImpl<ApiDefinitionMapper, ApiDefinition> implements ApiDefinitionService {

    private final ApiDefinitionMapper apiDefinitionMapper;

    @Override
    public Message<List<ApiDefinition>> findByDatasourceId(String datasourceId) {
        LambdaQuery<ApiDefinition> queryWrapper = new LambdaQuery<>();
        queryWrapper.eq(ApiDefinition::getDatasourceId, datasourceId);
        return Message.ok(super.query(queryWrapper));
    }

    @Override
    @Transactional
    public Message<String> save(ApiDefinition apiDefinition) {
        // 验证路径和方法是否重复
        if (isExistDuplicate(apiDefinition)) {
            throw new BusinessException(50001, "相同路径和方法的API已存在");
        }

        // 验证HTTP方法
        if (!isValidHttpMethod(apiDefinition.getMethod())) {
            throw new BusinessException(50001, "不支持的HTTP方法: " + apiDefinition.getMethod());
        }

        boolean result = super.insert(apiDefinition);
        return result ? Message.ok("新增成功") : Message.failed("新增失败");
    }

    @Override
    public Message<String> edit(ApiDefinition apiDefinition) {
        // 验证路径和方法是否重复
        if (isExistDuplicate(apiDefinition)) {
            throw new BusinessException(50001, "相同路径和方法的API已存在");
        }

        // 验证HTTP方法
        if (!isValidHttpMethod(apiDefinition.getMethod())) {
            throw new BusinessException(50001, "不支持的HTTP方法: " + apiDefinition.getMethod());
        }

        boolean result = super.update(apiDefinition);
        return result ? Message.ok("修改成功") : Message.failed("修改失败");
    }

    @Override
    public ApiDefinition findByPathAndMethod(String path, String method) {
        LambdaUpdateWrapper<ApiDefinition> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ApiDefinition::getPath, path);
        wrapper.eq(ApiDefinition::getMethod, method);
        wrapper.orderBy(ApiDefinition::getCreatedDate, OrderBy.DESC.getOrder());

        return super.query(wrapper).stream().findFirst().orElse(null);
    }

    @Override
    public Message<JpaPageResults<ApiDefinition>> page(ApiPageDto dto) {
        if (StringUtils.isBlank(dto.getAppId())) {
            throw new BusinessException(50001, "未查询到应用");
        }

        LambdaQuery<ApiDefinition> wrapper = new LambdaQuery<>();
        wrapper.eq(ApiDefinition::getAppId, dto.getAppId());
        if (StringUtils.isNotBlank(dto.getName())) {
            wrapper.like(ApiDefinition::getName, dto.getName());
        }
        dto.build();
        JpaPageResults<ApiDefinition> page = super.fetch(dto, wrapper);
        return Message.ok(page);
    }

    private boolean isExistDuplicate(ApiDefinition apiDefinition) {
        LambdaUpdateWrapper<ApiDefinition> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ApiDefinition::getPath, apiDefinition.getPath());
        wrapper.eq(ApiDefinition::getMethod, apiDefinition.getMethod());
        wrapper.eq(ApiDefinition::getAppId, apiDefinition.getAppId());
        if (StringUtils.isNotBlank(apiDefinition.getId())) {
            wrapper.notEq(ApiDefinition::getId, apiDefinition.getId());
        }

        return super.count(wrapper) > 0;
    }

    private boolean isValidHttpMethod(String method) {
        return method != null &&
                (method.equalsIgnoreCase("GET") ||
                        method.equalsIgnoreCase("POST") ||
                        method.equalsIgnoreCase("PUT") ||
                        method.equalsIgnoreCase("DELETE") ||
                        method.equalsIgnoreCase("PATCH"));
    }
}
