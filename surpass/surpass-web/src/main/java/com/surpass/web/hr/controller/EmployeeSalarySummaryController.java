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
import com.surpass.entity.hr.EmployeeSalarySummary;
import com.surpass.entity.hr.dto.SalaryDetailPageDto;
import com.surpass.entity.hr.dto.SalarySummaryChangeDto;
import com.surpass.entity.idm.UserInfo;
import com.surpass.persistence.service.EmployeeSalarySummaryService;
import com.surpass.validate.AddGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.YearMonth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/2/27 17:46
 */

@RestController
@RequestMapping("/employee/salary-summary")
@Slf4j
@RequiredArgsConstructor
public class EmployeeSalarySummaryController {
    static final Logger logger = LoggerFactory.getLogger(EmployeeSalarySummaryController.class);

    private final EmployeeSalarySummaryService employeeSalarySummaryService;

    @PostMapping("/save")
    public Message<String> save(@Validated(value = AddGroup.class) @RequestBody SalarySummaryChangeDto dto,
                                @CurrentUser UserInfo currentUser) {
        dto.setBookId(currentUser.getBookId());
        return employeeSalarySummaryService.save(dto);
    }

    @GetMapping(value = {"/fetch"})
    public Message<Page<EmployeeSalarySummary>> fetch(@ParameterObject SalaryDetailPageDto dto, @CurrentUser UserInfo currentUser) {
        dto.setBookId(currentUser.getBookId());
        logger.debug("fetch {}", dto);
        return employeeSalarySummaryService.pageList(dto);
    }

    @GetMapping("/summary")
    public Message<EmployeeSalarySummary> summary(@ParameterObject SalarySummaryChangeDto dto,@CurrentUser UserInfo currentUser) {
    	dto.setBookId(currentUser.getBookId());
    	if(dto.getBelongDateRange()!= null && dto.getBelongDateRange().length ==2) {
    		dto.setBelongDate(YearMonth.parse(dto.getBelongDateRange()[0]));
    		dto.setStartDateRange(dto.getBelongDateRange()[0]);
    		dto.setEndDateRange(dto.getBelongDateRange()[1]);
    	}
        return Message.ok(employeeSalarySummaryService.selectSalarySummary(dto));
    }

}
