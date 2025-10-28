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






package com.surpass.persistence.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.idm.UserInfo;
import com.surpass.entity.permissions.RoleMember;
import com.surpass.entity.permissions.Roles;
import com.surpass.entity.permissions.dto.RoleMemberPageDto;
import com.surpass.persistence.mapper.RoleMemberMapper;
import com.surpass.persistence.service.RoleMemberService;

import org.springframework.stereotype.Service;

@Service
public class RoleMemberServiceImpl  extends ServiceImpl<RoleMemberMapper,RoleMember> implements RoleMemberService {
	static final  Logger logger = LoggerFactory.getLogger(RoleMemberServiceImpl.class);

	@Autowired
	RoleMemberMapper groupMemberMapper;

	public RoleMemberMapper getMapper() {
		return groupMemberMapper;
	}

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
	public Page<Roles> rolesNoMember(Page page, RoleMemberPageDto dto) {
		return groupMemberMapper.rolesNoMember(page, dto);
	}

	@Override
	public Page<RoleMember> memberInRole(Page page, RoleMemberPageDto dto) {
		return groupMemberMapper.memberInRole(page, dto);
	}

	@Override
	public Page<RoleMember> memberNotInRole(Page page, RoleMemberPageDto dto) {
		return groupMemberMapper.memberNotInRole(page, dto);
	}

}
