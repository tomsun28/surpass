package com.surpass.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.dromara.mybatis.jpa.IJpaMapper;

import com.surpass.entity.RegisteredClient;

@Mapper
public interface RegisteredClientMapper extends IJpaMapper<RegisteredClient> {
	
	@Select({})
    public RegisteredClient findByClientId(String clientId);
	
	@Update("""
            update surpass_client 
			    set last_login_time 	= #{lastLoginTime} 
			where id = #{id}
        """)
	public void updateLastLoginTime(RegisteredClient client);
	
}
