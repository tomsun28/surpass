package com.surpass.entity.app.dto;

import lombok.Data;
import org.dromara.mybatis.jpa.entity.JpaPage;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/9 17:51
 */

@Data
public class AppClientPageDto extends JpaPage {
    private String clientName;

    private String clientId;

    private Integer clientType;
}
