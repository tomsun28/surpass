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






package com.surpass.web.contorller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.Message;
import com.surpass.entity.idm.UserInfo;
import com.surpass.entity.report.dto.DashBoardReqDto;
import com.surpass.entity.report.vo.DashBoardVo;
import com.surpass.persistence.service.ReportService;

/**
 * Index
 * @author Crystal.Sea
 *
 */
@RestController
public class DashboardController {

	private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
	@Autowired
	ReportService reportService;

	@GetMapping(value={"/dashboard"}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<DashBoardVo> dashboard(@CurrentUser UserInfo currentUser) {

		logger.debug("DashboardController /dashboard.");

		DashBoardReqDto dbReqDto = new DashBoardReqDto();
		dbReqDto.setBookId(currentUser.getBookId());

		DashBoardVo dbv = new DashBoardVo();
		dbv.setBookId(currentUser.getBookId());

		dbv.setDayCount(reportService.analysisDay(dbReqDto));
		dbv.setNewUsers(reportService.analysisNewUsers(dbReqDto));

		dbv.setOnlineUsers(reportService.analysisOnlineUsers(dbReqDto));
		dbv.setActiveUsers(reportService.analysisActiveUsers(dbReqDto));

		dbv.setReportMonth(reportService.analysisMonth(dbReqDto));
		dbv.setReportDayHour(reportService.analysisDayHour(dbReqDto));

		dbv.setReportBrowser(reportService.analysisBrowser(dbReqDto));

		dbv.setReportProvince(reportService.analysisProvince(dbReqDto));
		return new Message<>(dbv);
	}
}
