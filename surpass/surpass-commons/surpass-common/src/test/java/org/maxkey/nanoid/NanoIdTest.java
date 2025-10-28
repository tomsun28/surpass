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



package org.maxkey.nanoid;

import com.surpass.nanoid.NanoId;

public class NanoIdTest {

	public static void main(String[] args) {
		long s =System.currentTimeMillis();
		for(int i = 0; i < 1000000 ; i++) {
			System.out.println(i+" Generated NanoID : "+NanoId.randomNanoId(16));
		}
		System.out.println(System.currentTimeMillis()-s);
	}

}
