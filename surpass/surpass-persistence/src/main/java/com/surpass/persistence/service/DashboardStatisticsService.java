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

import com.surpass.entity.report.vo.DashboardStatisticsVo;
import com.surpass.entity.report.vo.ApiAccessTrendVo;
import com.surpass.entity.report.vo.RegionAccessVo;
import com.surpass.entity.report.vo.ApiTopVo;

import java.util.List;

public interface DashboardStatisticsService {
    
    DashboardStatisticsVo getDashboardStatistics();
    
    int getAppCount();
    
    int getApiCount();
    
    int getRoleCount();
    
    int getUserCount();
    
    int getClientCount();
    
    int getDatasourceCount();
    
    int getTodayApiCalls();
    
    int getWeekApiCalls();
    
    int getMonthApiCalls();
    
    double getApiSuccessRate();
    
    List<ApiAccessTrendVo> getApiAccessTrend(String startTime, String endTime, String type);
    
    List<RegionAccessVo> getRegionAccessData(String startTime, String endTime, String type);
    
    List<ApiTopVo> getApiTopList(String startTime, String endTime, String type);
}