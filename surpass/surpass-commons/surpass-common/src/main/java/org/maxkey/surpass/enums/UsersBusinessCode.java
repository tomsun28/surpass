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




package org.maxkey.surpass.enums;

import lombok.Getter;

@Getter
public enum UsersBusinessCode {

    USER_FORBIDDEN(500008, "账号被禁用"),

    USERNAME_USED(500009, "该登录名称已被使用"),

    MOBILE_USED(500010, "该手机号码已被使用"),

    EMAIL_USED(500011, "该邮箱地址已被使用"),

    USER_VERIFY_MOBILE_ABSENT(500005, "该手机号尚未绑定任何用户");

    final String msg;
    final Integer code;

    UsersBusinessCode(Integer code, String msg) {
        this.msg = msg;
        this.code = code;
    }

}
