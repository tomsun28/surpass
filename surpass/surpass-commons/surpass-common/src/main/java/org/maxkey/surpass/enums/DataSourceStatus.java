package org.maxkey.surpass.enums;

import lombok.Getter;

@Getter
public enum DataSourceStatus {
    DISABLED(0, "禁用"),
    ENABLED(1, "启用");

    private final Integer code;
    private final String description;

    DataSourceStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static DataSourceStatus getByCode(Integer code) {
        for (DataSourceStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
