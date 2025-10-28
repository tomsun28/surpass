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


package com.surpass.entity.config;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/2/12 10:53
 */

@TableName("jbx_config_insurance_fund")
@Data
public class ConfigInsuranceFund {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 缴费基数
     */
    private BigDecimal payBase;

    /**
     * 养老保险
     */
    private BigDecimal endowmentPersonal;

    private BigDecimal endowmentBusiness;


    /**
     * 医疗保险
     */

    private BigDecimal medicalPersonal;

    private BigDecimal medicalBusiness;


    /**
     * 失业保险
     */

    private BigDecimal unemploymentPersonal;

    private BigDecimal unemploymentBusiness;


    /**
     * 工伤保险
     */

    private BigDecimal employmentInjuryPersonal;

    private BigDecimal employmentInjuryBusiness;


    /**
     * 生育保险
     */

    private BigDecimal maternityPersonal;

    private BigDecimal maternityBusiness;

    /**
     * 大病医疗保险
     */
    private BigDecimal seriousMedicalBusiness;
    private BigDecimal seriousMedicalPersonal;
    /**
     * 住房公积金
     */

    private BigDecimal providentFundSupPersonal;

    private BigDecimal providentFundSupBusiness;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String bookId;


    // 获取百分比转换后的小数值
    public BigDecimal getEmploymentInjuryPersonalRate() {
        return employmentInjuryPersonal.divide(new BigDecimal("100"), 3, RoundingMode.HALF_UP);
    }

    public BigDecimal getProvidentFundSupPersonalRate() {
        return providentFundSupPersonal.divide(new BigDecimal("100"), 3, RoundingMode.HALF_UP);
    }

    public BigDecimal getUnemploymentPersonalRate() {
        return unemploymentPersonal.divide(new BigDecimal("100"), 3, RoundingMode.HALF_UP);
    }

    public BigDecimal getMedicalPersonalRate() {
        return medicalPersonal.divide(new BigDecimal("100"), 3, RoundingMode.HALF_UP);
    }

    public BigDecimal getMaternityPersonalRate() {
        return maternityPersonal.divide(new BigDecimal("100"), 3, RoundingMode.HALF_UP);
    }

    public BigDecimal getEndowmentPersonalRate() {
        return endowmentPersonal.divide(new BigDecimal("100"), 3, RoundingMode.HALF_UP);
    }

    // 企业
    public BigDecimal getEmploymentInjuryBusinessRate() {
        return employmentInjuryBusiness.divide(new BigDecimal("100"), 3, RoundingMode.HALF_UP);
    }

    public BigDecimal getProvidentFundSupBusinessRate() {
        return providentFundSupBusiness.divide(new BigDecimal("100"), 3, RoundingMode.HALF_UP);
    }

    public BigDecimal getUnemploymentBusinessRate() {
        return unemploymentBusiness.divide(new BigDecimal("100"), 3, RoundingMode.HALF_UP);
    }

    public BigDecimal getMedicalBusinessRate() {
        return medicalBusiness.divide(new BigDecimal("100"), 3, RoundingMode.HALF_UP);
    }

    public BigDecimal getMaternityBusinessRate() {
        return maternityBusiness.divide(new BigDecimal("100"), 3, RoundingMode.HALF_UP);
    }

    public BigDecimal getEndowmentBusinessRate() {
        return endowmentBusiness.divide(new BigDecimal("100"), 3, RoundingMode.HALF_UP);
    }
}
