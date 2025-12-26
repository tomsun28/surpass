package com.surpass.web.api.controller;

import com.surpass.constants.ConstsApiAttribute;
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
        return handleRequest(RequestMethod.GET, request,combineParam(null,paramMap));
    }

    @PostMapping("/**")
    public ResponseEntity<Object> postHandle(
    				HttpServletRequest request,
    				@RequestBody(required = false) Map<String, Object> bodyMap,
					@RequestParam(required = false) Map<String, Object> paramMap) {
        return handleRequest(RequestMethod.POST, request,combineParam(bodyMap,paramMap));
    }

    @PutMapping("/**")
    public ResponseEntity<Object> putHandle(
    				HttpServletRequest request,
    				@RequestBody(required = false) Map<String, Object> bodyMap,
					@RequestParam(required = false) Map<String, Object> paramMap) {
        return handleRequest(RequestMethod.PUT, request,combineParam(bodyMap,paramMap));
    }

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

    private ResponseEntity<Object> handleRequest(
    				RequestMethod method, 
    				HttpServletRequest request,
    				Map<String, Object> paramMap) {
        try {
            // 1. 获取请求路径
            String path = request.getAttribute(ConstsApiAttribute.API_REQUEST_PATH).toString();
            // 2.应用上下文
            String contextPath =  request.getAttribute(ConstsApiAttribute.API_REQUEST_CONTEXTPATH).toString();
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
