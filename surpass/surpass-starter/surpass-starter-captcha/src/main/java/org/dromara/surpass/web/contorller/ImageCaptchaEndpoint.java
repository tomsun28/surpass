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


package org.dromara.surpass.web.contorller;

import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.dromara.surpass.authn.jwt.service.AuthTokenService;
import org.dromara.surpass.crypto.Base64Utils;
import org.dromara.surpass.entity.Message;
import org.dromara.surpass.persistence.cache.MemCacheService;
import org.dromara.surpass.web.kaptcha.CaptchaContent;
import org.dromara.surpass.web.kaptcha.ImageCaptcha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.text.ParseException;


/**
 * ImageCaptchaEndpoint  Producer captcha.
 * <p>图片验证码，采用kaptcha生成动态验证码</p>
 *
 * @author Crystal.Sea
 */
@RestController
public class ImageCaptchaEndpoint {
    private static final Logger logger = LoggerFactory.getLogger(ImageCaptchaEndpoint.class);
    /**
     * 验证码生成器
     */
    @Autowired
    Producer captchaProducer;
    /**
     * 缓存服务
     */
    @Autowired
    MemCacheService memCacheService;
    /**
     * 认证令牌服务
     */
    @Autowired
    AuthTokenService authTokenService;

    /**
     * captcha image Producer./图片验证码生成
     */
    @GetMapping(value = {"/captcha"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Message<ImageCaptcha> captchaHandleRequest(
            @RequestParam(value = "captcha", required = false, defaultValue = "text") String captchaType,
            @RequestParam(value = "state", required = false, defaultValue = "state") String state) {
        try {
            CaptchaContent captchaContent = new CaptchaContent(
                    producerKaptchaKey(state),
                    captchaProducer.createText());

            arithmetic(captchaType, captchaContent);

            logger.trace("captcha content {} ", captchaContent);
            //存储到缓存
            memCacheService.put("", captchaContent.getKaptchaKey(), captchaContent.getKaptchaValue());
            // create the image with the text，生成BASE64的图片验证码
            BufferedImage bufferedImage = captchaProducer.createImage(captchaContent.getKaptchaImageText());
            String b64Image = Base64Utils.encodeImage(bufferedImage);

            logger.trace("b64Image {}", b64Image);

            return new Message<>(new ImageCaptcha(state, b64Image));
        } catch (Exception e) {
            logger.error("captcha Producer Error ", e);
        }
        return new Message<>(Message.FAIL);
    }

    private void arithmetic(String captchaType, CaptchaContent captchaContent) {
        if (captchaType.equalsIgnoreCase("Arithmetic")) {//算数计算
            captchaContent.setKaptchaValue(captchaContent.getKaptchaValue().replace("0", ""));
            Integer minuend = Integer.valueOf(captchaContent.getKaptchaValue().substring(0, 1));
            Integer subtrahend = Integer.valueOf(captchaContent.getKaptchaValue().substring(1, 2));
            if (minuend - subtrahend > 0) {
                captchaContent.setKaptchaValue((minuend - subtrahend) + "");
                captchaContent.setKaptchaImageText(minuend + "-" + subtrahend + "=?");
            } else {
                captchaContent.setKaptchaValue((minuend + subtrahend) + "");
                captchaContent.setKaptchaImageText(minuend + "+" + subtrahend + "=?");
            }
        }
    }

    private String producerKaptchaKey(String state) throws ParseException {
        //验证或者生成jwt签名state
        if (StringUtils.isNotBlank(state)
                && !state.equalsIgnoreCase("state")
                && authTokenService.validateJwtToken(state)) {
            //just validate state Token
        } else {
            state = authTokenService.genRandomJwt();//随机生成state
        }
        //验证码的key =  state 的 jwt id
        return authTokenService.resolveJWTID(state);
    }

    public void setCaptchaProducer(Producer captchaProducer) {
        this.captchaProducer = captchaProducer;
    }

}
