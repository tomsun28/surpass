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






package com.surpass.entity.history;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.baomidou.mybatisplus.annotation.TableName;
import com.surpass.entity.client.ClientResolve;
import com.surpass.entity.client.ClientUserAgent;


/**
 * @author Crystal.Sea
 *
 */

@Data
@NoArgsConstructor
@TableName("surpass_history_login")
public class HistoryLogin implements Serializable{
	@Serial
	private static final long serialVersionUID = -1321470643357719383L;

	public static final class CATEGORY{
		public static final Integer LOGIN 	= 1;
		public static final Integer LOGOUT 	= 2;
	}

	@TableId(type = IdType.ASSIGN_ID)
	String id;

	Integer category = CATEGORY.LOGIN;

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

	String deviceId;

	String application;

	Date operateTime;

	@TableField(exist=false)
	private String instName;

	@TableField(exist=false)
	Date startDate;

	@TableField(exist=false)
	Date endDate;

	@TableField(exist=false)
	String gradingUserId;

	public void setClientResolve(ClientResolve clientResolve) {
		this.country = clientResolve.getCountry();
		this.province = clientResolve.getProvince();
		this.city = clientResolve.getCity();
		this.location = clientResolve.getLocation();
	}

	public void setClientUserAgent(ClientUserAgent clientUserAgent) {
		this.browser = clientUserAgent.getName();
		this.platform = clientUserAgent.getPlatform();
	}

}
