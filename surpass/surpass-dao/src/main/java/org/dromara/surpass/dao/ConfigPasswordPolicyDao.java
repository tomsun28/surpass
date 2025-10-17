package org.dromara.surpass.dao;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.mybatis.jpa.IJpaMapper;
import org.dromara.surpass.pojo.entity.config.ConfigPasswordPolicy;

@Mapper
public interface ConfigPasswordPolicyDao extends IJpaMapper<ConfigPasswordPolicy> {
}
