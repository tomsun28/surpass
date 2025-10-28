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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.Message;
import com.surpass.entity.base.BookInitBalance;
import com.surpass.entity.book.dto.BookChangeDto;
import com.surpass.entity.config.ConfigCashFlowBalance;
import com.surpass.entity.config.dto.ConfigCashFlowChangeDto;
import com.surpass.entity.config.dto.ConfigCashFlowPageDto;
import com.surpass.entity.vo.CashFlowSubjectBalanceVo;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.BookInitBalanceMapper;
import com.surpass.persistence.mapper.ConfigCashFlowBalanceMapper;
import com.surpass.persistence.service.ConfigCashFlowBalanceService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/3/19 10:42
 */

@Service
@RequiredArgsConstructor
public class ConfigCashFlowBalanceServiceImpl extends ServiceImpl<ConfigCashFlowBalanceMapper, ConfigCashFlowBalance> implements ConfigCashFlowBalanceService {

    private final BookInitBalanceMapper bookInitBalanceMapper;

    @Override
    public Message<CashFlowSubjectBalanceVo> pageList(ConfigCashFlowPageDto dto) {
        LambdaQueryWrapper<ConfigCashFlowBalance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ConfigCashFlowBalance::getBookId, dto.getBookId())
                .orderByAsc(ConfigCashFlowBalance::getSortIndex);
        List<ConfigCashFlowBalance> list = super.list(wrapper);

        //获取科目初始余额
        LambdaQueryWrapper<BookInitBalance> wrapperBookInit = new LambdaQueryWrapper<>();
        wrapperBookInit.eq(BookInitBalance::getBookId, dto.getBookId());
        wrapperBookInit.eq(BookInitBalance::getLevel, 1);
        wrapperBookInit.eq(BookInitBalance::getIsCash, 1);

        List<BookInitBalance> bookInitBalances = bookInitBalanceMapper.selectList(wrapperBookInit);

        BigDecimal yearBalanceBeginning = bookInitBalances.stream()
                .map(BookInitBalance::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 用于存储中间计算结果的Map
        Map<String, ConfigCashFlowBalance> itemMap = new HashMap<>();
        for (ConfigCashFlowBalance item : list) {
            itemMap.put(item.getItemCode(), item);
        }

        // 根据itemCode找到对应的项目并赋值
        // 37-xj-qcye: 现金期初余额
        ConfigCashFlowBalance cashBeginningBalance = itemMap.get("37-xj-qcye");
        if (cashBeginningBalance != null) {
            cashBeginningBalance.setBalance(yearBalanceBeginning);
        }

        // 64-xj-xjqc: 现金及现金等价物期初余额
        ConfigCashFlowBalance cashEquivalentsBeginning = itemMap.get("64-xj-xjqc");
        if (cashEquivalentsBeginning != null) {
            cashEquivalentsBeginning.setBalance(yearBalanceBeginning);
        }

        // 计算 38-xj-qmye: 现金期末余额 = 36-xj-djje(当期净增加额) + 37-xj-qcye(期初余额)
        ConfigCashFlowBalance netIncrease = itemMap.get("36-xj-djje");
        ConfigCashFlowBalance cashEndingBalance = itemMap.get("38-xj-qmye");

        if (netIncrease != null && cashBeginningBalance != null && cashEndingBalance != null) {
            BigDecimal netIncreaseAmount = netIncrease.getBalance() != null ? netIncrease.getBalance() : BigDecimal.ZERO;
            BigDecimal endingAmount = netIncreaseAmount.add(yearBalanceBeginning);
            cashEndingBalance.setBalance(endingAmount);

            // 63-xj-xjqm: 现金及现金等价物期末余额
            ConfigCashFlowBalance cashEquivalentsEnding = itemMap.get("63-xj-xjqm");
            if (cashEquivalentsEnding != null) {
                cashEquivalentsEnding.setBalance(endingAmount);
            }

            // 67-xj-djqm: 当期净增加额(另一处) = 63-xj-xjqm(期末) - 64-xj-xjqc(期初)
            ConfigCashFlowBalance netIncrease2 = itemMap.get("67-xj-djqm");
            if (netIncrease2 != null && cashEquivalentsBeginning != null) {
                BigDecimal netIncrease2Amount = endingAmount.subtract(yearBalanceBeginning);
                netIncrease2.setBalance(netIncrease2Amount);
            }
        }

        CashFlowSubjectBalanceVo cashFlowSubjectBalanceVo = new CashFlowSubjectBalanceVo(list, bookInitBalances);
        return Message.ok(cashFlowSubjectBalanceVo);

    }

    @Override
    @Transactional
    public Message<String> save(ConfigCashFlowChangeDto dto) {
        List<ConfigCashFlowBalance> cashFlowItemDtos = dto.getCashFlowItemDtos();
        boolean result = super.updateBatchById(cashFlowItemDtos);

        return result ? Message.ok("保存成功") : Message.failed("保存失败");
    }

    @Override
    public Message<List<ConfigCashFlowBalance>> getSelectItem(Integer cashFlowItemType) {
        LambdaQueryWrapper<ConfigCashFlowBalance> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNull(ConfigCashFlowBalance::getBookId);
        wrapper.orderByAsc(ConfigCashFlowBalance::getSortIndex);
        if (cashFlowItemType == 0) {
            wrapper.eq(ConfigCashFlowBalance::getIsMain, 1);
        } else {
            wrapper.eq(ConfigCashFlowBalance::getIsAdditional, 1);
        }


        // Fetch the list
        List<ConfigCashFlowBalance> list = super.list(wrapper);

        if (ObjectUtils.isEmpty(list)) {
            throw new BusinessException(500001, "请执行现金流量初始化金额配置sql语句");
        }

        return Message.ok(list);
    }

	@Override
	public boolean deleteByBookIds(List<String> bookIds) {
		LambdaQueryWrapper<ConfigCashFlowBalance> wrapper = new LambdaQueryWrapper<>();
		wrapper.in(ConfigCashFlowBalance::getBookId, bookIds);
		this.getBaseMapper().delete(wrapper);
		return true;
	}

	@Override
	public boolean configCashFlowBalance(BookChangeDto dto) {
		String bookId = dto.getId();

        // 使用count查询判断记录是否存在
        Long count = this.getBaseMapper().selectCount(
                Wrappers.<ConfigCashFlowBalance>lambdaQuery()
                        .eq(ConfigCashFlowBalance::getBookId, bookId));

        if (count == 0) {
            List<ConfigCashFlowBalance> configList = this.getBaseMapper().selectList(Wrappers.<ConfigCashFlowBalance>lambdaQuery()
                    .isNull(ConfigCashFlowBalance::getBookId));
            if (ObjectUtils.isEmpty(configList)) {
                throw new BusinessException(500001, "请执行现金流量初始化金额配置sql语句");
            } else {
                // 使用Java 17特性优化批量复制并设置bookId
                CopyOptions copyOptions = CopyOptions.create()
                        .setIgnoreProperties("id", "createdBy", "createdDate", "modifiedBy", "modifiedDate", "book_id");

                // 一步完成复制和设置bookId
                List<ConfigCashFlowBalance> configCashFlowBalances = configList.stream()
                        .map(source -> {
                            ConfigCashFlowBalance target = new ConfigCashFlowBalance();
                            BeanUtil.copyProperties(source, target, copyOptions);
                            target.setBookId(bookId);
                            return target;
                        })
                        .toList();

                // 执行批量插入
                this.getBaseMapper().insertBatch(configCashFlowBalances);
            }
        }
		return false;
	}
}
