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
import com.surpass.entity.Message;
import com.surpass.entity.idm.UserInfo;
import com.surpass.entity.report.vo.DashBoardVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Index
 * @author Crystal.Sea
 *
 */
@RestController
@Tag(name = "Dashboard API", description = "仪表板相关接口")
public class DashboardController {

	private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

	@Operation(summary = "获取仪表板数据", description = "获取系统仪表板统计数据，包括用户数量、在线用户、活跃用户以及各类报表数据")
	@ApiResponse(responseCode = "200", description = "成功返回仪表板数据",
			content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = Message.class)))
	@GetMapping(value={"/dashboard"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<DashBoardVo> dashboard(@CurrentUser UserInfo currentUser) {

		logger.debug("DashboardController /dashboard.");

		DashBoardVo dbv = new DashBoardVo();

		return new Message<>(dbv);
	}
}
