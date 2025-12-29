package com.surpass.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.mybatis.jpa.annotations.SoftDelete;
import org.dromara.mybatis.jpa.entity.JpaEntity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/9 16:11
 */

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "surpass_client")
@Data
public class RegisteredClient extends JpaEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 6420851079685387078L;

    @Id
    @Column
    @GeneratedValue
    private String id;

    @Column(nullable = false, length = 100)
    private String clientName;

    @Column(nullable = false, length = 128)
    private String clientId;

    @Column(nullable = false, length = 256)
    private String clientSecret;

    @Column(nullable = false)
    private Integer clientType;

    @Column
    private String description;

    @Column
    private String contactName;

    @Column
    private String contactPhone;

    @Column
    private String contactEmail;

    @Column
    private String department;

    @Column
    private String ipWhiteList;

    @Column
    private Date lastLoginTime;

    @Column
    private String lastLoginIp;

    @Column
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
    
    private Set<String> ipAddrSet;
}
