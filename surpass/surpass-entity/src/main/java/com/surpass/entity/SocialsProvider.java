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

/**
 * @author Crystal.Sea
 *
 */

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@TableName("JBX_SOCIALS_PROVIDER")
public class SocialsProvider extends BaseEntity implements Serializable {
    static final long serialVersionUID = 1636727203025187769L;

	@TableId(type = IdType.ASSIGN_ID)
    String id;

	String provider;

	String providerName;

	String icon;

	String clientId;

	String clientSecret;

    String agentId;

    String display;

    long sortIndex;

    String scanCode;

    int status;

	String bookId;

	String redirectUri;

	String accountId;
	String bindTime;
	String unBindTime;
	String lastLoginTime;
	String state;

	boolean userBind;

	@TableField(fill = FieldFill.INSERT)
	@TableLogic(value="n",delval="y")
	String deleted;

	public SocialsProvider(SocialsProvider copy) {
		this.clientId = copy.getClientId();
		this.id = copy.getId();
		this.provider = copy.getProvider();
		this.providerName = copy.getProviderName();
		this.agentId = copy.getAgentId();
		this.icon = copy.getIcon();
		this.scanCode = copy.getScanCode();
	}
}
