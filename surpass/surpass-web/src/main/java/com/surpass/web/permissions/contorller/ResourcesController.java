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






package com.surpass.web.permissions.contorller;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.authn.annotation.CurrentUser;
import com.surpass.constants.ConstsAct;
import com.surpass.constants.ConstsActResult;
import com.surpass.constants.ConstsEntryType;
import com.surpass.entity.Message;
import com.surpass.entity.TreeAttributes;
import com.surpass.entity.TreeNode;
import com.surpass.entity.idm.UserInfo;
import com.surpass.entity.permissions.Resources;
import com.surpass.entity.permissions.dto.ResourcesPageDto;
import com.surpass.persistence.service.HistorySystemLogsService;
import com.surpass.persistence.service.ResourcesService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
@RequestMapping(value={"/permissions/resources"})
public class ResourcesController {
	static final Logger logger = LoggerFactory.getLogger(ResourcesController.class);

	static String rootId = "1";

	@Autowired
	ResourcesService resourcesService;

	@Autowired
	HistorySystemLogsService historySystemLogsService;

	@Autowired
	IdentifierGenerator identifierGenerator;

	@GetMapping(value = { "/fetch" }, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Page<Resources>> fetch(@ParameterObject ResourcesPageDto dto, @CurrentUser UserInfo currentUser) {
		logger.debug("fetch {}" , dto);
		return resourcesService.pageList(dto);
	}

	@GetMapping(value={"/query"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<List<Resources>>  query(@ParameterObject ResourcesPageDto dto,@CurrentUser UserInfo currentUser) {
		logger.debug("-query  {}" , dto);
		LambdaQueryWrapper<Resources> wrapper = new LambdaQueryWrapper<>();
		List<Resources>  resourceList = resourcesService.list(wrapper);
		if (resourceList != null) {
			 return new Message<>(Message.SUCCESS,resourceList);
		} else {
			 return new Message<>(Message.FAIL);
		}
	}

	@GetMapping(value = { "/get/{id}" }, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Resources> get(@PathVariable("id") String id) {
		Resources resource=resourcesService.getById(id);
		return new Message<>(resource);
	}

	@PostMapping(value={"/add"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Resources> insert(@RequestBody Resources resource,@CurrentUser UserInfo currentUser) {
		logger.debug("-Add  : {}" , resource);
		resource.setId(identifierGenerator.nextId(resource).toString());
		if(StringUtils.isBlank(resource.getPermission())) {
			resource.setPermission(resource.getId());
		}
		if (resourcesService.save(resource)) {
			historySystemLogsService.log(
					ConstsEntryType.RESOURCE,
					resource,
					ConstsAct.CREATE,
					ConstsActResult.SUCCESS,
					currentUser);
			return new Message<>(Message.SUCCESS);
		} else {
			return new Message<>(Message.FAIL);
		}
	}

	@PutMapping(value={"/update"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Resources> update(@RequestBody  Resources resource,@CurrentUser UserInfo currentUser) {
		logger.debug("-update  : {}" , resource);
		if (resourcesService.updateById(resource)) {
			historySystemLogsService.log(
					ConstsEntryType.RESOURCE,
					resource,
					ConstsAct.UPDATE,
					ConstsActResult.SUCCESS,
					currentUser);
		    return new Message<>(Message.SUCCESS);
		} else {
			return new Message<>(Message.FAIL);
		}
	}

	@DeleteMapping(value={"/delete"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Resources> delete(@RequestParam("ids") List<String> ids,@CurrentUser UserInfo currentUser) {
		logger.debug("-delete  ids : {} " , ids);
		if (resourcesService.removeByIds(ids)) {
			historySystemLogsService.log(
					ConstsEntryType.RESOURCE,
					ids,
					ConstsAct.DELETE,
					ConstsActResult.SUCCESS,
					currentUser);
			 return new Message<>(Message.SUCCESS);
		} else {
			return new Message<>(Message.FAIL);
		}
	}

	@GetMapping(value={"/tree"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<TreeAttributes> tree(@ModelAttribute Resources resource,@CurrentUser UserInfo currentUser) {
		logger.debug("-query  {}" , resource);
		List<Resources>  resourceList = resourcesService.list(Wrappers.<Resources>lambdaQuery());
		if (resourceList != null) {
			TreeAttributes treeAttributes = new TreeAttributes();
			int nodeCount = 0;
			for (Resources r : resourceList) {
				TreeNode treeNode = new TreeNode(r.getId(),r.getResName());
				treeNode.setParentKey(r.getParentId());
				treeNode.setParentTitle(r.getParentName());
				treeNode.setAttrs(r);
				treeNode.setLeaf(true);
				treeAttributes.addNode(treeNode);
				nodeCount ++;
				if(r.getId().equalsIgnoreCase(rootId)) {
					treeNode.setExpanded(true);
					treeNode.setLeaf(false);
					treeAttributes.setRootNode(treeNode);
				}
			}

			TreeNode rootNode = new TreeNode("1","Surpass");
			rootNode.setParentKey("1");
			rootNode.setExpanded(true);
			rootNode.setLeaf(false);
			treeAttributes.setRootNode(rootNode);

			treeAttributes.setNodeCount(nodeCount);
			 return new Message<>(Message.SUCCESS,treeAttributes);
		} else {
			 return new Message<>(Message.FAIL);
		}
	}

}
