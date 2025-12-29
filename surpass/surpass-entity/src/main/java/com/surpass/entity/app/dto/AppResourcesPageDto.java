package com.surpass.entity.app.dto;

import lombok.Data;
import org.dromara.mybatis.jpa.entity.JpaPage;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/17 16:53
 */

@Data
public class AppResourcesPageDto extends JpaPage {
    private String name;

    private String appId;

    private String classify;

    private String clientId;

    private String roleId;
}
