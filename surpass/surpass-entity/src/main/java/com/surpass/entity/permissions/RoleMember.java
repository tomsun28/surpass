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

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * @author 24096
 */
@Data
@NoArgsConstructor
@TableName("surpass_role_member")
public class RoleMember implements Serializable {

	@Serial
	private static final long serialVersionUID = -8059639972590554760L;

	@TableId(type = IdType.ASSIGN_ID)
	String id;

	String roleId;

	@TableField(exist = false)
	String roleName;

	String memberId;

	@TableField(exist = false)
	String memberName;

	// USER or POST
	String type;

	@TableField(exist = false)
	String instName;
	// for user
	@TableField(exist = false)
	String username;

	@TableField(exist = false)
	String displayName;

	@TableField(exist = false)
	String jobTitle;

	@TableField(exist = false)
	int gender;
	// for post
	@TableField(exist = false)
	String postCode;

	@TableField(exist = false)
	String postName;

	// department
	@TableField(exist = false)
	String departmentId;

	@TableField(exist = false)
	String department;

	@TableField(exist = false)
	String gradingUserId;

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
	 * @param groupId
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
