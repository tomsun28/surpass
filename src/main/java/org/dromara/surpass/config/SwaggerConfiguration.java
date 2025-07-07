package org.dromara.surpass.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  swagger配置 默认地址 http://localhost:8081/swagger-ui/index.html
 * @author tomsun28
 * @date 21:05 2018/3/17
 */
@EnableOpenApi
@Configuration
public class SwaggerConfiguration {

	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.OAS_30)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
				.paths(PathSelectors.any())
				.build()
				.securityContexts(securityContexts())
				.securitySchemes(Collections.singletonList(
						new ApiKey("Authorization", "Authorization", "header")));
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Swagger3接口文档")
				.description("更多请咨询服务开发者tomsun28。")
				.contact(new Contact("tomsun28。", "http://www.usthe.com", "tomsun28@outlook.com"))
				.version("2.0")
				.build();
	}


	private List<SecurityContext> securityContexts() {
		List<SecurityContext> securityContexts = new ArrayList<>();
		securityContexts.add(SecurityContext.builder()
				.securityReferences(defaultAuth())
				.forPaths(PathSelectors.any()).build());
		return securityContexts;
	}


	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		List<SecurityReference> securityReferences = new ArrayList<>();
		securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
		return securityReferences;
	}
}
