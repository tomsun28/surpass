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
 





package com.surpass.web.access.contorller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.authn.annotation.CurrentUser;
import com.surpass.authn.session.SessionManager;
import com.surpass.entity.Message;
import com.surpass.entity.access.SessionList;
import com.surpass.entity.access.dto.SessionListPageDto;
import com.surpass.entity.idm.UserInfo;
import com.surpass.persistence.service.HistorySystemLogsService;
import com.surpass.persistence.service.SessionListService;
import com.surpass.util.StrUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录会话管理.
 *
 * @author Crystal.sea
 *
 */

@RestController
@RequestMapping(value = { "/access/session" })
public class SessionController {
    static final Logger logger = LoggerFactory.getLogger(SessionController.class);

    @Autowired
    SessionListService sessionListService;

    @Autowired
    SessionManager sessionManager;

    @Autowired
	HistorySystemLogsService historySystemLogsService;

    /**
     * 查询登录日志.
     *
     * @param dto
     * @return
     */
    @GetMapping(value = { "/fetch" })
    public Message<Page<SessionList>> fetch(
    		@ParameterObject SessionListPageDto dto,
    			@CurrentUser UserInfo currentUser) {
        logger.debug("history/session/fetch {}" , dto);

        LambdaQueryWrapper<SessionList> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(SessionList::getOperateTime);
        return new Message<>(sessionListService.page(dto.build(), wrapper));
    }

    @DeleteMapping(value="/terminate")
    public Message<SessionList> terminate(@RequestParam("ids") String ids,@CurrentUser UserInfo currentUser) {
        logger.debug(ids);
        boolean isTerminated = false;
        try {
            for(String sessionId : StrUtils.string2List(ids, ",")) {
                if(currentUser.getSessionId().contains(sessionId)) {
                    continue;//skip current session
                }
                logger.trace("terminate session Id {} ",sessionId);
                sessionManager.terminate(sessionId,currentUser.getId(),currentUser.getUsername());
                if(!sessionManager.isRedis()) {
                	//TODO: sign session terminate
                }
            }
            isTerminated = true;
        }catch(Exception e) {
            logger.debug("terminate Exception .",e);
        }

        if(isTerminated) {
        	return new Message<>(Message.SUCCESS);
        } else {
        	return new Message<>(Message.ERROR);
        }
    }
}
