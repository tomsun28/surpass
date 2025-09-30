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

import lombok.Data;
import org.dromara.surpass.web.WebContext;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * 会话属性
 *
 * @author Crystal.Sea
 *
 */

@Data
public class Session implements Serializable{
	private static final long serialVersionUID = 1568480892398646468L;

	public  static final  int    MAX_EXPIRY_DURATION = 60 * 5; //default 5 minutes.

    public static final class  STYLE{
    	public static final  String    WEB 		= "web";
    	public static final  String    MGMT 	= "mgmt";
    	public static final  String    APP	 	= "app";
    	public static final String 	   PLAT 	= "plat";
    }

    /**
     * 会话id
     */
    String id;

    String style = STYLE.WEB;

    LocalDateTime startTimestamp;

    LocalDateTime lastAccessTime;

    LocalDateTime expiredTime;
    /**
     * 认证信息
     */
    Authentication authentication;

    public Session() {
        super();
        this.id = WebContext.genId();
        this.startTimestamp = LocalDateTime.now();
        this.lastAccessTime = LocalDateTime.now();
    }

    public Session(String sessionId) {
        super();
        this.id = sessionId;
        this.startTimestamp = LocalDateTime.now();
        this.lastAccessTime = LocalDateTime.now();
    }

    public Session(String sessionId,Authentication authentication) {
        super();
        this.id = sessionId;
        this.authentication = authentication;
        this.startTimestamp = LocalDateTime.now();
        this.lastAccessTime = LocalDateTime.now();
    }

}
