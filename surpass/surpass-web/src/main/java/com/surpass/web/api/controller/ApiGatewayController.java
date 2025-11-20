package com.surpass.web.api.controller;

import com.surpass.persistence.util.DynamicExecutionService;
import com.surpass.persistence.util.ResponseTemplateRenderer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Slf4j
public class ApiGatewayController {

    private final DynamicExecutionService dynamicExecutionService;

    private final ResponseTemplateRenderer responseTemplateRenderer;

    @GetMapping("/**")
    public ResponseEntity<String> handleGetRequest(HttpServletRequest request) {
        return handleRequest("GET", request);
    }

    @PostMapping("/**")
    public ResponseEntity<String> handlePostRequest(HttpServletRequest request) {
        return handleRequest("POST", request);
    }

    @PutMapping("/**")
    public ResponseEntity<String> handlePutRequest(HttpServletRequest request) {
        return handleRequest("PUT", request);
    }

    @DeleteMapping("/**")
    public ResponseEntity<String> handleDeleteRequest(HttpServletRequest request) {
        return handleRequest("DELETE", request);
    }

    private ResponseEntity<String> handleRequest(String method, HttpServletRequest request) {
        try {
            // 1. 获取请求路径
            String path = extractApiPath(request);

            // 2. 解析请求参数
            Map<String, Object> params = extractRequestParams(request);

            // 3. 执行API
            Object result = dynamicExecutionService.executeApi(path, method, params);

            // 4. 渲染响应
            String response = responseTemplateRenderer.renderResponse(
                    getDefaultResponseTemplate(), result);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("API网关处理失败: {} {}", method, request.getRequestURI(), e);
            return ResponseEntity.badRequest().body(
                    renderErrorResponse("API执行失败: " + e.getMessage()));
        }
    }

    private String extractApiPath(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        // 移除 /api/v1 前缀
        String path = requestUri.replaceFirst("/api/v1", "");
        if (path.isEmpty()) {
            path = "/";
        }
        return path;
    }

    private Map<String, Object> extractRequestParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();

        // 获取查询参数
        request.getParameterMap().forEach((key, values) -> {
            if (values.length > 0) {
                params.put(key, values[0]);
            }
        });

        // TODO: 处理POST请求的body参数
        // 这里可以扩展支持JSON body参数

        return params;
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
