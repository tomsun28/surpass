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



package org.dromara.surpass.persistence.service.impl;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.dromara.surpass.constants.ConstsHttpHeader;
import org.dromara.surpass.constants.ContentType;
import org.dromara.surpass.entity.ExcelImport;
import org.dromara.surpass.entity.idm.UserInfo;
import org.dromara.surpass.persistence.mapper.UserInfoMapper;
import org.dromara.surpass.persistence.service.UserInfoExcelService;
import org.dromara.surpass.persistence.service.UserInfoService;
import org.dromara.surpass.util.DateUtils;
import org.dromara.surpass.util.ExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import com.google.common.collect.Lists;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Repository
public class UserInfoExcelServiceImpl  extends JpaServiceImpl<UserInfoMapper,UserInfo> implements UserInfoExcelService {
	private static final  Logger logger = LoggerFactory.getLogger(UserInfoExcelServiceImpl.class);

	@Autowired
	UserInfoService userInfoService;

    private UserInfo buildFromSheetRow(Row row,UserInfo currentUser) {
		UserInfo userInfo = new UserInfo();
        userInfo.setCreatedDate(new Date());

		//用户标识
		userInfo.setId(ExcelUtils.getValue(row, 0));
		userInfo.setUserType(ExcelUtils.getValue(row, 1));
		userInfo.setUserState(ExcelUtils.getValue(row, 3));
		// 登录账号
		userInfo.setUsername(ExcelUtils.getValue(row, 4));
		// 密码
		userInfo.setPassword(ExcelUtils.getValue(row, 5));
		// 状态
		String status = ExcelUtils.getValue(row, 6);
		userInfo.setStatus(status.equals("") ? 1 : Integer.valueOf(status));

		// 用户显示
		userInfo.setDisplayName(ExcelUtils.getValue(row, 7));



		// 昵称
		userInfo.setNickName(ExcelUtils.getValue(row, 11));
		// 性别
		String gender = ExcelUtils.getValue(row, 12);
		//userInfo.setGender(gender.equals("") ? 1 : Integer.valueOf(gender));
		// 语言偏好
		userInfo.setPreferredLanguage(ExcelUtils.getValue(row, 13));
		// 时区
		userInfo.setTimeZone(ExcelUtils.getValue(row, 14));


		//如果导入存在密码，则进行密码修改
		if (StringUtils.isNotEmpty(userInfo.getPassword())) {
			userInfoService.passwordEncoder(userInfo);
		}
		// 设置租户标识
		userInfo.setInstId(currentUser.getInstId());

        return userInfo;
	}

    public void importFromExcel(ExcelImport excelImportFile, UserInfo currentUser) {
    	if (excelImportFile.isExcelNotEmpty() ) {
            try {
                List<UserInfo> userInfoList = Lists.newArrayList();
                Workbook workbook = excelImportFile.biuldWorkbook();
                int recordCount = 0;
                int sheetSize = workbook.getNumberOfSheets();
                for (int i = 0; i < sheetSize; i++) {
					//遍历sheet页
                    Sheet sheet = workbook.getSheetAt(i);
                    int rowSize = sheet.getLastRowNum() + 1;
                    for (int j = 1; j < rowSize; j++) {
						//遍历行
                        Row row = sheet.getRow(j);
                        if (row == null || j <2) {
							//略过空行和前3行
                            //continue;
                        } else {
							//其他行是数据行
                        	UserInfo userInfo = buildFromSheetRow(row,currentUser);
                            userInfoList.add(userInfo);
                            recordCount ++;
                            logger.debug("record {} user {} account {}",recordCount,userInfo.getDisplayName(),userInfo.getUsername());
                        }
                    }
                }
                // 数据去重
                if(!CollectionUtils.isEmpty(userInfoList)){
                    userInfoList = userInfoList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getUsername()))), ArrayList::new));
                    for (UserInfo userInfo : userInfoList) {
						//如果导入用户标识，则根据用户标识D判断是存在，进行更新和修改
						if(org.apache.commons.lang3.StringUtils.isNotEmpty(userInfo.getId())) {
							UserInfo temp = userInfoService.get(userInfo.getId());
							//如果标识不存在，则进行新增
							if (temp == null){
								userInfoService.saveOneUser(userInfo);
							} else {
								userInfoService.updateOneUser(userInfo);
							}
						} else {
							UserInfo exist = userInfoService.get(userInfo.getId());
							if (exist == null) {
								userInfoService.insert(userInfo);
							} else {
								userInfoService.update(userInfo);
							}
						}
					}
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }finally {
            	excelImportFile.closeWorkbook();
            }
        }
    }

    public void exportToExcel(String exportType,UserInfo userInfo, HttpServletResponse response) {
    	//判断导出模板还是导出用户还是template
		List<UserInfo> users = null;
		if (StringUtils.isNotEmpty(exportType) && "user".equalsIgnoreCase(exportType)) {
			users = userInfoService.findAll();
		}
		Workbook workbook = null;
		try {
			if (users!=null && users.size() > 5000){
				//数据量大导出方案，超过5000条数据采用对象，
				// 比如设置最大内存量为5000行， new SXSSFWookbook(5000)，
				// 当行数达到 5000 时，把内存持久化写到文件中，以此逐步写入，避免OOM。解决了大数据下导出的问题
				workbook = new SXSSFWorkbook(5000);
			} else {
				workbook = new XSSFWorkbook();
			}
			//创建sheet
			Sheet sheet = workbook.createSheet("用户列表");
			Row row = sheet.createRow(0);
			row.createCell(0).setCellValue("Maxkey User Export "+ DateUtils.getCurrentDateTimeAsString());
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,48));
			//构建头
			row = sheet.createRow(1);

			row.createCell(0).setCellValue("用户标识");
			row.createCell(1).setCellValue("用户类型");
			row.createCell(2).setCellValue("用户编号");
			row.createCell(3).setCellValue("用户状态 RESIDENT=在职,WITHDRAWN=离职,INACTIVE=停职留薪,RETIREE=退休");
			row.createCell(4).setCellValue("登录账号");
			row.createCell(5).setCellValue("登录密码");
			row.createCell(6).setCellValue("账号状态 1=活动,2=不活动,3=禁用,4=锁定,5=已删除");
			row.createCell(7).setCellValue("姓名");
			row.createCell(8).setCellValue("姓");
			row.createCell(9).setCellValue("名");

			row.createCell(10).setCellValue("中间名");
			row.createCell(11).setCellValue("昵称");
			row.createCell(12).setCellValue("性别 1=女,2=男");
			row.createCell(13).setCellValue("语言偏好");
			row.createCell(14).setCellValue("时区");
			row.createCell(15).setCellValue("AD域账号");
			row.createCell(16).setCellValue("所属机构");
			row.createCell(17).setCellValue("分支机构");
			row.createCell(20).setCellValue("成本中心");

			row.createCell(21).setCellValue("职位");
			row.createCell(22).setCellValue("级别");
			row.createCell(23).setCellValue("上级经理");
			row.createCell(24).setCellValue("助理");
			row.createCell(25).setCellValue("入职时间");
			row.createCell(26).setCellValue("离职时间");
			row.createCell(27).setCellValue("工作-国家");
			row.createCell(28).setCellValue("工作-省");
			row.createCell(29).setCellValue("工作-城市");
			row.createCell(30).setCellValue("工作-地址");

			row.createCell(31).setCellValue("邮编");
			row.createCell(32).setCellValue("传真");
			row.createCell(33).setCellValue("工作电话");
			row.createCell(34).setCellValue("工作邮件");
			row.createCell(35).setCellValue("证件类型 0=未知,1=身份证,2=护照,3=学生证,4=军人证");
			row.createCell(36).setCellValue("证件号码");
			row.createCell(37).setCellValue("出生日期");
			row.createCell(38).setCellValue("婚姻状态 0=未知,1=单身,2=已婚,3=离异,4=丧偶");
			row.createCell(39).setCellValue("开始工作时间");
			row.createCell(40).setCellValue("个人主页");

			row.createCell(41).setCellValue("即时通讯");
			row.createCell(42).setCellValue("国家");
			row.createCell(43).setCellValue("省");
			row.createCell(44).setCellValue("城市");
			row.createCell(45).setCellValue("家庭地址");
			row.createCell(46).setCellValue("家庭传真");
			row.createCell(47).setCellValue("家庭邮编");
			row.createCell(48).setCellValue("家庭邮箱");
			row.createCell(49).setCellValue("家庭电话");


			//导入数据=================================
			if (users != null ) {
				for (int i =0;i<users.size();i++){
					UserInfo user = users.get(i);
					row = sheet.createRow(i + 2);
					row.createCell(0).setCellValue(user.getId());
					row.createCell(1).setCellValue(user.getUserType());
					row.createCell(3).setCellValue(user.getUserState());
					row.createCell(4).setCellValue(user.getUsername());
					//row.createCell(5).setCellValue(user.getPassword());
					//不导出密码
					row.createCell(5).setCellValue("");
					row.createCell(6).setCellValue(user.getStatus());
					row.createCell(7).setCellValue(user.getDisplayName());


					row.createCell(11).setCellValue(user.getNickName());
					//row.createCell(12).setCellValue(user.getGender());
					row.createCell(13).setCellValue(user.getPreferredLanguage());
					row.createCell(14).setCellValue(user.getTimeZone());

				}
			}
			String fileName = "users";
			fileName = URLEncoder.encode(fileName, "UTF8");
			response.setContentType(ContentType.APPLICATION_MS_EXCEL);
			response.setHeader(ConstsHttpHeader.CONTENT_DISPOSITION, ConstsHttpHeader.ATTACHMENT_FILE.formatted(fileName));
			ServletOutputStream out = response.getOutputStream();
			workbook.write(out);
			out.flush();
			out.close();

		}catch (Exception e){
			logger.error("error:",e);
		} finally {
			if (users != null) {
				users.clear();
			}
			try {
				if(workbook != null) {
					workbook.close();
				}
			} catch (IOException e) {
				logger.error("error:",e);
			}
		}
    }

}
