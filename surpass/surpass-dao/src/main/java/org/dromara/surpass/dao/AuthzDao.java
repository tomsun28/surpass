package org.dromara.surpass.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.dromara.mybatis.jpa.IJpaMapper;
import org.dromara.surpass.pojo.entity.dto.QueryGroupMembersDto;
import org.dromara.surpass.pojo.entity.idm.UserInfo;
import org.dromara.surpass.pojo.entity.permissions.Roles;

import java.util.List;

@Mapper
public interface AuthzDao extends IJpaMapper<UserInfo> {
    public List<Roles> queryRolesByMembers(QueryGroupMembersDto dto) ;

    @Select("select * from  jbx_userinfo where id = #{userId} and deleted = 'n'")
    public UserInfo findUserById(@Param("userId") String userId ) ;
}
