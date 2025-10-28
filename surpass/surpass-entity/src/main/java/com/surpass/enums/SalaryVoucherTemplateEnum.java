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

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/5/8 10:32
 */

@Getter
public enum SalaryVoucherTemplateEnum {
    COMPANY_COSTS("companyCosts", "公司成本"),
    SALARY_PAYABLE("salaryPayable", "应发工资"),
    ACTUAL_SALARY("actualSalary", "实发工资"),
    PERSONAL_INCOME_TAX("personalIncomeTax", "个人所得税"),
    PERSONAL_WITHHOLDING_SOCIAL_SECURITY("personalWithholdingOfSocialSecurity", "个人代扣社保"),
    PERSONAL_WITHHOLDING_PROVIDENT_FUND("personalWithholdingOfProvidentFund", "个人代扣公积金"),
    ENTERPRISES_PAY_SOCIAL_INSURANCE("enterprisesPaySocialInsurance", "企业缴纳社保"),
    PROVIDENT_FUND_PAID_BY_ENTERPRISES("providentFundPaidByEnterprises", "企业缴纳公积金");

    /**
     *  获取数据库取值
     */
    private final String value;

    /**
     *  获取取值描述
     */
    private final String description;

    SalaryVoucherTemplateEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static SalaryVoucherTemplateEnum fromValue(String value) {
        for (SalaryVoucherTemplateEnum item : values()) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }

}
