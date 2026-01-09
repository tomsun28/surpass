package org.maxkey.surpass.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.mybatis.jpa.IJpaMapper;
import org.maxkey.surpass.entity.api.ApiDataSource;

@Mapper
public interface ApiDataSourceMapper extends IJpaMapper<ApiDataSource> {
}
