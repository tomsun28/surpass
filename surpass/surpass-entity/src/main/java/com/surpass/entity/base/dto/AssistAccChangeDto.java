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


package com.surpass.entity.base.dto;

import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 辅助核算项目对象 jbx_books_voucher_item
 *
 * @author wuyan
 * {@code @date} 2025-02-18
 */

@Data
public class AssistAccChangeDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 所属账套
     */
    @NotBlank(message = "所属账套不能为空", groups = {AddGroup.class, EditGroup.class})
    private String bookId;

    /**
     * 辅助核算类型
     */
    @NotBlank(message = "辅助核算类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private String assistType;

    /**
     * 辅助核算编码
     */
    @NotBlank(message = "辅助核算编码不能为空", groups = {AddGroup.class, EditGroup.class})
    private String assistCode;

    /**
     * 辅助核算名称
     */
    @NotBlank(message = "辅助核算名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String assistName;

    /**
     * 部门
     */
    private String dept;

    /**
     * 规格
     */
    private String spec;

    /**
     * 单位
     */
    private String unit;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否禁用：y/n
     */
    private String status;
}
