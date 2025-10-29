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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 *
 * @author Crystal.Sea
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@TableName("JBX_SOCIALS_ASSOCIATE")
public class SocialsAssociate extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = -207734216183303782L;

	@TableId(type = IdType.ASSIGN_ID)
	String id;

	String provider;
	String providerName;
	String icon;
	String userId;

	String username;

	String socialUserId;

	String socialUserInfo;
	String accessToken;
	String exAttribute;
}
