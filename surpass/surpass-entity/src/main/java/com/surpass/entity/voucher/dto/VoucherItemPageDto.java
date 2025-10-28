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

import com.surpass.entity.statement.dto.StatementParamsDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 凭证明细分页查询对象
 *
 * @author wuyan
 * {@code @date} 2025-01-14
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class VoucherItemPageDto extends StatementParamsDto {
    /**
	 *
	 */
	private static final long serialVersionUID = -5096607378915289388L;

	/**
     * 凭证ID
     */
    private String voucherId;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 会计科目ID
     */
    private String subjectId;

    /**
     * 科目编号
     */
    private String subjectCode;

    /**
     * 所属期间
     */
    private String[] belongDateRange;

    /**
     * 现金流量类型
     */
    private Integer cashFlowItemType;

    /**
     * 现金流量项目助记词
     */
    private String cashFlowItemCode;

}
