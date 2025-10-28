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


package com.surpass.entity.voucher.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.surpass.validate.EditGroup;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 凭证记录编辑对象
 *
 * @author wuyan
 * {@code @date} 2025-01-14
 */

@Data
public class VoucherTemplateItemChangeDto {
    /**
     * 主键
     */
    @NotNull(message = "编辑对象不能为空", groups = {EditGroup.class})
    private String id;

    /**
     * 关联编码
     */
    String relatedId;
    /**
     * 模板名称
     */
    String templateId;

    /**
     * 摘要
     */
    @NotNull(message = "编辑摘要不能为空", groups = {EditGroup.class})
    String summary;

    /**
     * 科目编码
     */
    @NotNull(message = "编辑科目不能为空", groups = {EditGroup.class})
    String subjectCode;

    /**
     * 余额方向 1 借 2 贷
     */
    @NotNull(message = "编辑借贷方向不能为空", groups = {EditGroup.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Integer direction;
}
