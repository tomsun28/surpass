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


package com.surpass.web.standard.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.standard.Standard;
import com.surpass.entity.standard.dto.StandardChangeDto;
import com.surpass.entity.standard.dto.StandardPageDto;
import com.surpass.persistence.service.StandardService;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2024/12/26 17:32
 */

@RestController
@RequestMapping("/standard")
public class StandardController {
    private static final Logger logger = LoggerFactory.getLogger(StandardController.class);

    @Autowired
    StandardService standardService;

    @GetMapping("/get/{id}")
    public Message<Standard> getById(@PathVariable(name="id") String id) {
        return new Message<>(Message.SUCCESS, standardService.getById(id));
    }

    @GetMapping(value = { "/fetch" })
    public Message<Page<Standard>> fetch(@ParameterObject StandardPageDto dto) {

        logger.debug("fetch {}",dto);

        return standardService.pageList(dto);
    }

    @GetMapping(value = { "/fetchAll" })
    public Message<List<Standard>> fetchAll(@RequestParam(name="status",required = false) Integer status) {
        LambdaQueryWrapper<Standard> wrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(status)) {
            wrapper.eq(Standard::getStatus, status);
        }else {
        	wrapper.eq(Standard::getStatus, 1);
        }
        return new Message<>(standardService.list(wrapper));
    }

    @PostMapping(value = { "/save" })
    public Message<String> save(@Validated(value = AddGroup.class) @RequestBody StandardChangeDto dto) {

        logger.debug("save {}",dto);

        return standardService.save(dto);
    }

    @PutMapping(value = { "/update" })
    public Message<String> update(@Validated(value = EditGroup.class) @RequestBody StandardChangeDto dto) {

        logger.debug("update {}",dto);

        return standardService.update(dto);
    }

    @DeleteMapping(value = { "/delete" })
    public Message<String> delete(@Validated @RequestBody ListIdsDto dto) {

        logger.debug("delete {}",dto);

        return standardService.delete(dto);
    }
}
