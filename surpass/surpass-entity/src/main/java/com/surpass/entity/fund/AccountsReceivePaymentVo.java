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
 * 简介说明: 应收/付账款仪表盘视图类
 *
 * @author wuyan
 * {@code @date} 2025/05/05 14:03:57
 * {@code @version} 1.0
 */

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountsReceivePaymentVo {

    /**
     * 总余额：”应收/付账款“科目余额。
     */
    private BigDecimal balance;

    /**
     * 应收/付款项下面各个科目余额
     */
    private List<BaseValue<BigDecimal>> subjectBalance;

    /**
     * 平均周转天数：
     * 年初截止上一期期间的应收账款周转天数，反映企业回款速度，天数越小则回款速度越快。
     * 计算公式为：年初至所选期末总天数/(2*营业收入/(期初应收账款+期末应收账款))
     */
    private Float cycleDays;
}
