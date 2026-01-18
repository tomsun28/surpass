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






package org.dromara.surpass.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections4.CollectionUtils;
import org.dromara.surpass.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Data;

@Data
public class ExtraAttrs {
	static final  Logger logger = LoggerFactory.getLogger(ExtraAttrs.class);

	List <ExtraAttr> attrs ;

	/**
	 *
	 */
	public ExtraAttrs() {
		super();
	}

	/**
	 *
	 */
	public ExtraAttrs(String arrayJsonString) {
		this.deserialize(arrayJsonString);
	}

	public void put(String attr,String value) {
		if(attrs == null){
			attrs = new ArrayList<>();
		}
		this.attrs.add(new ExtraAttr(attr,value));
	}

	public void put(String attr,String type,String value) {
		if(attrs == null){
			attrs = new ArrayList<>();
		}
		this.attrs.add(new ExtraAttr(attr,type,value));
	}

	public String get(String attr) {
		String value = null;
		if(CollectionUtils.isNotEmpty(attrs)){
			for(ExtraAttr extraAttr :attrs){
				if(extraAttr.getAttr().equals(attr)){
					value = extraAttr.getValue();
				}
			}
		}
		return value;
	}

	public Map<String,ExtraAttr > toHashMap(){
		HashMap<String,ExtraAttr > extraAttrsHashMap = new HashMap<>();
		for(ExtraAttr extraAttr : attrs){
			extraAttrsHashMap.put(extraAttr.getAttr(), extraAttr);
		}
		logger.debug("extraAttrs HashMap {}" , extraAttrsHashMap);
		return extraAttrsHashMap;
	}

	public Properties toProperties(){
		Properties properties=new Properties();
		for(ExtraAttr extraAttr :attrs){
			properties.put(extraAttr.getAttr(), extraAttr.getValue());
		}
		logger.debug("extraAttrs Properties {}" ,properties);
		return properties;
	}

	public String serialize(){
		String jsonString = JsonUtils.toString(attrs);
		logger.debug("jsonString {}" , jsonString);
		return jsonString;
	}

	public void deserialize(String arrayJsonString) {
		String extraAttrsJsonString = "{\"attrs\":" + arrayJsonString + "}";
		logger.debug("Extra Attrs Json String {}" , extraAttrsJsonString);
		ExtraAttrs jsonAttrs = JsonUtils.stringToObject(extraAttrsJsonString, ExtraAttrs.class);
		this.attrs = jsonAttrs.getAttrs();
	}

}
