package com.surpass.entity.api;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.mybatis.jpa.annotations.SoftDelete;
import org.dromara.mybatis.jpa.entity.JpaEntity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author 24096
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "surpass_datasource")
@Data
public class DataSource extends JpaEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -3630932571014858686L;

    @Id
    @Column
    @GeneratedValue
    private String id;

    @NotBlank(message = "数据源名称不能为空")
    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @NotBlank(message = "驱动类名不能为空")
    @Column(nullable = false, length = 200)
    private String driverClassName;

    @NotBlank(message = "数据库连接URL不能为空")
    @Column(nullable = false, length = 500)
    private String url;

    @NotBlank(message = "用户名不能为空")
    @Column(nullable = false, length = 100)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Column(nullable = false, length = 500)
    private String password;

    @NotNull(message = "状态不能为空")
    @Column(nullable = false)
    private Integer status = 1;

    @Column(length = 200)
    private String testSql = "SELECT 1";

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

}
