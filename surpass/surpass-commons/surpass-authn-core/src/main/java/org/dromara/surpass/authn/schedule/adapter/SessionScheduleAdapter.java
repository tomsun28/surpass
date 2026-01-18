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





package org.dromara.surpass.authn.schedule.adapter;

import java.io.Serializable;
import java.util.Date;

import org.dromara.surpass.authn.session.Session;
import org.dromara.surpass.authn.session.SessionManager;
import org.dromara.surpass.entity.access.SessionList;
import org.dromara.surpass.schedule.ScheduleAdapter;
import org.dromara.surpass.util.DateUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionScheduleAdapter extends ScheduleAdapter   implements Job , Serializable {
	static final  Logger logger = LoggerFactory.getLogger(SessionScheduleAdapter.class);

	private static final long serialVersionUID = 4782358765969474833L;

	transient SessionManager sessionManager;
	String style;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		 if(jobStatus == JOBSTATUS.RUNNING) {return;}
		 init(context);

		 logger.trace("running ... " );
        jobStatus = JOBSTATUS.RUNNING;
        try {
            if(sessionManager != null) {
            	int sessionCount = 0;
            	for (SessionList onlineSession : sessionManager.sessionList(style)) {
            		Session session = sessionManager.get(onlineSession.getSessionId());
            		if(session == null) {
            			logger.debug("TimeOut session {} user {}  Login at {} and TimeOut at {} ." ,
            					onlineSession.getSessionId(),
            					onlineSession.getUsername(),
            					onlineSession.getOperateTime(),
            					DateUtils.formatDateTime(new Date())
            			);
            			sessionManager.terminate(
            					onlineSession.getSessionId(),
            					onlineSession.getUserId(),
            					onlineSession.getUsername());
            		}else {
            			logger.debug("session {} user {} Login at {} , Last Access at {} will Expired at {}." ,
            					onlineSession.getSessionId(),
            					onlineSession.getUsername(),
            					session.getStartTimestamp(),
            					session.getLastAccessTime(),
            					session.getExpiredTime()
            			);
            			sessionCount ++ ;
            		}
            	}
            	logger.debug("current session count {} ." ,sessionCount);
            }
            logger.trace("finished  " );
            jobStatus = JOBSTATUS.FINISHED;
        }catch(Exception e) {
            jobStatus = JOBSTATUS.ERROR;
            logger.error("Exception " ,e);
        }

	}

	 @Override
	protected void init(JobExecutionContext context){
		 super.init(context);
    	if(sessionManager == null) {
    		sessionManager  = getParameter("sessionManager",SessionManager.class);
    		this.style 		= getParameter("style",String.class);
        }
    }
}
