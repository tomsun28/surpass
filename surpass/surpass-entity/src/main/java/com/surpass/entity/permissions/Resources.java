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

import com.baomidou.mybatisplus.annotation.*;
import com.surpass.entity.BaseEntity;

import lombok.EqualsAndHashCode;
import lombok.Data;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@TableName("JBX_RESOURCES")
public class Resources  extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 2567171742999638608L;

    @TableId(type = IdType.ASSIGN_ID)
    String id;
    /**
     * 资源名称
     */

    String resName;
    /**
     * 资源国际化标识
     */

    String i18n;
    /**
     * 图标
     */

    String icon;


    String iconSelected;
    /**
     * 资源类型MENU , ELEMENT , BUTTON , API , DATA , OTHER
     */

    String classify;
    /**
     * 资源路径/路由，前部/开头，后面不加
     */

    String requestUrl;

    /**
     * 参数
     */

    String params;

    /**
     * 请求方法 OPTIONS,GET,HEAD,POST,PUT,DELETE,TRACE,CONNECT
     */

    String requestMethod;

    /**
     * 资源操作 r-读操作 w-写操作 l-列表操作
     */

    String actionType;

    /**
     * 是否开放API n-否 y-是
     */

    String isOpen;
    /**
     * 否为外链 n-否 y-是
     */

    String isFrame;
    /**
     * 否缓存 n-否 y-是
     */

    String isCache;
    /**
     * 是否可见 n-否 y-是
     */

    String isVisible;
    /**
     * 样式
     */

    String resStyle;
    /**
     * 权限值
     */

    String permission;
    /**
     * 排序
     */

    int sortIndex;

    /**
     * 父级节点id
     */

    String parentId;
    /**
     * 父级节点名称
     */

    String parentName;
    /**
     * 状态
     */

    String status;
    /**
     * 描述
     */

    String description;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value="n",delval="y")
	String deleted;

}
