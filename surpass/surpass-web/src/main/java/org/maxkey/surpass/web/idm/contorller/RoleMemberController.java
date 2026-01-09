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






package org.maxkey.surpass.web.idm.contorller;

import java.util.List;

import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.maxkey.surpass.authn.annotation.CurrentUser;
import org.maxkey.surpass.authn.web.AuthorizationUtils;
import org.maxkey.surpass.constants.ConstsRoles;
import org.maxkey.surpass.entity.Message;
import org.maxkey.surpass.entity.idm.UserInfo;
import org.maxkey.surpass.entity.permissions.RoleMember;
import org.maxkey.surpass.entity.permissions.Roles;
import org.maxkey.surpass.entity.permissions.dto.RoleMemberDto;
import org.maxkey.surpass.entity.permissions.dto.RoleMemberPageDto;
import org.maxkey.surpass.entity.permissions.dto.RoleMemberUserGroupsDto;
import org.maxkey.surpass.persistence.service.HistorySystemLogsService;
import org.maxkey.surpass.persistence.service.RoleMemberService;
import org.maxkey.surpass.persistence.service.RolesService;
import org.maxkey.surpass.persistence.service.UserInfoService;
import org.maxkey.surpass.web.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value={"/idm/groupmembers"})
public class RoleMemberController {
	static final Logger logger = LoggerFactory.getLogger(RoleMemberController.class);

	@Autowired
	RoleMemberService groupMemberService;

	@Autowired
	RolesService groupsService;

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	HistorySystemLogsService historySystemLogsService;

	@GetMapping(value = { "/fetch" }, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<JpaPageResults<RoleMember>> fetch(
			@ParameterObject RoleMemberPageDto dto,
			@CurrentUser UserInfo currentUser) {
		logger.debug("fetch {}",dto);
		LambdaQuery<RoleMember> wrapper = new LambdaQuery<>();
		if(AuthorizationUtils.getAuthentication().getAuthorities().contains(ConstsRoles.ROLE_MANAGER)){
			logger.debug("Has ROLE_MANAGERS {}" ,currentUser.getId());
			wrapper.eq(RoleMember::getGradingUserId, currentUser.getId());
		}
		dto.build();
		JpaPageResults<RoleMember> page = groupMemberService.fetch(dto, wrapper);
		return new Message<>(Message.SUCCESS, page);
	}

	@GetMapping(value = { "/memberInGroup" })
	public Message<JpaPageResults<RoleMember>> memberInGroup(@ParameterObject RoleMemberPageDto dto,
															 @CurrentUser UserInfo currentUser) {
		logger.debug("groupMember : {}",dto);
		dto.build();

		return new Message<>(Message.SUCCESS, groupMemberService.memberInRole(dto));
	}

	@GetMapping(value = { "/memberNotInGroup" })
	public Message<JpaPageResults<RoleMember>> memberNotInGroup(@ParameterObject RoleMemberPageDto dto,
													   @CurrentUser UserInfo currentUser) {

		return new Message<>(groupMemberService.memberNotInRole(dto));
	}

	@GetMapping(value = { "/groupsNoMember" })
	public Message<JpaPageResults<Roles>> groupsNoMember(@ParameterObject RoleMemberPageDto dto, @CurrentUser UserInfo currentUser) {
		return new Message<>(Message.SUCCESS, groupMemberService.rolesNoMember(dto));
	}

	/**
	 * Members add to the Group
	 * @param currentUser
	 * @return
	 */
	@PostMapping(value = {"/add"})
	public Message<RoleMember> addGroupMember(@Validated @RequestBody RoleMemberDto dto,@CurrentUser UserInfo currentUser) {
		boolean result = true;
		for (int i = 0; i < dto.getMemberIds().size(); i++) {
			RoleMember newGroupMember =
					new RoleMember(
							dto.getRoleId(),
							dto.getMemberIds().get(i),
							dto.getType());
			newGroupMember.setId(WebContext.genId());
			result = groupMemberService.insert(newGroupMember);
		}
		if(result) {
			return new Message<>(Message.SUCCESS);
		}
		return new Message<>(Message.FAIL);
	}


	/**
	 * Member add to Groups
	 * @param currentUser
	 * @return
	 */
	@PostMapping(value = {"/addMember2Groups"})
	public Message<RoleMember> addMember2Groups(@Validated @RequestBody RoleMemberUserGroupsDto dto, @CurrentUser UserInfo currentUser) {
		UserInfo userInfo = userInfoService.findByUsername(dto.getUsername());

		boolean result = true;
		for (int i = 0; i < dto.getGroupIds().size(); i++) {
			RoleMember newGroupMember =
					new RoleMember(
							dto.getGroupIds().get(i),
							userInfo.getId(),
							"USER");
			newGroupMember.setId(WebContext.genId());
			result = groupMemberService.insert(newGroupMember);
		}
		if(result) {
			return new Message<>(Message.SUCCESS);
		}
		return new Message<>(Message.FAIL);
	}

	@DeleteMapping(value={"/delete"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<RoleMember> delete(@RequestParam("ids") List<String> ids,@CurrentUser UserInfo currentUser) {
		logger.debug("-delete ids : {}" , ids);
		if (groupMemberService.deleteBatch(ids)) {
			 return new Message<>(Message.SUCCESS);
		} else {
			return new Message<>(Message.FAIL);
		}
	}
}
