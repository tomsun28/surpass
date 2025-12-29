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
import com.surpass.entity.report.vo.DashboardStatisticsVo;
import com.surpass.entity.report.vo.ApiAccessTrendVo;
import com.surpass.entity.report.vo.RegionAccessVo;
import com.surpass.entity.report.vo.ApiTopVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Dashboard Controller
 * @author Crystal.Sea
 *
 */
@RestController
@RequestMapping("/statistics")
@Tag(name = "Dashboard Statistics API", description = "仪表板统计相关接口")
public class DashboardController {

	private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
	private static final Random random = new Random();
	private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

	@Operation(summary = "获取仪表板统计数据", description = "获取系统仪表板完整统计数据")
	@ApiResponse(responseCode = "200", description = "成功返回仪表板统计数据",
			content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = Message.class)))
	@GetMapping(value = "/dashboard", produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<DashboardStatisticsVo> getStatisticsDashboard(@CurrentUser UserInfo currentUser) {
		logger.debug("DashboardController /statistics/dashboard.");

		DashboardStatisticsVo data = new DashboardStatisticsVo();
		
		// 基础统计
		data.setAppCount(156);
		data.setApiCount(265);
		data.setRoleCount(42);
		data.setUserCount(2345);
		data.setClientCount(89);
		data.setDatasourceCount(23);
		
		// API访问统计
		data.setTodayApiCalls(123456);
		data.setWeekApiCalls(856789);
		data.setMonthApiCalls(3456789);
		data.setApiSuccessRate(98.7);
		
		// 趋势数据
		data.setApiAccessTrend(generateMockApiAccessTrend());
		data.setRegionAccessData(generateMockRegionAccessData());
		data.setApiTopList(generateMockApiTopData());

		return new Message<>(data);
	}

	@Operation(summary = "获取API访问趋势数据", description = "获取指定时间范围内的API访问趋势数据")
	@ApiResponse(responseCode = "200", description = "成功返回API访问趋势数据",
			content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = Message.class)))
	@GetMapping(value = "/api-access-trend", produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<List<ApiAccessTrendVo>> getApiAccessTrend(
			@Parameter(description = "开始时间") @RequestParam(required = false) String startTime,
			@Parameter(description = "结束时间") @RequestParam(required = false) String endTime,
			@Parameter(description = "时间类型") @RequestParam(required = false) String type) {
		logger.debug("DashboardController /statistics/api-access-trend.");

		List<ApiAccessTrendVo> trendData = generateMockApiAccessTrend();
		return new Message<>(trendData);
	}

	@Operation(summary = "获取区域访问数据", description = "获取指定时间范围内的区域访问统计数据")
	@ApiResponse(responseCode = "200", description = "成功返回区域访问数据",
			content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = Message.class)))
	@GetMapping(value = "/region-access", produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<List<RegionAccessVo>> getRegionAccessData(
			@Parameter(description = "开始时间") @RequestParam(required = false) String startTime,
			@Parameter(description = "结束时间") @RequestParam(required = false) String endTime,
			@Parameter(description = "时间类型") @RequestParam(required = false) String type) {
		logger.debug("DashboardController /statistics/region-access.");

		List<RegionAccessVo> regionData = generateMockRegionAccessData();
		return new Message<>(regionData);
	}

	@Operation(summary = "获取热门API列表", description = "获取指定时间范围内的热门API调用统计")
	@ApiResponse(responseCode = "200", description = "成功返回热门API数据",
			content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = Message.class)))
	@GetMapping(value = "/api-top", produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<List<ApiTopVo>> getApiTopList(
			@Parameter(description = "开始时间") @RequestParam(required = false) String startTime,
			@Parameter(description = "结束时间") @RequestParam(required = false) String endTime,
			@Parameter(description = "时间类型") @RequestParam(required = false) String type) {
		logger.debug("DashboardController /statistics/api-top.");

		List<ApiTopVo> topData = generateMockApiTopData();
		return new Message<>(topData);
	}

	@Operation(summary = "获取应用数量", description = "获取系统应用总数")
	@ApiResponse(responseCode = "200", description = "成功返回应用数量",
			content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = Message.class)))
	@GetMapping(value = "/app-count", produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Integer> getAppCount() {
		logger.debug("DashboardController /statistics/app-count.");
		return new Message<>(156);
	}

	@Operation(summary = "获取角色数量", description = "获取系统角色总数")
	@ApiResponse(responseCode = "200", description = "成功返回角色数量",
			content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = Message.class)))
	@GetMapping(value = "/role-count", produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Integer> getRoleCount() {
		logger.debug("DashboardController /statistics/role-count.");
		return new Message<>(42);
	}

	@Operation(summary = "获取用户数量", description = "获取系统用户总数")
	@ApiResponse(responseCode = "200", description = "成功返回用户数量",
			content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = Message.class)))
	@GetMapping(value = "/user-count", produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Integer> getUserCount() {
		logger.debug("DashboardController /statistics/user-count.");
		return new Message<>(2345);
	}

	@Operation(summary = "获取客户端数量", description = "获取系统客户端总数")
	@ApiResponse(responseCode = "200", description = "成功返回客户端数量",
			content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = Message.class)))
	@GetMapping(value = "/client-count", produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Integer> getClientCount() {
		logger.debug("DashboardController /statistics/client-count.");
		return new Message<>(89);
	}

	@Operation(summary = "获取数据源数量", description = "获取系统数据源总数")
	@ApiResponse(responseCode = "200", description = "成功返回数据源数量",
			content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(implementation = Message.class)))
	@GetMapping(value = "/datasource-count", produces = {MediaType.APPLICATION_JSON_VALUE})
	public Message<Integer> getDatasourceCount() {
		logger.debug("DashboardController /statistics/datasource-count.");
		return new Message<>(23);
	}

	// 模拟数据生成方法
	private List<ApiAccessTrendVo> generateMockApiAccessTrend() {
		List<ApiAccessTrendVo> trendData = new ArrayList<>();
		LocalDateTime now = LocalDateTime.now();
		
		for (int i = 0; i < 24; i++) {
			LocalDateTime time = now.withHour(i).withMinute(0).withSecond(0);
			
			int count = random.nextInt(1000) + 500;
			double successRate = random.nextDouble() * 0.2 + 0.8;
			int success = (int) (count * successRate);
			int failed = count - success;
			
			ApiAccessTrendVo trend = new ApiAccessTrendVo();
			trend.setDate(time.format(timeFormatter));
			trend.setCount(count);
			trend.setSuccess(success);
			trend.setFailed(failed);
			trend.setAvgResponseTime(random.nextInt(200) + 50);
			
			trendData.add(trend);
		}
		
		return trendData;
	}

	private List<RegionAccessVo> generateMockRegionAccessData() {
		String[] provinces = {
			"北京", "上海", "广东", "江苏", "浙江", "山东", "河南", "四川",
			"湖北", "湖南", "福建", "安徽", "河北", "陕西", "辽宁", "江西"
		};
		
		List<RegionAccessVo> regionData = new ArrayList<>();
		
		for (String province : provinces) {
			int count = random.nextInt(10000) + 100;
			int users = (int) (count * (random.nextDouble() * 0.5 + 0.1));
			double successRate = random.nextDouble() * 10 + 90;
			
			RegionAccessVo region = new RegionAccessVo();
			region.setProvince(province);
			region.setCount(count);
			region.setUsers(users);
			region.setSuccessRate(successRate);
			
			regionData.add(region);
		}
		
		regionData.sort((a, b) -> Integer.compare(b.getCount(), a.getCount()));
		return regionData;
	}

	private List<ApiTopVo> generateMockApiTopData() {
		String[] apiNames = {
			"用户登录认证",
			"获取用户信息",
			"查询权限列表",
			"创建新应用",
			"更新角色权限",
			"数据源连接测试",
			"API访问日志",
			"系统配置获取",
			"文件上传接口",
			"消息推送服务"
		};
		
		String[] paths = {
			"/api/auth/login",
			"/api/user/profile",
			"/api/permission/list",
			"/api/app/create",
			"/api/role/update",
			"/api/datasource/test",
			"/api/log/access",
			"/api/config/system",
			"/api/file/upload",
			"/api/message/push"
		};
		
		List<ApiTopVo> topData = new ArrayList<>();
		
		for (int i = 0; i < apiNames.length; i++) {
			int count = random.nextInt(10000) + 1000;
			int avgResponseTime = random.nextInt(400) + 50;
			double successRate = random.nextDouble() * 10 + 90;
			
			ApiTopVo api = new ApiTopVo();
			api.setApiName(apiNames[i]);
			api.setPath(paths[i]);
			api.setCount(count);
			api.setAvgResponseTime(avgResponseTime);
			api.setSuccessRate(successRate);
			
			topData.add(api);
		}
		
		topData.sort((a, b) -> Integer.compare(b.getCount(), a.getCount()));
		return topData;
	}
}
