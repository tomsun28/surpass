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


package com.surpass.persistence.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.surpass.entity.report.dto.DashBoardReqDto;
import com.surpass.entity.report.vo.DashBoardRepVo;

import java.util.List;

public interface ReportService  extends IService<DashBoardReqDto> {
	public Integer analysisDay(DashBoardReqDto dbReqDto) ;

	public Integer analysisNewUsers(DashBoardReqDto dbReqDto) ;

	public Integer analysisOnlineUsers(DashBoardReqDto dbReqDto) ;

	public Integer analysisActiveUsers(DashBoardReqDto dbReqDto) ;

	public List<DashBoardRepVo> analysisDayHour(DashBoardReqDto dbReqDto);

	public List<DashBoardRepVo> analysisMonth(DashBoardReqDto dbReqDto);

	public List<DashBoardRepVo> analysisProvince(DashBoardReqDto dbReqDto);

	public List<DashBoardRepVo> analysisBrowser(DashBoardReqDto dbReqDto);

}
