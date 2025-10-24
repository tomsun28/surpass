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






package org.dromara.surpass.autoconfigure;


import org.dromara.surpass.configuration.LoginConfig;
import org.dromara.surpass.ip2location.IpLocation;
import org.dromara.surpass.ip2location.IpLocationParser;
import org.dromara.surpass.ip2location.offline.Ip2regionV2;
import org.lionsoul.ip2region.xdb.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;

/**
 *
 * @author Crystal.Sea
 *
 */

@AutoConfiguration
public class IpLocationAutoConfiguration {
	static final  Logger logger = LoggerFactory.getLogger(IpLocationAutoConfiguration.class);

	/**
	 * 加载Ip2Region离线库数据 version 2.7.0
	 * @return Ip2regionV2
	 * @throws Exception
	 */
	public Ip2regionV2 ip2regionV2() throws IOException {
		logger.debug("IpRegion OffLine init...");
		ClassPathResource resource = new ClassPathResource("/ip2region/ip2region.xdb");
        byte[] dbBinStr = StreamUtils.copyToByteArray(resource.getInputStream());
        logger.debug("ip2region length {}",dbBinStr.length);
        Searcher searcher = Searcher.newWithBuffer(dbBinStr);
        return new Ip2regionV2(searcher);
	}


	/**
	 * 加载GeoIP2离线库数据 version 4.0.1
	 * @return GeoIp2V4
	 * @throws IOException
	 * @throws Exception
	 */
	/*
	public GeoIP2V4 geoIP2() throws IOException  {
		logger.debug("GeoIP2 OffLine init...");
		ClassPathResource resource = new ClassPathResource("/geoip2/GeoLite2-City.mmdb");
		DatabaseReader databaseReader = new DatabaseReader.Builder(resource.getInputStream()).build();
        return new GeoIP2V4(databaseReader);
	}
*/
	/**
	 * builder offline provider IpLocation
	 * @param offlineProvider
	 * @return IpLocation
	 */
	public IpLocation builderOfflineProvider(String offlineProvider) {
		IpLocation ipLocationOffLine = null;
		try {
			if(offlineProvider.equalsIgnoreCase("none")) {
				//do nothing
				logger.debug("IpLocation offline Provider none");
			}else if(offlineProvider.equalsIgnoreCase("Ip2Region")){
				ipLocationOffLine = ip2regionV2();
				logger.debug("IpLocation offline Provider Ip2Region");
			}else if(offlineProvider.equalsIgnoreCase("GeoIp2")){
				//ipLocationOffLine = geoIP2();
				logger.debug("IpLocation offline Provider GeoIp2");
			}
		}catch(Exception e) {
			logger.error("builder Offline IpLocation error", e);
		}
		return ipLocationOffLine;
	}

	/**
	 * builder Online Provider IpLocation
	 * @param onlineProvider
	 * @return IpLocation
	 */
	public IpLocation builderOnlineProvider(String onlineProvider) {
		//need on line provider
		IpLocation ipLocationOnLine = null;
		if(onlineProvider.equalsIgnoreCase("none")) {
			//do nothing
			logger.debug("IpLocation online Provider none");
		}
		return ipLocationOnLine;
	}

    /**
     * IP转换区域地址解析
     * @param isIplocation 是否转换
     * @param onlineProvider 在线转换实现提供商none/Ip138/Ipchaxun
     * @param offlineProvider 离线转换实现提供商none/Ip2Region/GeoIp2
     * @return IpLocationParser
     * @throws Exception
     */
    @Bean
	IpLocationParser ipLocationParser(LoginConfig loginConfig) {
        return new IpLocationParser(loginConfig.isIplocation(),
									builderOnlineProvider(loginConfig.getLocationOnlineProvider()),
									builderOfflineProvider(loginConfig.getLocationOfflineProvider())
		);
	}

}
