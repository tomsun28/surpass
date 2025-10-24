package org.dromara.surpass.pojo.entity.config;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.mybatis.jpa.entity.JpaEntity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/10/22 10:37
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Table(name = "surpass_config_email_senders")
@Entity
public class ConfigEmailSenders extends JpaEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 5624527187337540387L;

    @Id
    @Column
    @GeneratedValue
    String id;

    @Column
    private String account;

    @Column
    private String credentials;

    @Column
    private String smtpHost;

    @Column
    private Integer port;

    @Column
    private int sslSwitch;

    @Column
    private String sender;

    @Column
    private String encoding;

    @Column
    private String protocol;

    @Column
    private int status;

    @Column
    private String description;

    @Column
    String createdBy;

    @Column
    Date createdDate;

    @Column
    String modifiedBy;

    @Column
    Date modifiedDate;
}
