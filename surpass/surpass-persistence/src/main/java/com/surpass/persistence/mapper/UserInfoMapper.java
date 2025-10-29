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






package com.surpass.persistence.mapper;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.entity.idm.dto.UserInfoPageDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.surpass.constants.ConstsStatus;
import com.surpass.entity.ChangePassword;
import com.surpass.entity.idm.Organizations;
import com.surpass.entity.idm.UserInfo;


/**
 * @author Crystal.Sea
 *
 */
public interface UserInfoMapper  extends BaseMapper<UserInfo>{

	Page<UserInfo> fetchPageResults(Page page, @Param("Dto") UserInfoPageDto dto);

	@Select("select * from  surpass_userinfo where deleted = 'n' and username = #{value} and status = " + ConstsStatus.ACTIVE)
	public UserInfo findByUsername(String username);

	@Select("select * from  surpass_userinfo where deleted = 'n' and ( email = #{value} or mobile= #{value} ) and status = " + ConstsStatus.ACTIVE)
	public UserInfo findByEmailMobile(String emailMobile);

	public List<Organizations> findOrganizationsByUserId(String userId);

	public void updateLocked(UserInfo userInfo);

	public void updateLockout(UserInfo userInfo);

	public int 	changePassword(ChangePassword changePassword);

	public int 	updateEmail(UserInfo userInfo);

	public int 	updateMobile(UserInfo userInfo);

	public int 	updateProfile(UserInfo userInfo);

    @Update("update surpass_userinfo set status =  #{status} where id = #{id}")
   	public int 	updateStatus(UserInfo userInfo) ;
}
