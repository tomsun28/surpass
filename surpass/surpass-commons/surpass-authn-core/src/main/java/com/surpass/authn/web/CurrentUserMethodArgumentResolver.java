/*
 * Copyright [2025] [Surpass of copyright http://www.surpass.com]
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
 *
 */






package com.surpass.authn.web;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.idm.UserInfo;
import com.surpass.web.WebConstants;

/**
 * CurrentUser注解的注入实现
 *
 * @author Crystal.Sea
 *
 */
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    	//读取认证信息
    	Authentication  authentication  =
    			(Authentication ) webRequest.getAttribute(
    					WebConstants.AUTHENTICATION, RequestAttributes.SCOPE_SESSION);
    	UserInfo userInfo  = AuthorizationUtils.getUserInfo(authentication);
    	if (userInfo != null) {
            return userInfo;
        }
        throw new MissingServletRequestPartException("currentUser");
    }

    /**
     * 判断参数类型及注解
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(UserInfo.class)
                && parameter.hasParameterAnnotation(CurrentUser.class);
    }

}
