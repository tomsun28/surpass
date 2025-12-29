package com.surpass.persistence.mapper;

import com.surpass.entity.api.ApiDataSource;
import org.apache.ibatis.annotations.Mapper;
import org.dromara.mybatis.jpa.IJpaMapper;

@Mapper
public interface ApiDataSourceMapper extends IJpaMapper<ApiDataSource> {
}
