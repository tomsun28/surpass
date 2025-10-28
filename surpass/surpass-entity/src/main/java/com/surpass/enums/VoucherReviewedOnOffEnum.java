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

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Map;

/**
 * 凭证审核开关状态
 * 0-关闭;1-开启
 */

@Getter
public enum VoucherReviewedOnOffEnum implements BaseEnum {
    ON(1, "开启"),
    OFF(0, "关闭");

    private final Integer code;
    private final String name;

    VoucherReviewedOnOffEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Map<Object, Object> getMap() {
        Map<Object, Object> map = Maps.newHashMap();
        for (VoucherReviewedOnOffEnum status : VoucherReviewedOnOffEnum.values()) {
            map.put(status.getCode(), status.getName());
        }
        return map;
    }
}
