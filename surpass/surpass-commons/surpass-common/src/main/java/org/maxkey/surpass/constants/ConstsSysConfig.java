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


package org.maxkey.surpass.constants;

/**
 * 简介说明: 系统参数定义
 *
 * @author wuyan
 * {@code @date} 2025/03/18 10:02:48
 * {@code @version} 1.0
 */

public class ConstsSysConfig {

    //*************************************基本信息**************************************//

    /**
     * 当前账期
     */
    public static final String SYS_PAYMENT_TERM_CURRENT = "sys.payment.term.current";

    /**
     * 初始账期
     */
    public static final String SYS_PAYMENT_TERM_START = "sys.payment.term.start";

    /**
     * 是否完成初始化任务
     */
    public static final String SYS_INITIALIZE_TASK = "sys.initialize.task";

    //*************************************科目参数**************************************//

    /**
     * 科目级数
     * 科目级次和长度调大后，不能再调小（即：不能再恢复到调整前的级次和长度），请谨慎操作！
     */
    public static final String SYS_SUBJECT_LAVAL = "sys.subject.level";
    /**
     * 科目编码长度
     * 科目级次和长度调大后，不能再调小（即：不能再恢复到调整前的级次和长度），请谨慎操作！
     */
    public static final String SYS_SUBJECT_CODES_LENGTH = "sys.subject.codes.length";

    /**
     * 配置模板id
     */
    public static final String SYS_CONFIG_TEMPLATE_ID = "template";

    //*************************************系统参数**************************************//

    /**
     * 应收账款科目编号
     */
    public static final String SYS_DEFAULT_ACCOUNTS_RECEIVABLE = "sys.default.accountsReceivable";

    /**
     * 应付账款科目编号
     */
    public static final String SYS_DEFAULT_ACCOUNTS_PAYABLE = "sys.default.accountsPayable";

    /**
     * 短期应收款科目编号
     */
    public static final String SYS_DEFAULT_SHORT_TERM_ACCOUNTS_RECEIVABLE = "sys.default.shortTermAccountsReceivable";

    /**
     * 短期应付款科目编号
     */
    public static final String SYS_DEFAULT_SHORT_TERM_ACCOUNTS_PAYABLE = "sys.default.shortTermAccountsPayable";

    /**
     * 交易性金融资产科目编号
     */
    public static final String SYS_DEFAULT_TRADING_FINANCIAL_ASSETS = "sys.default.tradingFinancialAssets";

    /**
     * 应收票据科目编号
     */
    public static final String SYS_DEFAULT_BILL_RECEIVABLE = "sys.default.billReceivable";

    /**
     * 流动负债项编号
     */
    public static final String SYS_DEFAULT_CURRENT_LIABILITIES = "sys.default.currentLiabilities";
    /**
     * 流动资产项编号
     */
    public static final String SYS_DEFAULT_CURRENT_ASSETS = "sys.default.currentAssets";
    /**
     * 流动资产-存货项编号
     */
    public static final String SYS_DEFAULT_CURRENT_ASSETS_INVENTORY = "sys.default.currentAssetsInventory";
    /**
     * 现金项科目编号
     */
    public static final String SYS_DEFAULT_CASH_SUBJECT_CODES = "sys.default.cashSubjectCodes";

    /**
     * 利润表-净利润编号
     */
    public static final String SYS_DEFAULT_INCOME_NET_PROFIT = "sys.default.incomeNetProfit";

    /**
     * 利润表-营业收入编号
     */
    public static final String SYS_DEFAULT_INCOME_OPERATING_REVENUE = "sys.default.incomeOperatingRevenue";

    /**
     * 利润表-营业成本编号
     */
    public static final String SYS_DEFAULT_INCOME_OPERATING_COSTS = "sys.default.incomeOperatingCosts";

    /**
     * 利润表-营业利润编号
     */
    public static final String SYS_DEFAULT_INCOME_OPERATING_PROFIT = "sys.default.incomeOperatingProfit";


    /**
     * 利润表-管理费用
     */
    public static final String SYS_DEFAULT_ADMINISTRATIVE_EXPENSES = "sys.default.administrativeExpenses";


    /**
     * 利润表-销售费用
     */
    public static final String SYS_DEFAULT_SELLING_EXPENSES = "sys.default.sellingExpenses";


    /**
     * 利润表-财务费用
     */
    public static final String SYS_DEFAULT_FINANCIAL_EXPENSES = "sys.default.financialExpenses";

    /**
     * 利润表-税金及附加
     */
    public static final String SYS_DEFAULT_ADDED_TAX = "sys.default.addedTax";

    /**
     * 利润表-所得税费用
     */
    public static final String SYS_DEFAULT_INCOME_TAX_EXPENSES = "sys.default.incomeTaxExpenses";


}
