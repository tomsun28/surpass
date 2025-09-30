/*
 * Copyright [2025] [JinBooks of copyright http://www.jinbooks.com]
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


package org.dromara.surpass.enums;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 记账凭证状态
 */
@Getter
public enum VoucherStatusEnum implements BaseEnum {
    DRAFT("draft", "暂存"),
    UNDER_REVIEW("reviewing", "审核中"),
    COMPLETED("completed", "已完成"),
    REJECTED("rejected", "被拒绝"),
    CANCELLED("cancelled", "已取消");

    private final String value;
    private final String label;

    VoucherStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static VoucherStatusEnum get(String value) {
        for (VoucherStatusEnum status : VoucherStatusEnum.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public Map<Object, Object> getMap() {
        Map<Object, Object> map = new LinkedHashMap<>();
        for (VoucherStatusEnum status : VoucherStatusEnum.values()) {
            map.put(status.getValue(), status.getLabel());
        }
        return map;
    }
}
