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






package com.surpass.web.idm.contorller;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.authn.annotation.CurrentUser;
import com.surpass.constants.ConstsAct;
import com.surpass.constants.ConstsActResult;
import com.surpass.constants.ConstsEntryType;
import com.surpass.entity.Message;
import com.surpass.entity.idm.UserInfo;
import com.surpass.entity.permissions.Roles;
import com.surpass.entity.permissions.dto.RolesPageDto;
import com.surpass.persistence.service.RolesService;
import com.surpass.persistence.service.HistorySystemLogsService;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value={"/idm/groups"})
public class RolesController {
	static final Logger logger = LoggerFactory.getLogger(RolesController.class);

	@Autowired
	RolesService groupsService;

	@Autowired
	HistorySystemLogsService historySystemLogsService;

	@Autowired
	IdentifierGenerator identifierGenerator;

	@GetMapping(value = { "/fetch" }, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Page<Roles>> fetch(
			@ParameterObject RolesPageDto dto,
			@CurrentUser UserInfo currentUser) {

		LambdaQueryWrapper<Roles> wrapper = new LambdaQueryWrapper<>();

		if (StringUtils.isNotEmpty(dto.getRoleName())) {
			wrapper.like(Roles::getRoleName, dto.getRoleName());
		}
		return new Message<>(Message.SUCCESS, groupsService.page(dto.build(), wrapper));
	}

	@GetMapping(value={"/query"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Roles> query(@ModelAttribute Roles group,@CurrentUser UserInfo currentUser) {
		logger.debug("-query  : {}" , group);
		LambdaQueryWrapper<Roles> wrapper = new LambdaQueryWrapper<>();
		if (ObjectUtils.isNotEmpty(groupsService.list(wrapper))) {
			 return new Message<>(Message.SUCCESS);
		} else {
			 return new Message<>(Message.FAIL);
		}

	}

	@GetMapping(value = { "/get/{id}" }, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Roles> get(@PathVariable("id") String id,@CurrentUser UserInfo currentUser) {
		Roles group = groupsService.getById(id);
		return new Message<>(group);
	}

	@PostMapping(value={"/add"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Roles> insert(@Validated(value = AddGroup.class) @RequestBody Roles group, @CurrentUser UserInfo currentUser) {
		logger.debug("-Add  : {}" , group);
		group.setId(identifierGenerator.nextId("groups").toString());
		if(StringUtils.isBlank(group.getRoleCode())) {
			group.setRoleCode(group.getId());
		}
		group.setCreatedBy(currentUser.getId());
		if (groupsService.save(group)) {
			groupsService.refreshDynamicRoles(group);
		    historySystemLogsService.log(
					ConstsEntryType.ROLE,
					group,
					ConstsAct.CREATE,
					ConstsActResult.SUCCESS,
					currentUser);
		    return new Message<>(Message.SUCCESS);
		} else {
			return new Message<>(Message.FAIL);
		}
	}

	@PutMapping(value={"/update"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Roles> update(@Validated(value = EditGroup.class) @RequestBody Roles group,@CurrentUser UserInfo currentUser) {
		logger.debug("-update  group : {}" , group);
		if(group.getId().equalsIgnoreCase("ROLE_ALL_USER")) {
			group.setDefaultAllUser();
		}
		group.setModifiedBy(currentUser.getId());
		if (groupsService.updateById(group)) {
			groupsService.refreshDynamicRoles(group);
		    historySystemLogsService.log(
					ConstsEntryType.ROLE,
					group,
					ConstsAct.UPDATE,
					ConstsActResult.SUCCESS,
					currentUser);
		    return new Message<>(Message.SUCCESS);
		} else {
			return new Message<>(Message.FAIL);
		}
	}

	@DeleteMapping(value={"/delete"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Roles> delete(@RequestParam("ids") List<String> ids,@CurrentUser UserInfo currentUser) {
		logger.debug("-delete ids : {}" , ids);
		ids.removeAll(Arrays.asList("ROLE_ALL_USER","ROLE_ADMINISTRATORS","-1"));
		if (groupsService.removeByIds(ids)) {
			historySystemLogsService.log(
					ConstsEntryType.ROLE,
					ids,
					ConstsAct.DELETE,
					ConstsActResult.SUCCESS,
					currentUser);
			 return new Message<>(Message.SUCCESS);
		} else {
			return new Message<>(Message.FAIL);
		}
	}
}
