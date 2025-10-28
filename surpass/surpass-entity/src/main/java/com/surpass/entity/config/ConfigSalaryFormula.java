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


package com.surpass.entity.config;

import com.baomidou.mybatisplus.annotation.*;
import com.surpass.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/2/8 17:46
 */

@EqualsAndHashCode(callSuper = true)
@TableName("jbx_config_salary_formula")
@Data
public class ConfigSalaryFormula extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -39619691289916070L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    String ruleName;

    String ruleDescription;

    String formula;

    String formulaText;

    /**
     * 状态:1-启用;0-禁用
     */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;
}
