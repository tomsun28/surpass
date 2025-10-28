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
import com.surpass.entity.BaseSubject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2024/12/19 15:45
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "jbx_standard_subject", autoResultMap = true)
public class StandardSubject extends BaseSubject{
    @Serial
    private static final long serialVersionUID = -4940236669574217392L;

    String standardId;
}
