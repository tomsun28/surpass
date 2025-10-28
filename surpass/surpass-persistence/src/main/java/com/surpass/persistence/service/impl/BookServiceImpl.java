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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.Message;
import com.surpass.entity.book.Book;
import com.surpass.entity.book.dto.BookChangeDto;
import com.surpass.entity.book.dto.BookPageDto;
import com.surpass.entity.book.vo.BookVo;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.enums.BookBusinessExceptionEnum;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.BookMapper;
import com.surpass.persistence.service.*;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2024/12/31 11:15
 */

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
    @Autowired
    IdentifierGenerator identifierGenerator;

    @Autowired
    BookMapper bookMapper;

    @Autowired
    BookSubjectService bookSubjectService;

    @Autowired
    ConfigCashFlowBalanceService configCashFlowBalanceService;

    @Autowired
    StatementIncomeService statementIncomeService;

    @Autowired
    StatementBalanceSheetService statementBalanceSheetService;

    @Autowired
    ConfigSysService configSysService;

    @Autowired
    VoucherService voucherService;

    @Autowired
    VoucherTemplateService voucherTemplateService;

    @Autowired
    StandardSubjectCashFlowService standardSubjectCashFlowService;

    @Override
    public Message<Page<Book>> pageList(BookPageDto dto) {
        Page<Book> page = bookMapper.pageList(dto.build(), dto);

        return new Message<>(Message.SUCCESS, page);
    }

    @Override
    @Transactional
    public Message<String> save(BookChangeDto dto) {

        //校验账套名称是否重复
        checkIfTheNameExists(dto, false);

        dto.setId(identifierGenerator.nextId(dto).toString());

        // 账套配置参数初始化
        configSysService.initBooksConfig(dto.getId(),dto.getEnableDate().toString());

        //账套科目
        bookSubjectService.initBookSubject(dto);

        //新增现金流量余额配置
        configCashFlowBalanceService.configCashFlowBalance(dto);

        //新增账套利润表配置
        statementIncomeService.initIncomeStatement(dto);

        //新增账套资产负债表配置
        statementBalanceSheetService.initBalanceSheet(dto);

        //新增默认科目和现金流量的关系
        standardSubjectCashFlowService.saveTemplateRelationships(dto.getId());

        //新增凭证模板
        voucherTemplateService.insertBookTemplate(dto.getId(), dto.getStandardId());

        //新增账套
        Book newBook = new Book();
        BeanUtil.copyProperties(dto, newBook);
        boolean saveResult = super.save(newBook);

        return saveResult ? new Message<>(Message.SUCCESS, "新增成功") : new Message<>(Message.FAIL, "新增失败");
    }

    @Override
    @Transactional
    public Message<String> update(BookChangeDto dto) {
        checkIfTheNameExists(dto, true);

        //新增现金流量余额配置
        configCashFlowBalanceService.configCashFlowBalance(dto);

        //更新账套
        Book booksUpdate = new Book();
        BeanUtil.copyProperties(dto, booksUpdate);
        boolean result = super.updateById(booksUpdate);
        return result ? new Message<>(Message.SUCCESS, "修改成功") : new Message<>(Message.FAIL, "修改失败");
    }

    private void checkIfTheNameExists(BookChangeDto dto, boolean isEdit) {
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Book::getName, dto.getName());
        if (isEdit) {
            wrapper.ne(Book::getId, dto.getId());
        }
        List<Book> list = super.list(wrapper);
        if (ObjectUtils.isNotEmpty(list)) {
            throw new BusinessException(
                    BookBusinessExceptionEnum.DUPLICATE_SETNAME_EXIST.getCode(),
                    BookBusinessExceptionEnum.DUPLICATE_SETNAME_EXIST.getMsg()
            );
        }
    }


    @Override
    @Transactional
    public Message<String> delete(ListIdsDto dto) {
        List<String> bookIds = dto.getListIds();

        //校验是否为活跃状态
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Book::getStatus, 1);
        wrapper.in(Book::getId, bookIds);
        List<Book> books = bookMapper.selectList(wrapper);
        if (ObjectUtils.isNotEmpty(books)) {
            throw new BusinessException(
                    BookBusinessExceptionEnum.DISABLE_BEFORE_DELETE.getCode(),
                    BookBusinessExceptionEnum.DISABLE_BEFORE_DELETE.getMsg()
            );
        }

        //删除关联科目
        bookSubjectService.deleteByBookIds(bookIds);

        //删除现金流量余额配置
        configCashFlowBalanceService.deleteByBookIds(bookIds);

        //删除现金流量和科目的默认关系
        standardSubjectCashFlowService.deleteByBookIds(bookIds);

        //删除利润表配置及数据
    	statementIncomeService.deleteByBookIds(bookIds);

    	//删除资产负债表配置和数据
    	statementBalanceSheetService.deleteByBookIds(bookIds);

    	//删除科目余额表数据
    	statementBalanceSheetService.deleteByBookIds(bookIds);

    	//删除凭证模板及相关条目配置
    	voucherTemplateService.deleteByBookIds(bookIds);

        //删除凭证及相关条目
    	voucherService.deleteByBookIds(bookIds);

        //删除账套数据
        boolean result = super.removeByIds(bookIds);

        return result ? new Message<>(Message.SUCCESS, "删除成功") : new Message<>(Message.FAIL, "删除失败");
    }

    @Override
    public List<BookVo> listBooks(String userId) {
        return bookMapper.listBooks(userId);
    }

}
