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

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.Message;
import com.surpass.entity.book.Book;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.journal.JournalAccount;
import com.surpass.entity.journal.JournalEntry;
import com.surpass.entity.journal.dto.JournalEntryDto;
import com.surpass.entity.journal.dto.JournalEntryPageDto;
import com.surpass.entity.voucher.dto.GenerateVoucherDto;
import com.surpass.entity.voucher.dto.VoucherChangeDto;
import com.surpass.entity.voucher.dto.VoucherItemChangeDto;
import com.surpass.enums.VoucherStatusEnum;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.BookMapper;
import com.surpass.persistence.mapper.JournalEntryMapper;
import com.surpass.persistence.service.JournalAccountService;
import com.surpass.persistence.service.JournalEntryService;
import com.surpass.persistence.service.SettlementService;
import com.surpass.persistence.service.VoucherService;
import com.surpass.util.DateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class JournalEntryServiceImpl extends ServiceImpl<JournalEntryMapper, JournalEntry> implements JournalEntryService {

	@Autowired
	JournalAccountService journalAccountService;

	@Autowired
    BookMapper bookMapper;

	@Autowired
    VoucherService voucherService;

	@Autowired
	SettlementService settlementService;

    @Override
    public Message<Page<JournalEntry>> pageList(JournalEntryPageDto dto) {
        Page<JournalEntry> page = this.getBaseMapper().pageList(dto.build(), dto);

        return new Message<>(Message.SUCCESS, page);
    }

    @Override
    @Transactional
    public Message<String> save(JournalEntryDto dto) {

    	JournalEntry journalEntry = new JournalEntry();
        BeanUtil.copyProperties(dto, journalEntry);
        if(dto.getTradeDate() == null) {
        	journalEntry.setTradeDate(new Date());
        }
        //判断账期是否结账
        String period = DateUtils.format(journalEntry.getTradeDate(),DateUtils.FORMAT_DATE_YYYY_MM);
        Message<String> check = settlementService.check(dto.getBookId(), period);
        if(check.getCode() != 0) {
        	return check;
        }

        JournalAccount  journalAccount  = journalAccountService.getById(journalEntry.getAccId());

        if(journalEntry.getDirection().equalsIgnoreCase("i")
        		|| journalEntry.getDirection().equalsIgnoreCase("o")) {
        	//i收入和o期初
        	journalEntry.setExpenditure(null);
        	if(journalEntry.getDirection().equalsIgnoreCase("o")&&journalAccount.getOpeningBalance().equals(BigDecimal.ZERO)) {
        		journalAccount.setOpeningBalance(journalEntry.getIncome());
        	}
        	journalAccountService.income(journalEntry.getAccId(), journalEntry.getIncome());

        }else if(journalEntry.getDirection().equalsIgnoreCase("e")){
        	journalEntry.setIncome(null);
        	//支出
        	if(journalAccount.getBalance().subtract(journalEntry.getExpenditure()).doubleValue() < 0 ) {
        		throw new BusinessException(10001,"余额不足！");
        	}
        	journalAccountService.expenditure(journalEntry.getAccId(), journalEntry.getExpenditure());
        }

        if(journalAccount != null) {
        	JournalAccount journalAccountBalance  = journalAccountService.getById(journalEntry.getAccId());
        	//设置余额
        	journalEntry.setBalance(journalAccountBalance.getBalance());
        }
        boolean saveResult = super.save(journalEntry);

        return saveResult ? new Message<>(Message.SUCCESS, "新增成功") : new Message<>(Message.FAIL, "新增失败");
    }

    @Override
    @Transactional
    public Message<String> update(JournalEntryDto dto) {
        String id = dto.getId();
        JournalEntry journalEntry = super.getById(id);
        //判断账期是否结账
        String period = DateUtils.format(journalEntry.getTradeDate(),DateUtils.FORMAT_DATE_YYYY_MM);
        Message<String> check = settlementService.check(dto.getBookId(), period);
        if(check.getCode() != 0) {
        	return check;
        }

        BeanUtil.copyProperties(dto, journalEntry);
        boolean result = super.updateById(journalEntry);
        return result ? new Message<>(Message.SUCCESS, "修改成功") : new Message<>(Message.FAIL, "修改失败");
    }

    @Override
    @Transactional
    public Message<String> delete(ListIdsDto dto) {
        List<String> listIds = dto.getListIds();
        List<String> removeableListIds = new ArrayList<>();
        boolean result = false;
        for(String id : listIds) {
	        JournalEntry journalEntry = super.getById(id);
	        //判断账期是否结账
	        String period = DateUtils.format(journalEntry.getTradeDate(),DateUtils.FORMAT_DATE_YYYY_MM);
	        Message<String> check = settlementService.check(journalEntry.getBookId(), period);
	        if(check.getCode() == 0) {
	        	removeableListIds.add(id);
		        if(journalEntry.getDirection().equalsIgnoreCase("i")
		        		||journalEntry.getDirection().equalsIgnoreCase("o")) {
		        	//支出退回
		        	journalAccountService.expenditure(journalEntry.getAccId(), journalEntry.getIncome());
		        }else {
		        	//收入减去
		        	journalAccountService.income(journalEntry.getAccId(), journalEntry.getExpenditure());
		        }
	        }
        }
        result = super.removeBatchByIds(removeableListIds);

        return result ? new Message<>(Message.SUCCESS, "删除成功") : new Message<>(Message.FAIL, "删除失败");
    }

	@Override
	public Message<String> generateVoucher(GenerateVoucherDto dto) {
		String bookId = dto.getBookId();
		Book book = bookMapper.selectById(bookId);
		JournalEntry journalEntry = super.getById(dto.getId());

	     // 格式化日期
        Date formattedDate = DateUtils.getCurrentDate();

        //判断账期是否结账
        String period = DateUtils.format(formattedDate,DateUtils.FORMAT_DATE_YYYY_MM);
        Message<String> check = settlementService.check(bookId, period);
        if(check.getCode() != 0) {
        	return check;
        }

        BigDecimal amount = null;
		if(journalEntry.getDirection().equalsIgnoreCase("i")) {
			amount = journalEntry.getIncome();
		}else {
			amount = journalEntry.getExpenditure();
		}

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(formattedDate);
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH) + 1;

        // 封装凭证入参数据
        Integer wordNum = voucherService.getAbleWordNum(bookId,"记", null, null).getData();

        VoucherChangeDto voucherDto = new VoucherChangeDto();
        voucherDto.setWordHead("记");
        voucherDto.setWordNum(wordNum);
        voucherDto.setBookId(bookId);
        voucherDto.setCompanyName(book.getCompanyName());
        voucherDto.setVoucherDate(formattedDate);
        voucherDto.setVoucherYear(year);
        voucherDto.setVoucherMonth(month);
        voucherDto.setDebitAmount(amount);
        voucherDto.setCreditAmount(amount);

	     // 创建凭证项
        List<VoucherItemChangeDto> voucherItems = new ArrayList<>();
        //
        VoucherItemChangeDto debitItemDto = new VoucherItemChangeDto();
        debitItemDto.setSummary(journalEntry.getRemark());
        debitItemDto.setSubjectId(bookId);
        debitItemDto.setSubjectName(bookId);
        debitItemDto.setSubjectCode(bookId);
        debitItemDto.setDebitAmount(amount);
        debitItemDto.setAuxiliary(List.of());
        debitItemDto.setDetailedAccounts("");
        voucherItems.add(debitItemDto);
        //
        VoucherItemChangeDto creditItemDto = new VoucherItemChangeDto();
        creditItemDto.setSummary(journalEntry.getRemark());
        creditItemDto.setCreditAmount(amount);
        creditItemDto.setSubjectId(bookId);
        creditItemDto.setSubjectName(bookId);
        creditItemDto.setSubjectCode(bookId);
        creditItemDto.setAuxiliary(List.of());
        creditItemDto.setDetailedAccounts("");
        voucherItems.add(creditItemDto);

        voucherDto.setRemark(journalEntry.getRemark());
        // 暂存凭证
        voucherDto.setItems(voucherItems);
        //设置状态为暂存
        voucherDto.setStatus(VoucherStatusEnum.DRAFT.getValue());
        voucherService.save(voucherDto);

    	LambdaUpdateWrapper<JournalEntry> updateWrapper = new LambdaUpdateWrapper<>();
    	updateWrapper.set(JournalEntry::getVoucherId, voucherDto.getId());
    	updateWrapper.eq(JournalEntry::getId, dto.getId());
    	super.update(updateWrapper);

		return Message.ok(voucherDto.getId());
	}
}
