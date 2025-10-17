package org.dromara.surpass.pojo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.surpass.pojo.entity.idm.UserInfo;
import org.dromara.surpass.validate.EditGroup;

import java.io.Serializable;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/10/16 16:40
 */

@Data
@NoArgsConstructor
public class ChangePassword implements Serializable {

    /**
     *
     */
    static final long serialVersionUID = 655065036584798162L;
    String id;
    String userId;
    String username;
    String email;
    String mobile;
    String windowsAccount;
    String employeeNumber;
    String displayName;
    String oldPassword;

    @NotEmpty(message = "新密码不能为空", groups = {EditGroup.class})
    String password;

    @NotEmpty(message = "确认密码不能为空", groups = {EditGroup.class})
    String confirmPassword;

    String decipherable;
    String bookId;
    int passwordSetType;
    String passwordLastSetTime;

    @Schema(name = "secretKey", description = "密钥编码")
    String secretKey;

    public ChangePassword(String username,String password) {
        this.username = username;
        this.password = password;
    }

    public ChangePassword(UserInfo userInfo) {
        this.setId(userInfo.getId());
        this.setUserId(userInfo.getId());
        this.setUsername(userInfo.getUsername());
        this.setMobile(userInfo.getMobile());
        this.setEmail(userInfo.getEmail());
        this.setDecipherable(userInfo.getDecipherable());
        this.setPassword(userInfo.getPassword());
    }

    public void clearPassword() {
        this.password ="";
        this.decipherable = "";
    }
}
