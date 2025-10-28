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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 员工信息编辑对象
 *
 * @author wuyan
 * {@code @date} 2025-01-22
 */

@Data
public class EmployeeChangeDto {
    /**
     * 主键
     */
    @NotNull(message = "编辑对象不能为空", groups = {EditGroup.class})
    private String id;

    /**
     * 姓名
     */
    @NotEmpty(message = "姓名不能为空", groups = {AddGroup.class, EditGroup.class})
    private String displayName;

    /**
     * 电话号码
     */
//    @NotEmpty(message = "电话号码不能为空", groups = {AddGroup.class, EditGroup.class})
    private String mobile;

    /**
     * 邮箱地址
     */
//    @NotEmpty(message = "邮箱不能为空", groups = {AddGroup.class, EditGroup.class})
    private String email;

    /**
     * 性别:0-其他；1-男；2-女
     */
    @NotNull(message = "性别不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer gender;

    /**
     * 出生日期
     */
//    @NotNull(message = "出生日期不能为空", groups = {AddGroup.class, EditGroup.class})
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthDate;

    /**
     * 证件类型
     */
    @NotNull(message = "证件类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer idType;

    /**
     * 证件编码
     */
    @NotEmpty(message = "证件编码不能为空", groups = {AddGroup.class, EditGroup.class})
    private String idCardNo;

    /**
     * 学历
     */
//    @NotEmpty(message = "学历不能为空", groups = {AddGroup.class, EditGroup.class})
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

    /**
     * 工伤保险
     */
    private BigDecimal insuranceEmploymentInjury;

    /**
     * 生育保险
     */
    private BigDecimal insuranceMaternity;

    /**
     * 住房公积金
     */
    private BigDecimal housingProvidentFund;

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
    @NotEmpty(message = "所属部门不能为空", groups = {AddGroup.class, EditGroup.class})
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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date entryDate;

    /**
     * 离职日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date quitDate;

    /**
     * 状态:1-启用;0-禁用
     */
    @NotNull(message = "状态不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer status;

    /**
     * 缴费标准-基数统一:0-系统;1-自定义
     */
    private Integer payBaseRule;

    /**
     * 缴费基数
     */
    private BigDecimal payBaseNumber;

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
    @NotEmpty(message = "员工类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private String employeeType;

    /**
     * 员工状态
     */
    @NotEmpty(message = "员工状态不能为空", groups = {AddGroup.class, EditGroup.class})
    private String employeeStatus;

    private String bookId;
}
