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
 * @time: 2025/10/22 10:26
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "surpass_config_sms_provider")
public class ConfigSmsProvider extends JpaEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 4038523827927341391L;

    @Id
    @Column
    @GeneratedValue
    String id;

    @Column
    String provider;

    @Column
    String message;

    @Column
    String appKey;

    @Column
    String appSecret;

    @Column
    String templateId;

    @Column
    String signName;

    @Column
    String smsSdkAppId;

    @Column
    String description;

    @Column
    int status;

    @Column
    private String bookId;

    @Column
    String createdBy;

    @Column
    Date createdDate;

    @Column
    String modifiedBy;

    @Column
    Date modifiedDate;
}
