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
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.surpass.entity.book.Book;
import com.surpass.entity.book.BookSubject;
import com.surpass.entity.hr.*;
import com.surpass.entity.voucher.VoucherTemplate;
import com.surpass.entity.voucher.VoucherTemplateItem;
import com.surpass.entity.voucher.dto.GenerateVoucherDto;
import com.surpass.entity.voucher.dto.VoucherChangeDto;
import com.surpass.entity.voucher.dto.VoucherItemChangeDto;
import com.surpass.enums.VoucherStatusEnum;
import com.surpass.persistence.mapper.*;
import com.surpass.persistence.service.VoucherService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.constants.ConstsHttpHeader;
import com.surpass.constants.ConstsUser;
import com.surpass.constants.ContentType;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.hr.dto.SalaryDetailChangeDto;
import com.surpass.entity.hr.dto.SalaryDetailPageDto;
import com.surpass.entity.hr.dto.SalarySummaryChangeDto;
import com.surpass.entity.PeriodStr;
import com.surpass.entity.hr.vo.TaxDeductionExportVo;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.service.BookSubjectService;
import com.surpass.persistence.service.ConfigSysService;
import com.surpass.persistence.service.EmployeeSalaryService;
import com.surpass.util.PeriodDateUtils;
import com.surpass.util.DateUtils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/2/20 11:59
 */

@Service
public class EmployeeSalaryServiceImpl extends ServiceImpl<EmployeeSalaryMapper, EmployeeSalary> implements EmployeeSalaryService {
	static final Logger logger = LoggerFactory.getLogger(EmployeeSalaryServiceImpl.class);
    @Autowired
    private EmployeeSalaryMapper employeeSalaryMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    VoucherTemplateMapper voucherTemplateMapper;

    @Autowired
    VoucherTemplateItemMapper voucherTemplateItemMapper;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    ConfigSysService configSysService;

    @Autowired
    BookSubjectService bookSubjectService;

    @Override
    public Message<Page<EmployeeSalary>> pageList(SalaryDetailPageDto dto) {

        Page<EmployeeSalary> employeeSalaryPage = employeeSalaryMapper.pageList(dto.build(), dto);

        return Message.ok(employeeSalaryPage);
    }

    @Override
    @Transactional
    public Message<String> update(SalaryDetailChangeDto dto) {
        EmployeeSalary employeeSalary = BeanUtil.copyProperties(dto, EmployeeSalary.class);
        boolean result = super.updateById(employeeSalary);

        return result ? Message.ok("修改成功") : Message.failed("修改失败");
    }

    @Override
    @Transactional
    public Message<String> save(SalaryDetailChangeDto dto) {
        EmployeeSalary employeeSalary = BeanUtil.copyProperties(dto, EmployeeSalary.class);
        boolean result = super.save(employeeSalary);

        return result ? Message.ok("新增成功") : Message.failed("新增失败");
    }

    @Override
    @Transactional
    public Message<String> delete(ListIdsDto dto) {
        List<String> ids = dto.getListIds();
        boolean result = super.removeBatchByIds(ids);
        return result ? new Message<>(Message.SUCCESS, "删除成功") : new Message<>(Message.FAIL, "删除失败");
    }

    @Override
    public EmployeeSalary getById(Serializable id) {
        EmployeeSalary employeeSalary = super.getById(id);
        if (Objects.nonNull(employeeSalary)) {
            Employee employee = employeeMapper.selectById(employeeSalary.getEmployeeId());
            if (Objects.nonNull(employee)) {
                employeeSalary.setBankCardNo(employee.getBankCardNo());
                employeeSalary.setEmployeeName(employee.getDisplayName());
                employeeSalary.setEmployeeNumber(employee.getEmployeeNumber());
                return employeeSalary;
            }
            throw new BusinessException(50001, "查询不到该条员工数据");
        }

        throw new BusinessException(50001, "查询不到该条数据");
    }

	@Override
	public EmployeeSalarySummary selectSalarySummary(SalarySummaryChangeDto dto) {
		return employeeSalaryMapper.selectSalarySummary(dto);
	}

	@Override
    public Message<String> exportTaxItems(SalaryDetailPageDto dto, HttpServletResponse response) {
        List<TaxDeductionExportVo> taxDeductionExportVos = employeeSalaryMapper.exportGetSalaryDetail(dto);
        if (ObjectUtils.isEmpty(taxDeductionExportVos)) {
            throw new BusinessException(500001, "暂无数据");
        }
        Workbook workbook = null;
        try {
            String belongDate = dto.getBelongDate();
            // 解析 belongDate 为 Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M");
            Date date;

            date = sdf.parse(belongDate);
            // 计算 startPeriod 和 endPeriod
            PeriodStr period = PeriodDateUtils.convertToPeriod(date);
            String startPeriod = period.getStartPeriodStr();
            String endPeriod = period.getEndPeriodStr();

            if (taxDeductionExportVos.size() > 5000) {
                // 比如设置最大内存量为5000行， new SXSSFWookbook(5000)，
                // 当行数达到 5000 时，把内存持久化写到文件中，以此逐步写入，避免OOM。解决了大数据下导出的问题
                workbook = new SXSSFWorkbook(5000);
            } else {
                workbook = new XSSFWorkbook();
            }
            int rowCount = 0;
            //创建sheet
            Sheet sheet = workbook.createSheet(dto.getBelongDate() + "_正常工资薪金所得");
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue("Surpass Salary Export " + DateUtils.getCurrentDateTimeAsString());
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 30));
            // 2. 创建数据格式（保留两位小数）
            CellStyle cellStyle = workbook.createCellStyle();
            DataFormat dataFormat = workbook.createDataFormat();
            cellStyle.setDataFormat(dataFormat.getFormat("0.00"));
            //构建头
            row = sheet.createRow(rowCount++);
            int headerColumn = 0;
            row.createCell(headerColumn++).setCellValue("工号");
            row.createCell(headerColumn++).setCellValue("姓名");
            row.createCell(headerColumn++).setCellValue("证件类型");
            row.createCell(headerColumn++).setCellValue("证件号码");
            row.createCell(headerColumn++).setCellValue("所得期间起");
            row.createCell(headerColumn++).setCellValue("所得期间止");
            row.createCell(headerColumn++).setCellValue("本期收入");
            row.createCell(headerColumn++).setCellValue("本期免税收入");
            row.createCell(headerColumn++).setCellValue("基本养老保险费");
            row.createCell(headerColumn++).setCellValue("基本医疗保险费");
            row.createCell(headerColumn++).setCellValue("失业保险费");
            row.createCell(headerColumn++).setCellValue("住房公积金");
            row.createCell(headerColumn++).setCellValue("累计子女教育");
            row.createCell(headerColumn++).setCellValue("累计继续教育");
            row.createCell(headerColumn++).setCellValue("大病医疗");
            row.createCell(headerColumn++).setCellValue("累计住房贷款利息");
            row.createCell(headerColumn++).setCellValue("累计住房租金");
            row.createCell(headerColumn++).setCellValue("累计赡养老人");
            row.createCell(headerColumn++).setCellValue("累计3岁以下婴幼儿照护");
            row.createCell(headerColumn++).setCellValue("累计个人养老金");
            row.createCell(headerColumn++).setCellValue("企业(职业)年金");
            row.createCell(headerColumn++).setCellValue("商业健康保险");
            row.createCell(headerColumn++).setCellValue("税延养老保险");
            row.createCell(headerColumn++).setCellValue("其他");
            row.createCell(headerColumn++).setCellValue("准予扣除的捐赠额");
            row.createCell(headerColumn++).setCellValue("税前扣除项目合计");
            row.createCell(headerColumn++).setCellValue("减免税额");
            row.createCell(headerColumn++).setCellValue("减除费用标准");
            row.createCell(headerColumn++).setCellValue("已缴税额");
            row.createCell(headerColumn++).setCellValue("备注");

            for (TaxDeductionExportVo exportVo: taxDeductionExportVos) {
            	//导出正式员工 、实习生、退休返聘
            	if(exportVo.getEmployeeType().equalsIgnoreCase(ConstsUser.EMPLOYEE_TYPE.NORMAL)
            			||exportVo.getEmployeeType().equalsIgnoreCase(ConstsUser.EMPLOYEE_TYPE.INTERN)
            			||exportVo.getEmployeeType().equalsIgnoreCase(ConstsUser.EMPLOYEE_TYPE.RETIREMENT)) {
	                row = sheet.createRow(rowCount++);
	                int column = 0;
	                row.createCell(column++).setCellValue(exportVo.getEmployeeNumber());
	                row.createCell(column++).setCellValue(exportVo.getDisplayName());
	                row.createCell(column++).setCellValue(exportVo.getIdCardType());
	                row.createCell(column++).setCellValue(exportVo.getIdCardNo());
	                row.createCell(column++).setCellValue(startPeriod);
	                row.createCell(column++).setCellValue(endPeriod);
	                // 3. 统一格式化所有数值字段
	                double[] values = {
	                        exportVo.getIncome().doubleValue(),
	                        exportVo.getTaxFreeIncome().doubleValue(),
	                        exportVo.getInsuranceEndowment().doubleValue(),
	                        exportVo.getInsuranceMedical().doubleValue(),
	                        exportVo.getInsuranceUnemployment().doubleValue(),
	                        exportVo.getHousingProvidentFund().doubleValue(),
	                        exportVo.getEducation().doubleValue(),
	                        exportVo.getContinuingEducation().doubleValue(),
	                        exportVo.getMedical().doubleValue(),
	                        exportVo.getHousingLoan().doubleValue(),
	                        exportVo.getRent().doubleValue(),
	                        exportVo.getElderlyCare().doubleValue(),
	                        exportVo.getInfantsCare().doubleValue(),
	                        exportVo.getIndividualPension().doubleValue(),
	                        exportVo.getEnterprisePension().doubleValue(),
	                        exportVo.getCommercialHealth().doubleValue(),
	                        exportVo.getDeferredPension().doubleValue(),
	                        exportVo.getOthers().doubleValue(),
	                        exportVo.getDonationAllowed().doubleValue(),
	                        exportVo.getTotalPreTaxDeduction().doubleValue(),
	                        exportVo.getTaxDeductions().doubleValue(),
	                        exportVo.getDeductingStandards().doubleValue(),
	                        exportVo.getPaidTax().doubleValue()
	                };
	                // 4. 统一创建单元格并应用格式
	                for (int j = 0; j < values.length; j++) {
	                    Cell cell = row.createCell(column++ + j); // 从第6列开始
	                    cell.setCellValue(values[j]);
	                    cell.setCellStyle(cellStyle);
	                }
	                row.createCell(column++).setCellValue(exportVo.getRemark());
	            }
            }
            String fileName = "salary-" + belongDate;
            fileName = URLEncoder.encode(fileName, "UTF8");
            response.setContentType(ContentType.APPLICATION_MS_EXCEL);
            response.setHeader(ConstsHttpHeader.CONTENT_DISPOSITION, ConstsHttpHeader.ATTACHMENT_FILE.formatted(fileName));
            ServletOutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("error:", e);
        } finally {
            if (Objects.nonNull(workbook)) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    logger.error("error close ", e);
                }
            }
        }
        return null;
    }

    @Override
    @Transactional
    public Message<String> generateVoucher(GenerateVoucherDto dto) {
        String bookId = dto.getBookId();
        Book book = bookMapper.selectById(bookId);
        Integer voucherType = dto.getVoucherType();
        EmployeeSalary salary = super.getById(dto.getId());
        String tplCode = (voucherType == 2 ? "fp_lwf" : "zf_lwf");
        if (voucherType == 2 && StringUtils.isNotBlank(salary.getAccrualVoucherId())) {
            return Message.ok("收票凭证已生成");
        } else if (voucherType == 3 && StringUtils.isNotBlank(salary.getSalaryVoucherId())) {
            return Message.ok("发放凭证已生成");
        }

        String currentTerm = configSysService.getCurrentTerm(bookId);

        int year = Integer.parseInt(currentTerm.split("-")[0]);
        int month = Integer.parseInt(currentTerm.split("-")[1]);

        LambdaQueryWrapper<VoucherTemplate> itemTpl = Wrappers.lambdaQuery();
        itemTpl.eq(VoucherTemplate::getRelatedId, bookId);
        itemTpl.eq(VoucherTemplate::getCode, tplCode);
        itemTpl.eq(VoucherTemplate::getDeleted, "n");

        VoucherTemplate voucherTemplate = voucherTemplateMapper.selectOne(itemTpl);
        if(voucherTemplate == null) {
        	return Message.failed("凭证模板["+tplCode+"]未设置！");
        }

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
        	voucherDate = DateUtils.parse(voucherDateString, DateUtils.FORMAT_DATE_YYYY_MM_DD);
        }

        logger.debug("voucherTemplate {}", voucherTemplate);
        LambdaQueryWrapper<VoucherTemplateItem> itemLqw = Wrappers.lambdaQuery();
        itemLqw.eq(VoucherTemplateItem::getRelatedId, voucherTemplate.getRelatedId());
        itemLqw.eq(VoucherTemplateItem::getTemplateId, voucherTemplate.getId());
        List<VoucherTemplateItem> items = voucherTemplateItemMapper.selectList(itemLqw);

        BigDecimal debitAmount = BigDecimal.ZERO;
        BigDecimal creditAmount = BigDecimal.ZERO;

        List<VoucherItemChangeDto> voucherItems = new ArrayList<>();
        Map<String, VoucherTemplateItem> itemsMap = new HashMap<>();
        for (VoucherTemplateItem item : items) {
            itemsMap.put(item.getSubjectCode(), item);
        }

        if (voucherTemplate.getCode().equals("fp_lwf")) {//收发票
        	if(itemsMap.containsKey("660222")) {
	       		 //劳务费
	       		 debitAmount = debitAmount.add(salary.getPayAmount());
	       		 voucherItems.add(createVoucherItemDto(bookId, itemsMap.get("660222"), salary.getPayAmount()));
        	}
        	if(itemsMap.containsKey("222114")) {
	       		 //个税
        		creditAmount = creditAmount.add(salary.getPersonalTax());
	       		 voucherItems.add(createVoucherItemDto(bookId, itemsMap.get("222114"), salary.getPersonalTax()));
        	}
        	if(itemsMap.containsKey("224101")) {
	       		 //应付个人
        		 creditAmount = creditAmount.add(salary.getTotalAmount());
	       		 voucherItems.add(createVoucherItemDto(bookId, itemsMap.get("224101"), salary.getTotalAmount()));
        	}
        }else if (voucherTemplate.getCode().equals("zf_lwf")) {//发放劳务费
        	if(itemsMap.containsKey("224101")) {
	       		 //发放金额
	       		 debitAmount = debitAmount.add(salary.getTotalAmount());
	       		 voucherItems.add(createVoucherItemDto(bookId, itemsMap.get("224101"), salary.getTotalAmount()));
        	}

	   		for (VoucherTemplateItem item : items) {
	   			 if(item.getSubjectCode().startsWith("1002")) {
	   				 voucherItems.add(createVoucherItemDto(bookId, item, debitAmount));
	   			 }
	         }
	   		creditAmount = debitAmount;
        }

        Employee employee = employeeMapper.selectById(salary.getEmployeeId());

        VoucherChangeDto voucherChangeDto = createVoucherChangeDto(book, bookId, voucherDate, year, month, debitAmount);
        voucherChangeDto.setRemark(voucherTemplate.getRemark().replace("{yyyy}", year + "").replace("{mm}", month + "").replace("{name}", employee.getDisplayName()));
        voucherChangeDto.setItems(voucherItems);
        voucherChangeDto.setStatus(VoucherStatusEnum.DRAFT.getValue());

        voucherService.save(voucherChangeDto);

        LambdaUpdateWrapper<EmployeeSalary> updateWrapper = new LambdaUpdateWrapper<>();
        if (voucherType == 0 || voucherType == 2) {
            updateWrapper.set(EmployeeSalary::getAccrualVoucherId, voucherChangeDto.getId());
        } else if (voucherType == 1 || voucherType == 3) {
            updateWrapper.set(EmployeeSalary::getSalaryVoucherId, voucherChangeDto.getId());
        }
        updateWrapper.eq(EmployeeSalary::getId, dto.getId());
        super.update(updateWrapper);

        return Message.ok(voucherChangeDto.getId());
    }

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

    private VoucherChangeDto createVoucherChangeDto(Book book, String bookId,
                                                    Date voucherDate, Integer year, Integer month, BigDecimal amount) {

        Integer wordNum = voucherService.getAbleWordNum(bookId, "记", null, null).getData();

        VoucherChangeDto dto = new VoucherChangeDto();
        dto.setWordHead("记");
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

	@Override
	public Message<String> deleteVoucher(GenerateVoucherDto dto) {
        Integer voucherType = dto.getVoucherType();
        EmployeeSalary salary = super.getById(dto.getId());
        LambdaUpdateWrapper<EmployeeSalary> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(EmployeeSalary::getId, dto.getId());
        List<String> ids = new ArrayList<>();
        if(voucherType.equals(2)) {
        	updateWrapper.set(EmployeeSalary::getAccrualVoucherId, "");
        	ids.add(salary.getAccrualVoucherId());
        }else if(voucherType.equals(3)) {
        	updateWrapper.set(EmployeeSalary::getSalaryVoucherId, "");
        	ids.add(salary.getSalaryVoucherId());
        }
        voucherService.delete(ids, salary.getBookId());
        super.update(updateWrapper);

		return Message.ok("删除成功！");
	}
}
