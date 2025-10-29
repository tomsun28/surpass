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






package com.surpass.web.historys.contorller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.Message;
import com.surpass.entity.history.HistoryConnector;
import com.surpass.entity.history.dto.HistoryConnectorPageDto;
import com.surpass.entity.idm.UserInfo;
import com.surpass.persistence.service.HistoryConnectorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 连接器日志查询
 *
 * @author Crystal.sea
 *
 */

@RestController
@RequestMapping(value={"/historys"})
public class ConnectorHistoryController {
	static final Logger logger = LoggerFactory.getLogger(ConnectorHistoryController.class);

	@Autowired
	HistoryConnectorService historyConnectorService;

	/**
	 * @Description:
	 * @Param: [dto, currentUser]
	 * @Date: 2024/11/26 16:15
	 */
	@GetMapping(value={"/connectorHistory/fetch"})
    public Message<Page<HistoryConnector>> fetch(
    		@ModelAttribute("historyConnector") HistoryConnectorPageDto dto,
			@CurrentUser UserInfo currentUser){
        logger.debug("historys/historyConnector/fetch/ {}",dto);
		LambdaQueryWrapper<HistoryConnector> wrapper = new LambdaQueryWrapper<>();
		return new Message<>(historyConnectorService.page(dto.build(), wrapper));
    }

}
