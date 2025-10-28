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
import com.surpass.entity.book.vo.SettlementCarryforwardVo;
import com.surpass.entity.idm.UserInfo;
import com.surpass.entity.voucher.dto.GenerateVoucherDto;
import com.surpass.entity.voucher.dto.VoucherTemplatePageDto;
import com.surpass.persistence.service.SettlementCarryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2024/12/31 11:18
 */

@RestController
@RequestMapping("/settlementcarry")
public class SettlementCarryController {
    private static final Logger logger = LoggerFactory.getLogger(SettlementCarryController.class);

    @Autowired
    SettlementCarryService settlementCarryService;


    @GetMapping(value = { "/fetchcarry" })
    public Message<Page<SettlementCarryforwardVo>> fetchCarry(@ParameterObject VoucherTemplatePageDto dto,@CurrentUser UserInfo userInfo) {
        logger.debug("fetch {}",dto);
        dto.setRelatedId(userInfo.getBookId());
        dto.setBookId(userInfo.getBookId());
        return settlementCarryService.fetchCarry(dto);
    }

    @PostMapping("/generate-voucher")
    public Message<String> generateVoucher(@Validated @RequestBody GenerateVoucherDto dto, @CurrentUser UserInfo currentUser) {
        dto.setBookId(currentUser.getBookId());
        return settlementCarryService.generateVoucher(dto);
    }

    /**
     *
     * @param id 被删除项ID
     * @return 结果
     */
    @DeleteMapping(value = {"/delete/{voucherId}"})
    public Message<String> delete(@PathVariable("voucherId") String voucherId, @CurrentUser UserInfo currentUser) {
        return settlementCarryService.delete(currentUser.getBookId(), voucherId);
    }
}
