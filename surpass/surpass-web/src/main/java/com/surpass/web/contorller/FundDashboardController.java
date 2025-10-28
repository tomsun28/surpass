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


package com.surpass.web.contorller;

import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.Message;
import com.surpass.entity.fund.*;
import com.surpass.entity.idm.UserInfo;
import com.surpass.entity.statement.dto.StatementParamsDto;
import com.surpass.persistence.service.FundDashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 资金仪表盘
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/statistics")
@Slf4j
public class FundDashboardController {
    private final FundDashboardService fundDashboardService;

    /**
     * 资金余额
     *
     * @param currentUser 当前账户
     * @param params      查询参数
     * @return 结果
     */
    @GetMapping(value = {"/fund-balance"})
    public Message<FundBalanceVo> statisticsFundBalance(@CurrentUser UserInfo currentUser,
                                                        @ParameterObject StatementParamsDto params) {
        params.parse();
        params.setBookId(currentUser.getBookId());
        FundBalanceVo data = fundDashboardService.statisticsFundBalance(params);
        return Message.ok(data);
    }

    /**
     * 应收账款
     *
     * @param currentUser 当前账户
     * @param params      查询参数
     * @return 结果
     */
    @GetMapping(value = {"/accounts-receivable"})
    public Message<AccountsReceivePaymentVo> statisticsAccountsReceivable(@CurrentUser UserInfo currentUser,
                                                                          @ParameterObject StatementParamsDto params) {
        params.parse();
        params.setBookId(currentUser.getBookId());
        AccountsReceivePaymentVo data = fundDashboardService.statisticsAccountsReceivable(params);
        return Message.ok(data);
    }

    /**
     * 应付账款
     *
     * @param currentUser 当前账户
     * @param params      查询参数
     * @return 结果
     */
    @GetMapping(value = {"/accounts-payable"})
    public Message<AccountsReceivePaymentVo> statisticsAccountsPayable(@CurrentUser UserInfo currentUser,
                                                                       @ParameterObject StatementParamsDto params) {
        params.parse();
        params.setBookId(currentUser.getBookId());
        AccountsReceivePaymentVo data = fundDashboardService.statisticsAccountsPayable(params);
        return Message.ok(data);
    }

    /**
     * 预计可用现金
     *
     * @param currentUser 当前账户
     * @param params      查询参数
     * @return 结果
     */
    @GetMapping(value = {"/able-cash"})
    public Message<ExpectedAvailableFunds> statisticsAbleCash(@CurrentUser UserInfo currentUser,
                                                              @ParameterObject StatementParamsDto params) {
        params.parse();
        params.setBookId(currentUser.getBookId());
        ExpectedAvailableFunds data = fundDashboardService.statisticsAbleCash(params);
        return Message.ok(data);
    }

    /**
     * 其他科目指标
     *
     * @param currentUser 当前账户
     * @param params      查询参数
     * @return 结果
     */
    @GetMapping(value = {"/other-subjects"})
    public Message<List<OtherSubjectsVo>> statisticsOtherSubjects(@CurrentUser UserInfo currentUser,
                                                              @ParameterObject StatementParamsDto params) {
        params.parse();
        params.setBookId(currentUser.getBookId());
        List<OtherSubjectsVo> data = fundDashboardService.statisticsOtherSubjects(params);
        return Message.ok(data);
    }

    /**
     * 净利润
     *
     * @param currentUser 当前账户
     * @param params      查询参数
     * @return 结果
     */
    @GetMapping(value = {"/net-profit"})
    public Message<NetProfitVo> statisticsNetProfit(@CurrentUser UserInfo currentUser,
                                                              @ParameterObject StatementParamsDto params) {
        params.parse();
        params.setBookId(currentUser.getBookId());
        NetProfitVo data = fundDashboardService.statisticsNetProfit(params);
        return Message.ok(data);
    }

    /**
     * 收入成本
     *
     * @param currentUser 当前账户
     * @param params      查询参数
     * @return 结果
     */
    @GetMapping(value = {"/revenue-cost"})
    public Message<RevenueCostVo> statisticsRevenueCost(@CurrentUser UserInfo currentUser,
                                                        @ParameterObject StatementParamsDto params) {
        params.parse();
        params.setBookId(currentUser.getBookId());
        RevenueCostVo data = fundDashboardService.statisticsRevenueCost(params);
        return Message.ok(data);
    }

    /**
     * 费用
     *
     * @param currentUser 当前账户
     * @param params      查询参数
     * @return 结果
     */
    @GetMapping(value = {"/expense"})
    public Message<ExpenseVo> statisticsExpense(@CurrentUser UserInfo currentUser,
                                                        @ParameterObject StatementParamsDto params) {
        params.parse();
        params.setBookId(currentUser.getBookId());
        ExpenseVo data = fundDashboardService.statisticsExpense(params);
        return Message.ok(data);
    }

    /**
     * 费用
     *
     * @param currentUser 当前账户
     * @param params      查询参数
     * @return 结果
     */
    @GetMapping(value = {"/added-tax"})
    public Message<AddTaxVo> statisticsAddedTax(@CurrentUser UserInfo currentUser,
                                                        @ParameterObject StatementParamsDto params) {
        params.parse();
        params.setBookId(currentUser.getBookId());
        AddTaxVo data = fundDashboardService.statisticsAddedTax(params);
        return Message.ok(data);
    }


}
