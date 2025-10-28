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

import lombok.Data;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/2/14 11:29
 */

@Data
public class CreateSalaryTableDto {

/*    @NotEmpty(message = "所选薪资计算公式不能为空")
    private String formulaId;*/

    private String bookId;
}
