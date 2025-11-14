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
import com.surpass.constants.ConstsRoles;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.mybatis.jpa.annotations.SoftDelete;
import org.dromara.mybatis.jpa.entity.JpaEntity;

/**
 * @author 24096
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Table( name = "surpass_roles")
@Entity
public class Roles extends JpaEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 4660258495864814777L;

    @Id
    @Column
    @GeneratedValue
    private String id;

    @Column(name = "role_code")
    @NotEmpty(message = "用户组编码不能为空", groups = {AddGroup.class, EditGroup.class})
    String roleCode;

    @Column(name = "role_name")
    @NotEmpty(message = "用户组名称不能为空", groups = {AddGroup.class, EditGroup.class})
    String roleName;

    @Column
    String pattern;

    @Column
    String category;

    @Column
    String filters ;

    @Column(name = "org_ids_list")
    String orgIdsList;

    @Column
    int isdefault;

    @Column
    String description;

    @Column
    int status;

    @SoftDelete
    @Column
    String deleted;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_date")
    private String modifiedBy;

    @Column(name = "modified_date")
    private Date modifiedDate;

	private String instName;

	String gradingUserId;


    public Roles(String id) {
        this.id = id;
    }

    /**
     * Groups.
     * @param id String
     * @param groupName String
     * @param isdefault int
     */
    public Roles(String id, String roleName, int isdefault) {
        super();
        this.id = id;
        this.roleName = roleName;
        this.isdefault = isdefault;
    }

    public Roles(String id, String roleCode,String roleName, int isdefault) {
        super();
        this.id = id;
        this.roleCode = roleName;
        this.roleName = roleName;
        this.isdefault = isdefault;
    }

	/**
     * ROLE_ALL_USER must be
     * 		1, dynamic
     * 		2, all orgIdsList
	 *		3, not filters
     */
    public void setDefaultAllUser() {
    	this.pattern = ConstsRoles.Pattern.DYNAMIC;
    	this.orgIdsList ="";
		this.filters ="";
    }
}
