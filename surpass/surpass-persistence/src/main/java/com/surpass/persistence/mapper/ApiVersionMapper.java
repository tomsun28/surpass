package com.surpass.persistence.mapper;

import com.surpass.entity.api.ApiVersion;
import org.apache.ibatis.annotations.Mapper;
import org.dromara.mybatis.jpa.IJpaMapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface ApiVersionMapper extends IJpaMapper<ApiVersion> {
    int findMaxVersionByApiId(@Param("apiId") String apiId);
}
