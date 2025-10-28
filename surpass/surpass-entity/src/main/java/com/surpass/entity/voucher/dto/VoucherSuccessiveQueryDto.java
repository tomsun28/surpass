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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 凭证号非连续性查询参数
 *
 * @author wuyan
 * {@code @date} 2025-04-20
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VoucherSuccessiveQueryDto {
    public static final String[] WORD_HEADS = {"记", "收", "付", "转"};

    /**
     * 凭证字字头：“记”、“收”、“付”、“转”等
     */
    private String wordHead;

    /**
     * 起始凭证号
     */
    private Integer startWordNumber;

    /**
     * 所属账套
     */
    private String bookId;

    /**
     * 凭证号修复模式:sequential(顺序补齐)|date（日期重排）
     */
    private String successiveMethod;

    /**
     * 作废凭证参与凭证整理
     */
    private Boolean nullify;
}
