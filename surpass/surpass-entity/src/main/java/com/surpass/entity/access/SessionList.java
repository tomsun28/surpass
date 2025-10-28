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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.baomidou.mybatisplus.annotation.TableName;


/**
 * @author Crystal.Sea
 *
 */

@Data
@NoArgsConstructor
@TableName("JBX_SESSION_LIST")
public class SessionList implements Serializable{
	@Serial
	private static final long serialVersionUID = -1321470643357719383L;

	@TableId(type = IdType.ASSIGN_ID)
	String id;

	String sessionId;

	String style;

	String userId;

	String username;

	String displayName;

	String loginType;

	String message;

	String code;

	String provider;

	String ipAddr;

	String country;

	String province;

	String city;

	String location;

	String browser;

	String platform;

	String application;

	Date operateTime;

	@TableField(exist = false)
	private String instName;

	@TableField(exist = false)
	String startDate;

	@TableField(exist = false)
	String endDate;

	@TableField(exist = false)
	String gradingUserId;

}
