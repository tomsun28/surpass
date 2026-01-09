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


package org.maxkey.surpass.persistence.service;

import java.util.List;

import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.service.IJpaService;
import org.maxkey.surpass.entity.idm.UserInfo;
import org.maxkey.surpass.entity.permissions.RoleMember;
import org.maxkey.surpass.entity.permissions.Roles;
import org.maxkey.surpass.entity.permissions.dto.RoleMemberPageDto;

public interface RoleMemberService extends IJpaService<RoleMember> {
	List<UserInfo> queryMemberByRoleId(String groupId);

	JpaPageResults<Roles> rolesNoMember(RoleMemberPageDto dto);

	JpaPageResults<RoleMember> memberInRole(RoleMemberPageDto dto);

	JpaPageResults<RoleMember> memberNotInRole(RoleMemberPageDto dto);

}
