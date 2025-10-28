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
import com.surpass.entity.hr.Employee;
import com.surpass.entity.hr.dto.EmployeeChangeDto;
import com.surpass.entity.hr.dto.EmployeePageDto;
import com.surpass.entity.idm.UserInfo;
import com.surpass.persistence.service.EmployeeService;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 员工信息管理接口
 */

@RestController
@RequestMapping("/salary/employee")
@Slf4j
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping(value = {"/fetch"})
    public Message<Page<Employee>> fetch(@ParameterObject EmployeePageDto dto,
                                         @CurrentUser UserInfo currentUser) {
        log.debug("fetch {}", dto);
        dto.setBookId(currentUser.getBookId());
        return employeeService.pageList(dto);
    }

    @GetMapping("/get/{id}")
    public Message<Employee> getById(@PathVariable(name = "id") String id) {
        return Message.ok(employeeService.getById(id));
    }

    @PostMapping("/save")
    public Message<String> save(@Validated(value = AddGroup.class) @RequestBody EmployeeChangeDto dto,
                                @CurrentUser UserInfo currentUser) {
        dto.setBookId(currentUser.getBookId());
        return employeeService.save(dto);
    }

    @PutMapping("/update")
    public Message<String> update(@Validated(value = EditGroup.class) @RequestBody EmployeeChangeDto dto) {
        return employeeService.update(dto);
    }

    @DeleteMapping("/delete")
    public Message<String> delete(@RequestBody ListIdsDto dto) {
        return employeeService.delete(dto);
    }

}
