/*
 * Copyright [2025] [Surpass of copyright http://www.surpass.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */



package com.surpass.entity.permissions;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.mybatis.jpa.annotations.SoftDelete;
import org.dromara.mybatis.jpa.entity.JpaEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Table(name = "surpass_resources")
@Entity
public class Resources extends JpaEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 2567171742999638608L;

    @Id
    @Column
    @GeneratedValue
    String id;
    /**
     * 资源名称
     */

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
