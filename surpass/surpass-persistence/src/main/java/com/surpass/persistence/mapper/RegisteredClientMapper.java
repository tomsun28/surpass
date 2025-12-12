package com.surpass.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.dromara.mybatis.jpa.IJpaMapper;

import com.surpass.entity.RegisteredClient;

@Mapper
public interface RegisteredClientMapper extends IJpaMapper<RegisteredClient> {
}
