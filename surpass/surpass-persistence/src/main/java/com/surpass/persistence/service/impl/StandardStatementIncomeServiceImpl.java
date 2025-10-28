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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.surpass.entity.Message;
import com.surpass.entity.standard.StandardStatementIncome;
import com.surpass.entity.standard.StandardStatementRules;
import com.surpass.enums.StatementSymbolEnum;
import com.surpass.enums.StatementTypeEnum;
import com.surpass.persistence.mapper.StandardStatementIncomeMapper;
import com.surpass.persistence.mapper.StandardStatementRulesMapper;
import com.surpass.persistence.service.StandardStatementIncomeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class StandardStatementIncomeServiceImpl implements StandardStatementIncomeService {

    private final StandardStatementIncomeMapper statementIncomeMapper;
    private final StandardStatementRulesMapper rulesMapper;
    private final IdentifierGenerator identifierGenerator;

    /**
     * 获取利润配置明细
     *
     * @param id 主键
     * @return 结果
     */
    @Override
    public Message<StandardStatementIncome> get(StandardStatementIncome dto) {
    	LambdaQueryWrapper<StandardStatementIncome> sqw = Wrappers.lambdaQuery();
    	sqw.eq(StandardStatementIncome::getStandardId, dto.getStandardId());
    	sqw.eq(StandardStatementIncome::getItemCode, dto.getItemCode());
    	StandardStatementIncome statementIncome = statementIncomeMapper.selectOne(sqw);

        LambdaQueryWrapper<StandardStatementRules> lqw = Wrappers.lambdaQuery();
        lqw.eq(StandardStatementRules::getStandardId, dto.getStandardId());
        lqw.eq(StandardStatementRules::getItemCode, dto.getItemCode());
        lqw.eq(StandardStatementRules::getType, StatementTypeEnum.income.name());
        lqw.orderByAsc(StandardStatementRules::getSubjectCode);
        List<StandardStatementRules> rules = rulesMapper.selectList(lqw);

        statementIncome.setRules(rules);
        return Message.ok(statementIncome);
    }

    /**
     * 获取利润表配置
     *
     * @param yearPeriod 期间
     * @param bookId     账簿ID
     * @return 结果
     */
    @Override
    public Message<List<StandardStatementIncome>> list(StandardStatementIncome dto) {
        LambdaQueryWrapper<StandardStatementIncome> lqw = Wrappers.lambdaQuery();
        lqw.eq(StandardStatementIncome::getStandardId, dto.getStandardId());
        lqw.orderByAsc(StandardStatementIncome::getSortIndex);
        List<StandardStatementIncome> statementIncomes = statementIncomeMapper.selectList(lqw);
        return Message.ok(statementIncomes);
    }

    /**
     * 利润表配置
     *
     * @param dto 配置参数
     * @return 结果
     */
    @Override
    @Transactional
    public Message<StandardStatementIncome> save(StandardStatementIncome dto) {
        updateSortIndex(dto, StatementSymbolEnum.PLUS);
        if (StringUtils.isBlank(dto.getId())) {
            String id = identifierGenerator.nextId(dto).toString();
            dto.setId(id);
            statementIncomeMapper.insert(dto);
        } else {
            statementIncomeMapper.updateById(dto);
        }
        // 规则更新
        if (dto.getRules() != null) {
            for (StandardStatementRules rule : dto.getRules()) {
            	rule.setStandardId(dto.getStandardId());
                rule.setItemCode(dto.getItemCode());
                rule.setType(StatementTypeEnum.income.name());
            }
            saveRules(dto.getRules(),dto.getStandardId(), dto.getItemCode());
        }

        return Message.ok(dto);
    }

    /**
     * 报表规则配置
     *
     * @param dto 配置项
     * @return 结果
     */
    @Override
    public Message<List<StandardStatementRules>> saveRules(List<StandardStatementRules> dto,String standardId, String itemCode) {
        LambdaQueryWrapper<StandardStatementRules> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(StandardStatementRules::getStandardId, standardId);
        queryWrapper.eq(StandardStatementRules::getItemCode, itemCode);
        rulesMapper.delete(queryWrapper);
        if (!dto.isEmpty()) {
            rulesMapper.insertOrUpdateBatch(dto);
        }
        return Message.ok(dto);
    }

    @Override
    public Message<Boolean> delete(String id) {
    	StandardStatementIncome statementIncome = statementIncomeMapper.selectById(id);
        updateSortIndex(statementIncome, StatementSymbolEnum.MINUS);
        return Message.ok(statementIncomeMapper.deleteById(id) > 0);
    }


    /**
     * 获取配置
     *
     * @param reportItemId 类目ID
     * @return 结果
     */
    @Override
    public Message<List<StandardStatementRules>> getRules(String standardId,String itemCode) {
        LambdaQueryWrapper<StandardStatementRules> lqw = Wrappers.lambdaQuery();
        lqw.eq(StandardStatementRules::getStandardId, standardId);
        lqw.eq(StandardStatementRules::getItemCode, itemCode);
        lqw.eq(StandardStatementRules::getType, StatementTypeEnum.income.name());
        List<StandardStatementRules> rules = rulesMapper.selectList(itemCode);
        return Message.ok(rules);
    }


    /**
     * 更新行号，保证插入的行号，不影响到原有布局。
     *
     * @param dto    更新参数
     * @param symbol 操作数
     */
    private void updateSortIndex(StandardStatementIncome dto, StatementSymbolEnum symbol) {
        // 未发生变动的序号，不重复更新，减少压力
        if (!StringUtils.isBlank(dto.getId())) {
        	StandardStatementIncome statementIncome = statementIncomeMapper.selectById(dto.getId());
            if (statementIncome != null && statementIncome.getSortIndex().equals(dto.getSortIndex())) {
                return;
            }
        }

        LambdaQueryWrapper<StandardStatementIncome> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(StandardStatementIncome::getStandardId, dto.getStandardId())
                .ge(StandardStatementIncome::getSortIndex, dto.getSortIndex())
                .ne(StringUtils.isNotBlank(dto.getId()), StandardStatementIncome::getId, dto.getId())
                .orderByAsc(StandardStatementIncome::getSortIndex);
        List<StandardStatementIncome> statementIncomes = statementIncomeMapper.selectList(queryWrapper);
        int sortIndex = dto.getSortIndex();
        for (StandardStatementIncome statementIncome : statementIncomes) {
            if (symbol.equals(StatementSymbolEnum.PLUS)) {
                statementIncome.setSortIndex(++sortIndex);
            } else {
                statementIncome.setSortIndex(--sortIndex);
            }
        }
        if (!statementIncomes.isEmpty()) {
            statementIncomeMapper.updateBatchById(statementIncomes);
        }
    }
}
