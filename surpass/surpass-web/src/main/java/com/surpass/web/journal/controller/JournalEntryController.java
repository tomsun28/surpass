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
import com.surpass.authn.annotation.CurrentUser;
import com.surpass.authn.web.AuthorizationUtils;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.idm.UserInfo;
import com.surpass.entity.journal.JournalEntry;
import com.surpass.entity.journal.dto.JournalEntryDto;
import com.surpass.entity.journal.dto.JournalEntryPageDto;
import com.surpass.entity.voucher.dto.GenerateVoucherDto;
import com.surpass.persistence.service.JournalEntryService;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/journal/entry")
public class JournalEntryController {
    private static final Logger logger = LoggerFactory.getLogger(JournalEntryController.class);

    @Autowired
    JournalEntryService journalEntryService;

    @GetMapping(value = {"/fetch"})
    public Message<Page<JournalEntry>> fetch(@ParameterObject JournalEntryPageDto dto) {
    	dto.setBookId(AuthorizationUtils.getUserInfo().getBookId());
        logger.debug("fetch {}", dto);

        return journalEntryService.pageList(dto);
    }


    @GetMapping("/get/{id}")
    public Message<JournalEntry> getById(@PathVariable(name = "id") String id) {
        return new Message<>(Message.SUCCESS, journalEntryService.getById(id));
    }

    @PostMapping("/add")
    public Message<String> add(@Validated(value = AddGroup.class) @RequestBody JournalEntryDto dto) {
    	dto.setBookId(AuthorizationUtils.getUserInfo().getBookId());
        return journalEntryService.save(dto);
    }

    @PutMapping("/update")
    public Message<String> update(@Validated(value = EditGroup.class) @RequestBody JournalEntryDto dto) {
    	dto.setBookId(AuthorizationUtils.getUserInfo().getBookId());
        return journalEntryService.update(dto);
    }

    @DeleteMapping("/delete")
    public Message<String> delete(@Validated @ParameterObject ListIdsDto dto) {
        return journalEntryService.delete(dto);
    }

    @PostMapping("/generate-voucher")
    public Message<String> generateVoucher(@Validated @RequestBody GenerateVoucherDto dto, @CurrentUser UserInfo currentUser) {
        dto.setBookId(currentUser.getBookId());
        return journalEntryService.generateVoucher(dto);
    }
}
