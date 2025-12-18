package com.surpass.entity.app.dto;

import lombok.Data;
import org.dromara.mybatis.jpa.entity.JpaPage;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/17 16:53
 */

@Data
public class AppMenuPageDto extends JpaPage {
    private String menuName;

    private String appId;
}
