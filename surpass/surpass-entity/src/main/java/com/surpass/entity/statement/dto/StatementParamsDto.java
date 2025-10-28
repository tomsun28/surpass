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


package com.surpass.entity.statement.dto;

import com.surpass.entity.PageQuery;
import com.surpass.enums.CounterTypeEnum;
import com.surpass.enums.StatementPeriodTypeEnum;
import com.surpass.util.DateUtils;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 * 财务报表查询参数对象
 *
 * @author wuyan
 * {@code @date} 2025-02-03
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatementParamsDto extends PageQuery {

    /**
     *
     */
    private static final long serialVersionUID = -3371902023167307161L;
    /**
     * 账套ID
     */
    private String bookId;
    /**
     * 统计类型：年、季、月
     */
    private String periodType;

    /**
     * 所属季度：Q1、Q2、Q3、Q4
     */
    private String reportQuarter;

    /**
     * 查询日期：月（yyyy-MM）、季度（yyyy Q[1,2,3,4]）、半年（yyyy H[1,2]）、年（yyyy）
     */
    private String reportDate;


    private Integer year;
    private Integer month;
    private Integer quarter;
    private Integer half;

    private Integer endMonth;
    private Integer endQuarter;


    private String dateRangeStart;

    private String dateRangeEnd;

    /**
     * 时间范围：[起始时间，结束时间]
     */
    private String[] dateRange;

    /**
     * 汇总方式：SUM，MIN，MAX等
     */
    private String countType;

    /**
     * 显示辅助核算
     */
    private Boolean showAux;

    /**
     * 显示所有科目
     */
    private Boolean showAll;

    /**
     * 科目编码过滤
     */
    private List<String> subjectCodes;

    private String subjectCode;

    private String subjectName;

    public void parse() {
        if (StringUtils.isEmpty(bookId)) {
            throw new IllegalArgumentException("账套参数为空");
        }
        if (StringUtils.isEmpty(countType)) {
            this.countType = CounterTypeEnum.SUM.name();
        }
        try {
            // 尝试将 countType 转换为枚举，验证是否合法
            CounterTypeEnum.valueOf(this.countType);

            if (StatementPeriodTypeEnum.YEAR.getValue().equals(this.periodType)) {
                this.year = Integer.parseInt(reportDate.substring(0, 4));
                this.month = null;
                this.quarter = null;
                // 设置全年起始和结束日期
                this.dateRange = new String[]{this.year + "-01", this.year + "-12"};
                //年初日期
                this.dateRangeStart = year + "-01-01";
                //年终日期
                this.dateRangeEnd = year + "-12-31";
                this.reportDate = this.year + "-12";
            } else if (StatementPeriodTypeEnum.MONTH.getValue().equals(this.periodType)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                var date = java.time.YearMonth.parse(reportDate, formatter);
                this.year = date.getYear();
                this.month = date.getMonthValue();
                this.quarter = null;
                //本月
                this.dateRange = new String[]{reportDate, reportDate};
                //月初日期
                this.dateRangeStart = reportDate + "-01";
                //月末日期
                this.dateRangeEnd = DateUtils.lastDay(reportDate + "-01").toString();
            } else if (StatementPeriodTypeEnum.QUARTER.getValue().equals(this.periodType)) {
                this.year = Integer.parseInt(reportDate.substring(0, 4));
                if (StringUtils.isNotBlank(reportQuarter)) {
                    this.quarter = parseQuarter(reportQuarter);
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                    var date = java.time.YearMonth.parse(reportDate, formatter);
                    this.month = date.getMonthValue();
                    switch (month) {
                        case 1, 2, 3 -> this.quarter = 1;
                        case 4, 5, 6 -> this.quarter = 2;
                        case 7, 8, 9 -> this.quarter = 3;
                        case 10, 11, 12 -> this.quarter = 4;
                    }
                }
                this.reportQuarter = "Q" + this.quarter;
                this.month = null;

                if (this.quarter == 1) {
                    //报告年月
                    this.reportDate = this.year + "-03";
                    //季初日期
                    this.dateRangeStart = this.year + "-01-01";
                    //季末日期
                    this.dateRangeEnd = this.year + "-03-31";
                    //月份范围
                    this.dateRange = new String[]{this.year + "-01", this.year + "-03"};
                } else if (this.quarter == 2) {
                    //报告年月
                    this.reportDate = this.year + "-06";
                    //季初日期
                    this.dateRangeStart = this.year + "-04-01";
                    //季末日期
                    this.dateRangeEnd = this.year + "-06-30";
                    //月份范围
                    this.dateRange = new String[]{this.year + "-04", this.year + "-06"};
                } else if (this.quarter == 3) {
                    //报告年月
                    this.reportDate = this.year + "-09";
                    //季初日期
                    this.dateRangeStart = this.year + "-07-01";
                    //季末日期
                    this.dateRangeEnd = this.year + "-09-30";
                    //月份范围
                    this.dateRange = new String[]{this.year + "-07", this.year + "-09"};
                } else if (this.quarter == 4) {
                    //报告年月
                    this.reportDate = this.year + "-12";
                    //季初日期
                    this.dateRangeStart = this.year + "-10-01";
                    //季末日期
                    this.dateRangeEnd = this.year + "-12-31";
                    this.dateRange = new String[]{this.year + "-10", this.year + "-12"};
                }
            } else if (StatementPeriodTypeEnum.HALF_YEAR.getValue().equals(this.periodType)) {
                this.year = Integer.parseInt(reportDate.substring(0, 4));
                this.half = parseHalfYear(reportDate.substring(5, 7));
                if (this.half == 1) {
                    //报告年月
                    this.reportDate = this.year + "-06";
                    //起始-终结日期
                    this.dateRangeStart = this.year + "-01-01";
                    this.dateRangeEnd = this.year + "-06-30";
                    //月份范围
                    this.dateRange = new String[]{this.year + "-01", this.year + "-06"};
                } else if (this.half == 2) {
                    //报告年月
                    this.reportDate = this.year + "-12";
                    //起始-终结日期
                    this.dateRangeStart = this.year + "-07-01";
                    this.dateRangeEnd = this.year + "-12-31";
                    //月份范围
                    this.dateRange = new String[]{this.year + "-07", this.year + "-12"};
                }
            } else {
                if (!StatementPeriodTypeEnum.BETWEEN_MONTH.getValue().equals(this.periodType)) {
                    throw new IllegalArgumentException("无效的统计类型：" + this.periodType);
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private int parseQuarter(String quarterStr) throws IllegalArgumentException {
        return switch (quarterStr.toUpperCase()) {
            case "Q1" -> 1;
            case "Q2" -> 2;
            case "Q3" -> 3;
            case "Q4" -> 4;
            default -> throw new IllegalArgumentException("无效的季度标识：" + quarterStr);
        };
    }

    private int parseHalfYear(String halfStr) throws IllegalArgumentException {
        return switch (halfStr.toUpperCase()) {
            case "H1" -> 1;
            case "H2" -> 2;
            default -> throw new IllegalArgumentException("无效的季度标识：" + halfStr);
        };
    }

    /**
     * 获取时间范围中的所有月份
     */
    public List<String> getAllMonths() {
        return getAllMonths(null);
    }

    public List<String> getAllMonths(String currentMonth) {
        List<String> months = new ArrayList<>();
        if (dateRange == null || dateRange.length != 2) {
            throw new IllegalArgumentException("dateRange must contain exactly two elements: start and end.");
        }

        YearMonth start = YearMonth.parse(dateRange[0]);
        YearMonth end = currentMonth == null ? YearMonth.parse(dateRange[1])
                : currentMonth.compareTo(dateRange[1]) > 0
                ? YearMonth.parse(dateRange[1])
                : YearMonth.parse(currentMonth);

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }

        YearMonth current = start;
        while (!current.isAfter(end)) {
            months.add(current.toString()); // format is yyyy-MM by default
            current = current.plusMonths(1);
        }

        return months;
    }

    /**
     * 判断是否是季报所在月份
     *
     * @return true:是季报所在月份
     */
    public boolean isQuarterReportMonth() {
        this.parse();
        return isQuarterReportMonth(this.dateRange[1]);
    }

    /**
     * 判断是否季末
     *
     * @param yyyy_MM 2025-03 2025-06 2025-09 2025-12
     * @return boolean
     */
    public boolean isQuarterReportMonth(String yyyy_MM) {
        return yyyy_MM.endsWith("03")
                || yyyy_MM.endsWith("06")
                || yyyy_MM.endsWith("09")
                || yyyy_MM.endsWith("12");
    }

    /**
     * 判断是否是年报所在月份
     *
     * @return true:是年报所在月份
     */
    public boolean isYearReportMonth() {
        this.parse();
        return isYearReportMonth(this.dateRange[1]);
    }

    /**
     * 判断是否12月
     *
     * @param yyyy_MM 2025-12
     * @return boolean
     */
    public boolean isYearReportMonth(String yyyy_MM) {
        return yyyy_MM.endsWith("12");
    }
}
