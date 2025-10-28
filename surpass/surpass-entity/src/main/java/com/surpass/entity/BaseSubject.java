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


package com.surpass.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/1/17 16:38
 */

@Data
public class BaseSubject implements Serializable {
    @Serial
    private static final long serialVersionUID = -8364438812300012091L;

    @TableId(type = IdType.ASSIGN_ID)
    String id;

    /**
     * 种类
     */
    Integer category;

    /**
     * 科目编码
     */
    String code;

    /**
     * 科目名称
     */
    String name;

    /**
     * 科目全称
     */
    String displayName;

    /**
     * 拼音编码
     */
    String pinyinCode;
    /**
     * 拼音全称
     */
    String pinyinDisplayCode;
    /**
     * 借贷方向
     */
    String direction;

    Integer status;

    String parentId;

    String idPath;

    Integer level;

    Integer systemDefault;

    String unit;
    /**
     * 辅助核算
     */
    String auxiliary;
    /**
     * 币种
     */
    String currency;
    /**
     * 使用范围
     */
    String scope;
    /**
     * 分类
     */
    String classify;

    /**
     * 是否为现金类科目
     */
    Integer isCash;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value="n",delval="y")
    String deleted;

    @TableField(exist = false)
    String parentCode;

    @TableField(exist = false)
    String parentName;

    @TableField(exist = false)
    BigDecimal balance;

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(name = "createdBy",description = "创建人")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    protected String createdBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Schema(name = "createdDate",description = "创建时间")
    protected Date createdDate;

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(name = "modifiedBy",description = "更新人")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    protected String modifiedBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Schema(name = "modifiedDate",description = "更新时间")
    protected Date modifiedDate;
}
