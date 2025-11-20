package com.surpass.persistence.mapper;

import com.surpass.entity.api.ApiDefinition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.mybatis.jpa.IJpaMapper;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/11/18 16:44
 */

@Mapper
public interface ApiDefinitionMapper extends IJpaMapper<ApiDefinition> {

}
