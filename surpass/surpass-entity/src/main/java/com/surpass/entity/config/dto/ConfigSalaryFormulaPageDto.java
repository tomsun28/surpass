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

import com.surpass.entity.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/2/8 17:48
 */

@Data
@EqualsAndHashCode(callSuper=false)
public class ConfigSalaryFormulaPageDto extends PageQuery {
    /**
	 *
	 */
	private static final long serialVersionUID = -4144200997026123655L;
	String ruleName;
}
