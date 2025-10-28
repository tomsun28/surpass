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

import com.surpass.entity.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=false)
public class JournalSummaryPageDto extends PageQuery {

	/**
	 *
	 */
	private static final long serialVersionUID = -2209525588972406022L;

	String bookId;
	/**
	 * 年度N期
	 */
	Integer yearPeriod;

	/**
	 * 选择年度N期
	 */
	String yearPeriodPicker;

	/**
	 * 年度
	 */
	Integer years;

	/**
	 * 第*期
	 */
	Integer periods;

	/**
	 * 账户编码
	 */
    String accCode;

    /**
     * 账户名称
     */
    String accName;

}
