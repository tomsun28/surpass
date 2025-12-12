package com.surpass.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.dromara.mybatis.jpa.IJpaMapper;

import com.surpass.entity.RegisteredClient;

@Mapper
public interface RegisteredClientMapper extends IJpaMapper<RegisteredClient> {
	
	@Select({})
    public RegisteredClient findByClientId(String clientId);
}
