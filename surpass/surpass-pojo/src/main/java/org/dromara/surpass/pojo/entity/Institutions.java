/*
 * Copyright [2025] [JinBooks of copyright http://www.jinbooks.com]
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




package org.dromara.surpass.pojo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.mybatis.jpa.annotations.SoftDelete;
import org.dromara.mybatis.jpa.entity.JpaEntity;



import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Table(name = "surpass_institutions")
@Entity
public class Institutions extends JpaEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 7802508048874788235L;

	@Id
	@Column
	@GeneratedValue
	String id;

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

	@Column
	@SoftDelete
	String deleted;

	@Column
	String createdBy;

	@Column
	Date createdDate;

	@Column
	String modifiedBy;

	@Column
	Date modifiedDate;
}

