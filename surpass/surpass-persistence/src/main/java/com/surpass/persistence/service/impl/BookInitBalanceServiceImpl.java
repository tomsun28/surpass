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
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.constants.ConstsSysConfig;
import com.surpass.entity.Message;
import com.surpass.entity.base.BookInitBalance;
import com.surpass.entity.base.dto.BookInitBalanceChangeDto;
import com.surpass.entity.base.dto.BookInitBalancePageDto;
import com.surpass.entity.base.vo.BookInitBalanceVo;
import com.surpass.entity.book.BookSubject;
import com.surpass.entity.book.dto.SubjectPageDto;
import com.surpass.entity.statement.StatementSubjectBalance;
import com.surpass.enums.StatementPeriodTypeEnum;
import com.surpass.enums.SubjectDirectionEnum;
import com.surpass.enums.YesNoEnum;
import com.surpass.persistence.mapper.BookInitBalanceMapper;
import com.surpass.persistence.mapper.BookSubjectMapper;
import com.surpass.persistence.mapper.StatementSubjectBalanceMapper;
import com.surpass.persistence.service.BookInitBalanceService;
import com.surpass.persistence.service.ConfigSysService;
import com.surpass.persistence.service.StatementSubjectBalanceService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 初始余额配置Service业务层处理
 *
 * @author wuyan
 * {@code @date} 2025-02-18
 */
@RequiredArgsConstructor
@Service
public class BookInitBalanceServiceImpl extends ServiceImpl<BookInitBalanceMapper, BookInitBalance> implements BookInitBalanceService {

    private final BookInitBalanceMapper bookInitBalanceMapper;
    private final IdentifierGenerator identifierGenerator;
    private final BookSubjectMapper bookSubjectMapper;
    private final StatementSubjectBalanceMapper statementSubjectBalanceMapper;
    private final StatementSubjectBalanceService statementSubjectBalanceService;
    private final ConfigSysService configSysService;

    /**
     * 分页查询
     *
     * @param dto 分页参数
     * @return 查询结果
     */
    @Override
    public Message<List<BookInitBalanceVo>> list(BookInitBalancePageDto dto) {
        LambdaQueryWrapper<BookInitBalance> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getCategory() != null, BookInitBalance::getCategory, dto.getCategory());
        lqw.eq(BookInitBalance::getBookId, dto.getBookId());
        lqw.likeRight(StringUtils.isNotBlank(dto.getCode()), BookInitBalance::getCode, dto.getCode());
        lqw.likeRight(StringUtils.isNotBlank(dto.getName()), BookInitBalance::getName, dto.getName());
        List<BookInitBalanceVo> result = bookInitBalanceMapper.selectVoList(lqw);
        Map<String, BookInitBalanceVo> mapRes = new HashMap<>();
        for (BookInitBalanceVo bookInitBalanceVo : result) {
            mapRes.put(bookInitBalanceVo.getCode(), bookInitBalanceVo);
        }

        // 取所有会计科目，合并到已有初始配置中
        SubjectPageDto subjectPageDto = new SubjectPageDto();
        subjectPageDto.setBookId(dto.getBookId());
        subjectPageDto.setCategory(dto.getCategory());
        Page<BookSubject> subjectPage = bookSubjectMapper.pageListByBook(new Page<>(1, 10000), subjectPageDto);
        for (BookSubject bookSubject : subjectPage.getRecords()) {
            BookInitBalanceVo bookInitBalanceVo = mapRes.get(bookSubject.getCode());
            if (bookInitBalanceVo == null) {
                bookInitBalanceVo = new BookInitBalanceVo();
                BeanUtil.copyProperties(bookSubject, bookInitBalanceVo);
                bookInitBalanceVo.setBalance(BigDecimal.ZERO);
                bookInitBalanceVo.setCreditAmount(BigDecimal.ZERO);
                bookInitBalanceVo.setDebitAmount(BigDecimal.ZERO);
                bookInitBalanceVo.setOpeningYearBalanceDebit(BigDecimal.ZERO);
                bookInitBalanceVo.setOpeningYearBalanceCredit(BigDecimal.ZERO);
                bookInitBalanceVo.setOriginId(bookSubject.getId());
                bookInitBalanceVo.setId(null);
                bookInitBalanceVo.setCreatedBy(null);
                bookInitBalanceVo.setCreatedDate(null);
                bookInitBalanceVo.setModifiedDate(null);
                bookInitBalanceVo.setModifiedBy(null);
                bookInitBalanceVo.setHasVoucher(false);
                result.add(bookInitBalanceVo);
                mapRes.put(bookSubject.getCode(), bookInitBalanceVo);
            }
        }

        for (BookInitBalanceVo bookInitBalanceVo : result) {
            if (StringUtils.isBlank(bookInitBalanceVo.getOriginId())) {
                bookInitBalanceVo.setOriginId(bookInitBalanceVo.getId());
            }
        }

        // 比对科目和凭证使用情况，对于已使用凭证的科目不能再次修改
        List<String> codes = result.stream().map(BookInitBalanceVo::getCode).toList();
        List<StatementSubjectBalance> statementSubjectBalances = statementSubjectBalanceService.selectIsVoucherByCode(dto.getBookId(), codes);
        for (StatementSubjectBalance statementSubjectBalance : statementSubjectBalances) {
            BookInitBalanceVo bookInitBalanceVo = mapRes.get(statementSubjectBalance.getSubjectCode());
            if (bookInitBalanceVo != null) {
                bookInitBalanceVo.setHasVoucher(true);
            }
        }

        result = result.stream()
                .sorted(Comparator.comparing(BookInitBalanceVo::getCode))
                .collect(Collectors.toList());

        return Message.ok(result);
    }

    /**
     * 插入数据,同步科目表、科目余额表数据
     *
     * @param dtos 插入对象
     * @return 插入结果
     */
    @Override
    @Transactional
    public Message<String> save(List<BookInitBalanceChangeDto> dtos) {
        String currentTerm = configSysService.getCurrentTerm(dtos.get(0).getBookId());
        String initializeTask = configSysService.selectConfigByKey(dtos.get(0).getBookId(), ConstsSysConfig.SYS_INITIALIZE_TASK);
        if ("true".equals(initializeTask)) {
            return Message.failed("当前不允许操作，初始化已完成");
        }

        List<BookInitBalance> bookInitBalances = new ArrayList<>();
        // 所有孩子数据
        Map<String, List<BookInitBalance>> balanceMap = new HashMap<>();
        // 用于同步父级ID
        Map<String, String> originIdMap = new HashMap<>();
        dtos = dtos.stream().peek(dto -> {
            if (dto.getBalance() == null) {
                dto.setBalance(BigDecimal.ZERO);
            }
            if (dto.getOpeningYearBalanceDebit() == null) {
                dto.setOpeningYearBalanceDebit(BigDecimal.ZERO);
            }
            if (dto.getOpeningYearBalanceCredit() == null) {
                dto.setOpeningYearBalanceCredit(BigDecimal.ZERO);
            }
            if (dto.getDebitAmount() == null) {
                dto.setDebitAmount(BigDecimal.ZERO);
            }
            if (dto.getCreditAmount() == null) {
                dto.setCreditAmount(BigDecimal.ZERO);
            }
        }).toList();
        dtos.stream().filter(dto -> dto.getParentId() != null)
                .forEach(dto -> {
                    BookInitBalance bookInitBalance = BookInitBalance.builder().build();
                    BeanUtil.copyProperties(dto, bookInitBalance);

                    List<BookInitBalance> list = balanceMap.getOrDefault(bookInitBalance.getParentId(), new ArrayList<>());
                    list.add(bookInitBalance);
                    balanceMap.put(bookInitBalance.getParentId(), list);

                    bookInitBalances.add(bookInitBalance);
                    originIdMap.put(dto.getCode(), dto.getOriginId());
                });
        dtos.stream().filter(dto -> dto.getParentId() == null).forEach(dto -> {
            BookInitBalance bookInitBalance = BookInitBalance.builder().build();
            BeanUtil.copyProperties(dto, bookInitBalance);
            bookInitBalances.add(bookInitBalance);
            originIdMap.put(dto.getCode(), dto.getOriginId());
        });
        // 如果ID不存在，则生成新的ID，同步更新父级ID
        for (BookInitBalance bookInitBalance : bookInitBalances) {
            if (bookInitBalance.getId() == null) {
                String currentId = identifierGenerator.nextId(bookInitBalance).toString();
                bookInitBalance.setId(currentId);
                List<BookInitBalance> children = balanceMap.getOrDefault(originIdMap.get(bookInitBalance.getCode()), new ArrayList<>());
                for (BookInitBalance child : children) {
                    child.setParentId(currentId);
                }
            }
        }
        // 更新科目余额
        Map<String, BookSubject> map = new HashMap<>();
        List<String> codes = bookInitBalances.stream().map(BookInitBalance::getCode).toList();

        // 更新科目余额表
        LambdaQueryWrapper<StatementSubjectBalance> balanceLqw = Wrappers.lambdaQuery();
        balanceLqw.in(StatementSubjectBalance::getSubjectCode, codes);
        balanceLqw.eq(StatementSubjectBalance::getBookId, dtos.get(0).getBookId());
        balanceLqw.eq(StatementSubjectBalance::getPeriodType, StatementPeriodTypeEnum.MONTH.getValue());
        balanceLqw.eq(StatementSubjectBalance::getYearPeriod, currentTerm);
        List<StatementSubjectBalance> balances = statementSubjectBalanceMapper.selectList(balanceLqw);
        Map<String, StatementSubjectBalance> mapBalance = balances.stream()
                .collect(Collectors.toMap(StatementSubjectBalance::getSubjectCode, bookSubject -> bookSubject));
        List<StatementSubjectBalance> updateBalances = new ArrayList<>();
        for (BookInitBalance bookInitBalance : bookInitBalances) {
            StatementSubjectBalance balance = mapBalance.get(bookInitBalance.getCode());
            BookSubject bookSubject = map.get(bookInitBalance.getCode());
            // 已经存在凭证，不再更新
            if (balance != null && YesNoEnum.y.name().equals(balance.getIsVoucher())) {
                continue;
            } else if (balance == null) {
                balance = StatementSubjectBalance.builder()
                        .bookId(bookInitBalance.getBookId())
                        .periodType(StatementPeriodTypeEnum.MONTH.getValue())
                        .yearPeriod(currentTerm)
                        .sourceId(bookSubject != null ? bookSubject.getId() : "")
                        .parentId(bookSubject != null ? bookSubject.getParentId() : "")
                        .subjectCode(bookInitBalance.getCode())
                        .subjectName(bookInitBalance.getName())
                        .direction(bookInitBalance.getDirection())
                        .isAuxiliary(YesNoEnum.n.name())
                        .isVoucher(YesNoEnum.n.name())
                        .build();
                String currentId = identifierGenerator.nextId(balance).toString();
                balance.setId(currentId);
            }
            balance.setSourceId(bookSubject != null ? bookSubject.getId() : "");
            balance.setParentId(bookSubject != null ? bookSubject.getParentId() : "");
            // 年初
            balance.setOpeningYearBalanceDebit(bookInitBalance.getOpeningYearBalanceDebit());
            balance.setOpeningYearBalanceCredit(bookInitBalance.getOpeningYearBalanceCredit());
            // 本年累计
            balance.setYearToDateDebit(bookInitBalance.getDebitAmount());
            balance.setYearToDateCredit(bookInitBalance.getCreditAmount());
            // 期初余额
            balance.setOpeningBalanceDebit(bookInitBalance.getOpeningYearBalanceDebit()
                    .add(bookInitBalance.getDebitAmount()));
            balance.setOpeningBalanceCredit(bookInitBalance.getOpeningYearBalanceCredit()
                    .add(bookInitBalance.getCreditAmount()));
            // 本期发生额
            balance.setCurrentPeriodDebit(BigDecimal.ZERO);
            balance.setCurrentPeriodCredit(BigDecimal.ZERO);

            // 最终余额,其余余额更新
            balance.setBalance(bookInitBalance.getBalance());
            balance.setClosingBalanceCredit(BigDecimal.ZERO);
            balance.setClosingBalanceDebit(BigDecimal.ZERO);

            if (SubjectDirectionEnum.DEBIT.getValue().equals(balance.getDirection())) {
                balance.setClosingBalanceDebit(balance.getOpeningBalanceDebit()
                        .subtract(balance.getOpeningBalanceCredit()));
            } else {
                balance.setClosingBalanceCredit(balance.getOpeningBalanceCredit()
                        .subtract(balance.getOpeningBalanceDebit()));
            }

            //上月期末余额
            balance.setPrevBalance(BigDecimal.ZERO);
            //上月期末借贷余额
            balance.setPrevClosingBalanceDebit(BigDecimal.ZERO);
            balance.setPrevClosingBalanceCredit(BigDecimal.ZERO);
            //上月期末年度累计
            balance.setPrevYearToDateDebit(BigDecimal.ZERO);
            balance.setPrevYearToDateCredit(BigDecimal.ZERO);

            updateBalances.add(balance);
        }
        statementSubjectBalanceMapper.insertOrUpdateBatch(updateBalances);

        boolean save = bookInitBalanceMapper.insertOrUpdateBatch(bookInitBalances);
        return save ? new Message<>(Message.SUCCESS, "保存成功") : new Message<>(Message.FAIL, "保存失败");
    }
}
