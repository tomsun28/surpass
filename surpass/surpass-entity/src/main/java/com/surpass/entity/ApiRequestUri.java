package com.surpass.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiRequestUri {
	/**
	 * 请求路径
	 */
	String requestPath;
	
	/**
	 * 应用上下文
	 */
	String contextPath;
	
	/**
	 * 资源上下文
	 */
	String resourcePath;
	
}
