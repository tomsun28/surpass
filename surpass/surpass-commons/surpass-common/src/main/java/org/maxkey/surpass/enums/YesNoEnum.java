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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Yes、No枚举类
 *
 * @author wuyan
 */
@Getter
public enum YesNoEnum implements BaseEnum {
    /**
     * 是
     */
    y("是"),

    /**
     * 否
     */
    n("否");

    private final String label;

    YesNoEnum(String label) {
        this.label = label;
    }

    @Override
    public Map<Object, Object> getMap() {
        Map<Object, Object> map = new LinkedHashMap<>();
        for (YesNoEnum yn : YesNoEnum.values()) {
            map.put(yn.name(), yn.getLabel());
        }
        return map;
    }
}
