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

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2024/12/23 10:53
 */

@Data
public class SubjectChangeDto {

    @NotEmpty(message = "编辑对象不能为空", groups = {EditGroup.class})
    String id;

    @NotNull(message = "科目类型不能为空", groups = {AddGroup.class, EditGroup.class})
    Integer category;

    @NotEmpty(message = "科目编码不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(min = 2, max = 25, message = "组织编码的长度为2~25位", groups = {AddGroup.class, EditGroup.class})
    @Pattern(regexp = "[\\d\\-.]+", message = "科目编码只能包含数字和符号（如 - 和 .）", groups = {AddGroup.class, EditGroup.class})
    String code;

    @NotEmpty(message = "科目名称不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(max = 21, message = "科目名称的长度不能超过21位", groups = {AddGroup.class, EditGroup.class})
    String name;

    String pinyinCode;

    @NotNull(message = "余额方向不能为空", groups = {AddGroup.class, EditGroup.class})
    String direction;

    @NotNull(message = "是否为现金类科目不能为空", groups = {AddGroup.class, EditGroup.class})
    Integer isCash;

    /**
     * 使用范围
     */
    String scope;
    /**
     * 分类
     */
    String classify;

    @NotNull(message = "状态不能为空", groups = {AddGroup.class, EditGroup.class})
    Integer status;

    String parentId;

    /**
     * 辅助核算
     */
    String auxiliary;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    String standardId;

    String bookId;
}
