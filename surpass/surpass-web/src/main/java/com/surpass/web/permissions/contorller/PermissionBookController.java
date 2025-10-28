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

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.Message;
import com.surpass.entity.book.Book;
import com.surpass.entity.idm.UserInfo;
import com.surpass.entity.permissions.PermissionBook;
import com.surpass.entity.permissions.RoleMember;
import com.surpass.entity.permissions.dto.PermissionBookDto;
import com.surpass.entity.permissions.dto.PermissionBookPageDto;
import com.surpass.persistence.service.HistorySystemLogsService;
import com.surpass.persistence.service.PermissionBookService;
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
@RequestMapping(value={"/permissions/permissionBook"})
public class PermissionBookController {
	static final Logger logger = LoggerFactory.getLogger(PermissionBookController.class);

	@Autowired
	PermissionBookService permissionBookService;

	@Autowired
	HistorySystemLogsService historySystemLogsService;

	@Autowired
	IdentifierGenerator identifierGenerator;

	@GetMapping(value = { "/userAccessBook" })
	public Message<Page<Book>> userAccessBook(@ParameterObject PermissionBookPageDto dto,
													@CurrentUser UserInfo currentUser) {
		dto.setInstId(currentUser.getInstId());
		logger.debug("userAccessBook : {}",dto);
		return Message.ok(permissionBookService.userAccessBook(dto.build(), dto));
	}

	@GetMapping(value = { "/userNotAccessBook" })
	public Message<Page<Book>> userNotAccessBook(@ParameterObject PermissionBookPageDto dto,
													   @CurrentUser UserInfo currentUser) {
		dto.setInstId(currentUser.getInstId());
		logger.debug("userNotAccessBook : {}",dto);
		return Message.ok(permissionBookService.userNotAccessBook(dto.build(), dto));
	}


	/**
	 * Members add to the Group
	 * @param currentUser
	 * @return
	 */
	@PostMapping(value = {"/add"})
	public Message<PermissionBook> add(@Validated @RequestBody PermissionBookDto dto,@CurrentUser UserInfo currentUser) {
		boolean result = true;
		for (int i = 0; i < dto.bookIds().size(); i++) {
			PermissionBook newPermission =
					new PermissionBook(
							dto.userId(),
							dto.bookIds().get(i));
			result = permissionBookService.save(newPermission);
		}
		if(result) {
			return new Message<>(Message.SUCCESS);
		}
		return new Message<>(Message.FAIL);
	}

	@DeleteMapping(value={"/delete"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<RoleMember> delete(@RequestParam("ids") List<String> ids,@CurrentUser UserInfo currentUser) {
		logger.debug("-delete ids : {}" , ids);
		if (permissionBookService.removeBatchByIds(ids)) {
			 return new Message<>(Message.SUCCESS);
		} else {
			return new Message<>(Message.FAIL);
		}
	}

}
