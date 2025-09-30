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




package org.dromara.surpass.authn.session;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SessionDto {

	//@Schema(name = "id", description = "会话id")
	String  id;
	//@Schema(name = "cacheKey", description = "cacheKey")
	String cacheKey;
	//@Schema(name = "userId", description = "用户编码")
    String userId;

    //@Schema(name = "username", description = "登录账号")
    String username;

    //@Schema(name = "displayName", description = "显示名称")
	String displayName;

    //@Schema(name = "ipAddress", description = "登录IP地址")
    String ipAddress;

    //@Schema(name = "ipLocation", description = "登录IP地址归属")
    String ipLocation;

    //@Schema(name = "browser", description = "浏览器")
    String browser;

    //@Schema(name = "platform", description = "操作系统平台")
    String platform;

    //@Schema(name = "startTimestamp", description = "登录时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime startTimestamp;

}
