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






package org.maxkey.surpass.constants;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * ROLES.
 * @author Crystal.Sea
 *
 */
public final class ConstsRoles {

	public static final SimpleGrantedAuthority ROLE_SUPERVISOR 			= new SimpleGrantedAuthority("ROLE_SUPERVISOR");

    public static final SimpleGrantedAuthority ROLE_ADMINISTRATOR 		= new SimpleGrantedAuthority("ROLE_ADMINISTRATOR");

    public static final SimpleGrantedAuthority ROLE_MANAGER 			= new SimpleGrantedAuthority("ROLE_MANAGER");

    public static final SimpleGrantedAuthority ROLE_USER 				= new SimpleGrantedAuthority("ROLE_USER");

    public static final SimpleGrantedAuthority ROLE_ALL_USER 			= new SimpleGrantedAuthority("ROLE_ALL_USER");
    /**
     * 普通组/角色
     */
    public static final SimpleGrantedAuthority ROLE_GENERAL_USER 		= new SimpleGrantedAuthority("ROLE_GENERAL_USER");

    public static final List<GrantedAuthority> grantedAdminAuthoritys 	= new ArrayList<>();

    /**
     * 模型
     */
    public class Pattern{
    	/**
    	 * 动态模型
    	 */
    	public static final String DYNAMIC = "dynamic";
    	/**
    	 * 静态模型
    	 */
    	public static final String STATIC  = "static";

    }

    /**
     * 类型
     */
    public class Category{
    	/**
    	 * 超级管理员
    	 */
    	public static final String SUPERVISOR 		= "supervisor";
    	/**
    	 * 管理员
    	 */
    	public static final String ADMINISTRATOR 	= "administrator";
    	/**
    	 * 授权管理员-分级管理员
    	 */
    	public static final String MANAGER  		= "manager";
    	/**
    	 * 普通
    	 */
    	public static final String GENERAL  		= "general";

    }


    /**
     * 管理员角色
     */
    static {
    	//超级管理员
    	grantedAdminAuthoritys.add(ConstsRoles.ROLE_SUPERVISOR);
    	//管理员
    	grantedAdminAuthoritys.add(ConstsRoles.ROLE_ADMINISTRATOR);
        //授权管理员
    	grantedAdminAuthoritys.add(ConstsRoles.ROLE_MANAGER);
    }
}
