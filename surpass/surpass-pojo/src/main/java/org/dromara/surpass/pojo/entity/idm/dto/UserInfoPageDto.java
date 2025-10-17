package org.dromara.surpass.pojo.entity.idm.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.mybatis.jpa.entity.JpaPage;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/10/17 9:51
 */

@Data
@EqualsAndHashCode(callSuper=false)
public class UserInfoPageDto extends JpaPage {
    String username;

    String employeeNumber;

    String mobile;

    String email;

    String userType;

    String displayName;
}
