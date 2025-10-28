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


package com.surpass.entity.hr.dto;

import com.surpass.validate.EditGroup;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/2/20 10:35
 */

@Data
public class SalaryDetailChangeDto {

    @NotEmpty(message = "ID不能为空", groups = {EditGroup.class})
    private String id;

    private BigDecimal payBasic;

    private BigDecimal payMerit;

    private BigDecimal payPost;

    private BigDecimal bonus;

    private BigDecimal overtime;

    private BigDecimal allowance;

    private BigDecimal backPay;

    private BigDecimal totalSocialInsurance;

    /*劳务费*/
    private BigDecimal laborFee;

    private BigDecimal providentFund;

    private BigDecimal attendance;

    private BigDecimal otherDeductions;

    private BigDecimal personalTax;

    private BigDecimal payAmount;

    private BigDecimal totalAmount;

    private String bookId;

    /*公司社保*/
    private BigDecimal businessSocialInsurance;

    /*公司公积金*/
    private BigDecimal businessProvidentFund;

    /*应税工资*/
    private BigDecimal taxableWages;

    /*税务抵扣*/
    private BigDecimal taxDeduction;

    private BigDecimal businessExpenditureCosts;

    /**
     * 养老保险
     */
    private BigDecimal insuranceEndowment;

    /**
     * 医疗保险
     */
    private BigDecimal insuranceMedical;

    /**
     * 失业保险
     */
    private BigDecimal insuranceUnemployment;
}
