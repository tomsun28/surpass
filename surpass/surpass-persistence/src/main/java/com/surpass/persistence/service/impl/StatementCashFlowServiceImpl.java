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


package com.surpass.persistence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.Message;
import com.surpass.entity.statement.StatementCashFlow;
import com.surpass.persistence.mapper.StatementCashFlowMapper;
import com.surpass.persistence.service.StatementCashFlowService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/4/29 11:40
 */

@Service
public class StatementCashFlowServiceImpl extends ServiceImpl<StatementCashFlowMapper, StatementCashFlow> implements StatementCashFlowService {

    @Override
    public Message<String> changeSpecifyItem(StatementCashFlow statementCashFlow) {
        //期间
        String yearPeriod = statementCashFlow.getYearPeriod();
        String periodType = statementCashFlow.getPeriodType();
        LocalDate reportDate = null;
        if (StringUtils.hasText(yearPeriod)) {
            // 正常传了值，补齐成 yyyy-MM-01
            reportDate = LocalDate.parse(yearPeriod + "-01");
        } else {
            // 没传值，默认当前月第一天
            reportDate = LocalDate.now().withDayOfMonth(1);
        }
        statementCashFlow.setReportDate(reportDate);
        BigDecimal monthlyAmount = statementCashFlow.getMonthlyAmount();
        String bookId = statementCashFlow.getBookId();
        String itemCode = statementCashFlow.getItemCode();

        LambdaQueryWrapper<StatementCashFlow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StatementCashFlow::getBookId, bookId);
        wrapper.eq(StatementCashFlow::getReportDate, reportDate);
        wrapper.eq(StatementCashFlow::getPeriodType, periodType);
        wrapper.eq(StatementCashFlow::getItemCode, itemCode);

        List<StatementCashFlow> list = super.list(wrapper);

        boolean result = false;

        if (ObjectUtils.isNotEmpty(list)) {
            //修改
            StatementCashFlow statementCashFlowExist = list.get(0);
            statementCashFlowExist.setMonthlyAmount(monthlyAmount);
            result = super.updateById(statementCashFlowExist);
        } else {
            result = super.save(statementCashFlow);
        }

        return result ? Message.ok("保存成功") : Message.failed("保存失败");
    }
}
