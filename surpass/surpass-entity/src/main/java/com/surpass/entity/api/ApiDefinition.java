package com.surpass.entity.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.mybatis.jpa.annotations.SoftDelete;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 24096
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "surpass_api")
@Data
public class ApiDefinition extends JpaEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1444110511641978713L;

    @Id
    @Column
    @GeneratedValue
    private String id;

    @NotBlank(message = "API名称不能为空")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "API路径不能为空")
    @Column(nullable = false, length = 200)
    private String path;

    @NotBlank(message = "HTTP方法不能为空")
    @Column(nullable = false, length = 10)
    private String method;

    @Column(length = 500)
    private String description;

    @NotNull(message = "数据源ID不能为空")
    @Column(nullable = false)
    private String datasourceId;

    @Column
    private String appId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false)
    private DataSource dataSource;

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
