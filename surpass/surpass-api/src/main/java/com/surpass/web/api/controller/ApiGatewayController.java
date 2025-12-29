package com.surpass.web.api.controller;

import com.surpass.constants.ConstsApiAttribute;
import com.surpass.entity.ApiRequestUri;
import com.surpass.persistence.service.ApiExecuteService;
import com.surpass.persistence.util.ResponseTemplateRenderer;
import com.surpass.web.WebContext;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一OPEN API网关解析和执行
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiGatewayController {

    private final ApiExecuteService apiExecuteService;

    private final ResponseTemplateRenderer responseRenderer;

    /**
     * 通用GET请求
     */
    @GetMapping("/**")
    public ResponseEntity<Object> getHandle(
                    HttpServletRequest request,
                    @RequestParam(required = false) Map<String, Object> paramMap) {
        return handleRequest(RequestMethod.GET, request,combineParam(null,paramMap));
    }

    /**
     * 通用POST请求
     */
    @PostMapping("/**")
    public ResponseEntity<Object> postHandle(
                    HttpServletRequest request,
                    @RequestBody(required = false) Map<String, Object> bodyMap,
                    @RequestParam(required = false) Map<String, Object> paramMap) {
        return handleRequest(RequestMethod.POST, request,combineParam(bodyMap,paramMap));
    }

    /**
     * 通用PUT请求
     */
    @PutMapping("/**")
    public ResponseEntity<Object> putHandle(
                    HttpServletRequest request,
                    @RequestBody(required = false) Map<String, Object> bodyMap,
                    @RequestParam(required = false) Map<String, Object> paramMap) {
        return handleRequest(RequestMethod.PUT, request,combineParam(bodyMap,paramMap));
    }

    /**
     * 通用DELETE请求
     */
    @DeleteMapping("/**")
    public ResponseEntity<Object> deleteHandle(
                    HttpServletRequest request,
                    @RequestParam(required = false) Map<String, Object> paramMap) {
        return handleRequest(RequestMethod.DELETE, request,combineParam(null,paramMap));
    }
    
    /**
     * 请求参数合并
     * @param bodyMap
     * @param paramMap
     * @return
     */
    Map<String, Object> combineParam(Map<String, Object> bodyMap,Map<String, Object> paramMap){
        Map<String, Object> combinedParamMap = new HashMap<>();
        if(paramMap != null) {
            combinedParamMap.putAll(paramMap);
        }
        if(bodyMap != null) {
            combinedParamMap.putAll(bodyMap);
        }
        return combinedParamMap;
    }
    
    /**
     * 解析URL上下文
     * @param request
     * @return
     */
    private ApiRequestUri explainRequestUri(HttpServletRequest request) {
        ApiRequestUri apiRequestUri;
        if(request.getAttribute(ConstsApiAttribute.API_REQUEST_PATH) != null) {
            apiRequestUri = ApiRequestUri.builder()
                .requestPath(request.getAttribute(ConstsApiAttribute.API_REQUEST_PATH).toString())// 1. 获取请求路径
                .contextPath(request.getAttribute(ConstsApiAttribute.API_REQUEST_CONTEXT_PATH).toString())// 2.应用上下文
                .resourcePath(request.getAttribute(ConstsApiAttribute.API_REQUEST_RESOURCE_PATH).toString())// 3.资源上下文
                .build();
        }else {
            apiRequestUri = WebContext.explainRequestUri(request);
        }
        return apiRequestUri;
    }

    /**
     * 请求处理
     * @param method
     * @param request
     * @param paramMap
     * @return
     */
    private ResponseEntity<Object> handleRequest(
                    RequestMethod method, 
                    HttpServletRequest request,
                    Map<String, Object> paramMap) {
        try {
            // 1.获取URL上下文
            ApiRequestUri apiRequestUri = explainRequestUri(request);
            // 2. 执行API
            Object result = apiExecuteService.execute(apiRequestUri, method.name(), paramMap);
            // 3. 渲染响应
            Object response = responseRenderer.renderResponse(
            		responseRenderer.getDefaultResponseTemplate(), result);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (Exception e) {
            log.error("API网关处理失败: {} {}", method, request.getRequestURI(), e);
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(responseRenderer.renderErrorResponse("API执行失败: " + e.getMessage()));
        }
    }

}
