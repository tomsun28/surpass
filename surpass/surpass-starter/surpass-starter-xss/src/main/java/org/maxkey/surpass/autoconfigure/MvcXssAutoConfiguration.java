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






package org.maxkey.surpass.autoconfigure;

import jakarta.servlet.Filter;

import org.maxkey.surpass.web.WebHttpXssRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * XSS请求
 */
@AutoConfiguration
public class MvcXssAutoConfiguration implements WebMvcConfigurer {
    private static final  Logger logger = LoggerFactory.getLogger(MvcXssAutoConfiguration.class);

    @Bean
    FilterRegistrationBean<Filter> webHttpXssRequestFilter() {
        logger.debug("WebHttpXssRequestFilter init for /* ");
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>(new WebHttpXssRequestFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("webHttpXssRequestFilter");
        registrationBean.setOrder(3);
        return registrationBean;
    }

}
