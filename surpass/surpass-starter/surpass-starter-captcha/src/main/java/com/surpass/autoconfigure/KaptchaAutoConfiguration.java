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






package com.surpass.autoconfigure;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * 验证码自动配置
 *
 * @author Crystal.Sea
 *
 */
@AutoConfiguration
public class KaptchaAutoConfiguration {
    private static final  Logger logger = LoggerFactory.getLogger(KaptchaAutoConfiguration.class);
    /**
     * 验证码参数配置文件
     */
    public static final String KAPTCHA_PROPERTY      = "/kaptcha.properties";

    /**
     * Captcha Producer  Config .
     * @return Producer
     * @throws IOException kaptcha.properties is null
     */
    @Bean
    Producer captchaProducer() throws IOException {
    	//读取kaptcha.properties中的验证码配置信息
        Resource resource = new ClassPathResource(KAPTCHA_PROPERTY);
        logger.debug("Kaptcha config file {}" , resource.getURL());
        DefaultKaptcha  kaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.load(resource.getInputStream());
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }

}
