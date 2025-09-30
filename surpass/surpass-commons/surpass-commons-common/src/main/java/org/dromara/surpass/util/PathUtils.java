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






package org.dromara.surpass.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

public class PathUtils {
    static final Logger logger = LoggerFactory.getLogger(PathUtils.class);
    static PathUtils instance = null;
    String appPath;

    /**
     * getInstance .
     * @return
     */
    public static synchronized PathUtils getInstance() {
        if (instance == null) {
            instance = new PathUtils();
            PathUtils.logger.trace("getInstance()" + " new PathUtils instance");
        }
        return instance;
    }

    /**
     * PathUtils.
     */
    public PathUtils() {
    	appPath =System.getProperty("user.dir");
    	if(StringUtils.isBlank(appPath)) {
    		appPath = Paths.get("").toAbsolutePath().toString();
    	}
        System.setProperty("APP_PATH", appPath);
        logger.trace("PathUtils  App   Path  : {} " ,appPath);
    }

    public String getAppPath() {
        return appPath;
    }
}
