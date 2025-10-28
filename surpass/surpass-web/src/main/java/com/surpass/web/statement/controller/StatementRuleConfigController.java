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
import com.surpass.entity.statement.StatementRules;
import com.surpass.persistence.service.StatementBalanceSheetConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 报表规则配置接口
 */

@RestController
@RequestMapping("/statement/config/rules")
@Slf4j
@RequiredArgsConstructor
public class StatementRuleConfigController {
    private final StatementBalanceSheetConfigService configService;

    /**
     * 获取报表项统计规则配置
     */
    @GetMapping
    public Message<List<StatementRules>> getRules(@RequestParam("itemCode") String itemCode) {
        return configService.getRules(itemCode);
    }

    /**
     * 报表项统计规则配置
     *
     * @param dto 配置项
     * @return 结果
     */
    @PostMapping("/{itemCode}")
    public Message<List<StatementRules>> saveRules(@Validated @RequestBody List<StatementRules> dto,
                                                   @PathVariable("itemCode") String itemCode,
                                                   @CurrentUser UserInfo userInfo) {
        return configService.saveRules(dto, userInfo.getBookId(), itemCode);
    }
}
