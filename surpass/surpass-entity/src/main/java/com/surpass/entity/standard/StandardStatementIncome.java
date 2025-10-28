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


package com.surpass.entity.standard;

import com.baomidou.mybatisplus.annotation.*;
import com.surpass.entity.BaseEntity;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 利润表模板 jbx_standard_statement_income_item
 *
 * @author wuyan
 * {@code @date} 2025-02-03
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("jbx_standard_statement_income")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandardStatementIncome extends BaseEntity implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 1364790594151305735L;

	/**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 账套ID
     */
    @NotBlank(message = "准则不能为空", groups = {AddGroup.class, EditGroup.class})
    @Schema(name = "standardId", description = "所属准则")
    private String standardId;


    /**
     * 财务项目的名称
     */
    @NotBlank(message = "财务项目编码不能为空", groups = {AddGroup.class, EditGroup.class})
    @Schema(name = "itemCode", description = "财务项目编码")
    private String itemCode;

    /**
     * 财务项目的名称
     */
    @NotBlank(message = "财务项目不能为空", groups = {AddGroup.class, EditGroup.class})
    @Schema(name = "itemName", description = "财务项目的名称")
    private String itemName;

    /**
     * 排序序号
     */
//    @NotNull(message = "排序序号不能为空", groups = {AddGroup.class, EditGroup.class})
    @Schema(name = "sortIndex", description = "排序序号")
    private Integer sortIndex;

    /**
     * 级别
     */
    @Schema(name = "level", description = "级别")
    private Integer level;

    /**
     * 父级ID
     */
    @Schema(name = "parentItemCode", description = "父级Code")
    private String parentItemCode;

    @NotBlank(message = "计算方式(+,-)不能为空", groups = {AddGroup.class, EditGroup.class})
    @Schema(description = "计算方式：+，-")
    private String symbol;

    @Schema(name = "subjectFlag", description = "科目标志")
    private String subjectFlag;

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
    private List<StandardStatementRules> rules;

}
