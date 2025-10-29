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






package com.surpass.web.config.contorller;

import java.awt.print.Book;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.authn.annotation.CurrentUser;
import com.surpass.constants.ConstsAct;
import com.surpass.constants.ConstsActResult;
import com.surpass.constants.ConstsEntryType;
import com.surpass.entity.Institutions;
import com.surpass.entity.Message;
import com.surpass.entity.dto.InstitutionsPageDto;
import com.surpass.entity.idm.UserInfo;
import com.surpass.persistence.service.HistorySystemLogsService;
import com.surpass.persistence.service.InstitutionsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
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
@RequestMapping(value={"/config/institutions"})
public class InstitutionsController {
	static final Logger logger = LoggerFactory.getLogger(InstitutionsController.class);

	@Autowired
	InstitutionsService institutionsService;

	@Autowired
	HistorySystemLogsService historySystemLogsService;

	@GetMapping(value={"/getCurrent"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Institutions> getCurrent(@CurrentUser UserInfo currentUser){
		LambdaQueryWrapper<Institutions> wrapper = new LambdaQueryWrapper<>();
		wrapper.isNotNull(Institutions::getId);
		Institutions inst = institutionsService.getOne(wrapper);
		return new Message<>(Message.SUCCESS,inst);
	}

	@PutMapping(value={"/updateCurrent"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Institutions> updateCurrent(
			@RequestBody  Institutions inst,
			@CurrentUser UserInfo currentUser,
			BindingResult result) {
		logger.debug("update {} ",inst);
		if(institutionsService.updateById(inst)) {
			return new Message<>(Message.SUCCESS);
		} else {
			return new Message<>(Message.FAIL);
		}
	}

	@GetMapping(value = { "/fetch" }, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Page<Institutions>> fetch(@ParameterObject InstitutionsPageDto dto,
											 @CurrentUser UserInfo currentUser) {
		logger.debug("fetch {}" , dto);
		Page<Institutions> page = institutionsService.fetch(dto);
		return new Message<>(Message.SUCCESS, page);
	}

	@GetMapping(value={"/query"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<List<Institutions>> query(@ModelAttribute Institutions inst,@CurrentUser UserInfo currentUser) {
		logger.debug("-query  {}" , inst);
		LambdaQueryWrapper<Institutions> wrapper = new LambdaQueryWrapper<>();
		List<Institutions>  instsList = institutionsService.list(wrapper);
		if (instsList != null) {
			 return new Message<>(Message.SUCCESS,instsList);
		} else {
			 return new Message<>(Message.FAIL);
		}
	}

	@GetMapping(value = { "/get/{id}" }, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Institutions> get(@PathVariable("id") String id) {
		Institutions inst=institutionsService.getById(id);
		return new Message<>(inst);
	}

	@PostMapping(value={"/add"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Institutions> insert(@RequestBody Institutions inst,@CurrentUser UserInfo currentUser) {
		logger.debug("-Add  : {}" , inst);
		if (institutionsService.save(inst)) {
			historySystemLogsService.log(
					ConstsEntryType.POST,
					inst,
					ConstsAct.CREATE,
					ConstsActResult.SUCCESS,
					currentUser);
			return new Message<>(Message.SUCCESS);
		} else {
			return new Message<>(Message.FAIL);
		}
	}

	@PutMapping(value={"/update"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Institutions> update(@RequestBody  Institutions inst,@CurrentUser UserInfo currentUser) {
		logger.debug("-update  : {}" , inst);
		if (institutionsService.updateById(inst)) {
			historySystemLogsService.log(
					ConstsEntryType.POST,
					inst,
					ConstsAct.UPDATE,
					ConstsActResult.SUCCESS,
					currentUser);
		    return new Message<>(Message.SUCCESS);
		} else {
			return new Message<>(Message.FAIL);
		}
	}

	@DeleteMapping(value={"/delete"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Institutions> delete(@RequestParam("ids") List<String> ids,@CurrentUser UserInfo currentUser) {
		logger.debug("-delete  ids : {} " , ids);
		if (institutionsService.removeByIds(ids)) {
			historySystemLogsService.log(
					ConstsEntryType.POST,
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
