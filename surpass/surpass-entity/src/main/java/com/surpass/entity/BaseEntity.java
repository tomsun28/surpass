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

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.surpass.util.DateUtils;
import com.surpass.util.excel.ExcelExportCfg;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public BaseEntity(){

    }
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelExportCfg(dateFormat = DateUtils.FORMAT_DATE_YYYY_MM_DD_HH_MM_SS)
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
    @ExcelExportCfg(dateFormat = DateUtils.FORMAT_DATE_YYYY_MM_DD_HH_MM_SS)
    @Schema(name = "modifiedDate",description = "更新时间")
    protected Date modifiedDate;

}
