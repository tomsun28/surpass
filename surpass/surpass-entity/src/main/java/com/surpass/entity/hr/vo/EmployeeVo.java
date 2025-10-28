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


package com.surpass.entity.hr.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

import com.surpass.entity.voucher.Voucher;

/**
 * 员工信息视图对象
 *
 * @author wuyan
 * {@code @date} 2025-01-22
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class EmployeeVo extends Voucher {
    @Serial
    private static final long serialVersionUID = 1L;
}
