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
import com.surpass.authn.web.AuthorizationUtils;
import com.surpass.entity.ExcelImport;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.hr.EmployeeTaxDeduction;
import com.surpass.entity.hr.dto.EmployeeTaxDeductionDto;
import com.surpass.entity.hr.dto.EmployeeTaxDeductionPageDto;
import com.surpass.entity.idm.UserInfo;
import com.surpass.persistence.service.EmployeeTaxDeductionService;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 员工税务扣除管理接口
 */

@RestController
@RequestMapping("/employee/taxdeduction")
@Slf4j
@RequiredArgsConstructor
public class EmployeeTaxDeductionController {
    private final EmployeeTaxDeductionService employeeTaxDeductionService;

    @GetMapping(value = {"/fetch"})
    public Message<Page<EmployeeTaxDeduction>> fetch(@ParameterObject EmployeeTaxDeductionPageDto dto) {
        log.debug("fetch {}", dto);
        dto.setBookId(AuthorizationUtils.getUserInfo().getBookId());
        return employeeTaxDeductionService.pageList(dto);
    }

    @GetMapping("/get/{id}")
    public Message<EmployeeTaxDeduction> getById(@PathVariable(name = "id") String id) {
        return Message.ok(employeeTaxDeductionService.getById(id));
    }

    @PostMapping("/add")
    public Message<String> save(@Validated(value = AddGroup.class) @RequestBody EmployeeTaxDeductionDto dto) {
    	dto.setBookId(AuthorizationUtils.getUserInfo().getBookId());
    	return employeeTaxDeductionService.save(dto);
    }

    @PutMapping("/update")
    public Message<String> update(@Validated(value = EditGroup.class) @RequestBody EmployeeTaxDeductionDto dto) {
    	dto.setBookId(AuthorizationUtils.getUserInfo().getBookId());
    	return employeeTaxDeductionService.update(dto);
    }

    @DeleteMapping("/delete")
    public Message<String> delete(@RequestBody ListIdsDto dto) {
        return employeeTaxDeductionService.delete(dto);
    }

    @PostMapping(value = "/import")
    public Message<String> importing(
            @ModelAttribute("excelImportFile") ExcelImport excelImportFile,
            @CurrentUser UserInfo currentUser) {
        if (excelImportFile.isExcelNotEmpty()) {
        	employeeTaxDeductionService.importFromExcel(excelImportFile,currentUser);
        }

        return new Message<>(Message.SUCCESS);

    }

}
