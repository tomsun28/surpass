package com.surpass.persistence.mapper;

import com.surpass.entity.api.DataSource;
import org.apache.ibatis.annotations.Mapper;
import org.dromara.mybatis.jpa.IJpaMapper;

@Mapper
public interface DataSourceMapper extends IJpaMapper<DataSource> {
}
