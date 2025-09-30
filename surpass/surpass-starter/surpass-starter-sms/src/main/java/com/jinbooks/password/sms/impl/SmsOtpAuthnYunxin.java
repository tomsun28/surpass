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
 





package com.jinbooks.password.sms.impl;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Date;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import com.jinbooks.password.sms.SmsOtpAuthn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jinbooks.entity.idm.UserInfo;
import com.jinbooks.util.JsonUtils;
import com.jinbooks.util.StringGenerator;

/**
 * 网易云信短信验证.
 * @author shimingxy
 *
 */
public class SmsOtpAuthnYunxin extends SmsOtpAuthn {
    private static final  Logger logger = LoggerFactory.getLogger(SmsOtpAuthnYunxin.class);

	//发�?�验证码的请求路径URL
    private static final String SERVER_URL = "https://api.netease.im/sms/sendcode.action";
    //网易云信分配的账号，请替换你在管理后台应用下申请的Appkey
    private   String appKey = "94395d754eb55693043f5d6a2b772ef3";
    //网易云信分配的密钥，请替换你在管理后台应用下申请的appSecret
    private  String appSecret = "05d5485357bc";
    // 短信模板ID
    private  String templateId = "14860099";

    public SmsOtpAuthnYunxin() {
        otpType = OtpTypes.SMS;
    }

    public SmsOtpAuthnYunxin(String appKey, String appSecret, String templateId) {
    	otpType = OtpTypes.SMS;
		this.appKey = appKey;
		this.appSecret = appSecret;
		this.templateId = templateId;
	}

    @Override
    public boolean produce(UserInfo userInfo,String otpMsgType) {
        HttpPost httpPost = null;
        // 手机�?
        String mobile = userInfo.getMobile();
        if (mobile != null && !mobile.equals("")) {
            try {
                httpPost = new HttpPost(SERVER_URL);
                String curTime = String.valueOf((new Date()).getTime() / 1000L);
                /*
                 * 参�?�计算CheckSum的java代码，在上述文档的参数列表中，有CheckSum的计算文档示�?
                 */
                // 随机�?
                String nonce = new StringGenerator(
                        StringGenerator.DEFAULT_CODE_NUMBER,
                        6
                    ).randomGenerate();
                String checkSum = SmsOtpAuthnYunxinCheckSumBuilder
                        .getCheckSum(appSecret, nonce, curTime);
                logger.debug("AppKey {} , Nonce {} , CurTime {} , checkSum {}",appKey,nonce,curTime,checkSum);
                // 设置请求的header
                httpPost.addHeader("AppKey", appKey);
                httpPost.addHeader("Nonce", nonce);
                httpPost.addHeader("CurTime", curTime);
                httpPost.addHeader("CheckSum", checkSum);
                httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

                // 设置请求的的参数，requestBody参数
                //List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                /*
                 * 1.如果是模板短信，请注意参数mobile是有s的，详细参数配置请参考�?�发送模板短信文档�??
                 * 2.参数格式是jsonArray的格式，例如 "['13888888888','13666666666']"
                 * 3.params是根据你模板里面有几个参数，那里面的参数也是jsonArray格式
                 */
                //https://api.netease.im/sms/sendcode.action
                //authCode 用户自定义验证码
                //nvps.add(new BasicNameValuePair("authCode", ""));
                //https://api.netease.im/sms/verifycode.action
                //nvps.add(new BasicNameValuePair("code", "123456"));

                httpPost.setEntity(
                		new UrlEncodedFormEntity(
                				Arrays.asList(
                						new BasicNameValuePair("templateid", templateId),
                						new BasicNameValuePair("mobile", userInfo.getMobile()),
                						new BasicNameValuePair("codeLen", digits + ""))));
                CloseableHttpClient httpClient = HttpClients.createDefault();
                // 执行请求
                CloseableHttpResponse response = httpClient.execute(httpPost);
                /*
                 * 1.打印执行结果，打印结果一般会200�?315�?403�?404�?413�?414�?500
                 * 2.具体的code有问题的可以参�?�官网的Code状�?�表
                 * {"code":200,"msg":"1","obj":"740673"}
                 */
                String responseString = EntityUtils.toString(response.getEntity(), "utf-8");
                logger.debug("responseString {}" , responseString);
                YunxinSms  yunxinSms = JsonUtils.stringToObject(responseString,YunxinSms.class);
                logger.debug("responseEntity code {}" , yunxinSms.getObj());
                nonce = yunxinSms.getObj() == null ?nonce:yunxinSms.getObj();
                logger.debug("nonce {}" , nonce);
                this.optTokenStore.store(
                                        userInfo,
                                        nonce,
                                        userInfo.getMobile(),
                                        OtpTypes.SMS);
                return true;
            } catch  (Exception e) {
                logger.error(" produce code error ", e);
            } finally {
                if (httpPost != null) {
                    httpPost.reset();
                }
            }
        }
        return false;
    }

    @Override
    public boolean validate(UserInfo userInfo, String token) {
        return this.optTokenStore.validate(userInfo, token, OtpTypes.SMS, interval);
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public class YunxinSms {
        int code;
        String msg;
        String obj;

        public YunxinSms() {
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getObj() {
            return obj;
        }

        public void setObj(String obj) {
            this.obj = obj;
        }
    }

}

class SmsOtpAuthnYunxinCheckSumBuilder {
    // 计算并获取CheckSum
    public static String getCheckSum(String appSecret, String nonce, String curTime) {
        return encode("sha1", appSecret + nonce + curTime);
    }

    // 计算并获取md5�?
    public static String getMD5(String requestBody) {
        return encode("md5", requestBody);
    }

    private static String encode(String algorithm, String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest
                    = MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }
    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
}
