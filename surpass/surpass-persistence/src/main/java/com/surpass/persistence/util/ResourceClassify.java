package com.surpass.persistence.util;

import com.surpass.exception.BusinessException;

import java.util.Arrays;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/18 17:32
 */
public enum ResourceClassify {

    OPEN_API("openApi"),
    BUTTON("button"),
    MENU("menu"),
    API("api");

    private final String code;

    ResourceClassify(String code) {
        this.code = code;
    }

    public static ResourceClassify from(String code) {
        return Arrays.stream(values())
                .filter(e -> e.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new BusinessException(50001, "非法的资源类型"));
    }
}
