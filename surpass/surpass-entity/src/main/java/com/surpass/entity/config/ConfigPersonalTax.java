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
 * @time: 2025/2/6 17:45
 */


@EqualsAndHashCode(callSuper = true)
@TableName("jbx_config_personal_tax")
@Data
public class ConfigPersonalTax extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -686214470533448285L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    Integer level;

    Integer minNum;

    Integer maxNum;

    Integer taxRate;

    Double calculationDeduction;

    /**
     * 税率类型：0-工资；1-劳务报酬
     */
    Integer type;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;

    // 获取真实税率（将整数转换为小数）
    public double getRealRate() {
        return taxRate / 100.0;
    }
}
