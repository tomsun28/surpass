package com.surpass.entity.api.dto;

import lombok.Data;
import org.dromara.mybatis.jpa.entity.JpaPage;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/16 17:25
 */

@Data
public class ApiPageDto extends JpaPage {
    private String appId;

    private String name;
}
