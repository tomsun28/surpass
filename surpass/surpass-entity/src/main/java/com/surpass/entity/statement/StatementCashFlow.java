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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.surpass.entity.BaseEntity;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 现金流量表 jbx_statement_cash_flow
 *
 * @author wuyan
 * {@code @date} 2025-02-03
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("jbx_statement_cash_flow")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatementCashFlow extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 报表日期（期末日期）
     */
    @TableField(exist = false)
    @NotBlank(message = "期间不能为空", groups = {AddGroup.class, EditGroup.class})
    @Schema(name = "yearPeriod", description = "期间")
    private String yearPeriod;

    /**
     * 业务日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate reportDate;


    /**
     * 报表周期（如：月度、季度、年度）
     */
    @Schema(name = "periodType", description = "报表周期（如：月、季、年）")
    private String periodType;

    /**
     * 排序序号
     */
    @Schema(name = "sortIndex", description = "排序序号")
    private Integer sortIndex;

    /**
     * 财务项目的名称
     */
    @Schema(name = "itemName", description = "财务项目的名称")
    private String itemName;

    /**
     * 财务项目的code
     */
    @Schema(name = "itemCode", description = "财务项目的助记码")
    private String itemCode;

    /**
     * 本年累计金额
     */
    @Schema(name = "currentAmount", description = "本年累计金额")
    private BigDecimal currentAmount;

    /**
     * 本月金额
     */
    @Schema(name = "monthlyAmount", description = "本月金额")
    private BigDecimal monthlyAmount;

    /**
     * 本月金额
     */
    @Schema(name = "bookId", description = "账套编码")
    private String bookId;


    /**
     * 删除标记，默认为 'n' (未删除)，如果为 'y' 表示已删除
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;

    @TableField(exist = false)
    private Integer isTitle;

    @TableField(exist = false)
    private Integer isMain;

    @TableField(exist = false)
    private Integer isAdditional;

    /**
     * 是否为计算行 0-否;1-是
     */
    @TableField(exist = false)
    private Integer isResult;
}
