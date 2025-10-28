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


package com.surpass.entity.journal;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.surpass.entity.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 日记账-账户
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("jbx_journal_account")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalAccount  extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 3265176233566931867L;

	@TableId(type = IdType.ASSIGN_ID)
    String id;

	String bookId;
	/**
	 * 账户类型：现金cash  银行deposit
	 */
	String category;

	String accCode;

	String accName;

	String subjectId;

	String currency;

	String bankNo;

	String bank;

	Integer sortIndex;
	/**
	 * 期初余额
	 */
	BigDecimal openingBalance;
	/**
	 * 可用余额
	 */
	BigDecimal balance;

	String description;

	/**
     * 删除标记
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;

}
