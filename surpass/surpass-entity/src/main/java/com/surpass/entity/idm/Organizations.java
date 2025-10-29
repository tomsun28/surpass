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






package com.surpass.entity.idm;

import java.io.Serial;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import com.surpass.entity.BaseEntity;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * root organization node,<br> id = instId or id = parentId or parentId = -1 or parentId = 0
 * @author crystal.sea
 *
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@TableName("JBX_ORGANIZATIONS")
public class Organizations extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 5085413816404119803L;

    public static final String CLASS_TYPE = "Organization";
    public static final String ROOT_ORG_ID = "1";

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @NotEmpty(message = "组织编码不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(max = 50, message = "组织编码的长度不能超过50位", groups = {AddGroup.class, EditGroup.class})
    private String orgCode;

    @NotEmpty(message = "组织名称不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(max = 16, message = "组织名称的长度不能超过16位", groups = {AddGroup.class, EditGroup.class})
    private String orgName;

    @NotEmpty(message = "组织全称不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(max = 32, message = "组织全称的长度不能超过32位", groups = {AddGroup.class, EditGroup.class})
    private String fullName;

    private String parentId;

    private String parentCode;

    private String parentName;

    /**
     * 1. entity
     * 2. virtual
     */
    @NotEmpty(message = "类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private String type;

    private String codePath;

    private String namePath;
    //数据库关键字，解决人大金仓数据库适配修改2023-1-30-shibanglin
//    @Column(name = "JBX_organizations.level")

    private int level;

    private String hasChild;

    private String division;

    private String country;

    private String region;

    private String locality;

    private String street;

    private String address;

    private String contact;

    private String postalCode;

    private String phone;

    private String fax;

    private String email;

    private long sortIndex;

    private String ldapDn;

    private String description;

    private String extraAttrs;

    private int status;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value="n",delval="y")
    String deleted;

    @TableField(exist = false)
	String instName;

    @TableField(exist = false)
    String syncId;

    @TableField(exist = false)
    String syncName;

    @TableField(exist = false)
    String originId;

    @TableField(exist = false)
    String originId2;

    /**
     * 1任职机构，0兼职机构
     */
    @TableField(exist = false)
    int isPrimary = 0;

    @TableField(exist = false)
    boolean reorgNamePath;

    @TableField(exist = false)
    String gradingUserId;

}
