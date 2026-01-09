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






package org.maxkey.surpass.persistence.service.impl;

import java.util.Date;

import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.maxkey.surpass.entity.history.HistoryLogin;
import org.maxkey.surpass.persistence.mapper.HistoryLoginMapper;
import org.maxkey.surpass.persistence.service.HistoryLoginService;
import org.maxkey.surpass.web.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryLoginServiceImpl  extends JpaServiceImpl<HistoryLoginMapper,HistoryLogin> implements HistoryLoginService {
	private static Logger logger = LoggerFactory.getLogger(HistoryLoginServiceImpl.class);

	@Autowired
	HistoryLoginMapper historyLoginMapper;

	public HistoryLoginMapper getMapper() {
		return historyLoginMapper;
	}

	@Override
    public void insertHistory(HistoryLogin historyLogin) {
        historyLogin.setId(WebContext.genId());
        //Thread insert HistoryLogin
        new Thread(new HistoryLoginRunnable(this,historyLogin)).start();
    }

	public class HistoryLoginRunnable implements Runnable{

		HistoryLoginServiceImpl service;

		HistoryLogin historyLogin;

		public HistoryLoginRunnable(HistoryLoginServiceImpl historyLoginService, HistoryLogin historyLogin) {
			super();
			this.service = historyLoginService;
			this.historyLogin = historyLogin;
		}

		@Override
	    public void run() {
			logger.debug(" historyLogin {}" , historyLogin);
			historyLogin.setOperateTime(new Date());
			service.insert(historyLogin);
		}
	}

}
