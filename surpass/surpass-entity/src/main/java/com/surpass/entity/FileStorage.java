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
import lombok.NoArgsConstructor;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@NoArgsConstructor
@TableName("JBX_FILE_STORAGE")
public class FileStorage {

    @TableId(type = IdType.ASSIGN_ID)
    String id;

	@JsonIgnore
    byte[] dataStored ;

    @JsonIgnore
    @TableField(exist = false)
    MultipartFile uploadFile;

    String fileName;

    String contentType;

    long contentSize;

    String category;

    @TableField(exist = false)
    String imageBase64;

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
}
