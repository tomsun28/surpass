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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.entity.Message;
import com.surpass.entity.config.ConfigSalaryFormula;
import com.surpass.entity.config.dto.ConfigSalaryFormulaChangeDto;
import com.surpass.entity.config.dto.ConfigSalaryFormulaPageDto;
import com.surpass.entity.config.vo.ConfigSalaryFormulaVo;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.persistence.service.ConfigSalaryFormulaService;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/2/8 18:00
 */

@RestController
@RequestMapping("/config/salary/formula")
@Slf4j
public class ConfigSalaryFormulaController {
    static final Logger logger = LoggerFactory.getLogger(ConfigSalaryFormulaController.class);

    @Autowired
    ConfigSalaryFormulaService configSalaryFormulaService;

    @GetMapping(value = {"/fetch"})
    public Message<Page<ConfigSalaryFormula>> fetch(@ParameterObject ConfigSalaryFormulaPageDto dto) {
        logger.debug("fetch {}", dto);
        return configSalaryFormulaService.pageList(dto);
    }

    @GetMapping("/get/{id}")
    public Message<ConfigSalaryFormulaVo> getById(@PathVariable(name = "id") String id) {
        return configSalaryFormulaService.getById(id);
    }

    @PostMapping("/save")
    public Message<String> save(@Validated(value = AddGroup.class) @RequestBody ConfigSalaryFormulaChangeDto dto) {
        logger.debug("save {}", dto);
        return configSalaryFormulaService.save(dto);
    }

    @PutMapping("/update")
    public Message<String> update(@Validated(value = EditGroup.class) @RequestBody ConfigSalaryFormulaChangeDto dto) {
        logger.debug("update {}", dto);
        return configSalaryFormulaService.update(dto);
    }

    @DeleteMapping("/delete")
    public Message<String> delete(@RequestBody ListIdsDto dto) {
        return configSalaryFormulaService.delete(dto);
    }
}
