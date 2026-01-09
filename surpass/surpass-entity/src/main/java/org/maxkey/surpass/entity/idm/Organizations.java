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






package org.maxkey.surpass.entity.idm;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.mybatis.jpa.annotations.SoftDelete;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.maxkey.surpass.validate.AddGroup;
import org.maxkey.surpass.validate.EditGroup;

/**
 * root organization node,<br> id = instId or id = parentId or parentId = -1 or parentId = 0
 * @author crystal.sea
 *
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Table(name = "surpass_organizations")
@Entity
public class Organizations extends JpaEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 5085413816404119803L;

    public static final String CLASS_TYPE = "Organization";
    public static final String ROOT_ORG_ID = "1";

    @Id
    @Column
    @GeneratedValue
    private String id;

    @NotEmpty(message = "组织编码不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(max = 50, message = "组织编码的长度不能超过50位", groups = {AddGroup.class, EditGroup.class})
    @Column
    private String orgCode;

    @Column
    @NotEmpty(message = "组织名称不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(max = 16, message = "组织名称的长度不能超过16位", groups = {AddGroup.class, EditGroup.class})
    private String orgName;

    @Column
    @NotEmpty(message = "组织全称不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(max = 32, message = "组织全称的长度不能超过32位", groups = {AddGroup.class, EditGroup.class})
    private String fullName;

    @Column
    private String parentId;

    @Column
    private String parentCode;

    @Column
    private String parentName;

    /**
     * 1. entity
     * 2. virtual
     */
    @Column
    @NotEmpty(message = "类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private String type;

    @Column
    private String codePath;

    @Column
    private String namePath;
    //数据库关键字，解决人大金仓数据库适配修改2023-1-30-shibanglin
//    @Column(name = "surpass_organizations.level")

    @Column
    private int level;

    @Column
    private String hasChild;

    @Column
    private String division;

    @Column
    private String country;

    @Column
    private String region;

    @Column
    private String locality;

    @Column
    private String street;

    @Column
    private String address;

    @Column
    private String contact;

    @Column
    private String postalCode;

    @Column
    private String phone;

    @Column
    private String fax;

    @Column
    private String email;

    @Column
    private long sortIndex;

    @Column
    private String ldapDn;

    @Column
    private String description;

    @Column
    private String extraAttrs;

    @Column
    private int status;

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

	String instName;

    String syncId;

    String syncName;

    String originId;

    String originId2;

    /**
     * 1任职机构，0兼职机构
     */
    int isPrimary = 0;

    boolean reorgNamePath;

    String gradingUserId;

}
