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






package org.dromara.surpass.pojo.entity.access;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.mybatis.jpa.entity.JpaEntity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * @author Crystal.Sea
 *
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "SURPASS_SESSION_LIST")
public class SessionList extends JpaEntity implements Serializable{

	@Serial
	private static final long serialVersionUID = 8508473095603136057L;

	@Id
	@Column
	@GeneratedValue
	String id;

	@Column
	String sessionId;

	@Column
	String style;

	@Column
	String userId;

	@Column
	String username;

	@Column
	String displayName;

	@Column
	String loginType;

	@Column
	String message;

	@Column
	String code;

	@Column
	String provider;

	@Column
	String ipAddr;

	@Column
	String country;

	@Column
	String province;

	@Column
	String city;

	@Column
	String location;

	@Column
	String browser;

	@Column
	String platform;

	@Column
	String application;

	@Column
	Date operateTime;

	private String instName;

	String startDate;

	String endDate;

	String gradingUserId;

}
