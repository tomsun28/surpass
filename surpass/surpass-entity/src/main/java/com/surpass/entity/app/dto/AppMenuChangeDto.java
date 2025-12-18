package com.surpass.entity.app.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.dromara.mybatis.jpa.annotations.SoftDelete;

import java.util.Date;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/12/17 16:54
 */

@Data
public class AppMenuChangeDto {

    String id;

    /**
     * 菜单名称
     */
    String menuName;

    /**
     * 资源路径/路由，前部/开头，后面不加
     */
    String requestUrl;

    /**
     * 样式
     */
    String resStyle;

    /**
     * 权限值
     */
    String permission;

    private String appId;

    /**
     * 排序
     */
    @Column
    int sortIndex;

    /**
     * 父级节点id
     */
    String parentId;

    /**
     * 状态
     */
    String status;

    /**
     * 描述
     */
    String description;
}
