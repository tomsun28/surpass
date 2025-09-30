/*
 * Copyright [2025] [JinBooks of copyright http://www.jinbooks.com]
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






package org.dromara.surpass.authn.web;

import org.dromara.surpass.web.WebConstants;
import org.dromara.surpass.web.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.context.SecurityContextImpl;

/**
 * SecurityContext Session for Request , use SecurityContextHolderAwareRequestFilter
 *
 * @author Crystal.Sea
 *
 */
public class SessionSecurityContextHolderStrategy implements SecurityContextHolderStrategy {
    private static final Logger logger =
            LoggerFactory.getLogger(SessionSecurityContextHolderStrategy.class);

    @Override
    public void clearContext() {
        WebContext.removeAttribute(WebConstants.AUTHENTICATION);
    }

    @Override
    public SecurityContext getContext() {
        SecurityContext ctx =  createEmptyContext();
        Authentication  authentication = null;
        try {
            authentication = AuthorizationUtils.getAuthentication();
            if (authentication != null) {
                ctx.setAuthentication(authentication);
            }
        }catch(Exception e) {
            logger.trace("a session ", e);
        }


        return ctx;
    }

    @Override
    public void setContext(SecurityContext context) {
    	AuthorizationUtils.setAuthentication(context.getAuthentication());
    }

    @Override
    public SecurityContext createEmptyContext() {
        return new SecurityContextImpl();
    }

}
