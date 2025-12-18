package com.surpass.persistence.mapper;

import com.surpass.entity.app.AppResources;
import org.apache.ibatis.annotations.Mapper;
import org.dromara.mybatis.jpa.IJpaMapper;

@Mapper
public interface AppResourcesMapper extends IJpaMapper<AppResources> {
}
