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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.surpass.constants.ConstsSysConfig;
import com.surpass.entity.Message;
import com.surpass.entity.base.BookInitBalance;
import com.surpass.entity.book.Book;
import com.surpass.entity.book.Settlement;
import com.surpass.entity.config.ConfigCashFlowBalance;
import com.surpass.entity.statement.*;
import com.surpass.entity.statement.dto.StatementParamsDto;
import com.surpass.entity.statement.vo.StatementIncomeExport;
import com.surpass.entity.voucher.Voucher;
import com.surpass.entity.voucher.dto.VoucherItemPageDto;
import com.surpass.entity.voucher.vo.VoucherItemVo;
import com.surpass.enums.CashFlowItemEnum;
import com.surpass.enums.VoucherStatusEnum;
import com.surpass.enums.YesNoEnum;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.*;
import com.surpass.persistence.service.*;
import com.surpass.util.excel.ExcelDataModeEnum;
import com.surpass.util.excel.ExcelExporter;
import com.surpass.util.excel.ExcelParams;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StatementReportServiceImpl implements StatementReportService {

    private final BookSubjectMapper bookSubjectMapper;
    private final StatementSubjectBalanceMapper subjectBalanceMapper;
    private final StatementSubjectBalanceService subjectBalanceService;
    private final ConfigSysService configSysService;
    private final ConfigCashFlowBalanceMapper configCashFlowBalanceMapper;
    private final StatementIncomeMapper statementIncomeMapper;
    private final StatementIncomeItemMapper statementIncomeItemMapper;
    private final StatementCashFlowMapper statementCashFlowMapper;
    private final SettlementMapper settlementMapper;
    private final BookInitBalanceMapper bookInitBalanceMapper;
    private final BookMapper bookMapper;
    private final VoucherMapper voucherMapper;
    private final VoucherItemMapper voucherItemMapper;

    @Override
    public BigDecimal getEndingBalance(StatementParamsDto dto) {
        dto.parse();
        List<StatementCashFlow> data = cashFlowStatement(dto).getData();

        return data.stream()
                .filter(item -> "38-xj-qmye".equals(item.getItemCode()))
                .map(StatementCashFlow::getMonthlyAmount)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void cashFlowExport(StatementParamsDto dto, HttpServletResponse response) throws IOException {
        dto.parse();
        List<StatementCashFlow> cashFlows = cashFlowStatement(dto).getData();
        Book book = bookMapper.selectById(dto.getBookId());
        String templatePath = ResourceUtils
                .getURL("classpath:static/export-template/template-cash-flow.xlsx")
                .getPath();
        // 注意：Windows下getPath()前会带'/'，可做处理
        if (templatePath.startsWith("/")) {
            templatePath = templatePath.substring(1);
        }
        Path tempFilePath = Files.createTempFile("template-cash-flow_", ".xlsx");
        File tempFile = tempFilePath.toFile();

        List<StatementIncomeExport.Item> items = new ArrayList<>();
        cashFlows.forEach(item -> items.add(StatementIncomeExport.Item.builder()
                .itemName(item.getItemName())
                .rowNum(item.getSortIndex())
                .currentBalance(item.getMonthlyAmount())
                .yearBalance(item.getCurrentAmount())
                .build()));
        StatementIncomeExport data = StatementIncomeExport.builder()
                .companyName(book.getCompanyName())
                .date(dto.getReportDate())
                .items(items)
                .build();

        // 单项数据渲染
        ExcelParams<StatementIncomeExport> paramsObj = ExcelParams.<StatementIncomeExport>builder()
                .httpResponse(null)
                .mode(ExcelDataModeEnum.base_attribute)
                .dataModel(data)
                .outputDirectory(tempFile.getParent()) // 临时目录路径
                .outputFileName(tempFile.getName())    // 临时文件名
                .enableMergeCells(false)
                .autoSizeColumns(false)
                .recalculateFormulas(true)
                .templateFilePath(templatePath)
                .build();
        ExcelExporter.export(paramsObj);
        // 列表数据渲染
        ExcelParams<List<StatementIncomeExport.Item>> paramsList = ExcelParams.<List<StatementIncomeExport.Item>>builder()
                .httpResponse(response)
                .mode(ExcelDataModeEnum.include_list)
                .dataModel(items)
                .enableMergeCells(false)
                .autoSizeColumns(false)
                .recalculateFormulas(true)
                .templateFilePath(tempFile.getPath())
                .build();

        ExcelExporter.export(paramsList);
        // 最后删除临时文件
        if (tempFile.exists()) tempFile.delete();
    }

    /**
     * 科目余额表导出
     */
    @Override
    public void subjectBalanceExport(StatementParamsDto dto, HttpServletResponse response) throws IOException {
        dto.parse();
        List<StatementSubjectBalance> subjectBalances = subjectBalance(dto).getData();
        Book book = bookMapper.selectById(dto.getBookId());
        String templatePath = ResourceUtils
                .getURL("classpath:static/export-template/template-subject-balance.xlsx")
                .getPath();
        // 注意：Windows下getPath()前会带'/'，可做处理
        if (templatePath.startsWith("/")) {
            templatePath = templatePath.substring(1);
        }
        Path tempFilePath = Files.createTempFile("template-subject-balance_", ".xlsx");
        File tempFile = tempFilePath.toFile();

        Map<String, Object> data = new HashMap<>();
        data.put("date", dto.getReportDate());
        data.put("bookName", book.getName());

        // 单项数据渲染
        ExcelParams<Map<String, Object>> paramsObj = ExcelParams.<Map<String, Object>>builder()
                .httpResponse(null)
                .mode(ExcelDataModeEnum.base_attribute)
                .dataModel(data)
                .outputDirectory(tempFile.getParent()) // 临时目录路径
                .outputFileName(tempFile.getName())    // 临时文件名
                .enableMergeCells(false)
                .autoSizeColumns(false)
                .recalculateFormulas(true)
                .templateFilePath(templatePath)
                .build();
        ExcelExporter.export(paramsObj);
        // 列表数据渲染
        ExcelParams<List<StatementSubjectBalance>> paramsList = ExcelParams.<List<StatementSubjectBalance>>builder()
                .httpResponse(response)
                .mode(ExcelDataModeEnum.include_list)
                .dataModel(subjectBalances)
                .enableMergeCells(false)
                .autoSizeColumns(false)
                .recalculateFormulas(true)
                .templateFilePath(tempFile.getPath())
                .build();

        ExcelExporter.export(paramsList);
        // 最后删除临时文件
        if (tempFile.exists()) tempFile.delete();
    }

    /**
     * 凭证汇总表导出
     */
    @Override
    public void voucherSummaryExport(StatementParamsDto dto, HttpServletResponse response) throws IOException {
        dto.parse();
        List<StatementSubjectBalance> subjectBalances = voucherSummary(dto).getData();
        Book book = bookMapper.selectById(dto.getBookId());
        QueryWrapper<Voucher> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .select("SUM(receipt_num) AS files", "COUNT(*) AS total")
                .ge("voucher_date", dto.getDateRangeStart())
                .le("voucher_date", dto.getDateRangeEnd())
                .eq("book_id", dto.getBookId())
                .eq("status", VoucherStatusEnum.COMPLETED.getValue());
        Map<String, Object> result = voucherMapper.selectMaps(queryWrapper).get(0);
        String voucherNumber = result.get("total").toString();
        String voucherFileNum = result.get("files").toString();

        String templatePath = ResourceUtils
                .getURL("classpath:static/export-template/template-voucher-summary.xlsx")
                .getPath();
        // 注意：Windows下getPath()前会带'/'，可做处理
        if (templatePath.startsWith("/")) {
            templatePath = templatePath.substring(1);
        }
        Path tempFilePath = Files.createTempFile("template-voucher-summary_", ".xlsx");
        File tempFile = tempFilePath.toFile();

        Map<String, Object> data = new HashMap<>();
        data.put("voucherInfo", "凭证字: (所有) #凭证总张数: " + voucherNumber + "张 附件总张数: " + voucherFileNum + "张");
        data.put("date", dto.getDateRangeStart() + "至" + dto.getDateRangeEnd());
        data.put("bookName", book.getName());

        // 单项数据渲染
        ExcelParams<Map<String, Object>> paramsObj = ExcelParams.<Map<String, Object>>builder()
                .httpResponse(null)
                .mode(ExcelDataModeEnum.base_attribute)
                .dataModel(data)
                .outputDirectory(tempFile.getParent()) // 临时目录路径
                .outputFileName(tempFile.getName())    // 临时文件名
                .enableMergeCells(false)
                .autoSizeColumns(false)
                .recalculateFormulas(true)
                .templateFilePath(templatePath)
                .build();
        ExcelExporter.export(paramsObj);
        // 列表数据渲染
        ExcelParams<List<StatementSubjectBalance>> paramsList = ExcelParams.<List<StatementSubjectBalance>>builder()
                .httpResponse(response)
                .mode(ExcelDataModeEnum.include_list)
                .dataModel(subjectBalances)
                .enableMergeCells(false)
                .autoSizeColumns(false)
                .recalculateFormulas(true)
                .templateFilePath(tempFile.getPath())
                .build();

        ExcelExporter.export(paramsList);
        // 最后删除临时文件
        if (tempFile.exists()) tempFile.delete();
    }

    private Map<String, Boolean> checkTermIfSameYear(StatementParamsDto dto) {
        //是否为当前年份
        int currentYear = Integer.parseInt(dto.getDateRangeStart().substring(0, 4));
        String startDate = configSysService.selectConfigByKey(dto.getBookId(), ConstsSysConfig.SYS_PAYMENT_TERM_START);
        int startYear = Integer.parseInt(startDate.substring(0, 4));
        Map<String, Boolean> map = new HashMap<>();

        map.put("isSameYear", currentYear == startYear);
        map.put("isSameMonth", dto.getDateRangeStart().substring(0, 7).equals(startDate));


        return map;
    }

    @Override
    public Message<List<StatementCashFlow>> cashFlowStatement(StatementParamsDto dto) {
        dto.parse();

        //判断是否是起始账套年份
        Map<String, Boolean> stringBooleanMap = checkTermIfSameYear(dto);
        Boolean isSameYear = stringBooleanMap.get("isSameYear");
        Boolean isSameMonth = stringBooleanMap.get("isSameMonth");

        LambdaQueryWrapper<ConfigCashFlowBalance> configCashFlowBalanceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        configCashFlowBalanceLambdaQueryWrapper.eq(ConfigCashFlowBalance::getBookId, dto.getBookId());
        configCashFlowBalanceLambdaQueryWrapper.orderByAsc(ConfigCashFlowBalance::getSortIndex);
        //获取项目名称
        List<ConfigCashFlowBalance> configCashFlowBalances = configCashFlowBalanceMapper.selectList(configCashFlowBalanceLambdaQueryWrapper);
        if (configCashFlowBalances.isEmpty()) {
            throw new BusinessException(500001, "请先生成现金流量初始余额配置");
        }

        //根据全年获取指定项目金额
        VoucherItemPageDto voucherItemPageDtoYear = new VoucherItemPageDto();
        voucherItemPageDtoYear.setYear(Integer.parseInt(dto.getReportDate().substring(0, 4)));
        voucherItemPageDtoYear.setBookId(dto.getBookId());
        if ("month".equals(dto.getPeriodType())) {
            voucherItemPageDtoYear.setEndMonth(dto.getMonth());
        } else if ("quarter".equals(dto.getPeriodType())) {
            voucherItemPageDtoYear.setEndQuarter(dto.getQuarter());
        }
        List<VoucherItemVo> voucherItemVoYears = statementCashFlowMapper.fetchByCashFlowAccumulated(voucherItemPageDtoYear);
        //获取特定科目
        List<StatementCashFlow> statementCashFlowsSpecifyYears = statementCashFlowMapper.fetchSpecifyCashFlow(voucherItemPageDtoYear);
        Map<String, BigDecimal> stringBigDecimalMapSpecifyYears = generateSpecifyCashFlowBalance(statementCashFlowsSpecifyYears);
        Map<String, BigDecimal> stringBigDecimalMapYear = generateCashFlowBalance(voucherItemVoYears);
        stringBigDecimalMapYear.putAll(stringBigDecimalMapSpecifyYears);


        //根据时间范围获取指定项目金额
        VoucherItemPageDto voucherItemPageDto = new VoucherItemPageDto();
        voucherItemPageDto.setBookId(dto.getBookId());
        voucherItemPageDto.setYear(dto.getYear());
        voucherItemPageDto.setMonth(dto.getMonth());
        voucherItemPageDto.setQuarter(dto.getQuarter());
        List<VoucherItemVo> voucherItemVos = statementCashFlowMapper.fetchByCashFlow(voucherItemPageDto);
        //获取特定科目
        List<StatementCashFlow> statementCashFlowsSpecify = statementCashFlowMapper.fetchSpecifyCashFlow(voucherItemPageDto);

        Map<String, BigDecimal> stringBigDecimalMapSpecify = generateSpecifyCashFlowBalance(statementCashFlowsSpecify);
        Map<String, BigDecimal> stringBigDecimalMap = generateCashFlowBalance(voucherItemVos);

        stringBigDecimalMap.putAll(stringBigDecimalMapSpecify);

        //获取利润表净利润
        // 查出所有数据
        LambdaQueryWrapper<StatementIncome> lqw = Wrappers.lambdaQuery();
        lqw.eq(StatementIncome::getYearPeriod, dto.getReportDate());
        lqw.eq(StatementIncome::getPeriodType, dto.getPeriodType());
        lqw.eq(StatementIncome::getBookId, dto.getBookId());
        StatementIncome statementIncome = statementIncomeMapper.selectOne(lqw);

        if (statementIncome != null) {
            LambdaQueryWrapper<StatementIncomeItem> itemlqw = Wrappers.lambdaQuery();
            itemlqw.eq(StatementIncomeItem::getBookId, statementIncome.getBookId());
            itemlqw.eq(StatementIncomeItem::getIncomeId, statementIncome.getId());
            //净利润编码
            itemlqw.eq(StatementIncomeItem::getItemCode, 4);
            StatementIncomeItem statementIncomeItem = statementIncomeItemMapper.selectList(itemlqw).get(0);
            if (Objects.nonNull(statementIncomeItem)) {
                stringBigDecimalMapYear.put("41-xj-jlr", statementIncomeItem.getCumulativeBalance());
                stringBigDecimalMap.put("41-xj-jlr", statementIncomeItem.getCurrentBalance());
            }
        }

        //生成报表
        List<StatementCashFlow> statementCashFlows = getStatementCashFlows(configCashFlowBalances, stringBigDecimalMapYear,
                stringBigDecimalMap, isSameYear, isSameMonth, dto.getBookId(), dto.getDateRangeStart().substring(0, 7));

        return Message.ok(statementCashFlows);
    }

    /**
     * 科目余额报表
     *
     * @param dto 查询参数
     * @return 结果
     */
    @Override
    public Message<List<StatementSubjectBalance>> subjectBalance(StatementParamsDto dto) {
        dto.parse();
        String currentTerm = configSysService.selectConfigByKey(dto.getBookId(), ConstsSysConfig.SYS_PAYMENT_TERM_CURRENT);
        List<String> allMonths = dto.getAllMonths(currentTerm);

        List<StatementSubjectBalance> res = null;
        if (allMonths.size() > 1) {
            res = subjectBalanceMapper.groupCodeSubjectBalance(dto, allMonths,allMonths.get(0), allMonths.get(allMonths.size() - 1));
        } else {
            LambdaQueryWrapper<StatementSubjectBalance> lqw = Wrappers.lambdaQuery();
            lqw.in(StatementSubjectBalance::getYearPeriod, allMonths);
            lqw.eq(StatementSubjectBalance::getBookId, dto.getBookId());
            lqw.eq(!dto.getShowAll(), StatementSubjectBalance::getIsVoucher, YesNoEnum.y.name());
            res = subjectBalanceMapper.selectList(lqw);
        }

//        // 拉取父级数据
//        List<String> subjectIds = new ArrayList<>(res.stream().map(StatementSubjectBalance::getSourceId).toList());
//        List<String> parentIds = res.stream().filter(item -> item.getIsAuxiliary().equals(YesNoEnum.y.name()))
//                .map(StatementSubjectBalance::getParentId).toList();
//        subjectIds.addAll(parentIds);
//        if (!subjectIds.isEmpty()) {
//            LambdaQueryWrapper<BookSubject> lqwSubject = Wrappers.lambdaQuery();
//            lqwSubject.eq(BookSubject::getBookId, dto.getBookId());
//            List<BookSubject> bookSubjects = bookSubjectMapper.selectList(lqwSubject);
//            // 找出所有父级
//            List<String> subjectPaths = new ArrayList<>();
//            bookSubjects.forEach(bookSubject -> {
//                for (String subjectId : subjectIds) {
//                    if (bookSubject.getIdPath().contains(subjectId)) {
//                        subjectPaths.addAll(List.of(bookSubject.getIdPath().split("/")));
//                    }
//                }
//            });
//            // 创建父级
//            List<StatementSubjectBalance> balanceList = bookSubjects.stream()
//                    .filter(bookSubject -> subjectPaths.contains(bookSubject.getId()))
//                    .map(subject -> subjectBalanceService.create(subject, dto.getReportDate()))
//                    .toList();
//            // 合并
//            Set<String> existingSourceIds = res.stream().map(StatementSubjectBalance::getSourceId).collect(Collectors.toSet());
//            for (StatementSubjectBalance item : balanceList) {
//                if (!existingSourceIds.contains(item.getSourceId())) {
//                    res.add(item);
//                }
//            }
//        }
//        counterBalance(res);

        // 按照科目编号升序排列
        res.sort(Comparator.comparing(StatementSubjectBalance::getSubjectCode));

//        if (!dto.getShowAux()) {
//            res = res.stream().filter(item -> item.getIsAuxiliary().equals(YesNoEnum.n.name())).toList();
//        }
        return new Message<>(res);
    }

    private List<StatementSubjectBalance> generalLedgerBasic(StatementParamsDto dto) {
        dto.parse();
        String currentTerm = configSysService.selectConfigByKey(dto.getBookId(), ConstsSysConfig.SYS_PAYMENT_TERM_CURRENT);
        List<String> allMonths = dto.getAllMonths(currentTerm);
        return subjectBalanceMapper.getGeneralLedger(dto, allMonths,allMonths.get(0), allMonths.get(allMonths.size() - 1));
    }


    @Override
    public Message<Map<String, List<StatementSubjectBalance>>> generalLedger(StatementParamsDto dto) {
        List<StatementSubjectBalance> res = generalLedgerBasic(dto);

        Map<String, List<StatementSubjectBalance>> grouped = res.stream()
                .collect(Collectors.groupingBy(StatementSubjectBalance::getSubjectCode));


        return new Message<>(grouped);
    }

    @Override
    public Message<List<StatementSubjectBalance>> tAccount(StatementParamsDto dto) {
        dto.parse();
        String currentTerm = configSysService.selectConfigByKey(dto.getBookId(), ConstsSysConfig.SYS_PAYMENT_TERM_CURRENT);
        List<String> allMonths = dto.getAllMonths(currentTerm);

        // 获取期初余额
        List<StatementSubjectBalance> openingBalance = subjectBalanceMapper.getOpeningBalance(dto, allMonths.get(0));

        // 获取科目余额表的期末余额
        List<StatementSubjectBalance> closingBalance = subjectBalanceMapper.getClosingBalance(dto, allMonths.get(allMonths.size() - 1));

        // 获取会计分录明细记录
        List<StatementSubjectBalance> tAccountDetails = subjectBalanceMapper.getTAccountDetails(dto, allMonths, allMonths.get(0), allMonths.get(allMonths.size() - 1));

        // 合并所有数据返回给前端
        List<StatementSubjectBalance> result = new ArrayList<>();

        // 1. 添加期初余额记录（标记类型为期初）
        for (StatementSubjectBalance opening : openingBalance) {
            if (opening.getDebit().compareTo(BigDecimal.ZERO) != 0 ||
                    opening.getCredit().compareTo(BigDecimal.ZERO) != 0) {
                opening.setRecordType("opening"); // 标记为期初余额
                opening.setVoucherNumber("期初");
                opening.setVoucherDate(allMonths.get(0) + "-01"); // 设置为期初日期
                result.add(opening);
            }
        }

        // 2. 添加分录明细记录（标记类型为明细）
        for (StatementSubjectBalance detail : tAccountDetails) {
            detail.setRecordType("detail"); // 标记为分录明细
            result.add(detail);
        }

        // 3. 添加期末余额记录（标记类型为期末，用于前端平衡检查）
        for (StatementSubjectBalance closing : closingBalance) {
            closing.setRecordType("closing"); // 标记为期末余额
            closing.setVoucherNumber("期末");
            closing.setVoucherDate(allMonths.get(allMonths.size() - 1) + "-" +
                    String.format("%02d", Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH))); // 设置为期末日期
            closing.setSummary("期末余额");
            result.add(closing);
        }

        return Message.ok(result);
    }

    /**
     * 报表-凭证汇总
     *
     * @param dto 查询参数
     * @return 结果
     */
    @Override
    public Message<List<StatementSubjectBalance>> voucherSummary(StatementParamsDto dto) {
        dto.parse();
        List<StatementSubjectBalance> res = voucherItemMapper.voucherSubjectBalanceSummary(dto);

        return Message.ok(res);
    }

    /**
     * @Description: 生成报表数据
     * @Param: [configCashFlowBalances]
     * @return: java.util.List<com.surpass.entity.statement.StatementCashFlow>
     * @Author: xZen
     * @Date: 2025/4/3 17:28
     */
    private List<StatementCashFlow> getStatementCashFlows(List<ConfigCashFlowBalance> configCashFlowBalances, Map<String, BigDecimal> stringBigDecimalMapYear,
                                                          Map<String, BigDecimal> stringBigDecimalMap,
                                                          Boolean sameYear, Boolean isSameMonth, String bookId, String reportDate) {
        List<StatementCashFlow> statementCashFlows = new ArrayList<>();
        for (ConfigCashFlowBalance configCashFlowBalance : configCashFlowBalances) {
            StatementCashFlow statementCashFlow = new StatementCashFlow();
            statementCashFlow.setItemName(configCashFlowBalance.getItemName());
            statementCashFlow.setSortIndex(configCashFlowBalance.getSortIndex());
            statementCashFlow.setMonthlyAmount(stringBigDecimalMap.get(configCashFlowBalance.getItemCode()));
            if (sameYear) {
                statementCashFlow.setCurrentAmount(
                        Optional.ofNullable(stringBigDecimalMapYear.get(configCashFlowBalance.getItemCode()))
                                .orElse(BigDecimal.ZERO)
                                .add(configCashFlowBalance.getBalance())
                );
            } else {
                statementCashFlow.setCurrentAmount(stringBigDecimalMapYear.get(configCashFlowBalance.getItemCode()));
            }
            statementCashFlow.setItemCode(configCashFlowBalance.getItemCode());
            statementCashFlow.setIsTitle(configCashFlowBalance.getIsTitle());
            statementCashFlow.setIsAdditional(configCashFlowBalance.getIsAdditional());
            statementCashFlow.setIsMain(configCashFlowBalance.getIsMain());
            statementCashFlow.setIsResult(configCashFlowBalance.getIsResult());
            statementCashFlows.add(statementCashFlow);
        }

        //设置期初余额
        //获取科目初始余额
        LambdaQueryWrapper<BookInitBalance> wrapperBookInit = new LambdaQueryWrapper<>();
        wrapperBookInit.eq(BookInitBalance::getBookId, bookId);
        wrapperBookInit.eq(BookInitBalance::getLevel, 1);
        wrapperBookInit.eq(BookInitBalance::getIsCash, 1);
        List<BookInitBalance> bookInitBalances = bookInitBalanceMapper.selectList(wrapperBookInit);
        BigDecimal yearBalanceBeginning = bookInitBalances.stream()
                .map(BookInitBalance::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //期末余额
        BigDecimal endingBalance = BigDecimal.ZERO;
        if (Boolean.FALSE.equals(isSameMonth)) {
            endingBalance = Optional.ofNullable(
                            settlementMapper.selectOne(Wrappers.<Settlement>lambdaQuery()
                                    .eq(Settlement::getBookId, bookId)
                                    .eq(Settlement::getYearPeriod, getPreviousMonth(reportDate)))
                    ).map(Settlement::getEndingBalance)
                    .orElse(BigDecimal.ZERO);

        }

        BigDecimal amountTerm = yearBalanceBeginning;
        BigDecimal amountYear = yearBalanceBeginning;
        //如果为第一期，期初余额和年初余额从科目初始余额中获取

        if (Boolean.FALSE.equals(isSameMonth)) {
            //如果不是第一期但是第一年，期初余额取上一期的期末余额，年初余额保存不变
            amountTerm = endingBalance;
            if (Boolean.FALSE.equals(sameYear)) {
                //既不是第一期也不是第一年,期初余额是上一月的期末金额，年初余额上一年的最后一期的期末金额
                amountYear = Optional.ofNullable(
                                settlementMapper.selectOne(Wrappers.<Settlement>lambdaQuery()
                                        .eq(Settlement::getBookId, bookId)
                                        .eq(Settlement::getYearPeriod, getLastMonthOfPreviousYear(reportDate)))
                        ).map(Settlement::getEndingBalance)
                        .orElse(BigDecimal.ZERO);
            }
        }

        // 第二步：计算小计和净额
        Map<String, BigDecimal> monthlyResults = calculateSubtotalsAndNetAmountsWithProcessor(statementCashFlows, true, amountTerm);
        Map<String, BigDecimal> yearlyResults = calculateSubtotalsAndNetAmountsWithProcessor(statementCashFlows, false, amountYear);

        // 第三步：更新小计和净额值
        for (StatementCashFlow flow : statementCashFlows) {
            String code = flow.getItemCode();
            // 检查项目是否为小计或净额
            if (flow.getIsResult() == 1 || "56-xj-qita".equals(flow.getItemCode())) {
                flow.setMonthlyAmount(monthlyResults.get(code));
                flow.setCurrentAmount(yearlyResults.get(code));
            }
        }

        return statementCashFlows;
    }

    private Map<String, BigDecimal> generateSpecifyCashFlowBalance(List<StatementCashFlow> statementCashFlows) {
        Map<String, BigDecimal> resultMap = new HashMap<>();

        for (StatementCashFlow scf : statementCashFlows) {
            String key = scf.getItemCode();
            BigDecimal value = scf.getMonthlyAmount();

            resultMap.merge(key, value, BigDecimal::add);
        }

        return resultMap;
    }

    private Map<String, BigDecimal> generateCashFlowBalance(List<VoucherItemVo> voucherItems) {
        Map<String, BigDecimal> cashFlowMap = new HashMap<>();

        for (VoucherItemVo item : voucherItems) {
            String code = item.getCashFlowItemCode();
            BigDecimal balance = item.getCashFlowBalance();

            cashFlowMap.compute(code, (k, v) -> (v == null) ? balance : v.add(balance));
        }

        return cashFlowMap;
    }

    private static Map<String, BigDecimal> calculateSubtotalsAndNetAmountsWithProcessor(List<StatementCashFlow> flows, boolean isMonthly, BigDecimal beginBalance) {
        // 创建一个处理器对象来简化代码
        CashFlowProcessor processor = new CashFlowProcessor(flows, isMonthly);
        Map<String, BigDecimal> results = new HashMap<>();

        //设立期初余额
        results.put(CashFlowItemEnum.BEGINNING_CASH_BALANCE.getDbCode(), beginBalance);
        results.put(CashFlowItemEnum.BEGINNING_BALANCE_CASH.getDbCode(), beginBalance);

        // 一、经营活动产生的现金流量
        BigDecimal operatingCashInflow = processor.sumAmounts(CashFlowItemEnum.getOperatingCashInflowDbCodes());
        results.put(CashFlowItemEnum.OPERATING_CASH_INFLOW_SUBTOTAL.getDbCode(), operatingCashInflow);

        BigDecimal operatingCashOutflow = processor.sumAmounts(CashFlowItemEnum.getOperatingCashOutflowDbCodes());
        results.put(CashFlowItemEnum.OPERATING_CASH_OUTFLOW_SUBTOTAL.getDbCode(), operatingCashOutflow);

        BigDecimal operatingCashNet = operatingCashInflow.subtract(operatingCashOutflow);
        results.put(CashFlowItemEnum.OPERATING_CASH_NET.getDbCode(), operatingCashNet);

        // 二、投资活动产生的现金流量
        BigDecimal investingCashInflow = processor.sumAmounts(CashFlowItemEnum.getInvestingCashInflowDbCodes());
        results.put(CashFlowItemEnum.INVESTING_CASH_INFLOW_SUBTOTAL.getDbCode(), investingCashInflow);

        BigDecimal investingCashOutflow = processor.sumAmounts(CashFlowItemEnum.getInvestingCashOutflowDbCodes());
        results.put(CashFlowItemEnum.INVESTING_CASH_OUTFLOW_SUBTOTAL.getDbCode(), investingCashOutflow);

        BigDecimal investingCashNet = investingCashInflow.subtract(investingCashOutflow);
        results.put(CashFlowItemEnum.INVESTING_CASH_NET.getDbCode(), investingCashNet);

        // 三、筹资活动产生的现金流量
        BigDecimal financingCashInflow = processor.sumAmounts(CashFlowItemEnum.getFinancingCashInflowDbCodes());
        results.put(CashFlowItemEnum.FINANCING_CASH_INFLOW_SUBTOTAL.getDbCode(), financingCashInflow);

        BigDecimal financingCashOutflow = processor.sumAmounts(CashFlowItemEnum.getFinancingCashOutflowDbCodes());
        results.put(CashFlowItemEnum.FINANCING_CASH_OUTFLOW_SUBTOTAL.getDbCode(), financingCashOutflow);

        BigDecimal financingCashNet = financingCashInflow.subtract(financingCashOutflow);
        results.put(CashFlowItemEnum.FINANCING_CASH_NET.getDbCode(), financingCashNet);

        // 四、汇率变动对现金的影响
        BigDecimal exchangeRateEffect = processor.getAmount(CashFlowItemEnum.EXCHANGE_RATE_EFFECT.getDbCode());

        // 五、现金及现金等价物净增加额
        BigDecimal cashNetIncrease = operatingCashNet.add(investingCashNet).add(financingCashNet).add(exchangeRateEffect);
        results.put(CashFlowItemEnum.CASH_NET_INCREASE.getDbCode(), cashNetIncrease);

        // 六、期末现金及现金等价物余额
//        BigDecimal beginningCashBalance = processor.getAmount(CashFlowItemEnum.BEGINNING_CASH_BALANCE.getDbCode());
        BigDecimal endingCashBalance = beginBalance.add(cashNetIncrease);
        results.put(CashFlowItemEnum.ENDING_CASH_BALANCE.getDbCode(), endingCashBalance);

        //其他和经营活动产生的现金流量净额
        //先计算除了"其他"项外的所有项的和
        BigDecimal supplementaryInformation = processor.sumAmounts(CashFlowItemEnum.getSupplementaryInformationDbCodes());
        BigDecimal otherValue = operatingCashNet.subtract(supplementaryInformation);
        results.put(CashFlowItemEnum.OTHER_VALUE.getDbCode(), otherValue);
        results.put(CashFlowItemEnum.OPERATING_CASH_NET_SECOND.getDbCode(), operatingCashNet);

        //获取现金及现金等价物净增加情况
        results.put(CashFlowItemEnum.ENDING_BALANCE_CASH.getDbCode(), endingCashBalance);
//        BigDecimal beginningAmount = processor.getAmount(CashFlowItemEnum.BEGINNING_BALANCE_CASH.getDbCode());
        BigDecimal endingAmountEqui = processor.getAmount(CashFlowItemEnum.ENDING_BALANCE_CASH_EQUIVALENTS.getDbCode());
        BigDecimal beginningAmountEqui = processor.getAmount(CashFlowItemEnum.BEGINNING_BALANCE_CASH_EQUIVALENTS.getDbCode());
        BigDecimal netIncreaseCash = endingCashBalance.subtract(beginBalance).add(endingAmountEqui).subtract(beginningAmountEqui);
        results.put(CashFlowItemEnum.NET_INCREASE_CASH_EQUIVALENTS.getDbCode(), netIncreaseCash);

        return results;
    }

    /**
     * 现金流量处理器类，用于处理现金流量计算
     */
    private static class CashFlowProcessor {
        private final Map<String, StatementCashFlow> flowMap;
        private final boolean isMonthly;

        /**
         * 构造函数
         *
         * @param flows     现金流量项目列表
         * @param isMonthly 是否处理月度数据
         */
        public CashFlowProcessor(List<StatementCashFlow> flows, boolean isMonthly) {
            this.isMonthly = isMonthly;
            // 预处理：建立itemCode到StatementCashFlow的映射
            this.flowMap = new HashMap<>(flows.size());
            for (StatementCashFlow flow : flows) {
                this.flowMap.put(flow.getItemCode(), flow);
            }
        }

        /**
         * 获取指定项目代码的金额
         *
         * @param itemCode 项目代码
         * @return 项目金额，如果为null则返回0
         */
        public BigDecimal getAmount(String itemCode) {
            StatementCashFlow flow = flowMap.get(itemCode);
            if (flow == null) {
                return BigDecimal.ZERO;
            }
            BigDecimal amount = isMonthly ? flow.getMonthlyAmount() : flow.getCurrentAmount();
            return amount != null ? amount : BigDecimal.ZERO;
        }

        /**
         * 计算指定项目代码列表的金额总和
         *
         * @param itemCodes 项目代码列表
         * @return 金额总和
         */
        public BigDecimal sumAmounts(List<String> itemCodes) {
            return itemCodes.stream()
                    .map(this::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }

    private String getPreviousMonth(String reportDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth current = YearMonth.parse(reportDate, formatter);
        YearMonth previous = current.minusMonths(1);
        return previous.format(formatter);
    }

    public String getLastMonthOfPreviousYear(String reportDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth current = YearMonth.parse(reportDate, formatter);
        YearMonth lastMonthOfPrevYear = YearMonth.of(current.getYear() - 1, 12);
        return lastMonthOfPrevYear.format(formatter);
    }
}
