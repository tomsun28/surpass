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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.Message;
import com.surpass.entity.book.Settlement;
import com.surpass.entity.book.dto.SettlementPageDto;
import com.surpass.entity.book.vo.SettlementVerifyVo;
import com.surpass.entity.statement.dto.StatementParamsDto;
import com.surpass.entity.voucher.dto.VoucherSuccessiveDto;
import com.surpass.entity.voucher.vo.VoucherItemVo;
import com.surpass.enums.StatementPeriodTypeEnum;
import com.surpass.persistence.mapper.BookMapper;
import com.surpass.persistence.mapper.SettlementMapper;
import com.surpass.persistence.mapper.VoucherItemMapper;
import com.surpass.persistence.service.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class SettlementServiceImpl extends ServiceImpl<SettlementMapper, Settlement> implements SettlementService {
	private static final Logger logger = LoggerFactory.getLogger(SettlementServiceImpl.class);

	@Autowired
    IdentifierGenerator identifierGenerator;

    @Autowired
    BookMapper bookMapper;

    @Autowired
    BookSubjectService bookSubjectService;

    @Autowired
    SettlementMapper settlementMapper;

    @Autowired
    ConfigSysService configSysService;

    @Autowired
    VoucherService voucherService;

    @Autowired
    VoucherItemMapper voucherItemMapper;

    @Autowired
    StatementIncomeService statementIncomeService;

    @Autowired
    StatementBalanceSheetService statementBalanceSheetService;

    @Autowired
    StatementSubjectBalanceService statementSubjectBalanceService;

    @Autowired
    JournalAccountService journalAccountService;

	@Lazy
	@Autowired
	StatementReportService statementReportService;

	@Override
	public Message<Page<Settlement>> pageList(SettlementPageDto dto) {
		LambdaQueryWrapper<Settlement> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(Settlement::getBookId, dto.getBookId());
		wrapper.eq(Settlement::getYear, dto.getYear());
        List<Settlement> listSettlement = settlementMapper.selectList(wrapper);
        logger.debug("Settlement {}",listSettlement);
        Page<Settlement> pageResult = new Page<>();
        pageResult.setRecords(new ArrayList<>());
        String currentTerm = configSysService.getCurrentTerm(dto.getBookId());
        String termStart = configSysService.getTermStart(dto.getBookId());
        YearMonth currentTermYearMonth = YearMonth.parse(currentTerm);
        YearMonth termStartYearMonth = YearMonth.parse(termStart);
        Integer termStartYear = Integer.valueOf(termStart.split("-")[0]);
        logger.debug("term start {} , current {}",termStart,currentTerm);
        if(termStartYear <= dto.getYear()) {
	        for(int i = 1; i<=12 ;i++) {

	        	Settlement settlement = new Settlement();
	        	settlement.setBookId(dto.getBookId());
	        	settlement.setYear(dto.getYear());
	        	settlement.setStatus(6);
	        	settlement.setPeriod(String.format("%02d", i));
	        	settlement.setYearPeriod(dto.getYear()+"-"+settlement.getPeriod());
	        	YearMonth settlementYearMonth = YearMonth.parse(settlement.getYearPeriod());
	        	if(settlementYearMonth.isBefore(termStartYearMonth)) {
	        		//初始化前
	        		settlement.setStatus(4);
	        	}else if(settlementYearMonth.isAfter(currentTermYearMonth)) {
	        		//当前期后面
	        		settlement.setStatus(2);
	        	}else if(settlementYearMonth.equals(currentTermYearMonth)) {
	        		//当前期
	        		settlement.setStatus(1);
	        	}
	        	pageResult.getRecords().add(settlement);
	        }

	        for(Settlement settlement :pageResult.getRecords()) {
	        	for(Settlement s :listSettlement) {
	        		if(settlement.getYearPeriod().equals(s.getYearPeriod())) {
	        			settlement.setStatus(s.getStatus());
	        		}
	        	}
	        }
	        return new Message<>(Message.SUCCESS, pageResult);
        }
        return Message.failed("账期年份必须大于等于初始化账期");
	}


	/**
	 * 财务软件中关闭当期账务，生成财务报表（资产负债表  利润表）
	 */
	@Override
	@Transactional
	public Message<Settlement> checkout(Settlement dto) {
		//结账逻辑检测
		String currentTerm = configSysService.getCurrentTerm(dto.getBookId());
		YearMonth currentTermYearMonth = YearMonth.parse(currentTerm);
		LambdaQueryWrapper<Settlement> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(Settlement::getBookId, dto.getBookId());
		wrapper.eq(Settlement::getYear, dto.getYear());
		wrapper.eq(Settlement::getYearPeriod, currentTerm);
        Settlement storedSettlement = settlementMapper.selectOne(wrapper);
        if(storedSettlement == null) {
			Settlement settlement = new Settlement();
	    	settlement.setBookId(dto.getBookId());
	    	settlement.setYear(currentTermYearMonth.getYear());
	    	settlement.setYearPeriod(currentTerm);
	    	//当前期
	    	settlement.setCurrentTerm(currentTerm);
	    	//下一期
	    	settlement.setNextTerm(configSysService.getNextTerm(dto.getBookId()));
	    	settlement.setStatus(6);
			//存储当前现金流量报表的期末余额
			StatementParamsDto statementParamsDto = new StatementParamsDto();
			statementParamsDto.setReportDate(currentTerm);
			statementParamsDto.setPeriodType("month");
			statementParamsDto.setBookId(dto.getBookId());
			settlement.setEndingBalance(statementReportService.getEndingBalance(statementParamsDto));
			//资产负债表
			statementBalanceSheetService.checkout(settlement);
			//利润表报表
			statementIncomeService.checkout(settlement);
			//科目余额表
			statementSubjectBalanceService.checkout(settlement);
			//账户结账：本期期初余额=余额
			journalAccountService.checkout(settlement.getBookId());
			//本期结账
			settlementMapper.insert(settlement);
			//更新当前账期
			configSysService.termToNext(dto.getBookId());
        }else {
        	//测试用
        	Settlement settlement = new Settlement();
	    	settlement.setBookId(dto.getBookId());
	    	settlement.setYear(currentTermYearMonth.getYear());
	    	settlement.setYearPeriod(currentTerm);
	    	//当前期
	    	settlement.setCurrentTerm(currentTerm);
	    	//下一期
	    	settlement.setNextTerm(currentTermYearMonth.plusMonths(1).toString());
	    	settlement.setStatus(6);
	    	//statementSubjectBalanceService.checkout(settlement);
	    	//statementIncomeService.checkout(settlement);
        }
		return new Message<>(Message.SUCCESS, "结账完成");
	}


	@Override
	public Message<String> check(String bookId,String period) {
		String currentTerm = configSysService.getCurrentTerm(bookId);
        String termStart = configSysService.getTermStart(bookId);
        YearMonth currentTermYearMonth = YearMonth.parse(currentTerm);
        YearMonth termStartYearMonth = YearMonth.parse(termStart);
        //账期参数
        YearMonth termYearMonth = YearMonth.parse(period);
        logger.debug("term start {} , current {}",termStart,currentTerm);
        if(termYearMonth.isBefore(termStartYearMonth)) {
    		//初始化前
        	return Message.failed("账期["+period+"]不能小于初始化账期["+termStart+"]");
    	}

        LambdaQueryWrapper<Settlement> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(Settlement::getBookId, bookId);
		wrapper.eq(Settlement::getYear, termYearMonth.getYear());
		wrapper.eq(Settlement::getYearPeriod, period);
        Settlement storedSettlement = settlementMapper.selectOne(wrapper);
        if(storedSettlement != null) {
        	return Message.failed("账期["+period+"]结账完成");
        }

        if(termYearMonth.equals(currentTermYearMonth)) {
        	return Message.ok("当前账期["+period+"]未结账");
        }else {
        	return Message.ok("账期["+period+"]未结账");
        }
	}


	@Override
	public Message<List<SettlementVerifyVo>> verify(String bookId) {
		String currentTerm = configSysService.getCurrentTerm(bookId);

		/**
		 * 凭证号连续性检查
		 */
		int checkIndex = 1;
		List<SettlementVerifyVo> settlementVerifyList = new ArrayList<>();
		Message<List<VoucherSuccessiveDto>> voucherSuccessiveMsg = voucherService.checkSuccessiveAll(bookId);
		boolean verify = true;
		boolean warning = false;

		if(CollectionUtils.isEmpty(voucherSuccessiveMsg.getData())) {
			settlementVerifyList.add(new SettlementVerifyVo(checkIndex,"凭证号连续性检查",true,false));
		}else {
			verify = false;
			settlementVerifyList.add(new SettlementVerifyVo(checkIndex,"凭证号连续性检查",false,false));
		}

		/**
		 * 借贷方余额的检查
		 */
		checkIndex ++;
		StatementParamsDto statementParamsDto = new StatementParamsDto();
		statementParamsDto.setPeriodType(StatementPeriodTypeEnum.MONTH.getValue());
		statementParamsDto.setBookId(bookId);
		statementParamsDto.setReportDate(currentTerm);
		statementParamsDto.parse();

		//借方
		BigDecimal creditAmount = BigDecimal.ZERO;
		//贷方
		BigDecimal debitAmount = BigDecimal.ZERO;

		//读取科目余额表
        List<VoucherItemVo> voucherItemVos = voucherItemMapper.selectSubjectAmount(statementParamsDto);
        for (VoucherItemVo voucherItemVo : voucherItemVos) {
        	debitAmount  = debitAmount.add(voucherItemVo.getDebitAmount()== null?BigDecimal.ZERO:voucherItemVo.getDebitAmount());
        	creditAmount = creditAmount.add(voucherItemVo.getCreditAmount()== null?BigDecimal.ZERO:voucherItemVo.getCreditAmount());
        }
        if(creditAmount.equals(debitAmount)) {
        	settlementVerifyList.add(new SettlementVerifyVo(checkIndex,"凭证借贷方余额的检查",true,false));
        }else {
        	verify = false;
        	settlementVerifyList.add(new SettlementVerifyVo(checkIndex,"凭证借贷方余额的检查",false,false));
        }

		if(verify) {
			if(warning) {
				return new Message<>(Message.WARNING, "结账条件项检查完成，有警告信息，仍然可以结账",settlementVerifyList);
			}else {
				return new Message<>(Message.SUCCESS, "结账条件项检查完成",settlementVerifyList);
			}
		}else {
			return new Message<>(Message.FAIL, "结账条件项检查失败",settlementVerifyList);
		}
	}

}
