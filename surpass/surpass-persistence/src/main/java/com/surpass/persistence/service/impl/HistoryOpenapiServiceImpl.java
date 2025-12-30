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






package com.surpass.persistence.service.impl;

import java.util.Date;

import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.surpass.entity.history.HistoryOpenapi;
import com.surpass.persistence.mapper.HistoryOpenapiMapper;
import com.surpass.persistence.service.HistoryOpenapiService;
import com.surpass.web.WebContext;

import org.springframework.stereotype.Service;

@Service
public class HistoryOpenapiServiceImpl  extends JpaServiceImpl<HistoryOpenapiMapper,HistoryOpenapi> implements HistoryOpenapiService {
	private static Logger logger = LoggerFactory.getLogger(HistoryOpenapiServiceImpl.class);

	@Override
    public void insertHistory(HistoryOpenapi historyOpenapi) {
		historyOpenapi.setId(WebContext.genId());
        //Thread insert HistoryOpenapi
        new Thread(new HistoryOpenapiRunnable(this,historyOpenapi)).start();
    }

	public class HistoryOpenapiRunnable implements Runnable{

		HistoryOpenapiServiceImpl service;

		HistoryOpenapi historyOpenapi;

		public HistoryOpenapiRunnable(HistoryOpenapiServiceImpl historyLoginService, HistoryOpenapi historyOpenapi) {
			super();
			this.service = historyLoginService;
			this.historyOpenapi = historyOpenapi;
		}

		@Override
	    public void run() {
			logger.debug(" historyLogin {}" , historyOpenapi);
			historyOpenapi.setAccessTime(new Date());
			service.insert(historyOpenapi);
		}
	}

}
