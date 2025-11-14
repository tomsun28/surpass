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

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.mybatis.jpa.entity.JpaEntity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * @author 24096
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Table(name = "surpass_role_member")
@Entity
public class RoleMember extends JpaEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = -8059639972590554760L;

	@Id
	@Column
	@GeneratedValue
	String id;

	@Column
	String roleId;

	String roleName;

	@Column
	String memberId;

	String memberName;

	// USER or POST
	@Column
	String type;

	String instName;
	// for user
	String username;

	String displayName;

	String jobTitle;

	int gender;
	// for post
	String postCode;

	String postName;

	// department
	String departmentId;

	String department;

	String gradingUserId;

	/**
	 * 创建者
	 */
	@Column
	protected String createdBy;

	/**
	 * 创建时间
	 */
	@Column
	protected Date createdDate;

	/**
	 * @param roleId
	 * @param memberId
	 * @param type
	 */
	public RoleMember(String roleId, String memberId, String type) {
		super();
		this.roleId = roleId;
		this.memberId = memberId;
		this.type = type;
	}

	public RoleMember(String roleId, String memberId, String memberName, String type, String createdBy) {
		super();
		this.roleId = roleId;
		this.memberId = memberId;
		this.memberName = memberName;
		this.type = type;
		this.createdBy = createdBy;
	}

}
