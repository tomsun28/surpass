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

/**
 * 报表类型枚举
 */

public enum StatementTypeEnum {
    balance_sheet,          // 资产负债表
    income,                 // 利润表
    cash_flow,              // 现金流量表
    subject_balance,        // 科目余额表
    voucher_summary;        // 凭证汇总表
}
