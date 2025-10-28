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

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.surpass.entity.BaseEntity;
import lombok.*;

/**
 * 员工对象 jbx_employee
 *
 * @author wuyan
 * {@code @date} 2025-01-22
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("jbx_employee")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 姓名
     */
    private String displayName;

    /**
     * 电话号码
     */
    private String mobile;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 性别:0-其他；1-男；2-女
     */
    private Integer gender;

    /**
     * 出生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthDate;

    /**
     * 证件类型
     */
    private Integer idType;

    /**
     * 证件编码
     */
    private String idCardNo;

    /**
     * 学历
     */
    private String education;

    /**
     * 毕业院校
     */
    private String graduateFrom;

    /**
     * 毕业时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date graduateDate;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 银行卡
     */
    private String bankCardNo;

    /**
     * 住址
     */
    private String homeAddress;

    /**
     * 基本工资
     */
    private BigDecimal payBasic;

    /**
     * 绩效奖金
     */
    private BigDecimal payMerit;

    /**
     * 岗位工资
     */
    private BigDecimal payPost;

    /**
     * 劳务费
     */
    private BigDecimal laborFee;

    /**
     * 缴费标准-基数统一:0-系统;1-自定义
     */
    private Integer payBaseRule;

    /**
     * 缴费基数
     */
    private BigDecimal payBaseNumber;

    /**
     * 养老保险
     */
    private BigDecimal insuranceEndowment;

    /**
     * 养老保险基数规则规则:0-系统;1-自定义
     */
    private Integer insuranceEndowmentRule;

    /**
     * 医疗保险
     */
    private BigDecimal insuranceMedical;

    /**
     * 医疗保险基数规则规则:0-系统;1-自定义
     */
    private Integer insuranceMedicalRule;

    /**
     * 失业保险
     */
    private BigDecimal insuranceUnemployment;

    /**
     * 失业保险基数规则规则:0-系统;1-自定义
     */
    private Integer insuranceUnemploymentRule;

    /**
     * 工伤保险
     */
    private BigDecimal insuranceEmploymentInjury;

    /**
     * 工伤保险数规则规则:0-系统;1-自定义
     */
    private Integer insuranceEmploymentInjuryRule;

    /**
     * 生育保险
     */
    private BigDecimal insuranceMaternity;

    /**
     * 生育保险数规则规则:0-系统;1-自定义
     */
    private Integer insuranceMaternityRule;

    /**
     * 住房公积金
     */
    private BigDecimal housingProvidentFund;

    /**
     * 生育保险数规则规则:0-系统;1-自定义
     */
    private Integer housingProvidentFundRule;

    /**
     * 养老保险-补充
     */
    private BigDecimal insuranceEndowmentSup;

    /**
     * 医疗保险-补充
     */
    private BigDecimal insuranceMedicalSup;

    /**
     * 住房公积金-补充
     */
    private BigDecimal housingProvidentFundSup;

    /**
     * 工号
     */
    private String employeeNumber;

    /**
     * 部门ID
     */
    private String departmentId;

    /**
     * 职务
     */
    private String jobTitle;

    /**
     * 经理编号
     */
    private String managerId;

    /**
     * 入职日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date entryDate;

    /**
     * 离职日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date quitDate;

    /**
     * 账套ID
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String bookId;

    /**
     * 状态:1-启用;0-禁用
     */
    private Integer status;

    /**
     * 社保卡账户
     */
    private String insuranceFundCard;

    /**
     * 医保账户
     */
    private String medicalCard;

    /**
     * 员工类型
     */
    private String employeeType;

    /**
     * 员工状态
     */
    private String employeeStatus;


    /**
     * 删除标记
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;

    @TableField(exist = false)
    private String departmentName;

    @TableField(exist = false)
    private String booksSetName;
}
