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


package com.surpass.enums;

import lombok.Getter;

/**
 * 报表查询类型枚举类
 */

@Getter
public enum StatementPeriodTypeEnum {
    YEAR("year"),                       // 年度
    MONTH("month"),                     // 月度
    QUARTER("quarter"),                 // 季度
    HALF_YEAR("halfYear"),              // 半年
    BETWEEN_MONTH("between");           // 月份区间

    private final String value;

    StatementPeriodTypeEnum(String value) {
        this.value = value;
    }
}
