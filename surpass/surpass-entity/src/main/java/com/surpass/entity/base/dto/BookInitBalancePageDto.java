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


package com.surpass.entity.base.dto;

import com.surpass.entity.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 初始余额查询对象
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class BookInitBalancePageDto extends PageQuery {
    /**
	 *
	 */
	private static final long serialVersionUID = 6252697330250960515L;

	/**
     * 所属账套
     */
    private String bookId;

    /**
     * 类别
     */
    private Integer category;

    /**
     * 初始余额编码
     */
    private String assistType;

    /**
     * 初始余额名称
     */
    private String name;

    /**
     * 初始余额名称
     */
    private String code;
}
