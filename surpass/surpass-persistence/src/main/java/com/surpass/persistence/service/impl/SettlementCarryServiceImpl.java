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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.Message;
import com.surpass.entity.book.Book;
import com.surpass.entity.book.BookSubject;
import com.surpass.entity.book.Settlement;
import com.surpass.entity.book.SettlementCarryforward;
import com.surpass.entity.book.vo.SettlementCarryforwardVo;
import com.surpass.entity.hr.EmployeeSalarySummary;
import com.surpass.entity.statement.StatementSubjectBalance;
import com.surpass.entity.voucher.VoucherTemplate;
import com.surpass.entity.voucher.VoucherTemplateItem;
import com.surpass.entity.voucher.dto.GenerateVoucherDto;
import com.surpass.entity.voucher.dto.VoucherChangeDto;
import com.surpass.entity.voucher.dto.VoucherItemChangeDto;
import com.surpass.entity.voucher.dto.VoucherTemplatePageDto;
import com.surpass.enums.VoucherStatusEnum;
import com.surpass.persistence.mapper.BookMapper;
import com.surpass.persistence.mapper.EmployeeSalarySummaryMapper;
import com.surpass.persistence.mapper.SettlementCarryforwardMapper;
import com.surpass.persistence.mapper.SettlementMapper;
import com.surpass.persistence.mapper.VoucherTemplateItemMapper;
import com.surpass.persistence.mapper.VoucherTemplateMapper;
import com.surpass.persistence.service.*;
import com.surpass.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class SettlementCarryServiceImpl extends ServiceImpl<SettlementMapper, Settlement> implements SettlementCarryService {
    private static final Logger logger = LoggerFactory.getLogger(SettlementCarryServiceImpl.class);

    @Autowired
    IdentifierGenerator identifierGenerator;

    @Autowired
    BookMapper bookMapper;

    @Autowired
    BookSubjectService bookSubjectService;

    @Autowired
    ConfigSysService configSysService;

    @Autowired
    VoucherService voucherService;

    @Autowired
    VoucherTemplateItemMapper voucherTemplateItemMapper;

    @Autowired
    VoucherTemplateMapper voucherTemplateMapper;

    @Autowired
    SettlementCarryforwardMapper settlementCarryforwardMapper;

    @Autowired
    StatementSubjectBalanceService statementSubjectBalanceService;

    @Autowired
    EmployeeSalarySummaryMapper employeeSalarySummaryMapper;

    public Message<Page<SettlementCarryforwardVo>> fetchCarry(VoucherTemplatePageDto dto) {
        dto.setYearPeriod(configSysService.getCurrentTerm(dto.getBookId()));
        Page<SettlementCarryforwardVo> page = settlementCarryforwardMapper.pageList(dto.build(), dto);
        return Message.ok(page);
    }


    @Override
    public Message<String> generateVoucher(GenerateVoucherDto dto) {
        logger.debug("GenerateVoucherDto {}", dto);
        String bookId = dto.getBookId();
        Book book = bookMapper.selectById(bookId);
        String currentTerm = configSysService.getCurrentTerm(bookId);
        VoucherTemplate voucherTemplate = voucherTemplateMapper.selectById(dto.getTemplateId());
        logger.debug("voucherTemplate {}", voucherTemplate);
        LambdaQueryWrapper<VoucherTemplateItem> itemLqw = Wrappers.lambdaQuery();
        itemLqw.eq(VoucherTemplateItem::getRelatedId, voucherTemplate.getRelatedId());
        itemLqw.eq(VoucherTemplateItem::getTemplateId, voucherTemplate.getId());
        List<VoucherTemplateItem> items = voucherTemplateItemMapper.selectList(itemLqw);
        logger.debug("VoucherTemplateItems {}", items);

        BigDecimal debitAmount = BigDecimal.ZERO;
        BigDecimal creditAmount = BigDecimal.ZERO;

        Date voucherDate = null;
        if(voucherTemplate.getVoucherDate().equals(0)) {
        	voucherDate = configSysService.getCurrentTermLastDate(bookId);
        }else if(0 < voucherTemplate.getVoucherDate() && voucherTemplate.getVoucherDate()< 31 ){
        	String voucherDateString = "";
        	if(voucherTemplate.getVoucherDate() < 10) {
        		voucherDateString = currentTerm+"-0"+voucherTemplate.getVoucherDate();
        	}else {
        		voucherDateString = currentTerm+"-"+voucherTemplate.getVoucherDate();
        	}
        	voucherDate =DateUtils.parse(voucherDateString, DateUtils.FORMAT_DATE_YYYY_MM_DD);
        }

        int year = Integer.parseInt(currentTerm.split("-")[0]);
        int month = Integer.parseInt(currentTerm.split("-")[1]);

        VoucherChangeDto voucherChangeDto = createVoucherChangeDto(book, bookId, voucherTemplate.getWordHead(), voucherDate, year, month, debitAmount);
        voucherChangeDto.setRemark(voucherTemplate.getRemark().replace("{yyyy}", year + "").replace("{mm}", month + ""));

        List<VoucherItemChangeDto> voucherItems = new ArrayList<>();

        Map<String, VoucherTemplateItem> itemsMap = new HashMap<>();
        for (VoucherTemplateItem item : items) {
            itemsMap.put(item.getSubjectCode(), item);
        }

        if (voucherTemplate.getCode().startsWith("qm_jz_")) {

            //凭证 不转结
            voucherChangeDto.setCarryForward("y");

            if (voucherTemplate.getCode().equals("qm_jz_sr")) {//结转收入
                //贷
                //主营业务收入
                addVoucherItems(bookId, "6001", voucherItems, itemsMap.get("6001"));
                //其他业务收入
                addVoucherItems(bookId, "6301", voucherItems, itemsMap.get("6301"));
                //营业外收入
                addVoucherItems(bookId, "6051", voucherItems, itemsMap.get("6051"));
                for (VoucherItemChangeDto vt : voucherItems) {
                    debitAmount = debitAmount.add(vt.getDebitAmount());
                }
                creditAmount = debitAmount;
                //借
                //本年利润
                voucherItems.add(createVoucherItemDto(bookId, itemsMap.get("4103"), debitAmount));
            } else if (voucherTemplate.getCode().equals("qm_jz_cbfy")) {//结转成本
                //主营业务成本
                addVoucherItems(bookId, "6401", voucherItems, itemsMap.get("6401"));
                //税金及附加
                addVoucherItems(bookId, "6405", voucherItems, itemsMap.get("6405"));
                //销售费用
                addVoucherItems(bookId, "6601", voucherItems, itemsMap.get("6601"));
                //管理费用
                addVoucherItems(bookId, "6602", voucherItems, itemsMap.get("6602"));
                //财务费用
                addVoucherItems(bookId, "6603", voucherItems, itemsMap.get("6603"));
                //营业外支出
                addVoucherItems(bookId, "6711", voucherItems, itemsMap.get("6711"));
                for (VoucherItemChangeDto vt : voucherItems) {
                    creditAmount = creditAmount.add(vt.getCreditAmount());
                }
                debitAmount = creditAmount;
                //本年利润
                voucherItems.add(createVoucherItemDto(bookId, itemsMap.get("4103"), debitAmount));
            } else if (voucherTemplate.getCode().equals("qm_jz_sds")) {//结转所得税
                //所得税
                addVoucherItems(bookId, "6801", voucherItems, itemsMap.get("6801"));
                for (VoucherItemChangeDto vt : voucherItems) {
                    creditAmount = creditAmount.add(vt.getCreditAmount());
                }
                debitAmount = creditAmount;
                //本年利润
                voucherItems.add(createVoucherItemDto(bookId, itemsMap.get("4103"), debitAmount));
            } else if (voucherTemplate.getCode().equals("qm_jz_bnlr")) {//年末 结转本年利润
                if (month == 12) {
                    StatementSubjectBalance subjectBalance = statementSubjectBalanceService.getSubjectBalance(bookId, "4103");
                    voucherItems.add(createVoucherItemDto(bookId, itemsMap.get("4103"), subjectBalance.getBalance()));
                    //未分配利润
                    subjectBalance = statementSubjectBalanceService.getSubjectBalance(bookId, "410406");
                    voucherItems.add(createVoucherItemDto(bookId, itemsMap.get("410406"), subjectBalance.getBalance()));
                } else {
                    return Message.failed("非年末，无需结转本年利润");
                }
            }
        }else if (voucherTemplate.getCode().startsWith("jt_gz")||voucherTemplate.getCode().startsWith("jt_shebao")) {
        	//计提本月
        	LambdaQueryWrapper<EmployeeSalarySummary> salaryWrapper = new LambdaQueryWrapper<>();
        	salaryWrapper.eq(EmployeeSalarySummary::getBelongDate, currentTerm);
        	salaryWrapper.eq(EmployeeSalarySummary::getBookId, bookId);
        	salaryWrapper.eq(EmployeeSalarySummary::getLabel, "salary");
        	salaryWrapper.eq(EmployeeSalarySummary::getDeleted, "n");
        	EmployeeSalarySummary summary = employeeSalarySummaryMapper.selectOne(salaryWrapper);
        	if(summary != null) {
	        	 if (voucherTemplate.getCode().startsWith("jt_gz")){
	        		 for (VoucherTemplateItem item : items) {
	                     voucherItems.add(createVoucherItemDto(bookId, item, summary.getPayAmount()));
	                 }
	        	 }else if (voucherTemplate.getCode().startsWith("jt_shebao")){
	        		 for (VoucherTemplateItem item : items) {
	                     voucherItems.add(createVoucherItemDto(bookId, item, summary.getBusinessSocialInsurance()));
	                 }
	        	 }
        	}
        }else if (voucherTemplate.getCode().startsWith("zf_shebao")||voucherTemplate.getCode().startsWith("zf_gz")) {
        	//支付上月
        	String prevTerm = configSysService.getPrevTerm(bookId);
        	LambdaQueryWrapper<EmployeeSalarySummary> salaryWrapper = new LambdaQueryWrapper<>();
        	salaryWrapper.eq(EmployeeSalarySummary::getBelongDate, prevTerm);
        	salaryWrapper.eq(EmployeeSalarySummary::getBookId, bookId);
        	salaryWrapper.eq(EmployeeSalarySummary::getLabel, "salary");
        	salaryWrapper.eq(EmployeeSalarySummary::getDeleted, "n");
        	EmployeeSalarySummary summary = employeeSalarySummaryMapper.selectOne(salaryWrapper);
        	if(summary != null) {
	        	 if (voucherTemplate.getCode().startsWith("zf_gz")){
	        		//银行存款计算
	        		 BigDecimal creditYyckAmount = BigDecimal.ZERO;
	        		 //应付职工薪酬
	        		 if(itemsMap.containsKey("221101")) {
	        			 debitAmount = summary.getPayAmount();
	        			 creditYyckAmount = debitAmount;
	        			 voucherItems.add(createVoucherItemDto(bookId, itemsMap.get("221101"), debitAmount));
	        		 }
	        		 //个人社保
	        		 if(itemsMap.containsKey("122102")) {
	        			 voucherItems.add(createVoucherItemDto(bookId, itemsMap.get("122102"), summary.getTotalSocialInsurance()));
	        			 creditYyckAmount = creditYyckAmount.subtract(summary.getTotalSocialInsurance());
	        		 }
	        		 //个人所得税
	        		 if(itemsMap.containsKey("222114")) {
	        			 voucherItems.add(createVoucherItemDto(bookId, itemsMap.get("222114"), summary.getBusinessSocialInsurance()));
	        			 creditYyckAmount = creditYyckAmount.subtract(summary.getBusinessSocialInsurance());
	        		 }

	        		 for (VoucherTemplateItem item : items) {
	        			 if(item.getSubjectCode().startsWith("1002")) {
	        				 voucherItems.add(createVoucherItemDto(bookId, item, creditYyckAmount));
	        			 }
	                 }
	        		 creditAmount = debitAmount;
	        	 }else if (voucherTemplate.getCode().startsWith("zf_shebao")){
		        	if(itemsMap.containsKey("122102")) {
		        		 //社保-个人
		        		 debitAmount = debitAmount.add(summary.getTotalSocialInsurance());
		        		 voucherItems.add(createVoucherItemDto(bookId, itemsMap.get("122102"), summary.getTotalSocialInsurance()));
		        	}
		        	if(itemsMap.containsKey("221103")) {
		        		 //社保-单位
		        		 debitAmount = debitAmount.add(summary.getBusinessSocialInsurance());
		        		 voucherItems.add(createVoucherItemDto(bookId, itemsMap.get("221103"), summary.getBusinessSocialInsurance()));
		        	}
	        		 for (VoucherTemplateItem item : items) {
	        			 if(item.getSubjectCode().startsWith("1002")) {
	        				 voucherItems.add(createVoucherItemDto(bookId, item, debitAmount));
	        			 }
	                 }
	        		 creditAmount = debitAmount;
	        	 }
        	}
        }else {
            for (VoucherTemplateItem item : items) {
                voucherItems.add(createVoucherItemDto(bookId, item, BigDecimal.ZERO));
            }
        }

        voucherChangeDto.setItems(voucherItems);
        //草稿阶段
        voucherChangeDto.setStatus(VoucherStatusEnum.DRAFT.getValue());
        log.debug("voucherChangeDto {}", voucherChangeDto);
        //保持凭证
        voucherService.save(voucherChangeDto);

        //结转记录
        SettlementCarryforward settlementCarryforward = new SettlementCarryforward();
        settlementCarryforward.setBookId(bookId);
        settlementCarryforward.setYear(year);
        settlementCarryforward.setYearPeriod(currentTerm);
        settlementCarryforward.setVoucherId(voucherChangeDto.getId());
        settlementCarryforward.setVoucherTemplateId(voucherTemplate.getId());
        //保存结转记录
        settlementCarryforwardMapper.insert(settlementCarryforward);
        //返回凭证ID编码
        return Message.ok(voucherChangeDto.getId());
    }

    private boolean addVoucherItems(String bookId, String subjectCode, List<VoucherItemChangeDto> items, VoucherTemplateItem templateItem) {
        List<BookSubject> subjectList = bookSubjectService.selectSubjectAndChild(bookId, subjectCode);
        for (BookSubject s : subjectList) {
            if (isLeafSubject(s, subjectList) && s.getBalance().compareTo(BigDecimal.ZERO) != 0) {
                items.add(createVoucherItemDtoBySubject(bookId, s, templateItem, s.getBalance().abs()));
            }
        }
        return true;
    }

    private boolean isLeafSubject(BookSubject subject, List<BookSubject> subjectList) {
        boolean isLeaf = true;
        //仅有一条数据
        if (subjectList.size() == 1) {
            return true;
        }
        //多条数据
        for (BookSubject s : subjectList) {
            //跳过自己
            if (subject.getCode().equals(s.getCode())) {
                continue;
            }
            //有节点以当前节点开头认为不是叶节点
            if (s.getCode().startsWith(subject.getCode())) {
                isLeaf = false;
                break;
            }
        }
        return isLeaf;
    }


    /**
     * Creates the voucher change dto with common fields
     */
    private VoucherChangeDto createVoucherChangeDto(Book book, String bookId, String wordHead,
                                                    Date voucherDate, Integer year, Integer month, BigDecimal amount) {

        Integer wordNum = voucherService.getAbleWordNum(bookId, wordHead, null, null).getData();

        VoucherChangeDto dto = new VoucherChangeDto();
        dto.setWordHead(wordHead);
        dto.setWordNum(wordNum);
        dto.setBookId(bookId);
        dto.setCompanyName(book.getCompanyName());
        dto.setVoucherDate(voucherDate);
        dto.setVoucherYear(year);
        dto.setVoucherMonth(month);
        dto.setDebitAmount(amount);
        dto.setCreditAmount(amount);

        return dto;
    }

    /**
     * Creates a voucher item dto based on rule and direction
     */
    private VoucherItemChangeDto createVoucherItemDto(String bookId,
                                                      VoucherTemplateItem item, BigDecimal amount) {
        BookSubject bookSubject = bookSubjectService.selectSubject(bookId, item.getSubjectCode());

        VoucherItemChangeDto itemDto = new VoucherItemChangeDto();
        itemDto.setSummary(item.getSummary());
        itemDto.setSubjectId(bookSubject.getId());
        if (item.getDirection() == 1) {
            itemDto.setDebitAmount(amount);
        } else {
            itemDto.setCreditAmount(amount);
        }
        itemDto.setSubjectBalance(bookSubject.getBalance());
        itemDto.setAuxiliary(List.of());
        itemDto.setSubjectCode(bookSubject.getCode());
        itemDto.setSubjectName(bookSubject.getCode() + "-" + bookSubject.getName());
        itemDto.setDetailedAccounts("");

        return itemDto;
    }

    /**
     * Creates a voucher item dto based on rule and direction
     */
    private VoucherItemChangeDto createVoucherItemDtoBySubject(String bookId, BookSubject bookSubject,
                                                               VoucherTemplateItem item, BigDecimal amount) {
        VoucherItemChangeDto itemDto = new VoucherItemChangeDto();
        itemDto.setSummary(item.getSummary());
        itemDto.setSubjectId(bookSubject.getId());
        if (item.getDirection() == 1) {
            itemDto.setDebitAmount(amount);
        } else {
            itemDto.setCreditAmount(amount);
        }
        itemDto.setSubjectBalance(bookSubject.getBalance());
        itemDto.setAuxiliary(List.of());
        itemDto.setSubjectCode(bookSubject.getCode());
        itemDto.setSubjectName(bookSubject.getCode() + "-" + bookSubject.getName());
        itemDto.setDetailedAccounts("");

        return itemDto;
    }


    @Override
    public Message<String> delete(String bookId, String voucherId) {
        LambdaQueryWrapper<SettlementCarryforward> carryLqw = Wrappers.lambdaQuery();
        carryLqw.eq(SettlementCarryforward::getBookId, bookId);
        carryLqw.eq(SettlementCarryforward::getVoucherId, voucherId);
        SettlementCarryforward settlementCarryforward = settlementCarryforwardMapper.selectOne(carryLqw);
        ArrayList<String> voucherIds = new ArrayList<>();
        voucherIds.add(settlementCarryforward.getVoucherId());
        voucherService.delete(voucherIds, bookId);

        settlementCarryforwardMapper.delete(carryLqw);
        return null;
    }

}
