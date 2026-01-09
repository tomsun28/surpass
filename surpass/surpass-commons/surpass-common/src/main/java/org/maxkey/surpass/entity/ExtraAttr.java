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





/**
 *
 */
package org.maxkey.surpass.entity;

import lombok.Data;

/**
 * @author Administrator
 *
 */
@Data
public class ExtraAttr {

	String attr;
	String name;
	String type;
	String value;

	public ExtraAttr() {
		super();
	}

	/**
	 * @param attr
	 * @param value
	 */
	public ExtraAttr(String attr, String value) {
		super();
		this.attr = attr;
		this.value = value;
	}

	/**
	 * @param attr
	 * @param type
	 * @param value
	 */
	public ExtraAttr(String attr, String type, String value) {
		super();
		this.attr = attr;
		this.type = type;
		this.value = value;
	}

	/**
	 * @param attr
	 * @param name
	 * @param type
	 * @param value
	 */
	public ExtraAttr(String attr,String name, String type, String value) {
		super();
		this.attr = attr;
		this.name = name;
		this.type = type;
		this.value = value;
	}
}
