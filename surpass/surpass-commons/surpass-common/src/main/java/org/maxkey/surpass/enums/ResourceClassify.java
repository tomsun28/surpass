package org.maxkey.surpass.enums;

import java.util.Arrays;

import org.maxkey.surpass.exception.ServiceException;

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

    public String getCode() {
        return code;
    }

    public static ResourceClassify from(String code) {
        return Arrays.stream(values())
                .filter(e -> e.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new ServiceException("非法的资源类型",50001));
    }
}
