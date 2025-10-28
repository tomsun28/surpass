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


package com.surpass.entity.book.dto;

import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.YearMonth;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/1/2 9:42
 */

@Data
public class BookChangeDto {
    @NotEmpty(message = "编辑对象不能为空", groups = {EditGroup.class})
    String id;

    @NotEmpty(message = "账套名称不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(max = 21, message = "账套名称的长度不能超过21位", groups = {AddGroup.class, EditGroup.class})
    String name;

    @NotEmpty(message = "单位名称不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(max = 85, message = "单位名称的长度不能超过85位", groups = {AddGroup.class, EditGroup.class})
    String companyName;

    @NotNull(message = "建账期间不能为空", groups = {AddGroup.class, EditGroup.class})
    YearMonth enableDate;

    @NotEmpty(message = "会计制度不能为空", groups = {AddGroup.class, EditGroup.class})
    String standardId;

    @NotNull(message = "纳税性质不能为空", groups = {AddGroup.class, EditGroup.class})
    Integer vatType;

    String address;

    Integer industry;

    String creditCode;

    @NotNull(message = "凭证是否需要审核不能为空", groups = {AddGroup.class, EditGroup.class})
    Integer voucherReviewed;

    @NotNull(message = "状态不能为空", groups = {AddGroup.class, EditGroup.class})
    Integer status;

    String instId;
}
