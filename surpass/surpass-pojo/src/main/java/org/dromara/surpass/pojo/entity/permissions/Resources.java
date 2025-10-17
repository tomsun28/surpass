package org.dromara.surpass.pojo.entity.permissions;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.mybatis.jpa.annotations.SoftDelete;
import org.dromara.mybatis.jpa.entity.JpaEntity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/10/11 9:52
 */

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Table(name = "surpass_resources")
@Entity
public class Resources extends JpaEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 7916823739460751480L;

    @Id
    @Column
    @GeneratedValue
    String id;

    @Column
    String resName;

    /**
     * 资源国际化标识
     */
    @Column
    String i18n;

    /**
     * 图标
     */
    @Column
    String icon;

    @Column
    String iconSelected;

    /**
     * 资源类型MENU , ELEMENT , BUTTON , API , DATA , OTHER
     */
    @Column
    String classify;

    /**
     * 资源路径/路由，前部/开头，后面不加
     */
    @Column
    String requestUrl;

    /**
     * 参数
     */
    @Column
    String params;

    /**
     * 请求方法 OPTIONS,GET,HEAD,POST,PUT,DELETE,TRACE,CONNECT
     */
    @Column
    String requestMethod;

    /**
     * 资源操作 r-读操作 w-写操作 l-列表操作
     */
    @Column
    String actionType;

    /**
     * 是否开放API n-否 y-是
     */
    @Column
    String isOpen;

    /**
     * 否为外链 n-否 y-是
     */
    @Column
    String isFrame;

    /**
     * 否缓存 n-否 y-是
     */
    @Column
    String isCache;

    /**
     * 是否可见 n-否 y-是
     */
    @Column
    String isVisible;

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
     * 父级节点名称
     */
    @Column
    String parentName;

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

    @Column
    @SoftDelete
    String deleted;

    @Column
    String createdBy;

    @Column
    Date createdDate;

    @Column
    String modifiedBy;

    @Column
    Date modifiedDate;
}
