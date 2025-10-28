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


package com.surpass.entity.voucher.dto;

import com.surpass.entity.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 凭证字分页查询对象
 *
 * @author wuyan
 * {@code @date} 2025-01-14
 */

@Data
@EqualsAndHashCode(callSuper=false)
public class VoucherWordPageDto  extends PageQuery {
    /**
	 *
	 */
	private static final long serialVersionUID = -819437184479966227L;

	/**
     * 字头：“收”、“付”、“转”等
     */
    private String wordHead;

    /**
     * 年份
     */
    private Integer wordYear;

    /**
     * 月份
     */
    private Integer wordMonth;

    /**
     * 打印标题
     */
    private String printTitle;

    /**
     * 是否默认
     */
    private Integer isDefault;
}
