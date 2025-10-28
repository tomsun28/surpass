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


package com.surpass.persistence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.Message;
import com.surpass.entity.config.ConfigSalaryFormula;
import com.surpass.entity.config.dto.ConfigSalaryFormulaChangeDto;
import com.surpass.entity.config.dto.ConfigSalaryFormulaPageDto;
import com.surpass.entity.config.dto.ConfigSalaryItem;
import com.surpass.entity.config.dto.ConfigSalaryJson;
import com.surpass.entity.config.vo.ConfigSalaryFormulaVo;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.enums.BookBusinessExceptionEnum;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.ConfigSalaryFormulaMapper;
import com.surpass.persistence.service.ConfigSalaryFormulaService;
import com.surpass.util.JsonUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/2/8 17:55
 */

@Service
public class ConfigSalaryFormulaServiceImpl extends ServiceImpl<ConfigSalaryFormulaMapper, ConfigSalaryFormula> implements ConfigSalaryFormulaService {

    @Override
    public Message<ConfigSalaryFormulaVo> getById(String id) {
        ConfigSalaryFormula configSalaryFormula = super.getById(id);
        ConfigSalaryFormulaVo configSalaryFormulaVo = BeanUtil.copyProperties(configSalaryFormula, ConfigSalaryFormulaVo.class);
        String formula = configSalaryFormula.getFormula();
        configSalaryFormulaVo.setFormulaItems(new ArrayList<>());
        if (StringUtils.isNotBlank(formula)) {
            ConfigSalaryJson configSalaryJson = JsonUtils.stringToObject(formula, ConfigSalaryJson.class);
            if (Objects.nonNull(configSalaryJson)) {
                configSalaryFormulaVo.setFormulaItems(configSalaryJson.getFormulaItems());
            }
        }

        return Message.ok(configSalaryFormulaVo);
    }

    @Override
    public Message<Page<ConfigSalaryFormula>> pageList(ConfigSalaryFormulaPageDto dto) {
        LambdaQueryWrapper<ConfigSalaryFormula> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(dto.getRuleName())) {
            wrapper.like(ConfigSalaryFormula::getRuleName, dto.getRuleName());
        }

        Page<ConfigSalaryFormula> page = super.page(dto.build(), wrapper);
        return Message.ok(page);
    }

    @Override
    @Transactional
    public Message<String> save(ConfigSalaryFormulaChangeDto dto) {
        checkRuleName(dto, false);

        ConfigSalaryFormula configSalaryFormula = BeanUtil.copyProperties(dto, ConfigSalaryFormula.class);
        List<ConfigSalaryItem> formulaItems = dto.getFormulaItems();
        if (ObjectUtils.isNotEmpty(formulaItems)) {
            configSalaryFormula.setFormula(JsonUtils.toString(new ConfigSalaryJson(formulaItems)));
        }
        configSalaryFormula.setFormulaText(dto.getFormulaString());
        boolean result = super.save(configSalaryFormula);
        return result ? Message.ok("新增成功") : Message.failed("新增失败");
    }

    @Override
    @Transactional
    public Message<String> update(ConfigSalaryFormulaChangeDto dto) {
        checkRuleName(dto, true);

        ConfigSalaryFormula configSalaryFormula = BeanUtil.copyProperties(dto, ConfigSalaryFormula.class);
        List<ConfigSalaryItem> formulaItems = dto.getFormulaItems();
        if (ObjectUtils.isNotEmpty(formulaItems)) {
            configSalaryFormula.setFormula(JsonUtils.toString(new ConfigSalaryJson(formulaItems)));
        } else {
            configSalaryFormula.setFormula("");
        }
        configSalaryFormula.setFormulaText(dto.getFormulaString());

        boolean result = super.updateById(configSalaryFormula);
        return result ? Message.ok("修改成功") : Message.failed("修改失败");
    }

    /**
     * @Description: 检查公式名称
     * @Param: [dto, isEdit]
     * @return: void
     * @Author: xZen
     * @Date: 2025/2/11 14:57
     */
    private void checkRuleName(ConfigSalaryFormulaChangeDto dto, boolean isEdit) {
        LambdaQueryWrapper<ConfigSalaryFormula> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ConfigSalaryFormula::getRuleName, dto.getRuleName());
        if (isEdit) {
            wrapper.ne(ConfigSalaryFormula::getId, dto.getId());
        }
        List<ConfigSalaryFormula> list = super.list(wrapper);
        if (ObjectUtils.isNotEmpty(list)) {
            throw new BusinessException(50001, "薪资计算公式的规则名称不能重复，请修改");
        }
    }

    @Override
    @Transactional
    public Message<String> delete(ListIdsDto dto) {
        List<String> listIds = dto.getListIds();

        isDisable(listIds);

        boolean result = super.removeByIds(listIds);
        return result ? new Message<>(Message.SUCCESS, "删除成功") : new Message<>(Message.FAIL, "删除失败");
    }

    /**
     * @Description: 判断状态
     * @Param: [ids]
     * @return: void
     * @Author: xZen
     * @Date: 2025/2/11 15:13
     */
    private void isDisable(List<String> ids) {
        LambdaQueryWrapper<ConfigSalaryFormula> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ConfigSalaryFormula::getStatus, 1);
        wrapper.in(ConfigSalaryFormula::getId, ids);

        List<ConfigSalaryFormula> formulas = super.list(wrapper);
        if (ObjectUtils.isNotEmpty(formulas)) {
            throw new BusinessException(
                    BookBusinessExceptionEnum.DISABLE_BEFORE_DELETE.getCode(),
                    BookBusinessExceptionEnum.DISABLE_BEFORE_DELETE.getMsg());
        }
    }
}
