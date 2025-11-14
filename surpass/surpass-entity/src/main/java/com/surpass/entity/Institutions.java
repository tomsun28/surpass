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
import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.mybatis.jpa.annotations.SoftDelete;
import org.dromara.mybatis.jpa.entity.JpaEntity;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Table(name = "surpass_institutions")
@Entity
public class Institutions extends JpaEntity implements Serializable {
	static final long serialVersionUID = -2375872012431214098L;

	@Id
	@Column
	@GeneratedValue
	private String id;

	@Column
	String fullName;

	@Column
	String instName;

	@Column
	Integer instType;

	@Column
	String division;

	@Column
	String country;

	@Column
	String region;

	@Column
	String locality;

	@Column
	String street;

	@Column
	String address;

	@Column
	String contact;

	@Column
	String postalCode;

	@Column
	String phone;

	@Column
	String fax;

	@Column
	String email;

	@Column
	String description;

	@Column
	String logo;

	@Column
	String backgroundImage;

	@Column
	String domain;

	@Column
	int status;

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

	@Column
	String ownerId;

	@Column
	Integer maxBook;

	String ownerName;
}
