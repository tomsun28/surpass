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
import lombok.extern.slf4j.Slf4j;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.journal.JournalAccount;
import com.surpass.entity.journal.dto.JournalAccountDto;
import com.surpass.entity.journal.dto.JournalAccountPageDto;
import com.surpass.persistence.mapper.JournalAccountMapper;
import com.surpass.persistence.service.JournalAccountService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class JournalAccountServiceImpl extends ServiceImpl<JournalAccountMapper, JournalAccount> implements JournalAccountService {


    @Override
    public Message<Page<JournalAccount>> pageList(JournalAccountPageDto dto) {
        Page<JournalAccount> page = this.getBaseMapper().pageList(dto.build(), dto);
        return new Message<>(Message.SUCCESS, page);
    }

    @Override
    public Message<List <JournalAccount> > findAll(String bookId) {
    	LambdaQueryWrapper<JournalAccount> wrapper = new LambdaQueryWrapper<>();
    	wrapper.eq(JournalAccount::getBookId, bookId);
        return new Message<>(Message.SUCCESS, this.getBaseMapper().selectList(wrapper));
    }

    @Override
    @Transactional
    public Message<String> save(JournalAccountDto dto) {
        JournalAccount account = new JournalAccount();
        BeanUtil.copyProperties(dto, account);
        //通过新增记录初始化
        account.setOpeningBalance(null);
        boolean saveResult = super.save(account);
        return saveResult ? new Message<>(Message.SUCCESS, "新增成功") : new Message<>(Message.FAIL, "新增失败");
    }

    @Override
    @Transactional
    public Message<String> update(JournalAccountDto dto) {
        String id = dto.getId();
        JournalAccount account = super.getById(id);
        BeanUtil.copyProperties(dto, account);
        //通过新增记录初始化
        account.setOpeningBalance(null);
        boolean result = super.updateById(account);
        return result ? new Message<>(Message.SUCCESS, "修改成功") : new Message<>(Message.FAIL, "修改失败");
    }

    @Override
    @Transactional
    public Message<String> delete(ListIdsDto dto) {
        List<String> listIds = dto.getListIds();
        //删除
        boolean result = super.removeByIds(listIds);

        return result ? new Message<>(Message.SUCCESS, "删除成功") : new Message<>(Message.FAIL, "删除失败");
    }

	@Override
	public int income(String accId, BigDecimal income) {
		return this.getBaseMapper().income(accId, income);
	}

	@Override
	public int expenditure(String accId, BigDecimal expenditure) {
		return this.getBaseMapper().expenditure(accId, expenditure);
	}

	/**
	 * 结账：本期期初余额=余额
	 */
	@Override
	public int checkout(String bookId) {
		return this.getBaseMapper().checkout(bookId);
	}
}
