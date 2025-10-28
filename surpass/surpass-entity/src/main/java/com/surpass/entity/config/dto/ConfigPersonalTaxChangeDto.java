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


package com.surpass.entity.config.dto;

import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/2/7 10:02
 */

@Data
public class ConfigPersonalTaxChangeDto {

    @NotNull(message = "编辑对象不能为空", groups = {EditGroup.class})
    private String id;

    @NotNull(message = "等级不能为空", groups = {AddGroup.class, EditGroup.class})
    Integer level;

    Integer minNum;

    Integer maxNum;

    @NotNull(message = "税率不能为空", groups = {AddGroup.class, EditGroup.class})
    Integer taxRate;

    String bookId;

    Integer type;

    // 获取真实税率（将整数转换为小数）
    public double getRealRate() {
        return taxRate / 100.0;
    }
}
