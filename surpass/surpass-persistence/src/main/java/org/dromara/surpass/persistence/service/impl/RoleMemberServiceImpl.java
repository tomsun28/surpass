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

import java.util.List;

import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.dromara.surpass.entity.idm.UserInfo;
import org.dromara.surpass.entity.permissions.RoleMember;
import org.dromara.surpass.entity.permissions.Roles;
import org.dromara.surpass.entity.permissions.dto.RoleMemberPageDto;
import org.dromara.surpass.persistence.mapper.RoleMemberMapper;
import org.dromara.surpass.persistence.service.RoleMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleMemberServiceImpl extends JpaServiceImpl<RoleMemberMapper, RoleMember> implements RoleMemberService {
	static final  Logger logger = LoggerFactory.getLogger(RoleMemberServiceImpl.class);

	@Autowired
	RoleMemberMapper roleMemberMapper;

	public int addDynamicRoleMember(Roles dynamicGroup) {
	    return getMapper().addDynamicRoleMember(dynamicGroup);
	}

	public int deleteDynamicRoleMember(Roles dynamicGroup) {
	    return getMapper().deleteDynamicRoleMember(dynamicGroup);
	}

	public int deleteByRoleId(String groupId) {
        return getMapper().deleteByRoleId(groupId);
    }

	@Override
	public List<UserInfo> queryMemberByRoleId(String groupId){
		return getMapper().queryMemberByRoleId(groupId);
	}


	@Override
	public JpaPageResults<Roles> rolesNoMember(RoleMemberPageDto dto) {
		dto.build();
		return (JpaPageResults<Roles>) this.buildPageResults(dto, roleMemberMapper.rolesNoMember(dto));
	}

	@Override
	public JpaPageResults<RoleMember> memberInRole(RoleMemberPageDto dto) {
		dto.build();
		return (JpaPageResults<RoleMember>) this.buildPageResults(dto, roleMemberMapper.memberInRole(dto));
	}

	@Override
	public JpaPageResults<RoleMember> memberNotInRole(RoleMemberPageDto dto) {
		dto.build();
		return (JpaPageResults<RoleMember>) this.buildPageResults(dto, roleMemberMapper.memberNotInRole(dto));
	}

}
