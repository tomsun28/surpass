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






package org.dromara.surpass.constants;

public class ConstsLoginType {

	/**
	 * 本地登录
	 */
    public static final String NORMAL 			= "normal";

    /**
	 * 手机验证码登录
	 */
	public static final String MOBILE 			= "Mobile";

    public static final String JWT 				= "Jwt";

    public static final String CAS 				= "CAS";

    /**
     * 第三方登录
     */
    public static final String SOCIALSIGNON 	= "Social Sign On";

    public static final class TwoFactor{
    	/**
    	 * 2=邮箱验证码
    	 */
    	public static final String TWO_FACTOR_EMAIL 	= "TwoFactorEmail";
    	/**
    	 * 3=手机短信
    	 */
    	public static final String TWO_FACTOR_MOBILE 	= "TwoFactorMobile";
    }
}
