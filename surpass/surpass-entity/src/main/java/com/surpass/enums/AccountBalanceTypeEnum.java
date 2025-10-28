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
 * 报表取数规则
 */

public enum AccountBalanceTypeEnum {
    BALANCE, // 余额
    DEBIT_BALANCE, // 借方余额
    CREDIT_BALANCE, // 贷方余额
    SUBJECT_DEBIT_BALANCE, // 科目借方余额
    SUBJECT_CREDIT_BALANCE, // 科目贷方余额
    CURRENT_SUBJECT_DEBIT_BALANCE, // 本级科目借方余额
    CURRENT_SUBJECT_CREDIT_BALANCE, // 本级科目贷方余额
    PROFIT_AND_LOSS_AMOUNT, // 损益发生额
    DEBIT_AMOUNT, // 借方发生额
    CREDIT_AMOUNT, // 贷方发生额
}
