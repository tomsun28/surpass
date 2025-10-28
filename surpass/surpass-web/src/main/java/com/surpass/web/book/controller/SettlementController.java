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


package com.surpass.web.book.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.Message;
import com.surpass.entity.book.Settlement;
import com.surpass.entity.book.dto.SettlementPageDto;
import com.surpass.entity.book.vo.SettlementVerifyVo;
import com.surpass.entity.idm.UserInfo;
import com.surpass.persistence.service.SettlementService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2024/12/31 11:18
 */

@RestController
@RequestMapping("/settlement")
public class SettlementController {
    private static final Logger logger = LoggerFactory.getLogger(SettlementController.class);

    @Autowired
    SettlementService settlementService;

    @GetMapping(value = { "/fetch" })
    public Message<Page<Settlement>> fetch(@ParameterObject SettlementPageDto dto,@CurrentUser UserInfo userInfo) {
    	dto.setBookId(userInfo.getBookId());
        logger.debug("fetch {}",dto);

        return settlementService.pageList(dto);
    }

    @GetMapping(value = { "/checkout" })
    public Message<Settlement> checkout(@ParameterObject Settlement dto,@CurrentUser UserInfo userInfo) {
    	dto.setBookId(userInfo.getBookId());
    	return settlementService.checkout(dto);
    }

    @GetMapping(value = { "/verify" })
    public Message<List<SettlementVerifyVo>> verify(@CurrentUser UserInfo userInfo) {
    	return settlementService.verify(userInfo.getBookId());
    }
}
