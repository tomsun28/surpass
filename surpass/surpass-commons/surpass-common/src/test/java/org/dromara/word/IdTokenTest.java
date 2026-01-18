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






package org.dromara.word;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class IdTokenTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long iatTimestamp = 1730078562L; // 时间戳
		long expTimestamp = 1730078862L; // 时间戳
		dateToString("iat",iatTimestamp);
		dateToString("exp",expTimestamp);
	}

	public static LocalDateTime dateToString(String type,long timestamp) {
		Instant instant = Instant.ofEpochSecond(timestamp); // 使用时间戳创建Instant对象
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()); // 转换为本地日期时间
        System.out.println(type+ " Date and time: " + dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)); // 输出日期和时间
        return dateTime;
	}

}
