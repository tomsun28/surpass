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






package org.dromara.surpass.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邮件配置
 */
@Data
@NoArgsConstructor
public class EmailConfig {

	/**
	 * 账号
	 */

    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 地址
     */
    private String smtpHost;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * ssl
     */
    private boolean ssl;

    /**
     * 发送人
     */
    private String sender;



    public EmailConfig(String username, String password, String smtpHost, Integer port, boolean ssl, String sender) {
		super();
		this.username = username;
		this.password = password;
		this.smtpHost = smtpHost;
		this.port = port;
		this.ssl = ssl;
		this.sender = sender;
	}

}
