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






package org.dromara.surpass.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtils {
	private static final Logger logger 		= 	LoggerFactory.getLogger(JsonUtils.class);

    /**
     * jackson Transform json string to java bean object.
     *
     * @param json String
     * @param bean Object
     * @return Object
     */
    public static Object stringToObject(String json, Object bean) {
        try {
            bean = (new ObjectMapper()).readValue(json, bean.getClass());
        } catch (Exception e) {
        	logger.error("Exception readValue", e);
        }
        return bean;
    }

    /**
     * jackson Transform json string to java bean object.
     *
     * @param json String
     * @param bean Object
     * @return Object
     */
    public static Object stringToObject(String json, Object bean, String dateFormat) {
        try {
        	ObjectMapper mapper = new ObjectMapper();
        	mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        	mapper.setDateFormat(new SimpleDateFormat(dateFormat));
            bean = mapper.readValue(json, bean.getClass());
        } catch (Exception e) {
        	logger.error("Exception DateFormat readValue", e);
        }
        return bean;
    }

    /**
     * jackson Transform json string to java bean object.
     *
     * @param json String
     * @param cls Class
     * @return Object
     */
    public static <T> T stringToObject(String json, Class<T> cls) {
        T bean = null;
        try {
        	ObjectMapper mapper = new ObjectMapper();
        	mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
            bean = mapper.readValue(json, cls);
        } catch (Exception e) {
        	logger.error("Exception Class readValue", e);
        }
        return bean;
    }

    /**
     * jackson Transform json string to java bean object.
     *
     * @param json String
     * @param cls Class
     * @return Object
     */
    public static <T> T stringToObject(String json, Class<T> cls , String dateFormat) {
        T bean = null;
        try {
        	ObjectMapper mapper = new ObjectMapper();
        	mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        	mapper.setDateFormat(new SimpleDateFormat(dateFormat));
            bean = mapper.readValue(json, cls);
        } catch (Exception e) {
        	logger.error("Exception DateFormat readValue", e);
        }
        return bean;
    }



    /**
     * jackson Transform java bean object to json string.
     *
     * @param bean Object
     * @return string
     */
    public static String toString(Object bean) {
        String json = "";
        try {
            json = (new ObjectMapper()).writeValueAsString(bean);
        } catch (Exception e) {
        	logger.error("Exception writeValueAsString", e);
        }
        return json;
    }

}
