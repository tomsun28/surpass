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


package com.surpass.entity.standard.dto;

import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2024/12/27 17:11
 */

@Data
public class StandardChangeDto {

    @NotEmpty(message = "编辑对象不能为空", groups = {EditGroup.class})
    String id;

    @NotEmpty(message = "会计准则不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(max = 21, message = "会计准则的长度不能超过21位", groups = {AddGroup.class, EditGroup.class})
    String name;

    @NotNull(message = "状态不能为null", groups = {AddGroup.class, EditGroup.class})
    Integer status;
}
