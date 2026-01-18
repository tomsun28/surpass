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






package org.dromara.surpass.configuration;

import java.util.List;

import org.dromara.surpass.constants.ConstsCached;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 全局应用程序配置 包含 1、数据源配置 dataSoruceConfig 2、字符集转换配置 characterEncodingConfig
 * 3、webseal认证集成配置 webSealConfig 4、系统的配置 sysConfig 5、所有用户可访问地址配置 allAccessUrl
 *
 * 其中1、2、3项在applicationContext.xml中配置，配置文件applicationConfig.properties
 * 4项根据dynamic的属性判断是否动态从sysConfigService动态读取
 *
 * @author Crystal.Sea
 *
 */

@Data
@NoArgsConstructor
@Component
@Configuration
public class ApplicationConfig {

    @Value("${surpass.server.frontend.uri:https://www.surpass.com:4200}")
    private String frontendUri;

    @Value("${server.port:8080}")
    private int port;

    @Value("${surpass.server.cached:0}")
    int cached;

    @Value("#{'${surpass.server.restrict.hosts:}'.split(',')}")
	List<String> restrictHosts;

    @Value("${surpass.job.cron.schedule:0 0 0/1 * * ?}")
    String jobCronSchedule;

    @Value("${surpass.job.session.listener:false}")
    boolean isJobSessionListener;

    @Value("${surpass.register.mailtosupport:true}")
    boolean registerMailToSupport;

    public static String  databaseProduct = "MySQL";


    public static void setDatabaseProduct(String databaseProduct) {
		ApplicationConfig.databaseProduct = databaseProduct;
	}

	public boolean isCachedRedis() {
		return cached == ConstsCached.REDIS;
	}

	public boolean isCachedInMemory() {
		return cached == ConstsCached.INMEMORY;
	}

}
