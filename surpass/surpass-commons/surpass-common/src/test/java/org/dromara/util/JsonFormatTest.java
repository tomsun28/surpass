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



package org.dromara.util;

import java.util.Date;

import org.dromara.surpass.util.JsonUtils;
import org.dromara.surpass.uuid.UUID;

public class JsonFormatTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Students s = new Students();
		s.setId(UUID.generate().toString());
		s.setName("name");
		s.setAge(20);
		s.setType(10L);
		s.setD(new Date());
		System.out.println(JsonUtils.toString(s));
	}

}
