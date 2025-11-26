package com.surpass.entity.app;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.mybatis.jpa.annotations.SoftDelete;
import org.dromara.mybatis.jpa.entity.JpaEntity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/11/24 15:15
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "surpass_app")
@Data
public class App extends JpaEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1444110511641978713L;

    @Id
    @Column
    @GeneratedValue
    private String id;

    @Column(nullable = false, length = 100)
    private String appName;

    @Column(nullable = false, length = 128)
    private String clientId;

    @Column(nullable = false, length = 512)
    private String clientSecret;

    private Integer status;

    @SoftDelete
    @Column
    String deleted;

    @Column
    private String createdBy;

    @Column
    private Date createdDate;

    @Column
    private String modifiedBy;

    @Column
    private Date modifiedDate;

    // token 过期秒数（可配置）
    private Integer ttlSeconds;
}
