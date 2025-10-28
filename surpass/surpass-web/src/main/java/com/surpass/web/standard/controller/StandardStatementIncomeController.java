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


package com.surpass.web.standard.controller;

import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.Message;
import com.surpass.entity.idm.UserInfo;
import com.surpass.entity.standard.StandardStatementIncome;
import com.surpass.persistence.service.StandardStatementIncomeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 准则报表利润表配置接口
 */

@RestController
@RequestMapping("/standardstatementincome/")
@Slf4j
@RequiredArgsConstructor
public class StandardStatementIncomeController {

    private final StandardStatementIncomeService standardStatementIncomeService;

    /**
     * 获取利润表配信
     *
     * @param id 主键
     */
    @GetMapping(value = {"/get"})
    public Message<StandardStatementIncome> get(@ParameterObject StandardStatementIncome dto) {
        return standardStatementIncomeService.get(dto);
    }

    /**
     * 获取利润表配置
     *
     * @param
     */
    @GetMapping(value = {"/fetch"})
    public Message<List<StandardStatementIncome>> fetch(@ParameterObject StandardStatementIncome dto,@CurrentUser UserInfo userInfo) {
        return standardStatementIncomeService.list(dto);
    }

    /**
     * 利润表配置
     *
     * @param dto 查询参数
     * @return 结果
     */
    @PostMapping(value = {"/save"})
    public Message<StandardStatementIncome> save(@Validated @RequestBody StandardStatementIncome dto,
                                                           @CurrentUser UserInfo userInfo) {
        return standardStatementIncomeService.save(dto);
    }

    /**
     * 利润表配置
     *
     * @param id 被删除项ID
     * @return 结果
     */
    @DeleteMapping(value = {"/delete/{id}"})
    public Message<Boolean> delete(@PathVariable("id") String id) {
        return standardStatementIncomeService.delete(id);
    }

}
