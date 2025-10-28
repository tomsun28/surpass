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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.surpass.constants.ConstsSysConfig;
import com.surpass.entity.config.ConfigSys;
import com.surpass.entity.fund.*;
import com.surpass.entity.statement.*;
import com.surpass.entity.statement.dto.StatementParamsDto;
import com.surpass.entity.statement.vo.StatementBalanceSheetItemListVo;
import com.surpass.enums.StatementPeriodTypeEnum;
import com.surpass.persistence.mapper.StatementSubjectBalanceMapper;
import com.surpass.persistence.service.ConfigSysService;
import com.surpass.persistence.service.FundDashboardService;
import com.surpass.persistence.service.StatementBalanceSheetService;
import com.surpass.persistence.service.StatementIncomeService;
import com.surpass.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.surpass.constants.ConstsSysConfig.SYS_PAYMENT_TERM_CURRENT;

/**
 * 简介说明: 资金仪表盘
 *
 * @author wuyan
 * {@code @date} 2025/05/06 10:38:12
 * {@code @version} 1.0
 */

@RequiredArgsConstructor
@Service
public class FundDashboardServiceImpl implements FundDashboardService {
    private final ConfigSysService configSysService;
    private final StatementSubjectBalanceMapper subjectBalanceMapper;
    private final StatementBalanceSheetService balanceSheetService;
    private final StatementIncomeService statementIncomeService;

    /**
     * 统计资金余额
     *
     * @param params 查询参数
     * @return 结果
     */
    @Override
    public FundBalanceVo statisticsFundBalance(StatementParamsDto params) {
        String cashCodes = configSysService.selectConfigByKey(params.getBookId(), ConstsSysConfig.SYS_DEFAULT_CASH_SUBJECT_CODES);
        LambdaQueryWrapper<StatementSubjectBalance> lqw = buildQueryWrapper(params);
        lqw.isNull(StatementSubjectBalance::getParentId);
        lqw.in(StatementSubjectBalance::getSubjectCode, Arrays.asList(cashCodes.split(",")));
        List<BaseValue<BigDecimal>> listData = new ArrayList<>();
        FundBalanceVo data = FundBalanceVo.builder()
                .balance(BigDecimal.ZERO)
                .incomeFunds(BigDecimal.ZERO)
                .payoutFunds(BigDecimal.ZERO)
                .subjectBalance(listData)
                .netIncomeFunds(BigDecimal.ZERO)
                .build();
        List<StatementSubjectBalance> subjectBalances = subjectBalanceMapper.selectList(lqw);
        for (StatementSubjectBalance subjectBalance : subjectBalances) {
            BaseValue<BigDecimal> value = BaseValue.<BigDecimal>builder()
                    .name(subjectBalance.getSubjectName())
                    .value(subjectBalance.getBalance())
                    .build();
            listData.add(value);
            data.setBalance(data.getBalance().add(subjectBalance.getBalance()));
            data.setIncomeFunds(data.getIncomeFunds().add(subjectBalance.getCurrentPeriodDebit()));
            data.setPayoutFunds(data.getIncomeFunds().add(subjectBalance.getCurrentPeriodCredit()));
        }
        data.setNetIncomeFunds(data.getIncomeFunds().subtract(data.getPayoutFunds()));
        return data;
    }

    @Override
    public AccountsReceivePaymentVo statisticsAccountsReceivable(StatementParamsDto params) {
        AccountsReceivePaymentVo data = getAccountsReceivablePayable(ConstsSysConfig.SYS_DEFAULT_ACCOUNTS_RECEIVABLE, params);
        // todo 平均周转天数
        return data;
    }

    /**
     * 应付账款
     *
     * @param params 查询参数
     * @return 结果
     */
    @Override
    public AccountsReceivePaymentVo statisticsAccountsPayable(StatementParamsDto params) {
        return getAccountsReceivablePayable(ConstsSysConfig.SYS_DEFAULT_ACCOUNTS_PAYABLE, params);
    }

    /**
     * 预计可用资金
     *
     * @param params 参数
     * @return 结果
     */
    @Override
    public ExpectedAvailableFunds statisticsAbleCash(StatementParamsDto params) {
        ExpectedAvailableFunds data = ExpectedAvailableFunds.builder()
                .balance(BigDecimal.ZERO)
                .cashBalance(BigDecimal.ZERO)
                .accountsPayable(BigDecimal.ZERO)
                .accountsReceivable(BigDecimal.ZERO)
                .cashRatio(0f)
                .quickRatio(0f)
                .cashRatioLastYear(0f)
                .quickRatioLastYear(0f)
                .build();

        // 现有资金
        FundBalanceVo fundBalanceVo = statisticsFundBalance(params);
        data.setCashBalance(fundBalanceVo.getBalance());

        Map<String, String> configMap = configSysService.getBookConfigList(params.getBookId()).getData().stream()
                .collect(Collectors.toMap(ConfigSys::getConfigKey, ConfigSys::getConfigValue));

        // 短期应收账款
        String receivableCodes = configMap.get(ConstsSysConfig.SYS_DEFAULT_SHORT_TERM_ACCOUNTS_RECEIVABLE);
        LambdaQueryWrapper<StatementSubjectBalance> lqw = buildQueryWrapper(params);
        lqw.isNull(StatementSubjectBalance::getParentId);
        List<String> codes = Arrays.stream(receivableCodes.split(",")).toList();
        lqw.in(StatementSubjectBalance::getSubjectCode, codes);
        BigDecimal accountsReceivable = subjectBalanceMapper.selectList(lqw).stream()
                .map(item -> item.getCurrentPeriodDebit().subtract(item.getCurrentPeriodCredit()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        data.setAccountsReceivable(accountsReceivable);

        // 短期应付账款
        String payableCodes = configMap.get(ConstsSysConfig.SYS_DEFAULT_SHORT_TERM_ACCOUNTS_PAYABLE);
        lqw = buildQueryWrapper(params);
        lqw.isNull(StatementSubjectBalance::getParentId);
        codes = Arrays.stream(payableCodes.split(",")).toList();
        lqw.in(StatementSubjectBalance::getSubjectCode, codes);
        BigDecimal accountsPayable = subjectBalanceMapper.selectList(lqw).stream()
                .map(item -> item.getCurrentPeriodCredit().subtract(item.getCurrentPeriodDebit()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        data.setAccountsPayable(accountsPayable);
        data.setBalance(data.getCashBalance().add(data.getAccountsReceivable()).subtract(data.getAccountsPayable()));

        // 现金比率查询时间需要调整到年初到当前期之前
        StatementParamsDto ratioParams = StatementParamsDto.builder().build();
        BeanUtils.copyProperties(params, ratioParams);
        ratioParams.setPeriodType(StatementPeriodTypeEnum.BETWEEN_MONTH.getValue());
        String termCurrent = configMap.get(SYS_PAYMENT_TERM_CURRENT);
        String[] yearMonth = termCurrent.split("-");
        String startTime = yearMonth[0] + "-01";
        String endTime = yearMonth[0] + "-" + String.format("%02d", Integer.parseInt(yearMonth[1]));
        ratioParams.setDateRange(new String[]{startTime, endTime});
        calcCashRatio(false, ratioParams, data, configMap);

        // 同期统计
        startTime = String.format("%04d", Integer.parseInt(yearMonth[0]) - 1) + "-01";
        endTime = String.format("%04d", Integer.parseInt(yearMonth[0]) - 1)
                + "-" + String.format("%02d", Integer.parseInt(yearMonth[1]));
        ratioParams.setDateRange(new String[]{startTime, endTime});
        calcCashRatio(true, ratioParams, data, configMap);

        return data;
    }

    /**
     * 其它科目指标
     *
     * @param params 查询参数
     * @return 结果
     */
    @Override
    public List<OtherSubjectsVo> statisticsOtherSubjects(StatementParamsDto params) {
        LambdaQueryWrapper<StatementSubjectBalance> lqw = buildYearQueryWrapper(params);
        lqw.orderByAsc(StatementSubjectBalance::getYearPeriod);
        if (params.getSubjectCodes() != null) {
            lqw.likeRight(!params.getSubjectCodes().isEmpty(),
                    StatementSubjectBalance::getSubjectCode,
                    params.getSubjectCodes().get(0));
        }

        List<StatementSubjectBalance> subjectBalances = subjectBalanceMapper.selectList(lqw);
        Map<String, OtherSubjectsVo> data = new HashMap<>();

        for (StatementSubjectBalance subjectBalance : subjectBalances) {
            OtherSubjectsVo otherSubjectsVo = data.get(subjectBalance.getSubjectCode());
            if (otherSubjectsVo == null) {
                otherSubjectsVo = OtherSubjectsVo.builder()
                        .subjectName(subjectBalance.getSubjectName())
                        .subjectBalance(new ArrayList<>())
                        .build();
                data.put(subjectBalance.getSubjectCode(), otherSubjectsVo);
            }
            BaseValue<BigDecimal> value = BaseValue.<BigDecimal>builder()
                    .name(subjectBalance.getYearPeriod())
                    .value(subjectBalance.getBalance())
                    .build();
            otherSubjectsVo.getSubjectBalance().add(value);
        }

        fillSubjectBalance(data, String.valueOf(params.getYear()), params.getMonth());
        return data.values().stream().toList();
    }

    /**
     * 净利润表盘
     *
     * @param params 查询查询
     * @return 结果
     */
    @Override
    public NetProfitVo statisticsNetProfit(StatementParamsDto params) {
        NetProfitVo data = NetProfitVo.builder().build();

        StatementIncome statementIncome = statementIncomeService.generateIncomeStatement(params, false).getData();
        Map<String, String> configMap = configSysService.getBookConfigList(params.getBookId()).getData().stream()
                .collect(Collectors.toMap(ConfigSys::getConfigKey, ConfigSys::getConfigValue));

        // 净利润
        List<StatementIncomeItem> items = statementIncome.getItems();
        String incomeNetProfitCode = configMap.get(ConstsSysConfig.SYS_DEFAULT_INCOME_NET_PROFIT);
        StatementIncomeItem incomeItem = items.stream().filter(item -> item.getItemCode().equals(incomeNetProfitCode)).toList().get(0);
        data.setBalance(incomeItem.getCurrentBalance());
        // 营业收入
        String incomeOperatingCostsCode = configMap.get(ConstsSysConfig.SYS_DEFAULT_INCOME_OPERATING_REVENUE);
        BigDecimal incomeOperatingCostsItemBanlance = items.stream()
                .filter(item -> item.getItemCode().equals(incomeOperatingCostsCode)).toList().get(0).getCurrentBalance();
        // 净利润率
        BigDecimal percentage = BigDecimal.ZERO;
        if (incomeOperatingCostsItemBanlance.compareTo(BigDecimal.ZERO) != 0) {
            percentage = data.getBalance().divide(incomeOperatingCostsItemBanlance, 4, RoundingMode.HALF_UP)  // 保留4位中间值
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP); // 最终保留2位百分比
        }
        data.setBalanceRatio(percentage.floatValue());

        // 较同期，年算，则取当前年份的报表
        if (StatementPeriodTypeEnum.YEAR.getValue().equals(params.getPeriodType())) {
            params.setReportDate(params.getReportDate().substring(0, 4) + "-01");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.FORMAT_DATE_YYYY_MM);
        YearMonth yearMonth = YearMonth.parse(params.getReportDate(), formatter);
        // 年份减1
        yearMonth = yearMonth.minusYears(1);
        StatementParamsDto lastParams = StatementParamsDto.builder()
                .bookId(params.getBookId())
                .periodType(params.getPeriodType())
                .reportDate(yearMonth.format(formatter))
                .build();
        lastParams.parse();
        statementIncome = statementIncomeService.getIncomeStatement(lastParams, false).getData();
        BigDecimal balanceLastYear;
        if (statementIncome != null) {
            items = statementIncome.getItems();
            incomeItem = items.stream().filter(item -> item.getItemCode().equals(incomeNetProfitCode)).toList().get(0);
            balanceLastYear = incomeItem.getCurrentBalance();
            if (balanceLastYear.compareTo(BigDecimal.ZERO) != 0) {
                percentage = data.getBalance().subtract(balanceLastYear)
                        .divide(balanceLastYear, 4, RoundingMode.HALF_UP)  // 保留4位中间值
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP); // 最终保留2位百分比
            }
            data.setBalanceLastYear(percentage.floatValue());

            // 较同期净利润率
            incomeOperatingCostsItemBanlance = items.stream()
                    .filter(item -> item.getItemCode().equals(incomeOperatingCostsCode)).toList().get(0).getCurrentBalance();
            percentage = BigDecimal.ZERO;
            if (incomeOperatingCostsItemBanlance.compareTo(BigDecimal.ZERO) != 0) {
                percentage = balanceLastYear.divide(incomeOperatingCostsItemBanlance, 4, RoundingMode.HALF_UP)  // 保留4位中间值
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP); // 最终保留2位百分比
            }
            data.setBalanceRatioLastYear(data.getBalanceRatio() - percentage.floatValue());
        }

        if (StatementPeriodTypeEnum.YEAR.getValue().equals(params.getPeriodType())) {
            data.setBalanceLast(data.getBalanceLastYear());
            data.setBalanceRatioLast(data.getBalanceRatioLastYear());
        } else {
            // 较上期
            yearMonth = YearMonth.parse(params.getReportDate(), formatter);
            // 月份减1
            yearMonth = yearMonth.minusMonths(1);
            lastParams = StatementParamsDto.builder()
                    .bookId(params.getBookId())
                    .periodType(params.getPeriodType())
                    .reportDate(yearMonth.format(formatter))
                    .build();
            lastParams.parse();
            statementIncome = statementIncomeService.getIncomeStatement(lastParams, false).getData();
            BigDecimal balanceLast;
            if (statementIncome != null) {
                items = statementIncome.getItems();
                incomeItem = items.stream().filter(item -> item.getItemCode().equals(incomeNetProfitCode)).toList().get(0);
                balanceLast = incomeItem.getCurrentBalance();
                if (balanceLast.compareTo(BigDecimal.ZERO) != 0) {
                    percentage = data.getBalance().subtract(balanceLast)
                            .divide(balanceLast, 4, RoundingMode.HALF_UP)  // 保留4位中间值
                            .multiply(BigDecimal.valueOf(100))
                            .setScale(2, RoundingMode.HALF_UP); // 最终保留2位百分比
                }
                data.setBalanceLast(percentage.floatValue());

                // 较同期净利润率
                incomeOperatingCostsItemBanlance = items.stream()
                        .filter(item -> item.getItemCode().equals(incomeOperatingCostsCode)).toList().get(0).getCurrentBalance();
                percentage = BigDecimal.ZERO;
                if (incomeOperatingCostsItemBanlance.compareTo(BigDecimal.ZERO) != 0) {
                    percentage = balanceLast.divide(incomeOperatingCostsItemBanlance, 4, RoundingMode.HALF_UP)  // 保留4位中间值
                            .multiply(BigDecimal.valueOf(100))
                            .setScale(2, RoundingMode.HALF_UP); // 最终保留2位百分比
                }
                data.setBalanceRatioLast(data.getBalanceRatio() - percentage.floatValue());
            }
        }

        // 近期变化趋势
        List<String> recentMonths = getRecentMonths(configMap.get(SYS_PAYMENT_TERM_CURRENT), 5);
        List<BaseValue<BigDecimal>> balanceList = new ArrayList<>();
        List<BaseValue<Float>> balanceRatioList = new ArrayList<>();
        for (String recentMonth : recentMonths) {
            StatementParamsDto recentParams = StatementParamsDto.builder()
                    .bookId(params.getBookId())
                    .periodType(StatementPeriodTypeEnum.MONTH.getValue())
                    .reportDate(recentMonth)
                    .build();
            recentParams.parse();
            statementIncome = statementIncomeService.getIncomeStatement(recentParams, false).getData();
            items = statementIncome.getItems();
            incomeItem = items.stream().filter(item -> item.getItemCode().equals(incomeNetProfitCode)).toList().get(0);
            // 营业收入
            incomeOperatingCostsItemBanlance = items.stream()
                    .filter(item -> item.getItemCode().equals(incomeOperatingCostsCode)).toList().get(0).getCurrentBalance();
            // 净利润率
            percentage = BigDecimal.ZERO;
            if (incomeOperatingCostsItemBanlance.compareTo(BigDecimal.ZERO) != 0) {
                percentage = data.getBalance().divide(incomeOperatingCostsItemBanlance, 4, RoundingMode.HALF_UP)  // 保留4位中间值
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP); // 最终保留2位百分比
            }
            balanceList.add(new BaseValue<>(recentMonth, incomeItem.getCurrentBalance()));
            balanceRatioList.add(new BaseValue<>(recentMonth, percentage.floatValue()));
        }
        data.setBalanceList(balanceList);
        data.setBalanceRatioList(balanceRatioList);

        return data;
    }

    /**
     * 收入成本
     *
     * @param params 查询参数
     * @return RevenueCostVo
     */
    @Override
    public RevenueCostVo statisticsRevenueCost(StatementParamsDto params) {
        RevenueCostVo data = RevenueCostVo.builder().build();

        StatementIncome statementIncome = statementIncomeService.generateIncomeStatement(params, false).getData();
        Map<String, String> configMap = configSysService.getBookConfigList(params.getBookId()).getData().stream()
                .collect(Collectors.toMap(ConfigSys::getConfigKey, ConfigSys::getConfigValue));

        // 营业收入
        List<StatementIncomeItem> items = statementIncome.getItems();
        String incomeOperatingRevenueCode = configMap.get(ConstsSysConfig.SYS_DEFAULT_INCOME_OPERATING_REVENUE);
        BigDecimal incomeOperatingRevenueItemBanlance = items.stream()
                .filter(item -> item.getItemCode().equals(incomeOperatingRevenueCode)).toList().get(0).getCurrentBalance();
        data.setBalance(incomeOperatingRevenueItemBanlance);
        // 营业成本
        String incomeOperatingCostsCode = configMap.get(ConstsSysConfig.SYS_DEFAULT_INCOME_OPERATING_COSTS);
        BigDecimal incomeOperatingCostsItemBanlance = items.stream()
                .filter(item -> item.getItemCode().equals(incomeOperatingCostsCode)).toList().get(0).getCurrentBalance();
        data.setBalanceOperatingCosts(incomeOperatingCostsItemBanlance);

        // 毛利率：（毛利润 ÷ 营业收入）× 100%
        // 毛利润 = 营业收入 − 销售成本（或称“主营业务成本”）
        BigDecimal percentage = BigDecimal.ZERO;
        if (incomeOperatingRevenueItemBanlance.compareTo(BigDecimal.ZERO) != 0) {
            percentage = incomeOperatingRevenueItemBanlance.subtract(incomeOperatingCostsItemBanlance)
                    .divide(incomeOperatingRevenueItemBanlance, 4, RoundingMode.HALF_UP)  // 保留4位中间值
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP); // 最终保留2位百分比
        }
        data.setBalanceRatio(percentage.floatValue());

        // 较同期，年算，则取当前年份的报表
        if (StatementPeriodTypeEnum.YEAR.getValue().equals(params.getPeriodType())) {
            params.setReportDate(params.getReportDate().substring(0, 4) + "-01");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.FORMAT_DATE_YYYY_MM);
        YearMonth yearMonth = YearMonth.parse(params.getReportDate(), formatter);
        // 年份减1
        yearMonth = yearMonth.minusYears(1);
        StatementParamsDto lastParams = StatementParamsDto.builder()
                .bookId(params.getBookId())
                .periodType(params.getPeriodType())
                .reportDate(yearMonth.format(formatter))
                .build();
        lastParams.parse();
        statementIncome = statementIncomeService.getIncomeStatement(params, false).getData();
        BigDecimal balanceLastYear;
        if (statementIncome != null) {
            items = statementIncome.getItems();
            // 收入
            balanceLastYear = items.stream().filter(item -> item.getItemCode().equals(incomeOperatingRevenueCode)).toList().get(0).getCurrentBalance();
            if (balanceLastYear.compareTo(BigDecimal.ZERO) != 0) {
                percentage = data.getBalance().subtract(balanceLastYear)
                        .divide(balanceLastYear, 4, RoundingMode.HALF_UP)  // 保留4位中间值
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP); // 最终保留2位百分比
            }
            data.setBalanceLastYear(percentage.floatValue());
            // 成本
            incomeOperatingCostsItemBanlance = items.stream()
                    .filter(item -> item.getItemCode().equals(incomeOperatingCostsCode)).toList().get(0).getCurrentBalance();
            percentage = BigDecimal.ZERO;
            if (incomeOperatingCostsItemBanlance.compareTo(BigDecimal.ZERO) != 0) {
                percentage = data.getBalanceOperatingCosts().subtract(incomeOperatingCostsItemBanlance)
                        .divide(incomeOperatingCostsItemBanlance, 4, RoundingMode.HALF_UP)  // 保留4位中间值
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP); // 最终保留2位百分比
            }
            data.setBalanceRatioLastYear(percentage.floatValue());
        }

        if (StatementPeriodTypeEnum.YEAR.getValue().equals(params.getPeriodType())) {
            data.setBalanceLast(data.getBalanceLastYear());
            data.setBalanceRatioLast(data.getBalanceRatioLastYear());
        } else {
            // 较上期
            yearMonth = YearMonth.parse(params.getReportDate(), formatter);
            // 月份减1
            yearMonth = yearMonth.minusMonths(1);
            lastParams = StatementParamsDto.builder()
                    .bookId(params.getBookId())
                    .periodType(params.getPeriodType())
                    .reportDate(yearMonth.format(formatter))
                    .build();
            lastParams.parse();
            statementIncome = statementIncomeService.getIncomeStatement(params, false).getData();
            BigDecimal balanceLast;
            if (statementIncome != null) {
                items = statementIncome.getItems();
                // 收入
                balanceLast = items.stream().filter(item -> item.getItemCode().equals(incomeOperatingRevenueCode))
                        .toList().get(0).getCurrentBalance();
                if (balanceLast.compareTo(BigDecimal.ZERO) != 0) {
                    percentage = data.getBalance().subtract(balanceLast)
                            .divide(balanceLast, 4, RoundingMode.HALF_UP)  // 保留4位中间值
                            .multiply(BigDecimal.valueOf(100))
                            .setScale(2, RoundingMode.HALF_UP); // 最终保留2位百分比
                }
                data.setBalanceLast(percentage.floatValue());

                // 成本
                incomeOperatingCostsItemBanlance = items.stream()
                        .filter(item -> item.getItemCode().equals(incomeOperatingCostsCode))
                        .toList().get(0).getCurrentBalance();
                percentage = BigDecimal.ZERO;
                if (incomeOperatingCostsItemBanlance.compareTo(BigDecimal.ZERO) != 0) {
                    percentage = data.getBalanceOperatingCosts().subtract(incomeOperatingCostsItemBanlance)
                            .divide(incomeOperatingCostsItemBanlance, 4, RoundingMode.HALF_UP)  // 保留4位中间值
                            .multiply(BigDecimal.valueOf(100))
                            .setScale(2, RoundingMode.HALF_UP); // 最终保留2位百分比
                }
                data.setBalanceRatioLast(percentage.floatValue());
            }
        }

        // 近期变化趋势
        List<String> recentMonths = getRecentMonths(configMap.get(SYS_PAYMENT_TERM_CURRENT), 5);
        List<BaseValue<BigDecimal>> balanceList = new ArrayList<>();
        List<BaseValue<BigDecimal>> balanceRatioList = new ArrayList<>();
        for (String recentMonth : recentMonths) {
            StatementParamsDto recentParams = StatementParamsDto.builder()
                    .bookId(params.getBookId())
                    .periodType(StatementPeriodTypeEnum.MONTH.getValue())
                    .reportDate(recentMonth)
                    .build();
            recentParams.parse();
            statementIncome = statementIncomeService.getIncomeStatement(recentParams, false).getData();
            items = statementIncome.getItems();
            // 收入
            incomeOperatingRevenueItemBanlance = items.stream()
                    .filter(item -> item.getItemCode().equals(incomeOperatingRevenueCode))
                    .toList().get(0).getCurrentBalance();
            // 成本
            incomeOperatingCostsItemBanlance = items.stream()
                    .filter(item -> item.getItemCode().equals(incomeOperatingCostsCode)).toList().get(0).getCurrentBalance();
            balanceList.add(new BaseValue<>(recentMonth, incomeOperatingRevenueItemBanlance));
            balanceRatioList.add(new BaseValue<>(recentMonth, incomeOperatingCostsItemBanlance));
        }
        data.setBalanceList(balanceList);
        data.setBalanceRatioList(balanceRatioList);

        return data;
    }

    /**
     * 费用
     *
     * @param params 查询参数
     * @return 结果
     */
    @Override
    public ExpenseVo statisticsExpense(StatementParamsDto params) {
        ExpenseVo data = ExpenseVo.builder()
                .balance(BigDecimal.ZERO)
                .build();
        params.parse();
        Map<String, String> configMap = configSysService.getBookConfigList(params.getBookId()).getData().stream()
                .collect(Collectors.toMap(ConfigSys::getConfigKey, ConfigSys::getConfigValue));
        StatementIncome statementIncome = statementIncomeService.generateIncomeStatement(params, false).getData();
        List<StatementIncomeItem> items = statementIncome.getItems();
        sumExpense(configMap, items, data);

        // 收入比
        String incomeOperatingRevenueCode = configMap.get(ConstsSysConfig.SYS_DEFAULT_INCOME_OPERATING_REVENUE);
        BigDecimal incomeOperatingRevenueItemBanlance = items.stream()
                .filter(item -> item.getItemCode().equals(incomeOperatingRevenueCode)).toList().get(0).getCurrentBalance();
        BigDecimal percentage = BigDecimal.ZERO;
        if (incomeOperatingRevenueItemBanlance.compareTo(BigDecimal.ZERO) != 0) {
            percentage = data.getBalance()
                    .divide(incomeOperatingRevenueItemBanlance, 4, RoundingMode.HALF_UP)  // 保留4位中间值
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP); // 最终保留2位百分比
        }
        data.setBalanceIncomeRatio(percentage.floatValue());

        // 成本比
        String incomeOperatingCostsCode = configMap.get(ConstsSysConfig.SYS_DEFAULT_INCOME_OPERATING_COSTS);
        BigDecimal incomeOperatingCostsItemBanlance = items.stream()
                .filter(item -> item.getItemCode().equals(incomeOperatingCostsCode)).toList().get(0).getCurrentBalance();
        percentage = BigDecimal.ZERO;
        if (incomeOperatingCostsItemBanlance.compareTo(BigDecimal.ZERO) != 0) {
            percentage = data.getBalance()
                    .divide(incomeOperatingCostsItemBanlance, 4, RoundingMode.HALF_UP)  // 保留4位中间值
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP); // 最终保留2位百分比
        }
        data.setBalanceCostRatio(percentage.floatValue());

        // 较同期，年算，则取当前年份的报表
        if (StatementPeriodTypeEnum.YEAR.getValue().equals(params.getPeriodType())) {
            params.setReportDate(params.getReportDate().substring(0, 4) + "-01");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.FORMAT_DATE_YYYY_MM);
        YearMonth yearMonth = YearMonth.parse(params.getReportDate(), formatter);
        // 年份减1
        yearMonth = yearMonth.minusYears(1);
        StatementParamsDto lastParams = StatementParamsDto.builder()
                .bookId(params.getBookId())
                .periodType(params.getPeriodType())
                .reportDate(yearMonth.format(formatter))
                .build();
        lastParams.parse();
        statementIncome = statementIncomeService.getIncomeStatement(params, false).getData();
        if (statementIncome != null) {
            items = statementIncome.getItems();
            BigDecimal lastYearBalance = sumExpense(configMap, items, null);
            percentage = BigDecimal.ZERO;
            if (lastYearBalance.compareTo(BigDecimal.ZERO) != 0) {
                percentage = data.getBalance().subtract(lastYearBalance)
                        .divide(lastYearBalance, 4, RoundingMode.HALF_UP)  // 保留4位中间值
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP); // 最终保留2位百分比
            }
            data.setBalanceLastYear(percentage.floatValue());
        }
        // 较上期
        if (StatementPeriodTypeEnum.YEAR.getValue().equals(params.getPeriodType())) {
            data.setBalanceLast(data.getBalanceLastYear());
        } else {
            // 较上期
            yearMonth = YearMonth.parse(params.getReportDate(), formatter);
            // 月份减1
            yearMonth = yearMonth.minusMonths(1);
            lastParams = StatementParamsDto.builder()
                    .bookId(params.getBookId())
                    .periodType(params.getPeriodType())
                    .reportDate(yearMonth.format(formatter))
                    .build();
            lastParams.parse();
            statementIncome = statementIncomeService.getIncomeStatement(params, false).getData();
            if (statementIncome != null) {
                items = statementIncome.getItems();
                BigDecimal lastBalance = sumExpense(configMap, items, null);
                percentage = BigDecimal.ZERO;
                if (lastBalance.compareTo(BigDecimal.ZERO) != 0) {
                    percentage = data.getBalance().subtract(lastBalance)
                            .divide(lastBalance, 4, RoundingMode.HALF_UP)  // 保留4位中间值
                            .multiply(BigDecimal.valueOf(100))
                            .setScale(2, RoundingMode.HALF_UP); // 最终保留2位百分比
                }
                data.setBalanceLast(percentage.floatValue());
            }
        }

        return data;
    }

    /**
     * 税金及附加
     *
     * @param params 查询参数
     * @return 结果
     */
    @Override
    public AddTaxVo statisticsAddedTax(StatementParamsDto params) {
        Map<String, String> configMap = configSysService.getBookConfigList(params.getBookId()).getData().stream()
                .collect(Collectors.toMap(ConfigSys::getConfigKey, ConfigSys::getConfigValue));
        AddTaxVo data = AddTaxVo.builder()
                .taxRatio(new ArrayList<>())
                .tax(new ArrayList<>())
                .balance(BigDecimal.ZERO)
                .ratio(0F)
                .build();

        // 应纳税额
        StatementIncome statementIncome = statementIncomeService.generateIncomeStatement(params, false).getData();
        List<StatementIncomeItem> items = statementIncome.getItems();
        String addedTaxCode = configMap.get(ConstsSysConfig.SYS_DEFAULT_ADDED_TAX);
        String incomeTaxCode = configMap.get(ConstsSysConfig.SYS_DEFAULT_INCOME_TAX_EXPENSES);
        BigDecimal balance = items.stream()
                .filter(item -> item.getItemCode().equals(addedTaxCode)
                        || item.getItemCode().equals(incomeTaxCode))
                .map(StatementIncomeItem::getCurrentBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
        data.setBalance(balance);

        // 近期变化趋势
        List<String> recentMonths = getRecentMonths(configMap.get(SYS_PAYMENT_TERM_CURRENT), 5);
        List<BaseValue<BigDecimal>> addedTaxList = new ArrayList<>();
        for (String recentMonth : recentMonths) {
            StatementParamsDto recentParams = StatementParamsDto.builder()
                    .bookId(params.getBookId())
                    .periodType(StatementPeriodTypeEnum.MONTH.getValue())
                    .reportDate(recentMonth)
                    .build();
            recentParams.parse();
            statementIncome = statementIncomeService.getIncomeStatement(recentParams, false).getData();
            items = statementIncome.getItems();
            balance = items.stream()
                    .filter(item -> item.getItemCode().equals(addedTaxCode)
                            || item.getItemCode().equals(incomeTaxCode))
                    .map(StatementIncomeItem::getCurrentBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
            addedTaxList.add(BaseValue.<BigDecimal>builder()
                    .name(recentMonth)
                    .value(balance)
                    .build());
        }
        data.setTax(addedTaxList);
        return data;
    }


    /**
     * 计算费用
     *
     * @param configMap 参数
     * @param items     收支项目
     * @param data      刷新实体类
     * @return 总费用
     */
    private BigDecimal sumExpense(Map<String, String> configMap, List<StatementIncomeItem> items, ExpenseVo data) {
        List<BaseValue<BigDecimal>> balanceList = new ArrayList<>();
        BigDecimal balance = BigDecimal.ZERO;
        Map<String, String> listKeys = Map.of(
                ConstsSysConfig.SYS_DEFAULT_ADMINISTRATIVE_EXPENSES, "管理费用",
                ConstsSysConfig.SYS_DEFAULT_SELLING_EXPENSES, "销售费用",
                ConstsSysConfig.SYS_DEFAULT_FINANCIAL_EXPENSES, "财务费用"
        );
        for (String listKey : listKeys.keySet()) {
            String code = configMap.get(listKey);
            BigDecimal cost = items.stream()
                    .filter(item -> item.getItemCode().equals(code)).toList().get(0).getCurrentBalance();
            balanceList.add(new BaseValue<>(listKeys.get(listKey), cost));
            balance = balance.add(cost);
        }

        if (data != null) {
            data.setBalanceList(balanceList);
            data.setBalance(balance);
        }

        return balance;
    }

    /**
     * 获取最近几个月，加上inputDateStr
     *
     * @param inputDateStr 输入的日期字符串
     * @param monthsCount  月份数
     * @return 结果：monthsCount + 1
     */
    public static List<String> getRecentMonths(String inputDateStr, int monthsCount) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.FORMAT_DATE_YYYY_MM);
        YearMonth start = YearMonth.parse(inputDateStr, formatter);
        List<String> result = new ArrayList<>();
        for (int i = monthsCount; i >= 0; i--) {
            YearMonth ym = start.minusMonths(i);
            result.add(ym.format(formatter));
        }
        return result;
    }


    /**
     * 自动补全月份
     *
     * @param data 数据
     * @param year 年份
     */
    public static void fillSubjectBalance(Map<String, OtherSubjectsVo> data, String year, int maxMonth) {
        for (OtherSubjectsVo vo : data.values()) {
            Map<String, BigDecimal> balanceMap = vo.getSubjectBalance().stream()
                    .collect(Collectors.toMap(BaseValue::getName, BaseValue::getValue, (a, b) -> b));
            List<BaseValue<BigDecimal>> completed = new ArrayList<>();
            for (int i = 1; i <= maxMonth; i++) {
                String ym = String.format("%s-%02d", year, i);
                BigDecimal value = balanceMap.getOrDefault(ym, BigDecimal.ZERO)
                        .divide(BigDecimal.valueOf(10000), 2, RoundingMode.HALF_UP); // 转万元，保留两位小数

                String monthName = i + "月";
                BaseValue<BigDecimal> item = new BaseValue<>();
                item.setName(monthName);
                item.setValue(value);
                completed.add(item);
            }
            vo.setSubjectBalance(completed);
        }
    }


    /**
     * 计算现金比率
     *
     * @param isLast    是否同期
     * @param params    查询参数
     * @param data      统计结果
     * @param configMap 配置信息
     */
    private void calcCashRatio(boolean isLast, StatementParamsDto params,
                               ExpectedAvailableFunds data,
                               Map<String, String> configMap) {
        // 现有资金
        FundBalanceVo fundBalanceVo = statisticsFundBalance(params);

        // 现金比率：
        // 年初至最近已结账期间的现金比率，反映企业现金及现金等价物对流动负债的保障程度。
        // 计算公式为：现金比率=(货币资金+交易性金融资产+应收票据)／流动负债
        String tradingFinancialAssetsCode = configMap.get(ConstsSysConfig.SYS_DEFAULT_TRADING_FINANCIAL_ASSETS);
        LambdaQueryWrapper<StatementSubjectBalance> lqw = buildQueryWrapper(params);
        lqw.isNull(StatementSubjectBalance::getParentId);
        lqw.eq(StatementSubjectBalance::getSubjectCode, tradingFinancialAssetsCode);
        BigDecimal tradingFinancialAssetsBalance = subjectBalanceMapper.selectList(lqw).stream()
                .map(item -> item.getCurrentPeriodDebit().subtract(item.getCurrentPeriodCredit()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        String billReceivableCode = configMap.get(ConstsSysConfig.SYS_DEFAULT_BILL_RECEIVABLE);
        lqw = buildQueryWrapper(params);
        lqw.isNull(StatementSubjectBalance::getParentId);
        lqw.eq(StatementSubjectBalance::getSubjectCode, billReceivableCode);
        BigDecimal billReceivableBalance = subjectBalanceMapper.selectList(lqw).stream()
                .map(item -> item.getCurrentPeriodDebit().subtract(item.getCurrentPeriodCredit()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // 资产负载数据
        String currentLiabilitiesCode = configMap.get(ConstsSysConfig.SYS_DEFAULT_CURRENT_LIABILITIES);
        StatementBalanceSheet statementBalanceSheet = balanceSheetService.queryBalanceSheet(params, false).getData();
        StatementBalanceSheetItemListVo itemListVo = statementBalanceSheet.getItems();
        List<StatementBalanceSheetItem> assetItems = itemListVo.getAssets();
        List<StatementBalanceSheetItem> liabilityItems = itemListVo.getLiability();
        Optional<StatementBalanceSheetItem> maxItem = liabilityItems.stream()
                .filter(item -> currentLiabilitiesCode.equals(item.getParentItemCode()))
                .max(Comparator.comparing(StatementBalanceSheetItem::getItemCode));
        BigDecimal liabilitiesBalance = BigDecimal.ZERO;
        if (maxItem.isPresent()) {
            liabilitiesBalance = maxItem.get().getCurrentBalance();
        }

        BigDecimal add = fundBalanceVo.getBalance().add(tradingFinancialAssetsBalance).add(billReceivableBalance);
        BigDecimal percentage = BigDecimal.ZERO;
        if (liabilitiesBalance.compareTo(BigDecimal.ZERO) != 0) {
            percentage = add.divide(liabilitiesBalance, 4, RoundingMode.HALF_UP)  // 保留4位中间值
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP); // 最终保留2位百分比
        }
        if (isLast) {
            data.setCashRatioLastYear(percentage.floatValue());
        } else {
            data.setCashRatio(percentage.floatValue());
        }

        // 速动比率：
        // 年初至最近已结账期间的速动比率，反映企业速动资产（流动资产中扣除存货部分）对流动负债的保障程度。
        // 计算公式为：速动比率=(流动资产-存货)/流动负债
        String currentAssetsCode = configMap.get(ConstsSysConfig.SYS_DEFAULT_CURRENT_ASSETS);
        String currentAssetsInventoryCode = configMap.get(ConstsSysConfig.SYS_DEFAULT_CURRENT_ASSETS_INVENTORY);
        BigDecimal assetsBalance = BigDecimal.ZERO;
        final BigDecimal[] assetsInventoryBalance = {BigDecimal.ZERO};
        maxItem = assetItems.stream()
                .filter(item -> currentAssetsCode.equals(item.getParentItemCode()))
                .peek(item -> {
                    if (currentAssetsInventoryCode.equals(item.getItemCode())) {
                        assetsInventoryBalance[0] = item.getCurrentBalance();
                    }
                })
                .max(Comparator.comparing(StatementBalanceSheetItem::getSortIndex));
        if (maxItem.isPresent()) {
            assetsBalance = maxItem.get().getCurrentBalance();
        }
        add = assetsBalance.subtract(assetsInventoryBalance[0]);
        percentage = BigDecimal.ZERO;
        if (liabilitiesBalance.compareTo(BigDecimal.ZERO) != 0) {
            percentage = add.divide(liabilitiesBalance, 4, RoundingMode.HALF_UP)  // 保留4位中间值
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP); // 最终保留2位百分比
        }
        if (isLast) {
            data.setQuickRatioLastYear(percentage.floatValue());
        } else {
            data.setQuickRatio(percentage.floatValue());
        }
    }

    /**
     * 取得应收/付账款相关余额信息
     *
     * @param type 收/付
     * @return 结果
     */
    private AccountsReceivePaymentVo getAccountsReceivablePayable(String type, StatementParamsDto params) {
        String code = configSysService.selectConfigByKey(params.getBookId(), type);
        LambdaQueryWrapper<StatementSubjectBalance> lqw = buildQueryWrapper(params);
        lqw.likeRight(StatementSubjectBalance::getSubjectCode, code);
        List<BaseValue<BigDecimal>> listData = new ArrayList<>();
        AccountsReceivePaymentVo data = AccountsReceivePaymentVo.builder()
                .balance(BigDecimal.ZERO)
                .cycleDays(0f)
                .subjectBalance(listData)
                .build();
        List<StatementSubjectBalance> subjectBalances = subjectBalanceMapper.selectList(lqw);
        for (StatementSubjectBalance subjectBalance : subjectBalances) {
            if (subjectBalance.getSubjectCode().equals(code)) {
                data.setBalance(subjectBalance.getBalance());
            } else {
                listData.add(BaseValue.<BigDecimal>builder()
                        .name(subjectBalance.getSubjectName())
                        .value(subjectBalance.getBalance())
                        .build());
            }
        }
        return data;
    }

    private LambdaQueryWrapper<StatementSubjectBalance> buildQueryWrapper(StatementParamsDto params) {
        LambdaQueryWrapper<StatementSubjectBalance> lqw = Wrappers.lambdaQuery();
        lqw.eq(StatementSubjectBalance::getBookId, params.getBookId());
        lqw.eq(StatementSubjectBalance::getPeriodType, StatementPeriodTypeEnum.MONTH.getValue());
        lqw.ge(StatementSubjectBalance::getYearPeriod, params.getDateRange()[0]);
        lqw.le(StatementSubjectBalance::getYearPeriod, params.getDateRange()[1]);
        return lqw;
    }

    private LambdaQueryWrapper<StatementSubjectBalance> buildYearQueryWrapper(StatementParamsDto params) {
        LambdaQueryWrapper<StatementSubjectBalance> lqw = Wrappers.lambdaQuery();
        lqw.eq(StatementSubjectBalance::getBookId, params.getBookId());
        lqw.eq(StatementSubjectBalance::getPeriodType, StatementPeriodTypeEnum.MONTH.getValue());
        String currentTerm = configSysService.getCurrentTerm(params.getBookId());
        lqw.ge(StatementSubjectBalance::getYearPeriod, currentTerm.split("-")[0]+"-01");
        lqw.le(StatementSubjectBalance::getYearPeriod, currentTerm);
        return lqw;
    }
}
