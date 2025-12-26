/*
 * Copyright [2025] [MaxKey of copyright http://www.maxkey.top]
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
 */
 

package com.surpass.interceptor;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.surpass.authn.token.AccessToken;
import com.surpass.authn.token.TokenManager;
import com.surpass.authn.web.AuthorizationUtils;
import com.surpass.constants.ConstsApiAttribute;
import com.surpass.crypto.password.PasswordReciprocal;
import com.surpass.util.AuthorizationHeader;
import com.surpass.util.AuthorizationHeaderUtils;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor token和权限处理.
 * @author Crystal.Sea
 *
 */
@Component
public class OpenApiPermissionAdapter  implements AsyncHandlerInterceptor  {
    private static final Logger logger = LoggerFactory.getLogger(OpenApiPermissionAdapter.class);

    static final String PASSWORD = AuthorizationHeader.Credential.BEARER;
    
    @Autowired
    TokenManager tokenManager;
    
    @Autowired
    PasswordReciprocal passwordReciprocal;
    
    /*
     * 请求前处理
     *  (non-Javadoc)
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
        logger.trace("API Permission Adapter pre handle");
         AuthorizationHeader headerCredential = AuthorizationHeaderUtils.resolve(request);
         
        //判断Authorization
        if(headerCredential != null && StringUtils.isNotBlank(headerCredential.getCredential()) && headerCredential.isBearer() ){
            UsernamePasswordAuthenticationToken authenticationToken = null;
        	String accessToken = passwordReciprocal.decoder(headerCredential.getCredential());
        	AccessToken token = tokenManager.get(accessToken);
            if(token != null ) {
                ArrayList<SimpleGrantedAuthority> grantedAuthoritys = new ArrayList<>();
                grantedAuthoritys.add(new SimpleGrantedAuthority("ROLE_USER"));
                User user = new User(token.getClientId(), PASSWORD, grantedAuthoritys);
                authenticationToken= new UsernamePasswordAuthenticationToken(user, PASSWORD, grantedAuthoritys);
            }else {
                logger.trace("accessToken {} not exists . ",accessToken);
            }
            
            if(authenticationToken !=null && authenticationToken.isAuthenticated()) {
                AuthorizationUtils.setAuthentication(authenticationToken);
                // 1. 获取请求路径
                String path = extractApiPath(request);
                // 2.应用上下文
                String contextPath =  "/"+path.split("/")[1];
                request.setAttribute(ConstsApiAttribute.API_REQUEST_PATH, path);
                request.setAttribute(ConstsApiAttribute.API_REQUEST_CONTEXTPATH, contextPath);
                return true;
            }
        }
        
        logger.trace("No Authentication ... forward to /login");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/login");
        dispatcher.forward(request, response);
        
        return false;
    }
    
    private String extractApiPath(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        int indexOf = requestUri.indexOf("/api/");
        if (indexOf < 0) {
            throw new IllegalArgumentException("Missing /api prefix in URI: " + requestUri);
        }
        return requestUri.substring(indexOf + "/api/".length() - 1);
    }
}
