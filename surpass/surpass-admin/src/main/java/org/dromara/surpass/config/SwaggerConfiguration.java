package org.dromara.surpass.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import java.time.LocalDateTime;

/**
 *  swagger配置 默认地址 http://localhost:8081/swagger-ui/index.html
 * @author tomsun28
 * @date 21:05 2018/3/17
 */
@Configuration
public class SwaggerConfiguration {

	@Bean
	OpenAPI openApi() {
		return new OpenAPI().info(apiInfo());
	}

	private Info apiInfo() {
		return new Info()
				.title("Swagger3接口文档")
				.description("更多请咨询服务开发者tomsun28。")
				.version("2.0")
				.termsOfService("http://www.dromara.org")
				.license(
					new License()
						.name("Copyright (c) 2018 - %s , Apache v2 ".formatted(LocalDateTime.now().getYear()))
						.url("http://www.dromara.org")
				);
	}

}
