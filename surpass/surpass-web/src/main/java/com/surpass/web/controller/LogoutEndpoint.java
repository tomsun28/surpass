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






package com.surpass.web.controller;

import com.surpass.authn.annotation.CurrentUser;
import com.surpass.authn.session.SessionManager;
import com.surpass.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.surpass.entity.idm.UserInfo;

/**
 * 前端注销
 *
 * @author Crystal.Sea
 *
 */
@RestController
public class LogoutEndpoint {
	private static final Logger logger = LoggerFactory.getLogger(LogoutEndpoint.class);

	@Autowired
	SessionManager sessionManager;

	/**
	 * 前端会话注销,直接踢出会话
	 * @param currentUser
	 * @return
	 */
 	@GetMapping(value={"/logout"}, produces = {MediaType.APPLICATION_JSON_VALUE})
 	public Message<String> logout(@CurrentUser UserInfo currentUser){
 		logger.debug("session {} user {}({}) logout",currentUser.getSessionId(),currentUser.getUsername(),currentUser.getId());
 		sessionManager.terminate(
 				currentUser.getSessionId(),
 				currentUser.getId(),
 				currentUser.getUsername());
 		return new Message<>();
 	}

}
