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




package org.dromara.surpass.persistence.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.dromara.surpass.constants.ConstsRoles;
import org.dromara.surpass.entity.dto.QueryGroupMembersDto;
import org.dromara.surpass.entity.idm.UserInfo;
import org.dromara.surpass.entity.permissions.Roles;
import org.dromara.surpass.persistence.mapper.AuthzMapper;
import org.dromara.surpass.persistence.service.AuthzService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

@Repository
public class AuthzServiceImpl   extends JpaServiceImpl<AuthzMapper,UserInfo> implements AuthzService {
	private static final Logger logger = LoggerFactory.getLogger(AuthzServiceImpl.class);

	@Autowired
	AuthzMapper authzMapper;

	@Override
	public List<Roles> queryRoles(UserInfo userInfo){
		// query groups for user
        QueryGroupMembersDto groupMembersDto = new QueryGroupMembersDto();
        groupMembersDto.add(userInfo.getId());
        List<Roles> listGroup = authzMapper.queryRolesByMembers(groupMembersDto);
        logger.debug("listGroup : {}" , listGroup);
        return listGroup;
	}

	@Override
	public List<Roles> queryRolesByMembers(UserInfo userInfo){
		// query groups for user
        QueryGroupMembersDto groupMembersDto = new QueryGroupMembersDto();
        groupMembersDto.add(userInfo.getId());
        List<Roles> listGroup = authzMapper.queryRolesByMembers(groupMembersDto);
        logger.debug("listGroup : {}" , listGroup);
        return listGroup;
	}

	/**
     * grant Authority by userinfo
     *
     * @param userInfo
     * @return ArrayList<GrantedAuthority>
     */
	@Override
    public List<GrantedAuthority> grantAuthority(UserInfo userInfo) {
    	List<Roles> listGroup = queryRoles(userInfo);
        //set default groups
        ArrayList<GrantedAuthority> grantedAuthority = new ArrayList<>();
        grantedAuthority.add(ConstsRoles.ROLE_USER);
        grantedAuthority.add(ConstsRoles.ROLE_ALL_USER);
        grantedAuthority.add(ConstsRoles.ROLE_GENERAL_USER);
        for (Roles group : listGroup) {
            grantedAuthority.add(new SimpleGrantedAuthority(group.getId()));
            //Group Code和id不同的情况
            if(!grantedAuthority.contains(new SimpleGrantedAuthority(group.getRoleCode()))) {
            	grantedAuthority.add(new SimpleGrantedAuthority(group.getRoleCode()));
            }
            //判断角色类型
        	if(group.getCategory().equals(ConstsRoles.Category.SUPERVISOR)) {
        		grantedAuthority.add(ConstsRoles.ROLE_SUPERVISOR);
        	}else if(group.getCategory().equals(ConstsRoles.Category.ADMINISTRATOR)) {
        		grantedAuthority.add(ConstsRoles.ROLE_ADMINISTRATOR);
        	}else if(group.getCategory().equals(ConstsRoles.Category.MANAGER)) {
        		grantedAuthority.add(ConstsRoles.ROLE_MANAGER);
        	}
        }
        logger.debug("Authority : {}" , grantedAuthority);
        return grantedAuthority;
    }

    public UserInfo findUserById(String userId) {
		return authzMapper.findUserById(userId);
	}

	public List<Roles> queryGroupsByMembers(QueryGroupMembersDto dto) {
		return authzMapper.queryRolesByMembers(dto);
	}

}
