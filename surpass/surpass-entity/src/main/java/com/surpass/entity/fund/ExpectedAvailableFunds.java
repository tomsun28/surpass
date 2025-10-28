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


package com.surpass.entity.fund;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 简介说明: 预计可用资金仪表盘视图类
 *
 * @author wuyan
 * {@code @date} 2025/05/06 09:46:26
 * {@code @version} 1.0
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpectedAvailableFunds {

    /**
     * 预计可用资金
     */
    private BigDecimal balance;

    /**
     * 现有资金
     */
    private BigDecimal cashBalance;

    /**
     * 短期应收款
     */
    private BigDecimal accountsReceivable;

    /**
     * 短期应付款
     */
    private BigDecimal accountsPayable;

    /**
     * 现金比率
     */
    private Float cashRatio;

    /**
     * 同期现金比率
     */
    private Float cashRatioLastYear;

    /**
     * 速动比率
     */
    private Float quickRatio;

    /**
     * 同期速动比率
     */
    private Float quickRatioLastYear;
}
