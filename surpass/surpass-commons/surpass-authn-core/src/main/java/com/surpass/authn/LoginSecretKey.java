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




package com.surpass.authn;

import java.io.Serializable;
import java.time.Duration;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Instant;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginSecretKey  implements  Serializable {
	private static final long serialVersionUID = 3425230885254989952L;

	String secretKey;

	String publicKey ;

	String privateKey;

	Date createTime;

    Date expires;

	public LoginSecretKey(String secretKey,String publicKey, String privateKey) {
		super();
		this.secretKey = secretKey;
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.expires = new DateTime().plus(Duration.ofHours(24).toMillis()).toDate();
		this.createTime = Instant.now().toDate();
	}

	public LoginSecretKey(String secretKey,String publicKey, String privateKey,int expiresHours) {
		super();
		this.secretKey = secretKey;
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.expires = new DateTime().plus(Duration.ofHours(expiresHours).toMillis()).toDate();
		this.createTime = Instant.now().toDate();
	}

}
