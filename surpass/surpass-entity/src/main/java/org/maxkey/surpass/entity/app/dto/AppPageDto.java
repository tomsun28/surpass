package org.maxkey.surpass.entity.app.dto;

import lombok.Data;
import org.dromara.mybatis.jpa.entity.JpaPage;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/2 9:44
 */

@Data
public class AppPageDto extends JpaPage {
    private String appName;

    private String appCode;
}
