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






package org.dromara.surpass.entity;

import java.io.Serializable;

import org.dromara.surpass.entity.idm.UserInfo;
import org.dromara.surpass.validate.EditGroup;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
public class ChangePassword implements Serializable {

	/**
	 *
	 */
	static final long serialVersionUID = 655065036584798162L;
	String id;
	String userId;
	String username;
	String email;
	String mobile;
	String windowsAccount;
	String employeeNumber;
	String displayName;
	String oldPassword;

	@NotEmpty(message = "新密码不能为空", groups = {EditGroup.class})
	String password;

	@NotEmpty(message = "确认密码不能为空", groups = {EditGroup.class})
	String confirmPassword;

	String decipherable;

	int passwordSetType;
	String passwordLastSetTime;

	@Schema(name = "secretKey", description = "密钥编码")
    String secretKey;

	public ChangePassword(String username,String password) {
		this.username = username;
		this.password = password;
	}

	public ChangePassword(UserInfo userInfo) {
		this.setId(userInfo.getId());
		this.setUserId(userInfo.getId());
		this.setUsername(userInfo.getUsername());
		this.setMobile(userInfo.getMobile());
		this.setEmail(userInfo.getEmail());
		this.setDecipherable(userInfo.getDecipherable());
		this.setPassword(userInfo.getPassword());
	}

	public void clearPassword() {
		this.password ="";
		this.decipherable = "";
	}
}
