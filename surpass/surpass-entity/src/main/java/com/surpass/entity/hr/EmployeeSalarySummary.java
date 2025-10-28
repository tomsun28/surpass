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


package com.surpass.entity.hr;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.surpass.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.YearMonth;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/2/27 17:35
 */

@EqualsAndHashCode(callSuper = true)
@TableName("jbx_employee_salary_summary")
@Data
public class EmployeeSalarySummary extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -8083166894062059473L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String label;

    private Integer peopleNumber;

    @JsonFormat(pattern="yyyy-MM")
    YearMonth belongDate;

    private BigDecimal payBasic;

    private BigDecimal payMerit;

    private BigDecimal payPost;

    private BigDecimal bonus;

    private BigDecimal overtime;

    private BigDecimal allowance;

    private BigDecimal backPay;

    /*个人代扣社保*/
    private BigDecimal totalSocialInsurance;

    /*劳务费*/
    private BigDecimal laborFee;

    /*个人代扣公积金*/
    private BigDecimal providentFund;

    private BigDecimal attendance;

    private BigDecimal otherDeductions;

    /*个税*/
    private BigDecimal personalTax;
    /**
     * 应发工资 = 工资+应增-应扣
     */
    private BigDecimal payAmount;

    /*实发工资*/
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

    /*公司支出成本*/
    private BigDecimal businessExpenditureCosts;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;

    private String description;

}
