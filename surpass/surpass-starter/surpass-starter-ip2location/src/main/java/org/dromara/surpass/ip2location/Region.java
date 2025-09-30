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






package org.dromara.surpass.ip2location;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.surpass.entity.client.ClientResolve;

/**
 * IP所属区域实体
 *
 * @author Crystal.sea
 *
 */

@Data
@NoArgsConstructor
public class Region {

	String ipAddr;

	/**
	 * 国家
	 */
	String country;

	/**
	 * 省/州
	 */
	String province;

	/**
	 * 城市
	 */
	String city;

	/**
	 * 区域位置
	 */
	String addr;


	public Region(String addr) {
		this.addr = addr;
	}

	public Region(String ipAddr,String country, String province, String city, String addr) {
		super();
		this.ipAddr = ipAddr;
		this.country = country;
		this.province = province;
		this.city = city;
		this.addr = addr;
	}

	public ClientResolve toClientResolve() {
		return  new ClientResolve(this.getIpAddr(),this.getAddr(),this.getCountry(),this.getProvince(),this.getCity());
	}

}
