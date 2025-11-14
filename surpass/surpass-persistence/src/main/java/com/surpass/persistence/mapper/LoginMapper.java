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






/**
 *
 */
package com.surpass.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.surpass.entity.idm.UserInfo;
import org.dromara.mybatis.jpa.IJpaMapper;

/**
 * @author Crystal.sea
 *
 */
@Mapper
public  interface LoginMapper extends IJpaMapper<UserInfo> {

	@Select("select * from  surpass_userinfo where  id = #{id} and  deleted = 'n' ")
	public UserInfo findById(@Param ("id") String id ) ;

	@Select("select * from  surpass_userinfo where deleted = 'n' and (username = #{username} or mobile = #{username})")
	public UserInfo findByUsername(@Param ("username") String username ) ;

	@Update("""
			update surpass_userinfo set  
	            bad_password_count    = 0 , 
	            login_failed_count    = 0 ,  
	            login_count          = login_count + 1, 
	            last_login_time       = #{lastLoginTime} , 
	            last_login_ip         = #{lastLoginIp} , 
	            is_online            = 1
			where id = #{id}  
        """)
	public void updateLastLogin(UserInfo userInfo);


	@Update("""
            update surpass_userinfo 
			    set is_locked 	= #{isLocked} , 
			    un_lock_time 		= #{unLockTime} 
			where id = #{id}
        """)
	public void updateLockUser(UserInfo userInfo);

	@Update("""
            update surpass_userinfo 
			set bad_password_count = 0 , 
				login_failed_count = 0 ,
				is_locked 		 = #{isLocked} , 
			    un_lock_time 		 = #{unLockTime} 
			where id = #{id}
        """)
	public void updateUnLockUser(UserInfo userInfo);


	@Update("""
           update surpass_userinfo set 
                bad_password_count = bad_password_count + 1 , 
                bad_password_time  = #{badPasswordTime} ,
                login_failed_count = login_failed_count + 1 , 
                last_login_time  = #{loginFailedTime}
           where id = #{id}
        """)
	public void updateBadPasswordCount(UserInfo userInfo);

	@Update("""
	      update surpass_userinfo set 
				login_failed_count = login_failed_count + 1 , 
				last_login_time  = #{loginFailedTime}
		  where id = #{id}
	        """)
	public void updateLoginFailedCount(UserInfo userInfo);

	@Update("""
		  update surpass_userinfo set 
			  	bad_password_count = 0 , 
				bad_password_time = #{badPasswordTime} ,
                login_failed_count = 0 , 
                last_login_time = #{loginFailedTime}
		  where id = #{id}
	        """)
	public void updateLoginFailedCountRest(UserInfo userInfo);

}
