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
import com.surpass.constants.ConstsSysConfig;
import com.surpass.entity.Message;
import com.surpass.entity.statement.StatementIncomeItem;
import com.surpass.entity.statement.StatementRules;
import com.surpass.entity.statement.StatementSubjectBalance;
import com.surpass.enums.StatementSymbolEnum;
import com.surpass.enums.StatementTypeEnum;
import com.surpass.persistence.mapper.StatementIncomeItemMapper;
import com.surpass.persistence.mapper.StatementRulesMapper;
import com.surpass.persistence.mapper.StatementSubjectBalanceMapper;
import com.surpass.persistence.service.StatementIncomeConfigService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class StatementIncomeConfigServiceImpl implements StatementIncomeConfigService {

    private final StatementIncomeItemMapper statementIncomeItemMapper;
    private final StatementRulesMapper rulesMapper;
    private final IdentifierGenerator identifierGenerator;
    private final StatementSubjectBalanceMapper subjectBalanceMapper;

    /**
     * 获取利润配置明细
     *
     * @param id 主键
     * @return 结果
     */
    @Override
    public Message<StatementIncomeItem> get(String bookId ,String itemCode) {
    	 LambdaQueryWrapper<StatementIncomeItem> itemLqw = Wrappers.lambdaQuery();
    	 itemLqw.eq(StatementIncomeItem::getBookId, bookId);
    	 itemLqw.eq(StatementIncomeItem::getIncomeId, ConstsSysConfig.SYS_CONFIG_TEMPLATE_ID);
    	 itemLqw.eq(StatementIncomeItem::getItemCode, itemCode);
        StatementIncomeItem statementIncome = statementIncomeItemMapper.selectOne(itemLqw);

        LambdaQueryWrapper<StatementRules> lqw = Wrappers.lambdaQuery();
        lqw.eq(StatementRules::getBookId, bookId);
        lqw.eq(StatementRules::getItemCode, itemCode);
        lqw.eq(StatementRules::getType, StatementTypeEnum.income.name());
        lqw.orderByAsc(StatementRules::getSubjectCode);
        List<StatementRules> rules = rulesMapper.selectList(lqw);

        if (!rules.isEmpty()) {
            Map<String, StatementRules> rulesMap = new HashMap<>();
            List<String> codes = rules.stream().map(item -> {
                rulesMap.put(item.getSubjectCode(), item);
                return item.getSubjectCode();
            }).toList();
            LambdaQueryWrapper<StatementSubjectBalance> lqwSubject = Wrappers.lambdaQuery();
            lqwSubject.eq(StatementSubjectBalance::getBookId, bookId);
            lqwSubject.in(StatementSubjectBalance::getSubjectCode, codes);
            List<StatementSubjectBalance> subjectBalances = subjectBalanceMapper.selectList(lqwSubject);
            for (StatementSubjectBalance subjectBalance : subjectBalances) {
                StatementRules statementRules = rulesMap.get(subjectBalance.getSubjectCode());
                updateRuleBalance(subjectBalance, statementRules);
            }
        }
        if(statementIncome != null) {
        	statementIncome.setRules(rules);
        }
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
    public Message<List<StatementIncomeItem>> list(String bookId) {
        LambdaQueryWrapper<StatementIncomeItem> lqw = Wrappers.lambdaQuery();
        lqw.eq(StatementIncomeItem::getBookId, bookId);
        lqw.eq(StatementIncomeItem::getIncomeId, ConstsSysConfig.SYS_CONFIG_TEMPLATE_ID);
        lqw.orderByAsc(StatementIncomeItem::getSortIndex);
        List<StatementIncomeItem> statementIncomes = statementIncomeItemMapper.selectList(lqw);
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
    public Message<StatementIncomeItem> save(StatementIncomeItem dto) {
        updateSortIndex(dto, StatementSymbolEnum.PLUS);
        if (StringUtils.isBlank(dto.getId())) {
            String id = identifierGenerator.nextId(dto).toString();
            dto.setId(id);
            statementIncomeItemMapper.insert(dto);
        } else {
        	statementIncomeItemMapper.updateById(dto);
        }
        // 规则更新
        if (dto.getRules() != null) {
            for (StatementRules rule : dto.getRules()) {
            	rule.setBookId(dto.getBookId());
                rule.setItemCode(dto.getItemCode());
                rule.setType(StatementTypeEnum.income.name());
            }
            saveRules(dto.getRules(),dto.getBookId(), dto.getItemCode());
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
    public Message<List<StatementRules>> saveRules(List<StatementRules> dto,String bookId , String itemCode) {
        LambdaQueryWrapper<StatementRules> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(StatementRules::getBookId, bookId);
        queryWrapper.eq(StatementRules::getItemCode, itemCode);
        rulesMapper.delete(queryWrapper);
        if (!dto.isEmpty()) {
            rulesMapper.insertOrUpdateBatch(dto);
        }
        return Message.ok(dto);
    }

    @Override
    public Message<Boolean> delete(String id) {
        StatementIncomeItem statementIncome = statementIncomeItemMapper.selectById(id);
        updateSortIndex(statementIncome, StatementSymbolEnum.MINUS);
        return Message.ok(statementIncomeItemMapper.deleteById(id) > 0);
    }

    @Override
    public Message<StatementSubjectBalance> getSubjectBalance(StatementSubjectBalance params) {
        LambdaQueryWrapper<StatementSubjectBalance> lqw = Wrappers.lambdaQuery();
        lqw.eq(StatementSubjectBalance::getBookId, params.getBookId());
        lqw.eq(StringUtils.isNotBlank(params.getSubjectCode()), StatementSubjectBalance::getSubjectCode, params.getSubjectCode());
        lqw.eq(StringUtils.isNotBlank(params.getSourceId()), StatementSubjectBalance::getSourceId, params.getSourceId());
        lqw.eq(StringUtils.isNotBlank(params.getYearPeriod()), StatementSubjectBalance::getYearPeriod, params.getYearPeriod());
        return Message.ok(subjectBalanceMapper.selectOne(lqw));
    }

    /**
     * 获取配置
     *
     * @param reportItemId 类目ID
     * @return 结果
     */
    @Override
    public Message<List<StatementRules>> getRules(String bookId ,String itemCode) {
        LambdaQueryWrapper<StatementRules> lqw = Wrappers.lambdaQuery();
        lqw.eq(StatementRules::getBookId, bookId);
        lqw.eq(StatementRules::getItemCode, itemCode);
        lqw.eq(StatementRules::getType, StatementTypeEnum.income.name());
        List<StatementRules> rules = rulesMapper.selectList(itemCode);
        return Message.ok(rules);
    }

    @Override
    public void updateRuleBalance(StatementSubjectBalance subjectBalance, StatementRules statementRules) {
        if (subjectBalance != null) {
            statementRules.setOpeningYearBalance(subjectBalance.getOpeningYearBalanceDebit().subtract(subjectBalance.getOpeningYearBalanceCredit()));
            statementRules.setClosingBalance(subjectBalance.getBalance());
        }else {
            statementRules.setOpeningYearBalance(BigDecimal.ZERO);
            statementRules.setClosingBalance(BigDecimal.ZERO);
        }
    }

    /**
     * 更新行号，保证插入的行号，不影响到原有布局。
     *
     * @param dto    更新参数
     * @param symbol 操作数
     */
    private void updateSortIndex(StatementIncomeItem dto, StatementSymbolEnum symbol) {
        // 未发生变动的序号，不重复更新，减少压力
        if (!StringUtils.isBlank(dto.getId())) {
            StatementIncomeItem statementIncome = statementIncomeItemMapper.selectById(dto.getId());
            if (statementIncome != null && statementIncome.getSortIndex().equals(dto.getSortIndex())) {
                return;
            }
        }

        LambdaQueryWrapper<StatementIncomeItem> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(StatementIncomeItem::getBookId, dto.getBookId())
                .ge(StatementIncomeItem::getSortIndex, dto.getSortIndex())
                .ne(StringUtils.isNotBlank(dto.getId()), StatementIncomeItem::getId, dto.getId())
                .orderByAsc(StatementIncomeItem::getSortIndex);
        List<StatementIncomeItem> statementIncomes = statementIncomeItemMapper.selectList(queryWrapper);
        int sortIndex = dto.getSortIndex();
        for (StatementIncomeItem statementIncome : statementIncomes) {
            if (symbol.equals(StatementSymbolEnum.PLUS)) {
                statementIncome.setSortIndex(++sortIndex);
            } else {
                statementIncome.setSortIndex(--sortIndex);
            }
        }
        if (!statementIncomes.isEmpty()) {
        	statementIncomeItemMapper.updateBatchById(statementIncomes);
        }
    }

}
