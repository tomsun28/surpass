package com.surpass.entity.app;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.mybatis.jpa.annotations.SoftDelete;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.entity.JpaPage;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/17 15:20
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Table(name = "surpass_app_menu")
@Entity
public class AppMenu extends JpaEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1880949592566056023L;

    @Id
    @Column
    @GeneratedValue
    String id;

    /**
     * 菜单名称
     */
    @Column
    String menuName;

    /**
     * 资源路径/路由，前部/开头，后面不加
     */
    @Column
    String requestUrl;

    /**
     * 样式
     */
    @Column
    String resStyle;

    /**
     * 权限值
     */
    @Column
    String permission;

    @Column
    private String appId;

    /**
     * 排序
     */
    @Column
    int sortIndex;

    /**
     * 父级节点id
     */
    @Column
    String parentId;

    /**
     * 状态
     */
    @Column
    String status;

    /**
     * 描述
     */
    @Column
    String description;

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

    private String parentName;
}
