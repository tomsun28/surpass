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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 余额方向：借方、贷方
 */

@Getter
public enum SubjectDirectionEnum implements BaseEnum {
    DEBIT("1", "借方"),             // 借方
    CREDIT("2", "贷方");            // 贷方

    private final String value;
    private final String label;

    SubjectDirectionEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public Map<Object, Object> getMap() {
        Map<Object, Object> map = new LinkedHashMap<>();
        for (SubjectDirectionEnum subjectDirectionEnum : SubjectDirectionEnum.values()) {
            map.put(subjectDirectionEnum.value, subjectDirectionEnum.label);
        }
        return map;
    }
}
