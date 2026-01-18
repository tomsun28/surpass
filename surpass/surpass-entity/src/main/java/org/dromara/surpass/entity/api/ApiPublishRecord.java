package org.dromara.surpass.entity.api;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.surpass.entity.app.AppResources;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 24096
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "surpass_api_publish")
@Data
public class ApiPublishRecord extends JpaEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 8343598254466057720L;

    @Id
    @Column
    @GeneratedValue
    private String id;

    @NotNull(message = "API ID不能为空")
    @Column(nullable = false)
    private String apiId;

    @NotNull(message = "版本ID不能为空")
    @Column(nullable = false)
    private String versionId;

    @Column(updatable = false)
    private LocalDateTime publishTime;

    @Column(length = 100)
    private String operator;

    @Column(length = 500)
    private String description;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_id", insertable = false, updatable = false)
    private AppResources apiDefinition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "version_id", insertable = false, updatable = false)
    private ApiVersion apiVersion;
}
