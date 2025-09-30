package org.dromara.surpass.pojo.entity.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.mybatis.jpa.entity.JpaEntity;

import java.io.Serial;
import java.io.Serializable;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/9/30 15:25
 */

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "surpass_config_login_policy")
@Data
@Entity
public class ConfigLoginPolicy extends JpaEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -3540864038292087513L;

    @Id
    @Column
    @GeneratedValue
    String id;

    @NotNull
    @Column
    int sessionValidity;

    @NotNull
    @Column
    int tokenValidity;

    @NotNull
    @Column
    String isFirstPasswordModify;

    @NotNull
    @Column
    String captcha;

    @NotNull
    @Column
    String captchaMgt;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column
    int twoFactor;

    @NotNull
    @Column
    int loginAttempts;

    @NotNull
    @Column
    String isAutoLock;

    @NotNull
    @Column
    int lockInterval;

    @NotNull
    @Column
    int passwordAttempts;

    @NotNull
    @Column
    String passwordAttemptsCaptcha;

    @NotNull
    @Column
    String scanCode;

    @NotNull
    @Column
    String isMobile;

    @NotNull
    @Column
    String isSocial;

    @Column
    String redirectUri;

    @Column
    int terminals;

    public boolean isTwoFactor(){
        return this.twoFactor > 0;
    }

}
