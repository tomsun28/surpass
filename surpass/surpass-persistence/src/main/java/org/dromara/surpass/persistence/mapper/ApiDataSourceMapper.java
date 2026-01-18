package org.dromara.surpass.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.mybatis.jpa.IJpaMapper;
import org.dromara.surpass.entity.api.ApiDataSource;

@Mapper
public interface ApiDataSourceMapper extends IJpaMapper<ApiDataSource> {
}
