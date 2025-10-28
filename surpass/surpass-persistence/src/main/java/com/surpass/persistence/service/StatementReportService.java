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
import com.surpass.entity.statement.StatementCashFlow;
import com.surpass.entity.statement.StatementSubjectBalance;
import com.surpass.entity.statement.dto.StatementParamsDto;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 财务报表业务接口
 */
public interface StatementReportService {

    Message<List<StatementCashFlow>> cashFlowStatement(StatementParamsDto dto);

    Message<List<StatementSubjectBalance>> subjectBalance(StatementParamsDto dto);

    Message<Map<String, List<StatementSubjectBalance>>> generalLedger(StatementParamsDto dto);

    Message<List<StatementSubjectBalance>> tAccount(StatementParamsDto dto);

    Message<List<StatementSubjectBalance>> voucherSummary(StatementParamsDto dto);

    /**
     * @Description: 获取当前期的期末余额
     * @Param: [dto]
     * @return: java.math.BigDecimal
     * @Author: xZen
     * @Date: 2025/5/14 17:36
     */
    BigDecimal getEndingBalance(StatementParamsDto dto);

    void cashFlowExport(StatementParamsDto dto, HttpServletResponse response) throws IOException;

    void subjectBalanceExport(StatementParamsDto dto, HttpServletResponse response) throws IOException;

    void voucherSummaryExport(StatementParamsDto dto, HttpServletResponse response) throws IOException;
}
