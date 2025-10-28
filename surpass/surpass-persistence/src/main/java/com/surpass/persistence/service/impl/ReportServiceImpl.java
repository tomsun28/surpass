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
 





package com.surpass.persistence.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.report.dto.DashBoardReqDto;
import com.surpass.entity.report.vo.DashBoardRepVo;
import com.surpass.persistence.mapper.ReportMapper;
import com.surpass.persistence.service.ReportService;

@Repository
public class ReportServiceImpl  extends ServiceImpl<ReportMapper,DashBoardReqDto> implements ReportService {

	@Autowired
	ReportMapper reportMapper;

	public ReportMapper getMapper() {
		return reportMapper;
	}


	public Integer analysisDay(DashBoardReqDto dbReqDto) {
		return getMapper().analysisDay(dbReqDto);
	}
	
	public Integer analysisNewUsers(DashBoardReqDto dbReqDto) {
		return getMapper().analysisNewUsers(dbReqDto);
	}
	
	public Integer analysisOnlineUsers(DashBoardReqDto dbReqDto) {
		return getMapper().analysisOnlineUsers(dbReqDto);
	}
	
	public Integer analysisActiveUsers(DashBoardReqDto dbReqDto) {
		return getMapper().analysisActiveUsers(dbReqDto);
	}
	
	public List<DashBoardRepVo> analysisDayHour(DashBoardReqDto dbReqDto){
		return getMapper().analysisDayHour(dbReqDto);
	}
	
	public List<DashBoardRepVo> analysisMonth(DashBoardReqDto dbReqDto){
		return getMapper().analysisMonth(dbReqDto);
	}
	public List<DashBoardRepVo> analysisProvince(DashBoardReqDto dbReqDto){
		List<DashBoardRepVo> listDbRepVo = getMapper().analysisProvince(dbReqDto);
		
		for(DashBoardRepVo dbRepVo : listDbRepVo) {
			String name = dbRepVo.getReportName();
			if (name.endsWith("省")
					|| name.endsWith("市")
					|| name.endsWith("特别行政区")
					|| name.endsWith("自治区")) {
				name = name.replace("省","")
						.replace("市","")
						.replace("特别行政区","")
						.replace("自治区","");
				dbRepVo.setName(name);
			}
		}
		return listDbRepVo;
	}

	public List<DashBoardRepVo> analysisBrowser(DashBoardReqDto dbReqDto){
		return getMapper().analysisBrowser(dbReqDto);
	}

}
