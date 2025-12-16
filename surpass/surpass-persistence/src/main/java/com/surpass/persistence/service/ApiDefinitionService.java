package com.surpass.persistence.service;

import com.surpass.entity.Message;
import com.surpass.entity.api.ApiDefinition;
import com.surpass.entity.api.dto.ApiPageDto;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.service.IJpaService;

import java.util.List;

public interface ApiDefinitionService extends IJpaService<ApiDefinition> {
    Message<List<ApiDefinition>> findByDatasourceId(String datasourceId);

    Message<String> save(ApiDefinition apiDefinition);

    Message<String> edit(ApiDefinition apiDefinition);

    ApiDefinition findByPathAndMethod(String path, String method);

    Message<JpaPageResults<ApiDefinition>> page(ApiPageDto dto);
}
