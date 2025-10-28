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

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@TableName("surpass_institutions")
public class Institutions extends BaseEntity implements Serializable {
	static final long serialVersionUID = -2375872012431214098L;

	@TableId(type = IdType.ASSIGN_ID)
	String id;

	String fullName;

	String instName;

	Integer instType;

	String division;

	String country;

	String region;

	String locality;

	String street;

	String address;

	String contact;

	String postalCode;

	String phone;

	String fax;

	String email;

	String description;

	String logo;

	String backgroundImage;

	String domain;

	int status;

	@TableField(fill = FieldFill.INSERT)
	@TableLogic(value="n",delval="y")
	String deleted;

	String ownerId;

	Integer maxBook;

	@TableField(exist = false)
	String ownerName;
}
