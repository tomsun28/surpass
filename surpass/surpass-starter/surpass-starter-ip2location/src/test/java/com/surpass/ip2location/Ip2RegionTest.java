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






package com.surpass.ip2location;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.lionsoul.ip2region.xdb.Searcher;
import org.maxkey.surpass.ip2location.offline.Ip2regionV2;
import org.springframework.util.StreamUtils;

public class Ip2RegionTest {

	public static void main(String[] args) throws Exception {
		String ip ="112.84.95.103";
		File file= new File("D:\\workspace\\workspace-surpass-ee\\surpass\\surpass-starter\\surpass-starter-ip2location\\src\\main\\resources\\ip2region\\ip2region.xdb");
		InputStream is = new FileInputStream(file);

        byte[] dbBinStr = StreamUtils.copyToByteArray(is);
        Searcher searcher = Searcher.newWithBuffer(dbBinStr);
        Ip2regionV2 ip2regionV2 = new Ip2regionV2(searcher);
        System.out.println(ip2regionV2.region(ip));

	}
}

