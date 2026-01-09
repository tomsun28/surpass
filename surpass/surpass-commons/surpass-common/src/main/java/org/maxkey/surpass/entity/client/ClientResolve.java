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




package org.maxkey.surpass.entity.client;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClientResolve implements Serializable{
	private static final long serialVersionUID = 3557014779495463632L;

	/**
	 * 登录IP地址
	 */
	String ipAddr;

	/**
	 * 登录IP地址归属
	 */
	String location;

	/**
	 * 登录IP地址归属国家
	 */
	String country;

	/**
	 * 登录IP地址归属省、州
	 */
	String province;

	/**
	 * 登录IP地址归属城市
	 */
	String city;

	/**
	 * 浏览器
	 */
	String browser;

	/**
	 * 操作系统平台
	 */
	String platform;

	/**
	 * userAgent Hash
	 */
	String userAgentHash;

	public ClientResolve(ClientUserAgent clientUserAgent) {
		super();
		this.browser = clientUserAgent.getName();
		this.platform = clientUserAgent.getPlatform();
		this.userAgentHash = clientUserAgent.getUserAgentHash();
	}

	public ClientResolve(String ipAddr, String location, String country, String province, String city) {
		super();
		this.ipAddr = ipAddr;
		this.location = location;
		this.country = country;
		this.province = province;
		this.city = city;
	}
}
