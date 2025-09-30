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

public final class ConstsUser {

    public static class MARRIAGE {
        /**
         * 未知
         */
        public static final int UNKNOWN             = 0;
        /**
         * 单身
         */
        public static final int SINGLE              = 1;
        /**
         * 结婚
         */
        public static final int MARRIED             = 2;
        /**
         * 离异
         */
        public static final int DIVORCE             = 3;
        /**
         * 丧偶
         */
        public static final int WIDOWED             = 4;

    }

    public static class GENDER {
        /**
         * 未知
         */
        public static final int UNKNOWN             = 0;
        /**
         * 女性
         */
        public static final int FEMALE              = 1;
        /**
         * 男性
         */
        public static final int MALE                = 2;

        public static class EN {
            /**
             * 未知
             */
            public static final String UNKNOWN          = "unknown";
            /**
             * 女性
             */
            public static final String FEMALE           = "female";
            /**
             * 男性
             */
            public static final String MALE             = "male";
        }

        public static class ZH {
            /**
             * 未知
             */
            public static final String UNKNOWN          = "未知";
            /**
             * 女性
             */
            public static final String FEMALE           = "女";
            /**
             * 男性
             */
            public static final String MALE             = "男";
        }
    }



    public static class IDTYPE {
        /**
         * 未知
         */
        public static final int UNKNOWN             = 0;
        /**
         * 身份证
         */
        public static final int IDCARD              = 1;
        /**
         * 护照
         */
        public static final int PASSPORT            = 2;
        /**
         * 学生证
         */
        public static final int STUDENTCARD         = 3;
        /**
         * 军人证
         */
        public static final int MILITARYCARD        = 4;
    }

    public static class EMPLOYMENTSTATUS {
        /**
         * 在册人员
         */
        public static final int ACTIVE              = 1;
        /**
         * 离职人员
         */
        public static final int WITHDRAWN           = 2;
        /**
         * 停薪留职人员
         */
        public static final int INACTIVE            = 3;
        /**
         * 退休人员
         */
        public static final int RETIREE             = 4;

    }

    public static class  EMPLOYEE_TYPE{
    	/**
         * 正常
         */
        public static final String NORMAL              = "NORMAL";

        /**
         * 兼职
         */
        public static final String PARTTIME            = "PARTTIME";

        /**
         * 实习生
         */
        public static final String INTERN              = "INTERN";
        /**
         * 退休返聘
         */
        public static final String RETIREMENT          = "RETIREMENT";

    }

    public static class ATTRS {

        public static final String RANDOMID                 = "randomId";
        /**
         * 用户编码
         */
        public static final String ID                       = "id";
        public static final String USER                     = "user";
        public static final String USERID                   = "userId";
        public static final String USER_ID                  = "user_id";
        public static final String EXTERNAL_ID              = "external_id";
        /**
         * 登录账号
         */
        public static final String USERNAME                 = "username";
        public static final String SUB                      = "sub";
        public static final String NAME                     = "name";
        /**
         * 手机号
         */
        public static final String MOBILE                   = "mobile";
        public static final String PHONE_NUMBER             = "phone_number";
        public static final String PHONE_NUMBER_VERIFIED    = "phone_number_verified";


        /**
         * 邮箱
         */
        public static final String EMAIL                    = "email";
        public static final String EMAIL_VERIFIED           = "email_verified";

        /**
         * PINYIN DisplayName  <br> eg 诸葛亮 zhugl
         */
        public static final String PINYIN_SHORTNAME         = "pinYinShortName";
        /**
         * PINYIN DisplayName <br> eg 诸葛亮 zhugeliang
         */
        public static final String PINYIN_FULLNAME          = "pinYinFullName";

        /**
         * 工号
         */
        public static final String EMPLOYEE_NUMBER          = "employeeNumber";
        /**
         * windows域账号
         */
        public static final String WINDOWS_ACCOUNT          = "windowsAccount";
        /**
         * 身份证号码
         */
        public static final String ID_CARD_NO               = "idCardNo";
        /**
         * 姓名
         */
        public static final String DISPLAYNAME              = "displayName";
        public static final String REALNAME                 = "realname";
        public static final String PREFERRED_USERNAME       = "preferred_username";

        /**
         * 名
         */
        public static final String FIRSTNAME                = "firstName";
        public static final String GIVEN_NAME               = "given_name";

        public static final String MIDDLE_NAME              = "middle_name";

        /**
         * 姓
         */
        public static final String LASTNAME                 = "lastName";
        public static final String FAMILY_NAME              = "family_name";

        public static final String NICKNAME                 = "nickname";


        /**
         * 出生日期
         */
        public static final String BIRTHDAY                 = "birthday";
        public static final String BIRTHDATE                = "birthdate";
        /**
         * 性别
         */
        public static final String GENDER                   = "gender";
        /**
         * 职务
         */
        public static final String TITLE                    = "title";
        /**
         * 所属部门
         */
        public static final String DEPARTMENT               = "department";
        /**
         * 所属部门编码
         */
        public static final String DEPARTMENTID             = "departmentId";
        /**
         * 工作地址
         */
        public static final String WORK_REGION              = "workRegion";
        /**
         * 机构编码
         */
        public static final String INSTID                   = "instId";
        /**
         * 创建日期
         */
        public static final String CREATEDDATE              = "createdDate";

        /**
         * 状态
         */
        public static final String STATE                    = "state";



        public static final String ADDRESS                  = "address";
        /**
         * 国家
         */
        public static final String COUNTRY                  = "country";
        /**
         * 省/州
         */
        public static final String REGION                   = "region";
        /**
         * 城市
         */
        public static final String LOCALITY                 = "locality";
        /**
         * 街道
         */
        public static final String STREET_ADDRESS           = "street_address";
        /**
         * 格式区域
         */
        public static final String FORMATTED                = "formatted";

        public static final String WEBSITE                  = "website";
        /**
         * 邮编
         */
        public static final String POSTAL_CODE              = "postal_code";

        public static final String ZONE_INFO                = "zoneinfo";

        public static final String LOCALE                   = "locale";

        public static final String UPDATED_TIME             = "updated_time";

    }
}
