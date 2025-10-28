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


package com.surpass.entity.hr.dto;

import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.YearMonth;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/2/28 11:12
 */

@Data
public class SalarySummaryChangeDto {

    @NotNull(message = "编辑对象不能为空", groups = {EditGroup.class})
    private String id;

//    @NotEmpty(message = "名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String label;

    private String description;

    private String bookId;

    @NotNull(message = "所属月份不能为空", groups = {AddGroup.class, EditGroup.class})
    private YearMonth belongDate;

    String[] belongDateRange;

    String startDateRange;

    String endDateRange;
}
