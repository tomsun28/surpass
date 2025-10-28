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


package com.surpass.web.statement.controller;

import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.Message;
import com.surpass.entity.idm.UserInfo;
import com.surpass.entity.statement.StatementIncomeItem;
import com.surpass.persistence.service.StatementIncomeConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 报表利润表配置接口
 */

@RestController
@RequestMapping("/statement/config/income")
@Slf4j
@RequiredArgsConstructor
public class StatementIncomeConfigController {

    private final StatementIncomeConfigService statementIncomeConfigService;

    /**
     * 获取利润表配信
     *
     * @param itemCode 主键
     */
    @GetMapping(value = {"/{itemCode}"})
    public Message<StatementIncomeItem> get(@PathVariable("itemCode") String itemCode,@CurrentUser UserInfo userInfo) {
        return statementIncomeConfigService.get(userInfo.getBookId(), itemCode);
    }

    /**
     * 获取利润表配置
     */
    @GetMapping(value = {"/fetch"})
    public Message<List<StatementIncomeItem>> fetch(@CurrentUser UserInfo userInfo) {
        return statementIncomeConfigService.list(userInfo.getBookId());
    }

    /**
     * 利润表配置
     *
     * @param dto 查询参数
     * @return 结果
     */
    @PostMapping
    public Message<StatementIncomeItem> save(@Validated @RequestBody StatementIncomeItem dto,
                                                           @CurrentUser UserInfo userInfo) {
        dto.setBookId(userInfo.getBookId());
        return statementIncomeConfigService.save(dto);
    }

    /**
     * 利润表配置
     *
     * @param id 被删除项ID
     * @return 结果
     */
    @DeleteMapping(value = {"/{id}"})
    public Message<Boolean> delete(@PathVariable("id") String id) {
        return statementIncomeConfigService.delete(id);
    }
}
