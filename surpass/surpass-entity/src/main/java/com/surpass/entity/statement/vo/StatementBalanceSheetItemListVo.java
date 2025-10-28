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


package com.surpass.entity.statement.vo;

import com.surpass.entity.statement.StatementBalanceSheetItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 资产负债表-列表数据
 *
 * @author wuyan
 * {@code @date} 2025-05-22
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatementBalanceSheetItemListVo implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = -8705446953882946583L;

	/**
     * 资产行
     */
    List<StatementBalanceSheetItem> assets;

    /**
     * 负载行
     */
    List<StatementBalanceSheetItem> liability;



}
