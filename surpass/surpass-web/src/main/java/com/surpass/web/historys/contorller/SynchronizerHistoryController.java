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
import com.surpass.entity.history.HistorySynchronizer;
import com.surpass.entity.idm.UserInfo;
import com.surpass.persistence.service.HistorySynchronizerService;
import com.surpass.web.historys.contorller.dto.HistorySynchronizerPageDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 同步器日志查询
 *
 * @author Crystal.sea
 *
 */

@RestController
@RequestMapping(value={"/historys"})
public class SynchronizerHistoryController {
	static final Logger logger = LoggerFactory.getLogger(SynchronizerHistoryController.class);

	@Autowired
	HistorySynchronizerService historySynchronizerService;

	/**
     * @param dto
     * @return
     */
	@GetMapping(value={"/synchronizerHistory/fetch"})
    public Message<Page<HistorySynchronizer>> fetch(
    			@ParameterObject HistorySynchronizerPageDto dto,
    			@CurrentUser UserInfo currentUser){
        logger.debug("historys/synchronizerHistory/fetch/ {}",dto);
		LambdaQueryWrapper<HistorySynchronizer> wrapper = new LambdaQueryWrapper<>();
        return new Message<>(historySynchronizerService.page(dto.build(), wrapper));
    }

}
