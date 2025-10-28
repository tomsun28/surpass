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


package com.surpass.entity;

import lombok.*;

/**
 * 简介说明: 科目辅助项目对象
 *
 * @author wuyan
 * {@code @date} 2025/03/21 15:20:26
 * {@code @version} 1.0
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubjectAuxiliary {
    /**
     * ID
     */
    private String id;

    /**
     * 名称
     */
    private String label;

    /**
     * 辅助项目类型
     */
    private String value;

    /**
     * 是否必填
     */
    private Boolean must;
}
