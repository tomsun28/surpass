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


package com.surpass.persistence.service;

import com.surpass.entity.Message;
import com.surpass.entity.standard.StandardStatementIncome;
import com.surpass.entity.standard.StandardStatementRules;

import java.util.List;

/**
 * 财务报表配置业务接口
 */
public interface StandardStatementIncomeService {

    Message<StandardStatementIncome> get(StandardStatementIncome dto);

    /**
     * 资产负载表配置
     */
    Message<StandardStatementIncome> save(StandardStatementIncome dto);

    public Message<List<StandardStatementIncome>> list(StandardStatementIncome dto);

    /**
     * 报表规则配置
     * @param dto 配置项
     * @return 结果
     */
    Message<List<StandardStatementRules>> saveRules(List<StandardStatementRules> dto,String standardId, String itemCode);


    Message<List<StandardStatementRules>> getRules(String standardId,String itemCode);

    Message<Boolean> delete(String id);

}
