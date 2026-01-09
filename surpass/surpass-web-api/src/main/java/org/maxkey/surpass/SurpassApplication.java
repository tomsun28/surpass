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

package org.maxkey.surpass;

import org.joda.time.DateTime;
import org.maxkey.surpass.web.InitializeContext;
import org.maxkey.surpass.web.ProductEnvironment;
import org.maxkey.surpass.web.WebContext;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


/**
 * Surpass系统启动入口
 *
 * @author Surpass
 */
@SpringBootApplication
@MapperScan("org.maxkey.surpass.persistence.mapper")
public class SurpassApplication extends SpringBootServletInitializer {
    private static final Logger _logger = LoggerFactory.getLogger(SurpassApplication.class);

    public static void main(String[] args) {
        _logger.info("Start Surpass Application ...");
        ProductEnvironment.listEnvVars();

        new InitializeContext(SpringApplication.run(SurpassApplication.class, args)).init();

        _logger.info("Surpass at {}", new DateTime());
        _logger.info("Surpass Port {}", WebContext.getServerPort());
        _logger.info("Surpass started.");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SurpassApplication.class);
    }

}
