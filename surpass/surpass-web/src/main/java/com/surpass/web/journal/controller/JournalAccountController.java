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


package com.surpass.web.journal.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.authn.web.AuthorizationUtils;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.journal.JournalAccount;
import com.surpass.entity.journal.dto.JournalAccountDto;
import com.surpass.entity.journal.dto.JournalAccountPageDto;
import com.surpass.persistence.service.JournalAccountService;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/journal/account")
public class JournalAccountController {
    private static final Logger logger = LoggerFactory.getLogger(JournalAccountController.class);

    @Autowired
    JournalAccountService journalAccountService;

    @GetMapping(value = {"/fetch"})
    public Message<Page<JournalAccount>> fetch(@ParameterObject JournalAccountPageDto dto) {
    	dto.setBookId(AuthorizationUtils.getUserInfo().getBookId());
        logger.debug("fetch {}", dto);

        return journalAccountService.pageList(dto);
    }

    @GetMapping(value = {"/findAll"})
    public Message<List<JournalAccount>> findAll() {
        return journalAccountService.findAll(AuthorizationUtils.getUserInfo().getBookId());
    }

    /**
     * 所有账户余额汇总
     * @return
     */
    @GetMapping(value = {"/allBalance"})
    public Message<BigDecimal> allBalance() {
    	Message<List<JournalAccount>> accountListMsg = journalAccountService.findAll(AuthorizationUtils.getUserInfo().getBookId());
    	BigDecimal balance = BigDecimal.ZERO;
    	if(accountListMsg.getCode()== 0 && accountListMsg.getData()!= null) {
    		for(JournalAccount account : accountListMsg.getData()) {
    			balance = balance.add(account.getBalance());
    		}
    	}
    	logger.debug("Account balance ",balance);
        return Message.ok(balance);
    }

    @GetMapping("/get/{id}")
    public Message<JournalAccount> getById(@PathVariable(name = "id") String id) {
        return new Message<>(Message.SUCCESS, journalAccountService.getById(id));
    }

    @PostMapping("/add")
    public Message<String> add(@Validated(value = AddGroup.class) @RequestBody JournalAccountDto dto) {
    	dto.setBookId(AuthorizationUtils.getUserInfo().getBookId());
        return journalAccountService.save(dto);
    }

    @PutMapping("/update")
    public Message<String> update(@Validated(value = EditGroup.class) @RequestBody JournalAccountDto dto) {
    	dto.setBookId(AuthorizationUtils.getUserInfo().getBookId());
    	return journalAccountService.update(dto);
    }

    @DeleteMapping("/delete")
    public Message<String> delete(@Validated @ParameterObject ListIdsDto dto) {
        return journalAccountService.delete(dto);
    }
}
