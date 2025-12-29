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
import com.surpass.entity.ApiRequestUri;
import com.surpass.persistence.service.AuthzClientService;
import com.surpass.util.AuthorizationHeader;
import com.surpass.util.AuthorizationHeaderUtils;
import com.surpass.web.WebContext;

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
    AuthzClientService authzClientService;
    
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
         AccessToken token = null;
        //判断Authorization
        if(headerCredential != null && StringUtils.isNotBlank(headerCredential.getCredential()) && headerCredential.isBearer() ){
            UsernamePasswordAuthenticationToken authenticationToken = null;
            String accessToken = passwordReciprocal.decoder(headerCredential.getCredential());
            token = tokenManager.get(accessToken);
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
                
                ApiRequestUri apiRequestUri = WebContext.explainRequestUri(request);
                request.setAttribute(ConstsApiAttribute.API_REQUEST_PATH, apiRequestUri.getRequestPath());
                request.setAttribute(ConstsApiAttribute.API_REQUEST_CONTEXT_PATH, apiRequestUri.getContextPath());
                request.setAttribute(ConstsApiAttribute.API_REQUEST_RESOURCE_PATH, apiRequestUri.getResourcePath());
                logger.debug("ApiRequestUri {} ",apiRequestUri);
                return authzClientService.enforce(apiRequestUri,token.getClientId());
            }
        }
        
        logger.trace("No Authentication ... forward to /login");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/login");
        dispatcher.forward(request, response);
        
        return false;
    }

}
