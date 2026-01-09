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




package org.maxkey.surpass.web;

import java.lang.management.ManagementFactory;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang3.ArchUtils;
import org.apache.commons.lang3.arch.Processor;
import org.maxkey.surpass.util.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

public class ProductEnvironment {

	private static final Logger logger = LoggerFactory.getLogger(ProductEnvironment.class);

    ProductEnvironment() {}

	/**
     * List Environment Variables.
     */
    public static void listEnvVars() {
        logger.info(WebConstants.DELIMITER);

    	String pid = ManagementFactory.getRuntimeMXBean().getName();
        logger.info("Starting with PID {}",pid.substring(0, pid.indexOf("@")));

    	logger.info("Running with Spring Boot v{}, Spring v{}",
        		SpringApplication.class.getPackage().getImplementationVersion(),
        		org.springframework.core.SpringVersion.class.getPackage().getImplementationVersion());

        logger.info("List Environment Variables ");
        Map<String, String> map = System.getenv();
        SortedSet<String> keyValueSet = new TreeSet<>();
        for (Iterator<String> itr = map.keySet().iterator(); itr.hasNext();) {
            String key = itr.next();
            keyValueSet.add(key);
        }
        // out
        if(logger.isTraceEnabled()) {
	        for (Iterator<String> it = keyValueSet.iterator(); it.hasNext();) {
	            String key = it.next();
	            logger.trace("{} = {}" , String.format("%-30s", key) , map.get(key));
	        }
        }
        logger.info("APP_HOME : {}" , PathUtils.getInstance().getAppPath());
        Processor.Type processorType = ArchUtils.getProcessor().getType();
        if (Objects.isNull(processorType)){
        	processorType = Processor.Type.UNKNOWN;
        }
        logger.info("OS       : {}({} {}), version {}",
                    SystemUtils.OS_NAME,
                    SystemUtils.OS_ARCH,
                    processorType,
                    SystemUtils.OS_VERSION

                );

        logger.info("COMPUTER : {}", map.get("COMPUTERNAME"));
        logger.info("USERNAME : {}", map.get("USERNAME"));
        logger.info("JAVA     :");
        logger.info("{} java version {}, class {}",
                        SystemUtils.JAVA_VENDOR,
                        SystemUtils.JAVA_VERSION,
                        SystemUtils.JAVA_CLASS_VERSION
                    );
        logger.info("{} (build {}, {})",
                        SystemUtils.JAVA_VM_NAME,
                        SystemUtils.JAVA_VM_VERSION,
                        SystemUtils.JAVA_VM_INFO
                    );

        logger.info(WebConstants.DELIMITER);

        //WARN No Root logger was configured, creating default ERROR-level Root logger with Console appender
        System.setProperty("nacos.logging.default.config.enabled", "false");
    }
}
