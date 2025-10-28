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


package com.surpass.web.hr.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.hr.EmployeeSalaryTemp;
import com.surpass.entity.hr.dto.CreateSalaryTableDto;
import com.surpass.entity.hr.dto.SalaryDetailChangeDto;
import com.surpass.entity.hr.dto.SalaryDetailPageDto;
import com.surpass.entity.idm.UserInfo;
import com.surpass.persistence.service.EmployeeSalaryTempService;
import com.surpass.validate.EditGroup;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/2/5 16:51
 */

@RestController
@RequestMapping("/salary/detail")
@Slf4j
public class EmployeeSalaryTempController {
    static final Logger logger = LoggerFactory.getLogger(EmployeeSalaryTempController.class);

    @Autowired
    private EmployeeSalaryTempService jbxSalaryDetailService;

    @GetMapping(value = {"/fetch"})
    public Message<Page<EmployeeSalaryTemp>> fetch(@ParameterObject SalaryDetailPageDto dto, @CurrentUser UserInfo currentUser) {
        dto.setBookId(currentUser.getBookId());
        log.debug("fetch {}", dto);
        return jbxSalaryDetailService.pageList(dto);
    }

    @PostMapping("/createTable")
    public Message<String> createTable(@Validated @RequestBody CreateSalaryTableDto dto, @CurrentUser UserInfo currentUser) {
        dto.setBookId(currentUser.getBookId());
        return jbxSalaryDetailService.createTable(dto);
    }

    @GetMapping("/get/{id}")
    public Message<EmployeeSalaryTemp> getById(@PathVariable(name = "id") String id) {
        return Message.ok(jbxSalaryDetailService.getById(id));
    }

    @PutMapping("/update")
    public Message<String> update(@Validated(value = EditGroup.class) @RequestBody SalaryDetailChangeDto dto,
                                  @CurrentUser UserInfo currentUser) {
        dto.setBookId(currentUser.getBookId());
        logger.debug("-update  {}", dto);
        return jbxSalaryDetailService.update(dto);
    }

    @PostMapping("/submit-detail")
    public Message<String> createFinalDetail(@CurrentUser UserInfo currentUser) {
        SalaryDetailPageDto dto = new SalaryDetailPageDto();
        dto.setBookId(currentUser.getBookId());
        logger.debug("-createFinalDetail  {}", dto);
        return jbxSalaryDetailService.createFinalDetail(dto);
    }

    @GetMapping("/re-calculate")
    public Message<EmployeeSalaryTemp> reCalculate(@ParameterObject EmployeeSalaryTemp employeeSalaryTemp) {
        return jbxSalaryDetailService.reCalculate(employeeSalaryTemp);
    }

    @DeleteMapping("/delete")
    public Message<String> delete(@RequestBody ListIdsDto dto) {
        return jbxSalaryDetailService.delete(dto);
    }
}
