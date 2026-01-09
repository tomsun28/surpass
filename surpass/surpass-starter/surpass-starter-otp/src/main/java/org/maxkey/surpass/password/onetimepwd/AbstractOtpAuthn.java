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






package org.maxkey.surpass.password.onetimepwd;

import org.maxkey.surpass.entity.idm.UserInfo;
import org.maxkey.surpass.password.onetimepwd.token.AbstractOtpTokenStore;
import org.maxkey.surpass.password.onetimepwd.token.InMemoryOtpTokenStore;
import org.maxkey.surpass.util.StringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AbstractOTPAuthn.
 * @author Administrator
 *
 */
public abstract class AbstractOtpAuthn {
    private static final  Logger logger = LoggerFactory.getLogger(AbstractOtpAuthn.class);

    protected AbstractOtpTokenStore optTokenStore = new InMemoryOtpTokenStore();

    //验证码有效間隔
    protected int interval = 30;

    // 验证码长度，范围4～10，默认为6
    protected int digits = 6;

    protected String crypto = "HmacSHA1";

    protected String defaultEncoding ="utf-8";

    StringGenerator stringGenerator;

    protected String otpType = OtpTypes.TIMEBASED_OTP;

    public static final class OtpTypes {
        // 手机
        public static final String  MOBILE 			= "MOBILE";
        // 短信
        public static final  String SMS 				= "SMS";
        // 邮箱
        public static final  String EMAIL 				= "EMAIL";
        //TIMEBASED_OPT
        public static final  String TIMEBASED_OTP 		= "TOPT";
        // HmacOTP
        public static final  String HOTP_OTP 			= "HOTP";

        public static final  String RSA_OTP 			= "RSA";

        public static final  String CAP_OTP 			= "CAP";

        public static final  String MQ_OTP 				= "MQ";

    }

    public  static final class OtpMsgTypes {
    	/**
    	 * login
    	 */
    	 public static final  String  LOGIN 			= "LOGIN";
    	 /**
    	  * register
    	  */
    	 public static final  String  REGISTER  		= "REGISTER";
    	 /**
    	  * Forgot Password
    	  */
    	 public static final  String  FORGOT			= "FORGOT";
    }

    public abstract boolean produce(UserInfo userInfo,String otpMsgType);

    public abstract boolean validate(UserInfo userInfo, String token);

    protected String defaultProduce(UserInfo userInfo) {
        return genToken(userInfo);
    }

    /**
     * genToken.
     * @param userInfo UserInfo
     * @return
     */
    public String genToken(UserInfo userInfo) {
        if (stringGenerator == null) {
            stringGenerator = new StringGenerator(StringGenerator.DEFAULT_CODE_NUMBER, digits);
        }
        String token = stringGenerator.randomGenerate();
        logger.debug("Generator token {}" , token);
        return token;
    }

    /**
     *  the interval.
     * @return the interval
     */
    public int getInterval() {
        return interval;
    }

    /**
     * interval the interval to set.
     * @param interval the interval to set
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }

    /**
     * digits.
     * @return the digits
     */
    public int getDigits() {
        return digits;
    }

    /**
     * digits the digits to set.
     * @param digits the digits to set
     */
    public void setDigits(int digits) {
        this.digits = digits;
    }

    /**
     * crypto.
     * @return the crypto
     */
    public String getCrypto() {
        return crypto;
    }

    /**
     * crypto the crypto to set.
     * @param crypto the crypto to set
     */
    public void setCrypto(String crypto) {
        this.crypto = crypto;
    }

    public String getOtpType() {
        return otpType;
    }

    public void setOtpType(String optType) {
        this.otpType = optType;
    }

    public void setOptTokenStore(AbstractOtpTokenStore optTokenStore) {
        this.optTokenStore = optTokenStore;
    }

    public void initPropertys() {

    }

    public String getDefaultEncoding() {
        return defaultEncoding;
    }

    public void setDefaultEncoding(String defaultEncoding) {
        this.defaultEncoding = defaultEncoding;
    }

}
