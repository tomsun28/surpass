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
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 报表统计规则实体对象 jbx_statement_rules
 *
 * @author wuyan
 * {@code @date} 2025-03-19
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("jbx_statement_rules")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatementRules extends BaseEntity implements Serializable {

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
    @NotBlank(message = "账套不能为空", groups = {AddGroup.class, EditGroup.class})
    @Schema(name = "bookId", description = "所属账套")
    private String bookId;

    @NotBlank(message = "报表类型不能为空", groups = {AddGroup.class, EditGroup.class})
    @Schema(description = "报表类型")
    private String type;

    @NotBlank(message = "报表类目不能为空", groups = {AddGroup.class, EditGroup.class})
    @Schema(description = "报表类目ID")
    private String itemCode;

    @NotBlank(message = "科目代码不能为空", groups = {AddGroup.class, EditGroup.class})
    @Schema(description = "科目代码")
    private String subjectCode;

    @NotBlank(message = "取数规则不能为空", groups = {AddGroup.class, EditGroup.class})
    @Schema(description = "取数规则")
    private String rule;

    @NotBlank(message = "计算方式(+,-)不能为空", groups = {AddGroup.class, EditGroup.class})
    @Schema(description = "计算方式：+，-")
    private String symbol;

    /**
     * 期末余额
     */
    @TableField(exist = false)
    private BigDecimal closingBalance;

    /**
     * 年初余额
     */
    @TableField(exist = false)
    private BigDecimal openingYearBalance;
}
