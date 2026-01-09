package org.maxkey.surpass.enums;

import lombok.Getter;

@Getter
public enum ApiVersionStatus {
    DRAFT(0, "草稿"),
    PENDING(1, "待发布"),
    PUBLISHED(2, "已发布"),
    OFFLINE(3, "下线");

    private final Integer code;
    private final String description;

    ApiVersionStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ApiVersionStatus getByCode(Integer code) {
        for (ApiVersionStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
