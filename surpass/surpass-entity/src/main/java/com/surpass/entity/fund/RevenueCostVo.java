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
import java.util.List;

/**
 * 简介说明: 收入成本视图类
 *
 * @author wuyan
 * {@code @date} 2025/05/05 14:03:57
 * {@code @version} 1.0
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueCostVo {

    /**
     * 营业收入
     */
    private BigDecimal balance;

    /**
     * 营业成本
     */
    private BigDecimal balanceOperatingCosts;

    /**
     * 同期收入
     */
    private Float balanceLastYear;

    /**
     * 上期收入
     */
    private Float balanceLast;

    /**
     * 毛利率
     */
    private Float balanceRatio;

    /**
     * 同期成本
     */
    private Float balanceRatioLastYear;

    /**
     * 上期成本
     */
    private Float balanceRatioLast;

    /**
     * 近期变动趋势-收入
     */
    private List<BaseValue<BigDecimal>> balanceList;

    /**
     * 近期变动趋势-成本
     */
    private List<BaseValue<BigDecimal>> balanceRatioList;
}
