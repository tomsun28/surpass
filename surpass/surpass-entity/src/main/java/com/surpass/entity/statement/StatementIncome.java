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
import java.util.List;
import java.util.Map;

/**
 * 利润表 jbx_statement_income
 *
 * @author wuyan
 * {@code @date} 2025-02-03
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("jbx_statement_income")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatementIncome extends BaseEntity implements Serializable {

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

    @NotBlank(message = "期间不能为空", groups = {AddGroup.class, EditGroup.class})
    @Schema(name = "yearPeriod", description = "期间")
    private String yearPeriod;

    /**
     * 报表周期（如：month、quarter、year）
     */
    @NotBlank(message = "报表周期（月、季、年）不能为空", groups = {AddGroup.class, EditGroup.class})
    @Schema(name = "periodType", description = "报表周期（如：月、季、年）")
    private String periodType;

    /**
     * 删除标记，默认为 'n' (未删除)，如果为 'y' 表示已删除
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;

    /**
     * 计算规则
     */
    @TableField(exist = false)
    private List<StatementIncomeItem> items;

    @TableField(exist = false)
    private Map<String,StatementIncomeItem> itemMap;

}
