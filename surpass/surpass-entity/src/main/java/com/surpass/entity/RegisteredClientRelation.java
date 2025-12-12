package com.surpass.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.mybatis.jpa.entity.JpaEntity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/11 10:13
 */

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "surpass_client_app_relation")
@Data
public class RegisteredClientRelation extends JpaEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -5956203862521328378L;

    @Id
    @Column
    @GeneratedValue
    private String id;

    @Column(nullable = false, length = 45)
    private String clientId;

    @Column(nullable = false, length = 45)
    private String appId;

    @Column
    private String createdBy;

    @Column
    private Date createdDate;
}
