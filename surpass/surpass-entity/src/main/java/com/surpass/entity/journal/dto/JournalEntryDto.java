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


package com.surpass.entity.journal.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;



@Data
public class JournalEntryDto {

	String id;

	String bookId;
	/**
	 * 账户类型：现金cash  银行deposit
	 */
	String category;

	String remark;

	String accId;

	String accCode;

	String accName;

	String subjectId;

	String voucherId;
	/**
	 * 方向收入和支出，  I或E，Income and Expenditure
	 */
	String direction;

	BigDecimal income;

	BigDecimal expenditure;

	BigDecimal balance;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	Date tradeDate;

	String description;
}
