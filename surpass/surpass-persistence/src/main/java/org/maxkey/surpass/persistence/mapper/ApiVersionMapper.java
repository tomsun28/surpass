package org.maxkey.surpass.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.mybatis.jpa.IJpaMapper;
import org.maxkey.surpass.entity.api.ApiVersion;
import org.springframework.data.repository.query.Param;

@Mapper
public interface ApiVersionMapper extends IJpaMapper<ApiVersion> {
    int findMaxVersionByApiId(@Param("apiId") String apiId);
}
