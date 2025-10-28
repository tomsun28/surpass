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

import jakarta.servlet.Filter;

import com.surpass.web.WebHttpRestrictHostRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.surpass.configuration.ApplicationConfig;

/**
 * 请求域名限定自动装配
 */
@AutoConfiguration
public class MvcRestrictHostAutoConfiguration implements WebMvcConfigurer {
    private static final  Logger logger = LoggerFactory.getLogger(MvcRestrictHostAutoConfiguration.class);

    @Bean
    FilterRegistrationBean<Filter> webHttpRestrictHostRequestFilter(
                                                ApplicationConfig applicationConfig) {
        logger.debug("WebHttpRestrictHostRequestFilter init for /* ");
        FilterRegistrationBean<Filter> registrationBean =
        		new FilterRegistrationBean<>(new WebHttpRestrictHostRequestFilter(applicationConfig.getRestrictHosts()));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("webHttpRestrictHostRequestFilter");
        registrationBean.setOrder(4);
        return registrationBean;
    }

}
