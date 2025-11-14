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






package com.surpass.entity.access;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.mybatis.jpa.entity.JpaEntity;


/**
 * @author Crystal.Sea
 *
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Table(name = "surpass_session_list")
@Entity
public class SessionList extends JpaEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = -1321470643357719383L;

	@Id
	@Column
	@GeneratedValue
	private String id;

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
