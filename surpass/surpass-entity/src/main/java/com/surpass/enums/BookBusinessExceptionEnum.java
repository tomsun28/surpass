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


package com.surpass.enums;

import lombok.Getter;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2024/12/23 17:18
 */
@Getter
public enum BookBusinessExceptionEnum {

    ILLEGAL_MOVE_ORG(500001, "非法的移动操作"),

    DUPLICATE_SUBJECTSCODE_EXIST(500002, "当前会计准则已存在相同的科目编码，请重新输入"),

    SUB_SUBJECTS_EXISTS(500003, "请先移除/移动当前会计科目下的子科目"),

    SUB_SUBJECTS_ACTIVE(500004, "请先禁用当前会计科目下的活跃子科目"),

    PARENT_ORGS_FORBIDDEN(500005, "请先启用当前子科目的父级会计科目"),

    DUPLICATE_DEEP_LIMIT(500006, "超出最大科目深度10级"),

    DUPLICATE_SUBJECTS_EXIST(500007, "当前会计准则已存在相同的科目名称，请重新输入"),

    DUPLICATE_SETNAME_EXIST(500008, "当前系统已存在相同的账套名称，请重新输入"),

    ALREADY_STANDARD_USED(500009, "当前会计科目已被会计制度使用无法被禁用"),

    ALREADY_SET_USED(500010, "当前会计科目已被账套使用无法被禁用"),

    DISABLE_BEFORE_DELETE(500011, "请先禁用当前数据再进行删除操作"),

    DELETE_HAS_VOUCHER(500012, "当前科目已被使用，不可删除");

    final String msg;

    final Integer code;

    BookBusinessExceptionEnum(Integer code, String msg) {
        this.msg = msg;
        this.code = code;
    }

}
