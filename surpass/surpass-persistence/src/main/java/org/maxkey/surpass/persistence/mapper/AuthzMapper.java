/*
 * Copyright [2025] [Surpass of copyright http://www.surpass.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */



package org.maxkey.surpass.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.dromara.mybatis.jpa.IJpaMapper;
import org.maxkey.surpass.entity.dto.QueryGroupMembersDto;
import org.maxkey.surpass.entity.idm.UserInfo;
import org.maxkey.surpass.entity.permissions.Roles;

@Mapper
public  interface AuthzMapper extends IJpaMapper<UserInfo> {


	public List<Roles> queryRolesByMembers(QueryGroupMembersDto dto) ;

	@Select("select * from  surpass_userinfo where id = #{userId} and deleted = 'n'")
	public UserInfo findUserById(@Param ("userId") String userId ) ;

}
