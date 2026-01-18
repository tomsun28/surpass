package org.dromara.surpass.entity;

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
 * @time: 2025/12/29 14:52
 */

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "surpass_permission")
@Data
public class RoleAppResourcesPermission extends JpaEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -787619059528286968L;

    @Id
    @Column
    @GeneratedValue
    private String id;

    @Column(nullable = false, length = 45)
    private String roleId;

    @Column(nullable = false, length = 45)
    private String resourceId;

    @Column(nullable = false, length = 45)
    private String appId;
    
    @Column
    int category = 2;

    @Column
    private String createdBy;

    @Column
    private Date createdDate;
}
