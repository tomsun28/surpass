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

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import com.surpass.authn.SignedPrincipal;
import com.surpass.util.DateUtils;
import com.surpass.web.WebConstants;

/**
 * 监听会话创建和销毁时间
 *
 * @author Crystal.Sea
 *
 */
@WebListener
public class HttpSessionListenerAdapter implements HttpSessionListener {
    private static final Logger logger = LoggerFactory.getLogger(HttpSessionListenerAdapter.class);

    public HttpSessionListenerAdapter() {
        super();
        logger.debug("SessionListenerAdapter inited . ");
    }

    /**
     * session Created
     */
    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {
        logger.trace("new session Created : {} " , sessionEvent.getSession().getId());
    }

    /**
     * session Destroyed
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
        HttpSession session = sessionEvent.getSession();
        Authentication  authentication  = (Authentication ) session.getAttribute(WebConstants.AUTHENTICATION);
        Object principal  = authentication == null ? null : authentication.getPrincipal();
        logger.trace("principal {}",principal);
        if(principal != null ) {
        	if(principal instanceof SignedPrincipal  signPrincipal && signPrincipal.getUserInfo() != null) {
        		logger.trace("{} HttpSession Id  {} for userId  {} , username {} @Ticket {} Destroyed" ,
        			DateUtils.getCurrentDateTimeAsString(),
        			session.getId(),
        			signPrincipal.getUserInfo().getId(),
        			signPrincipal.getUserInfo().getUsername(),
        			signPrincipal.getSessionId());
        	}else if(principal instanceof User user) {
        		logger.trace("{} HttpSession Id  {} for username {} password {} Destroyed" ,
        			DateUtils.getCurrentDateTimeAsString(),
        			session.getId(),
        			user.getUsername(),
        			user.getPassword());
        	}else{
        		logger.trace("{} HttpSession Id  {} for principal {} Destroyed" ,
        			DateUtils.getCurrentDateTimeAsString(),
        			session.getId(),
        			principal);
        	}
        }else {
        	logger.trace("{} HttpSession Id  {} Destroyed" ,
        			DateUtils.getCurrentDateTimeAsString(),
        			session.getId());
        }
    }

}
