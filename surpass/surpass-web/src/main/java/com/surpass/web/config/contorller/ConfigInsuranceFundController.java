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


package com.surpass.web.config.contorller;

import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.Message;
import com.surpass.entity.config.ConfigInsuranceFund;
import com.surpass.entity.idm.UserInfo;
import com.surpass.persistence.service.ConfigInsuranceFundService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/2/12 15:11
 */

@RestController
@RequestMapping("/config/insurance_fund")
@Slf4j
public class ConfigInsuranceFundController {
    static final Logger logger = LoggerFactory.getLogger(ConfigInsuranceFundController.class);

    @Autowired
    private ConfigInsuranceFundService configInsuranceFundService;

    @GetMapping("/getCurrent")
    public Message<ConfigInsuranceFund> getCurrent(@CurrentUser UserInfo currentUser){
        String bookId = currentUser.getBookId();
        return configInsuranceFundService.getCurrent(bookId);
    }

    @PutMapping("/updateCurrent")
    public Message<String> updateCurrent(@RequestBody ConfigInsuranceFund configInsuranceFund,
                                         @CurrentUser UserInfo currentUser) {
        configInsuranceFund.setBookId(currentUser.getBookId());
        logger.debug("update {} ",configInsuranceFund);
        if(configInsuranceFundService.saveOrUpdate(configInsuranceFund)) {
            return new Message<>(Message.SUCCESS);
        } else {
            return new Message<>(Message.FAIL);
        }
    }
}
