package com.surpass.persistence.mapper;

import com.surpass.entity.app.AppClient;
import org.apache.ibatis.annotations.Mapper;
import org.dromara.mybatis.jpa.IJpaMapper;

@Mapper
public interface AppClientMapper extends IJpaMapper<AppClient> {
}
