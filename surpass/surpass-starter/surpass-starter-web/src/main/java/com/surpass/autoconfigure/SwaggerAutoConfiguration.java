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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import com.surpass.configuration.SwaggerConfig;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@AutoConfiguration
public class SwaggerAutoConfiguration {
	static final Logger logger = LoggerFactory.getLogger(SwaggerAutoConfiguration.class);

    @Bean
    GroupedOpenApi userApi(SwaggerConfig swaggerConfig){
        String[] paths = {
        		"/login",
        		"/logout",
        		"/login/**",
        		"/logout/**",
        		"/authz/**",
        		"/authz/**/**",
        		"/metadata/saml20/**" ,
        		"/onlineticket/validate/**",
        		"/api/connect/v10/userinfo",
        		"/api/oauth/v20/me"

        	};
        String[] packagedToMatch = { "org.surpass.authz" };
        return GroupedOpenApi.builder().group(swaggerConfig.getTitle())
                .pathsToMatch(paths)
                .packagesToScan(packagedToMatch).build();
    }

    @Bean
    OpenAPI docOpenAPI(SwaggerConfig swaggerConfig) {
		return new OpenAPI()
				.info(
					new Info()
						.title(swaggerConfig.getTitle())
						.description(swaggerConfig.getDescription())
						.version(swaggerConfig.getVersion())
						.termsOfService("https://www.surpass.top/")
						.license(
							new License()
								.name("Copyright (c) 2024, surpass and/or its affiliates. All rights reserved")
								.url("https://www.surpass.top/")
						)
				).
				externalDocs(
						new ExternalDocumentation()
						.description("surpass.top contact support@maxsso.net")
						.url("https://www.surpass.top/")
				);
	}
}
