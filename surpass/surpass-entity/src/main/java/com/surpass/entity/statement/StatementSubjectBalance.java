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


package com.surpass.entity.statement;

import com.baomidou.mybatisplus.annotation.*;
import com.surpass.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 科目余额数据 jbx_statement_subject_balance
 *
 * @author wuyan
 * {@code @date} 2025-02-03
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("jbx_statement_subject_balance")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatementSubjectBalance extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 账套ID
     */
    @Schema(name = "bookId", description = "所属账套")
    private String bookId;

    @Schema(name = "yearPeriod", description = "期间")
    private String yearPeriod;

    /**
     * 报表周期（如：month、quarter、year）
     */
    @Schema(name = "periodType", description = "报表周期（如：月、季、年）")
    private String periodType;

    /**
     * 排序序号
     */
    @Schema(name = "sortIndex", description = "排序序号")
    private Integer sortIndex;

    /**
     * 原始数据id
     */
    @Schema(name = "sourceId", description = "原始数据id")
    private String sourceId;

    /**
     * 父级科目id
     */
    @Schema(name = "parentId", description = "父级科目id")
    private String parentId;

    /**
     * 科目编码
     */
    @Schema(name = "subjectCode", description = "科目编码")
    private String subjectCode;

    /**
     * 科目名称
     */
    @Schema(name = "subjectName", description = "科目名称")
    private String subjectName;

    /**
     * 借贷方向
     */
    @Schema(name = "direction", description = "借贷方向")
    String direction;

    /**
     * 余额
     */
    @Schema(name = "balance", description = "余额")
    private BigDecimal balance;

    /**
     * 期初余额（借方）
     */
    @Schema(name = "openingBalanceDebit", description = "期初余额（借方）")
    private BigDecimal openingBalanceDebit;

    /**
     * 期初余额（贷方）
     */
    @Schema(name = "openingBalanceCredit", description = "期初余额（贷方）")
    private BigDecimal openingBalanceCredit;

    /**
     * 年初余额（借方）
     */
    @Schema(name = "openingYearBalanceDebit", description = "年初余额（借方）")
    private BigDecimal openingYearBalanceDebit;

    /**
     * 年初余额（贷方）
     */
    @Schema(name = "openingYearBalanceCredit", description = "年初余额（贷方）")
    private BigDecimal openingYearBalanceCredit;

    /**
     * 本期发生额（借方）
     */
    @Schema(name = "currentPeriodDebit", description = "本期发生额（借方）")
    private BigDecimal currentPeriodDebit;

    /**
     * 本期发生额（贷方）
     */
    @Schema(name = "currentPeriodCredit", description = "本期发生额（贷方）")
    private BigDecimal currentPeriodCredit;

    /**
     * 本年累计发生额（借方）
     */
    @Schema(name = "yearToDateDebit", description = "本年累计发生额（借方）")
    private BigDecimal yearToDateDebit;

    /**
     * 本年累计发生额（贷方）
     */
    @Schema(name = "yearToDateCredit", description = "本年累计发生额（贷方）")
    private BigDecimal yearToDateCredit;

    /**
     * 期末余额（借方）
     */
    @Schema(name = "closingBalanceDebit", description = "期末余额（借方）")
    private BigDecimal closingBalanceDebit;

    /**

     */
    @Schema(name = "closingBalanceCredit", description = "期末余额（贷方）")
    private BigDecimal closingBalanceCredit;

    /**
     * 上月末余额
     */
    @Schema(name = "prevbalance", description = "上月余额")
    @TableField(fill = FieldFill.INSERT)
    private BigDecimal prevBalance;


    /**
     * 上月期末余额（借方）
     */
    @Schema(name = "prevClosingBalanceDebit", description = "上月期末余额（借方）")
    @TableField(fill = FieldFill.INSERT)
    private BigDecimal prevClosingBalanceDebit;

    /**
     * 上月期末余额（贷方）
     */
    @Schema(name = "prevClosingBalanceCredit", description = "上月期末余额（贷方）")
    @TableField(fill = FieldFill.INSERT)
    private BigDecimal prevClosingBalanceCredit;

    /**
     * 上月本年累计发生额（借方）
     */
    @Schema(name = "prevYearToDateDebit", description = "上月本年累计发生额（借方）")
    @TableField(fill = FieldFill.INSERT)
    private BigDecimal prevYearToDateDebit;

    /**
     * 上月本年累计发生额（贷方）
     */
    @Schema(name = "prevYearToDateCredit", description = "上月本年累计发生额（贷方）")
    @TableField(fill = FieldFill.INSERT)
    private BigDecimal prevYearToDateCredit;

    /**
     * 是否辅助核算项:n-否;y-是
     */
    private String isAuxiliary;

    /**
     * 当前期是否使用：n-否;y-是
     */
    private String isVoucher;

    /**
     * 删除标记，默认为 'n' (未删除)，如果为 'y' 表示已删除
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;

    @TableField(exist = false)
    private String summary;

    @TableField(exist = false)
    private BigDecimal debit;

    @TableField(exist = false)
    private BigDecimal credit;

    @TableField(exist = false)
    private String voucherNumber;

    @TableField(exist = false)
    private String voucherDate;

    @TableField(exist = false)
    private String recordType;
}
