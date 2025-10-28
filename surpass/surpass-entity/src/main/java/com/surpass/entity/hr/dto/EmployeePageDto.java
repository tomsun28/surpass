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

import com.surpass.entity.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 员工信息分页查询对象
 *
 * @author wuyan
 * {@code @date} 2025-01-22
 */

@Data
@EqualsAndHashCode(callSuper=false)
public class EmployeePageDto extends PageQuery {
    /**
	 *
	 */
	private static final long serialVersionUID = -90886914726064826L;

	/**
     * 姓名
     */
    private String displayName;

    /**
     * 工号
     */
    private String employeeNumber;

    /**
     * 部门ID
     */
    private String departmentId;

    /**
     * 当前账套ID
     */
    private String bookId;
}
