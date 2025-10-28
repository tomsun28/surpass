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


package com.surpass.util;

/**
 * 简介说明: 凭证相关工具类
 *
 * @author wuyan
 * {@code @date} 2025/01/17 10:06:17
 * {@code @version} 1.0
 */

public class VoucherUtils {
    /**
     * 凭证字号位数
     */
    public static final int VOUCHER_WORD_NUM_LEN = 4;

    /**
     * 生成凭证字：[字头][年份]第[号码]号
     *
     * @param head  字头
     * @param year  年份
     * @param month 月份
     * @param num   字号
     * @return 结果
     */
    public static String createWord(String head, int year, int month, int num) {
        String formatNumber = formatNumber(num);
        return head + "" + formatYear(year) + formatMonth(month) + "第" + formatNumber + "号";
    }

    /**
     * 生成指定位数的数字字符串，不足补零
     *
     * @param number 数字
     * @return 结果
     */
    public static String formatNumber(int number) {
        return String.format("%0" + VOUCHER_WORD_NUM_LEN + "d", number);
    }

    /**
     * 年份格式化
     *
     * @param year 年份
     * @return 结果
     */
    public static String formatYear(int year) {
        return String.format("%0" + 4 + "d", year);
    }

    /**
     * 年份格式化
     *
     * @param month 月份
     * @return 结果
     */
    public static String formatMonth(int month) {
        return String.format("%0" + 2 + "d", month);
    }
}
