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

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.surpass.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 税务个人附加扣除
 */
@EqualsAndHashCode(callSuper = true)
@TableName("jbx_employee_tax_deduction")
@Data
public class EmployeeTaxDeduction  extends BaseEntity {

	/**
	 *
	 */
	private static final long serialVersionUID = -4470698555495529250L;

	String id;
	 /**
     * 账套ID
     */
    String bookId;
	/**
	 * 工号
	 */
	String employeeNo;
	/**
	 * 姓名
	 */
	String employeeName;
	/**
	 * 证件类型
	 */
	String idCardType;
	/**
	 * 证件号
	 */
	String idCardNo;
	/**
	 * 子女教育
	 */
	Double education;
	/**
	 * 继续教育
	 */
	Double continuingEducation;
	/**
	 * 大病医疗
	 */
	Double medical;
	/**
	 * 住房贷款利息
	 */
	Double housingLoan;
	/**
	 * 住房租金
	 */
	Double rent;
	/**
	 * 赡养老人
	 */
	Double elderlyCare;
	/**
	 * 3岁以下婴幼儿照护
	 */
	Double infantsCare;
	/**
	 * 个人养老金
	 */
	Double individualPension;
	/**
	 * 企业(职业)年金
	 */
	Double enterprisePension;
	/**
	 * 商业健康保险
	 */
	Double commercialHealth;
	/**
	 * 税延养老保险
	 */
	Double deferredPension;
	/**
	 * 准予扣除的捐赠额
	 */
	Double donationAllowed;
	/**
	 * 其他费用扣除
	 */
	Double others;
	/**
	 * 年度期间
	 */
	int yearPeriod;
	/**
	 * 年度
	 */
	int years;
	/**
	 * 月份
	 */
	int periods;

	/**
     * 删除标记
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    String deleted;

	public Double getTotalTaxDeduction() {
		return this.education + this.continuingEducation + this.medical + this.housingLoan + this.rent + this.elderlyCare + this.infantsCare +
				this.individualPension + this.enterprisePension + this.commercialHealth + this.deferredPension + this.donationAllowed +
				this.others;
	}
}
