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


package com.surpass.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/4/15 11:25
 */
@Getter
public enum CashFlowItemEnum {
    // 一、经营活动产生的现金流量（流入）
    CASH_FROM_SALES("2-jy-sqxj", "销售商品、提供劳务收到的现金"),
    TAX_REFUND("3-jy-sffh", "收到的税费返还"),
    OTHER_OPERATING_CASH_INFLOW("4-jy-sdqt", "收到其他与经营活动有关的现金"),
    OPERATING_CASH_INFLOW_SUBTOTAL("5-jy-lrxj", "经营活动现金流入小计"),

    // 一、经营活动产生的现金流量（流出）
    CASH_PAID_FOR_GOODS("6-jy-zfxj", "购买商品、接受劳务支付的现金"),
    CASH_PAID_TO_EMPLOYEES("7-jy-zgzf", "支付给职工以及为职工支付的现金"),
    TAXES_PAID("8-jy-sfzf", "支付的各项税费"),
    OTHER_OPERATING_CASH_OUTFLOW("9-jy-zfqt", "支付其他与经营活动有关的现金"),
    OPERATING_CASH_OUTFLOW_SUBTOTAL("10-jy-lcxj", "经营活动现金流出小计"),

    // 一、经营活动产生的现金流量（净额）
    OPERATING_CASH_NET("11-jy-lljh", "经营活动产生的现金流量净额"),

    // 二、投资活动产生的现金流量（流入）
    CASH_FROM_INVESTMENT_RECOVERY("13-tz-sdxj", "收回投资收到的现金"),
    CASH_FROM_INVESTMENT_INCOME("14-tz-qdxj", "取得投资收益收到的现金"),
    CASH_FROM_DISPOSAL_ASSETS("15-tz-gdje", "处置固定资产、无形资产和其他长期资产收回的现金净额"),
    CASH_FROM_DISPOSAL_SUBSIDIARIES("16-tz-zsje", "处置子公司及其他营业单位收到的现金净额"),
    OTHER_INVESTING_CASH_INFLOW("17-tz-qtxj", "收到其他与投资活动有关的现金"),
    INVESTING_CASH_INFLOW_SUBTOTAL("18-tz-lrxj", "投资活动现金流入小计"),

    // 二、投资活动产生的现金流量（流出）
    CASH_PAID_FOR_ASSETS("19-tz-gzxj", "购建固定资产、无形资产和其他长期资产支付的现金"),
    CASH_PAID_FOR_INVESTMENTS("20-tz-zfxj", "投资支付的现金"),
    CASH_PAID_FOR_SUBSIDIARIES("21-tz-qdzs", "取得子公司及其他营业单位支付的现金净额"),
    OTHER_INVESTING_CASH_OUTFLOW("22-tz-zfqt", "支付其他与投资活动有关的现金"),
    INVESTING_CASH_OUTFLOW_SUBTOTAL("23-tz-lcxj", "投资活动现金流出小计"),

    // 二、投资活动产生的现金流量（净额）
    INVESTING_CASH_NET("24-tz-llje", "投资活动产生的现金流量净额"),

    // 三、筹资活动产生的现金流量（流入）
    CASH_FROM_INVESTMENTS("26-cz-xsxj", "吸收投资收到的现金"),
    CASH_FROM_BORROWINGS("27-cz-qdjk", "取得借款收到的现金"),
    OTHER_FINANCING_CASH_INFLOW("28-cz-sdxj", "收到其他与筹资活动有关的现金"),
    FINANCING_CASH_INFLOW_SUBTOTAL("29-cz-lrxj", "筹资活动现金流入小计"),

    // 三、筹资活动产生的现金流量（流出）
    CASH_REPAYMENT_OF_BORROWINGS("30-cz-zfxj", "偿还债务支付的现金"),
    CASH_PAID_FOR_DIVIDENDS("31-cz-fpgl", "分配股利、利润或偿付利息支付的现金"),
    OTHER_FINANCING_CASH_OUTFLOW("32-cz-zfhd", "支付其他与筹资活动有关的现金"),
    FINANCING_CASH_OUTFLOW_SUBTOTAL("33-cz-lcxj", "筹资活动现金流出小计"),

    // 三、筹资活动产生的现金流量（净额）
    FINANCING_CASH_NET("34-cz-hdje", "筹资活动产生的现金流量净额"),

    // 四、汇率变动对现金的影响
    EXCHANGE_RATE_EFFECT("35-hl-djje", "四、汇率变动对现金及现金等价物的影响"),

    // 五、现金及现金等价物净增加额
    CASH_NET_INCREASE("36-xj-djje", "五、现金及现金等价物净增加额"),

    // 六、期末现金及现金等价物余额
    BEGINNING_CASH_BALANCE("37-xj-qcye", "加：期初现金及现金等价物余额"),
    ENDING_CASH_BALANCE("38-xj-qmye", "六、期末现金及现金等价物余额"),

    //补充资料
    NET_PROFIT("41-xj-jlr", "净利润"),
    PROVISION_ASSET_IMPAIRMENT("42-xj-jtzc", "加：计提的资产减值准备"),
    DEPRECIATION_FIXED_ASSETS("43-xj-zczk", "固定资产折旧"),
    AMORTIZATION_INTANGIBLE_ASSETS("44-xj-zctx", "无形资产摊销"),
    AMORTIZATION_LONGTERM_DEFERRED_EXPENSES("45-xj-fytx", "长期待摊费用摊销"),
    DISPOSAL_FIXED_ASSETS("46-xj-zcss", "处置固定资产、无形资产和其他长期资产的损失(减：收益)"),
    LOSS_SCRAPPING_FIXED_ASSETS("47-xj-bfss", "固定资产报废损失"),
    LOSS_CHANGE_FAIR_VALUE("48-xj-gyss", "公允价值变动损失（收益以“-”号填列）"),
    FINANCIAL_EXPENSES("49-xj-cwfy", "财务费用（减：收入）"),
    INVESTMENT_LOSSES("50-xj-tzss", "投资损失(减：收益)"),
    DECREASE_DEFERRED_TAX_ASSETS("51-xj-dyjs", "递延所得税资产减少（增加以“-”号填列）"),
    INCREASE_DEFERRED_TAX_LIABILITIES("52-xj-dyzj", "递延所得税负债增加（减少以“-”号填列）"),
    REDUCE_INVENTORY("53-xj-chjs", "存货的减少(减：增加)"),
    DECREASE_OPERATING_RECEIVABLES("54-xj-jyjs", "经营性应收项目的减少(减：增加)"),
    INCREASE_OPERATING_PAYABLE("55-xj-jyzj", "经营性应付项目的增加(减：减少)"),
    OTHER_VALUE("56-xj-qita", "其他"),
    OPERATING_CASH_NET_SECOND("57-xj-jyje", "经营活动产生的现金流量净额"),

    //现金及现金等价物净增加情况
    ENDING_BALANCE_CASH("63-xj-xjqm", "现金期末余额"),
    BEGINNING_BALANCE_CASH("64-xj-xjqc", "减：现金的期初余额"),
    ENDING_BALANCE_CASH_EQUIVALENTS("65-xj-xjqm", "加：现金等价物的期末余额"),
    BEGINNING_BALANCE_CASH_EQUIVALENTS("66-xj-djqc", "减：现金等价物的期初余额"),
    NET_INCREASE_CASH_EQUIVALENTS("67-xj-djqm", "现金及现金等价物净增加额");


    /**
     * -- GETTER --
     *  获取数据库中存储的实际代码
     */
    // 数据库中存储的实际代码
    private final String dbCode;

    /**
     * -- GETTER --
     *  获取项目描述
     */
    private final String description;

    CashFlowItemEnum(String dbCode, String description) {
        this.dbCode = dbCode;
        this.description = description;
    }

    /**
     * 获取枚举名称作为代码
     */
    public String getCode() {
        return this.name();
    }

    /**
     * 根据数据库代码查找对应的枚举
     */
    public static CashFlowItemEnum getByDbCode(String dbCode) {
        for (CashFlowItemEnum item : values()) {
            if (item.dbCode.equals(dbCode)) {
                return item;
            }
        }
        throw new IllegalArgumentException("未知的现金流量项目代码: " + dbCode);
    }

    /**
     * 获取经营活动现金流入项目列表
     */
    public static List<CashFlowItemEnum> getOperatingCashInflowItems() {
        return Arrays.asList(CASH_FROM_SALES, TAX_REFUND, OTHER_OPERATING_CASH_INFLOW);
    }

    /**
     * 获取经营活动现金流入项目数据库代码列表
     */
    public static List<String> getOperatingCashInflowDbCodes() {
        return getOperatingCashInflowItems().stream()
                .map(CashFlowItemEnum::getDbCode)
                .toList();
    }

    /**
     * 获取经营活动现金流出项目列表
     */
    public static List<CashFlowItemEnum> getOperatingCashOutflowItems() {
        return Arrays.asList(CASH_PAID_FOR_GOODS, CASH_PAID_TO_EMPLOYEES, TAXES_PAID, OTHER_OPERATING_CASH_OUTFLOW);
    }

    /**
     * 获取经营活动现金流出项目数据库代码列表
     */
    public static List<String> getOperatingCashOutflowDbCodes() {
        return getOperatingCashOutflowItems().stream()
                .map(CashFlowItemEnum::getDbCode)
                .toList();
    }

    /**
     * 获取投资活动现金流入项目列表
     */
    public static List<CashFlowItemEnum> getInvestingCashInflowItems() {
        return Arrays.asList(CASH_FROM_INVESTMENT_RECOVERY, CASH_FROM_INVESTMENT_INCOME,
                CASH_FROM_DISPOSAL_ASSETS, CASH_FROM_DISPOSAL_SUBSIDIARIES,
                OTHER_INVESTING_CASH_INFLOW);
    }

    /**
     * 获取投资活动现金流入项目数据库代码列表
     */
    public static List<String> getInvestingCashInflowDbCodes() {
        return getInvestingCashInflowItems().stream()
                .map(CashFlowItemEnum::getDbCode)
                .toList();
    }

    /**
     * 获取投资活动现金流出项目列表
     */
    public static List<CashFlowItemEnum> getInvestingCashOutflowItems() {
        return Arrays.asList(CASH_PAID_FOR_ASSETS, CASH_PAID_FOR_INVESTMENTS,
                CASH_PAID_FOR_SUBSIDIARIES, OTHER_INVESTING_CASH_OUTFLOW);
    }

    /**
     * 获取投资活动现金流出项目数据库代码列表
     */
    public static List<String> getInvestingCashOutflowDbCodes() {
        return getInvestingCashOutflowItems().stream()
                .map(CashFlowItemEnum::getDbCode)
                .toList();
    }

    /**
     * 获取筹资活动现金流入项目列表
     */
    public static List<CashFlowItemEnum> getFinancingCashInflowItems() {
        return Arrays.asList(CASH_FROM_INVESTMENTS, CASH_FROM_BORROWINGS, OTHER_FINANCING_CASH_INFLOW);
    }

    /**
     * 获取筹资活动现金流入项目数据库代码列表
     */
    public static List<String> getFinancingCashInflowDbCodes() {
        return getFinancingCashInflowItems().stream()
                .map(CashFlowItemEnum::getDbCode)
                .toList();
    }

    /**
     * 获取筹资活动现金流出项目列表
     */
    public static List<CashFlowItemEnum> getFinancingCashOutflowItems() {
        return Arrays.asList(CASH_REPAYMENT_OF_BORROWINGS, CASH_PAID_FOR_DIVIDENDS, OTHER_FINANCING_CASH_OUTFLOW);
    }

    /**
     * 获取筹资活动现金流出项目数据库代码列表
     */
    public static List<String> getFinancingCashOutflowDbCodes() {
        return getFinancingCashOutflowItems().stream()
                .map(CashFlowItemEnum::getDbCode)
                .toList();
    }


    /**
     * 获取补充资料现金流出项目列表
     */
    public static List<CashFlowItemEnum> getSupplementaryInformationFlowItems() {
        return Arrays.asList(NET_PROFIT, PROVISION_ASSET_IMPAIRMENT, DEPRECIATION_FIXED_ASSETS, AMORTIZATION_INTANGIBLE_ASSETS,
                AMORTIZATION_LONGTERM_DEFERRED_EXPENSES, DISPOSAL_FIXED_ASSETS, LOSS_SCRAPPING_FIXED_ASSETS, LOSS_CHANGE_FAIR_VALUE,
                FINANCIAL_EXPENSES, FINANCIAL_EXPENSES, DECREASE_DEFERRED_TAX_ASSETS, INCREASE_DEFERRED_TAX_LIABILITIES,
                REDUCE_INVENTORY, DECREASE_OPERATING_RECEIVABLES, INCREASE_OPERATING_PAYABLE);
    }

    /**
     * 获取补充资料数据库代码列表除其他项目
     */
    public static List<String> getSupplementaryInformationDbCodes() {
        return getSupplementaryInformationFlowItems().stream()
                .map(CashFlowItemEnum::getDbCode)
                .toList();
    }

    /**
     * 获取现金及现金等价物净增加情况项目列表
     */
    public static List<CashFlowItemEnum> getNetIncreaseCashEquivalentsFlowItems() {
        return Arrays.asList(ENDING_BALANCE_CASH, BEGINNING_BALANCE_CASH, ENDING_BALANCE_CASH_EQUIVALENTS,
                BEGINNING_BALANCE_CASH_EQUIVALENTS, NET_INCREASE_CASH_EQUIVALENTS);
    }

    /**
     * 获取现金及现金等价物净增加情况
     */
    public static List<String> getNetIncreaseCashEquivalents() {
        return getNetIncreaseCashEquivalentsFlowItems().stream()
                .map(CashFlowItemEnum::getDbCode)
                .toList();
    }
}
