package com.surpass.web.api.controller;

import com.surpass.persistence.util.DynamicExecutionService;
import com.surpass.persistence.util.ResponseTemplateRenderer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api-v1")
@RequiredArgsConstructor
@Slf4j
public class ApiGatewayController  {

    private final DynamicExecutionService dynamicExecutionService;
    private final ResponseTemplateRenderer responseTemplateRenderer;

    @GetMapping("/**")
    public ResponseEntity<Object> handleGetRequest(HttpServletRequest request) {
        return handleRequest("GET", request);
    }

    @PostMapping("/**")
    public ResponseEntity<Object> handlePostRequest(HttpServletRequest request) {
        return handleRequest("POST", request);
    }

    @PutMapping("/**")
    public ResponseEntity<Object> handlePutRequest(HttpServletRequest request) {
        return handleRequest("PUT", request);
    }

    @DeleteMapping("/**")
    public ResponseEntity<Object> handleDeleteRequest(HttpServletRequest request) {
        return handleRequest("DELETE", request);
    }

    private ResponseEntity<Object> handleRequest(String method, HttpServletRequest request) {
        try {
            // 1. 提取上下文路径和API路径
            PathInfo pathInfo = extractPathInfo(request);

            // 2. 解析请求参数
            Map<String, Object> params = extractRequestParams(request);

            // 3. 执行API（传入contextPath）
//            Object result = dynamicExecutionService.executeApi(
//                    pathInfo.getApiPath(),
//                    method,
//                    pathInfo.getContextPath(),
//                    params
//            );

            Object result = new Object();
            // 4. 渲染响应
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

    /**
     * 提取路径信息
     * 请求URI格式: /api-v1/{contextPath}/{apiPath}
     * 例如: /api-v1/admin/user/list
     *       contextPath = /admin
     *       apiPath = /user/list
     */
    private PathInfo extractPathInfo(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        int indexOf = requestUri.indexOf("/api-v1");

        if (indexOf < 0) {
            throw new IllegalArgumentException("Missing /api-v1 prefix in URI: " + requestUri);
        }

        // 去掉 /api-v1 前缀
        String remainingPath = requestUri.substring(indexOf + "/api-v1".length());

        if (remainingPath.isEmpty() || remainingPath.equals("/")) {
            throw new IllegalArgumentException("Missing context path and api path");
        }

        // 分割路径: /contextPath/apiPath
        // 例如: /admin/user/list -> contextPath=/admin, apiPath=/user/list
        String[] parts = remainingPath.split("/", 3); // 限制分割为3部分: ["", "admin", "user/list"]

        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid URI format, expected: /api-v1/{contextPath}/{apiPath}");
        }

        String contextPath = "/" + parts[1]; // /admin
        String apiPath = "/" + parts[2];      // /user/list

        return new PathInfo(contextPath, apiPath);
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

    /**
     * 路径信息内部类
     */
    private static class PathInfo {
        private final String contextPath;
        private final String apiPath;

        public PathInfo(String contextPath, String apiPath) {
            this.contextPath = contextPath;
            this.apiPath = apiPath;
        }

        public String getContextPath() {
            return contextPath;
        }

        public String getApiPath() {
            return apiPath;
        }
    }
}
