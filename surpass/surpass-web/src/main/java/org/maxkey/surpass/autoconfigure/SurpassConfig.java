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






package org.maxkey.surpass.autoconfigure;

import org.maxkey.surpass.authn.realm.jdbc.JdbcAuthenticationRealm;
import org.maxkey.surpass.authn.schedule.adapter.SessionScheduleAdapter;
import org.maxkey.surpass.authn.session.Session;
import org.maxkey.surpass.authn.session.SessionManager;
import org.maxkey.surpass.configuration.ApplicationConfig;
import org.maxkey.surpass.persistence.service.LoginService;
import org.maxkey.surpass.persistence.service.PasswordPolicyValidatorService;
import org.maxkey.surpass.schedule.ScheduleAdapterBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@AutoConfiguration
public class SurpassConfig {
    private static final  Logger logger = LoggerFactory.getLogger(SurpassConfig.class);

    //authenticationRealm for MaxKeyMgtApplication
    @Bean
    JdbcAuthenticationRealm authenticationRealm(
            @Qualifier("passwordEncoder") PasswordEncoder passwordEncoder,
            PasswordPolicyValidatorService passwordPolicyValidator,
            LoginService loginService) {

        JdbcAuthenticationRealm authenticationRealm = new JdbcAuthenticationRealm(
        		passwordEncoder,
        		passwordPolicyValidator,
        		loginService);

        logger.debug("JdbcAuthenticationRealm inited.");
        return authenticationRealm;
    }

    /**
     * 非集群情况下，自动加载本地会话的监测，集群使用Scheduler项目基于Redis任务
     * @param scheduler
     * @param sessionManager
     * @param applicationConfig
     * @return
     * @throws SchedulerException
     */
    @Bean
    String sessionScheduleAdapter(
            Scheduler scheduler,
            SessionManager sessionManager,
            ApplicationConfig applicationConfig) throws SchedulerException {
    	 if (applicationConfig.isJobSessionListener() && applicationConfig.isCachedInMemory()) {
	        new ScheduleAdapterBuilder()
	        	.setCron("0 0/10 * * * ?")
	        	.setJobClass(SessionScheduleAdapter.class)
	        	.setScheduler(scheduler)
	        	.setJobData("sessionManager",sessionManager)
	        	.setJobData("style",Session.STYLE.MGMT)
	        	.build();
	        logger.debug("InMemory Session Schedule Adapter inited .");
    	 }
    	 return "sessionScheduleAdapter";
    }

}
