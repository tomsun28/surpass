package com.surpass.web.api.controller;

import com.surpass.persistence.util.DynamicExecutionService;
import com.surpass.persistence.util.ResponseTemplateRenderer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ApiGatewayController {

    private final DynamicExecutionService dynamicExecutionService;

    private final ResponseTemplateRenderer responseTemplateRenderer;

    @GetMapping("/**")
    public ResponseEntity<Object> getHandle(
    				HttpServletRequest request,
    				@RequestParam(required = false) Map<String, Object> paramMap) {
        return handleRequest(RequestMethod.GET, request,paramMap);
    }

    @PostMapping("/**")
    public ResponseEntity<Object> postHandle(
    				HttpServletRequest request,
    				@RequestBody(required = false) Map<String, Object> bodyMap,
					@RequestParam(required = false) Map<String, Object> paramMap) {
    	//参数合并
    	paramMap.putAll(bodyMap);
        return handleRequest(RequestMethod.POST, request,paramMap);
    }

    @PutMapping("/**")
    public ResponseEntity<Object> putHandle(
    				HttpServletRequest request,
    				@RequestBody(required = false) Map<String, Object> bodyMap,
					@RequestParam(required = false) Map<String, Object> paramMap) {
    	//参数合并
    	paramMap.putAll(bodyMap);
        return handleRequest(RequestMethod.PUT, request,paramMap);
    }

    @DeleteMapping("/**")
    public ResponseEntity<Object> deleteHandle(
    				HttpServletRequest request,
    				@RequestParam(required = false) Map<String, Object> paramMap) {
        return handleRequest(RequestMethod.DELETE, request,paramMap);
    }

    private ResponseEntity<Object> handleRequest(
    				RequestMethod method, 
    				HttpServletRequest request,
    				Map<String, Object> paramMap) {
        try {
            // 1. 获取请求路径
            String path = extractApiPath(request);
            // 2.应用上下文
            String contextPath =  path.split("/")[0];
            // 2. 执行API
            Object result = dynamicExecutionService.executeApi(contextPath,path, method.name(), paramMap);
            // 3. 渲染响应
            Object response = responseTemplateRenderer.renderResponse(
                    getDefaultResponseTemplate(), result);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            log.error("API网关处理失败: {} {}", method, request.getRequestURI(), e);
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(renderErrorResponse("API执行失败: " + e.getMessage()));
        }
    }

    private String extractApiPath(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        int indexOf = requestUri.indexOf("/api/");
        if (indexOf < 0) {
            throw new IllegalArgumentException("Missing /api prefix in URI: " + requestUri);
        }
        return requestUri.substring(indexOf + "/api/".length() - 1);
    }

    private String getDefaultResponseTemplate() {
        return "{\"code\":0,\"data\":#{data},\"message\":\"success\"}";
    }

    private String renderErrorResponse(String message) {
        try {
            return "{\"code\":1,\"data\":null,\"message\":\"" + message + "\"}";
        } catch (Exception e) {
            return "{\"code\":1,\"data\":null,\"message\":\"系统错误\"}";
        }
    }
}
