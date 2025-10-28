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
import com.surpass.entity.book.Book;
import com.surpass.entity.book.Settlement;
import com.surpass.entity.book.dto.BookChangeDto;
import com.surpass.entity.standard.StandardStatementBalanceSheet;
import com.surpass.entity.standard.StandardStatementRules;
import com.surpass.entity.statement.*;
import com.surpass.entity.statement.dto.StatementParamsDto;
import com.surpass.entity.statement.vo.StatementBalanceSheetExport;
import com.surpass.entity.statement.vo.StatementBalanceSheetItemListVo;
import com.surpass.enums.AssetOrLiabilityEnum;
import com.surpass.enums.StatementPeriodTypeEnum;
import com.surpass.enums.StatementSymbolEnum;
import com.surpass.enums.StatementTypeEnum;
import com.surpass.persistence.mapper.*;
import com.surpass.persistence.service.ConfigSysService;
import com.surpass.persistence.service.StatementBalanceSheetService;
import com.surpass.util.excel.ExcelDataModeEnum;
import com.surpass.util.excel.ExcelExporter;
import com.surpass.util.excel.ExcelParams;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StatementBalanceSheetServiceImpl implements StatementBalanceSheetService {
    private final ConfigSysService configSysService;
    private final StatementBalanceSheetMapper balanceSheetMapper;
    private final StatementBalanceSheetItemMapper balanceSheetItemMapper;
    private final IdentifierGenerator identifierGenerator;
    private final StatementRulesMapper statementRulesMapper;
    private final StandardStatementBalanceSheetMapper standardStatementBalanceSheetMapper;
    private final StandardStatementRulesMapper standardStatementRulesMapper;
    private final BookMapper bookMapper;
    private final StatementRulesMapper rulesMapper;
    private final StatementSubjectBalanceMapper subjectBalanceMapper;

    /**
     * 报表-资产负债表
     *
     * @param dto   查询参数
     * @param force 是否强制实时统计,如果为否，则非当前期从报表中查询，否则从科目余额表查询
     * @return 结果
     */
    @Override
    @Transactional
    public Message<StatementBalanceSheet> queryBalanceSheet(StatementParamsDto dto, boolean force) {
        dto.parse();
        // 查询历史报表
        LambdaQueryWrapper<StatementBalanceSheet> lqw = Wrappers.lambdaQuery();
        lqw.eq(StatementBalanceSheet::getBookId, dto.getBookId());
        lqw.eq(StatementBalanceSheet::getYearPeriod, dto.getReportDate());
        StatementBalanceSheet balanceSheet = balanceSheetMapper.selectOne(lqw);

        String currentTerm = configSysService.getCurrentTerm(dto.getBookId());
        List<String> allMonths = dto.getAllMonths(currentTerm);
        List<StatementBalanceSheetItem> items;
        // 查询范围包含了当前期,或者没数据，则实时统计
        boolean isNull = balanceSheet == null;
        if (isNull || allMonths.contains(currentTerm) || force) {
            balanceSheet = new StatementBalanceSheet();
            String currentId = identifierGenerator.nextId(balanceSheet).toString();
            balanceSheet.setId(currentId);
            balanceSheet.setBookId(dto.getBookId());
            balanceSheet.setYearPeriod(dto.getReportDate());
            balanceSheet.setPeriodType(dto.getPeriodType());

            // 查询条目
            LambdaQueryWrapper<StatementBalanceSheetItem> itemLqw = Wrappers.lambdaQuery();
            itemLqw.eq(StatementBalanceSheetItem::getBookId, dto.getBookId());
            itemLqw.eq(StatementBalanceSheetItem::getBalanceSheetId, ConstsSysConfig.SYS_CONFIG_TEMPLATE_ID);
            items = balanceSheetItemMapper.selectList(itemLqw);

            if (!items.isEmpty()) {
                // 插入当前数据
                if (isNull
                        && !StatementPeriodTypeEnum.BETWEEN_MONTH.getValue().equals(dto.getPeriodType())) {
                    for (StatementBalanceSheetItem item : items) {
                        String itemId = identifierGenerator.nextId(item).toString();
                        item.setId(itemId);
                        item.setBalanceSheetId(currentId);
                        item.setBookId(dto.getBookId());
                        item.setInitialBalance(BigDecimal.ZERO);
                        item.setCurrentBalance(BigDecimal.ZERO);
                        //balanceSheetItemMapper.insert(item);
                    }

                    //balanceSheetMapper.insert(balanceSheet);
                }

                // 统计总金额,因为是期末和年初，则只需要统计第一个月和最后一个月的数额
                if (allMonths.size() == 1) {
                    refreshItemsBalance(items, dto.getBookId(), allMonths.get(0), 3);
                } else {
                    refreshItemsBalance(items, dto.getBookId(), allMonths.get(0), 1);
                    refreshItemsBalance(items, dto.getBookId(), allMonths.get(allMonths.size() - 1), 2);
                }
            }
        }else {// 拉取历史数据
            // 查询条目
            LambdaQueryWrapper<StatementBalanceSheetItem> itemLqw = Wrappers.lambdaQuery();
            itemLqw.eq(StatementBalanceSheetItem::getBookId, dto.getBookId());
            itemLqw.eq(StatementBalanceSheetItem::getBalanceSheetId, balanceSheet.getId());
            items = balanceSheetItemMapper.selectList(itemLqw);
        }

        StatementBalanceSheetItemListVo itemListVo = insertSubtotals(items);
        itemListVo.getAssets().sort(Comparator.comparing(StatementBalanceSheetItem::getItemCode));
        itemListVo.getLiability().sort(Comparator.comparing(StatementBalanceSheetItem::getItemCode));
        balanceSheet.setItems(itemListVo);
        return new Message<>(balanceSheet);
    }

    /**
     * 更新资产负载表。
     *
     * @param dto  查询参数
     * @param save 是否更新入库，为true时会将当前查询到的数据更新入库。原有数据会覆盖。
     * @return 统计结果
     */
    @Override
    @Transactional
    public Message<StatementBalanceSheet> balanceSheet(StatementParamsDto dto, boolean save) {
        dto.parse();
        StatementBalanceSheet statementBalanceSheet = queryBalanceSheet(dto, true).getData();

        if (save) {
            StatementBalanceSheetItemListVo itemListVo = statementBalanceSheet.getItems();
            List<StatementBalanceSheetItem> balanceSheetItems = itemListVo.getAssets();
            balanceSheetItems.addAll(itemListVo.getLiability());
            LambdaQueryWrapper<StatementBalanceSheetItem> itemLqw = Wrappers.lambdaQuery();
            itemLqw.eq(StatementBalanceSheetItem::getBookId, dto.getBookId());
            itemLqw.eq(StatementBalanceSheetItem::getBalanceSheetId, statementBalanceSheet.getId());
            balanceSheetItemMapper.delete(itemLqw);

            for (StatementBalanceSheetItem item : balanceSheetItems) {
                String itemId = identifierGenerator.nextId(item).toString();
                item.setId(itemId);
                item.setBalanceSheetId(statementBalanceSheet.getId());
                item.setBookId(dto.getBookId());
            }
            balanceSheetItemMapper.insertBatch(balanceSheetItems);
        }
        return Message.ok(statementBalanceSheet);
    }

    @Override
    public void initBalanceSheet(BookChangeDto dto) {
        LambdaQueryWrapper<StandardStatementBalanceSheet> sItemlqw = Wrappers.lambdaQuery();
        sItemlqw.eq(StandardStatementBalanceSheet::getStandardId, dto.getStandardId());
        List<StandardStatementBalanceSheet> items = standardStatementBalanceSheetMapper.selectList(sItemlqw);
        List<StatementBalanceSheetItem> balanceSheetItems = new ArrayList<>();
        for (StandardStatementBalanceSheet item : items) {
            StatementBalanceSheetItem balanceSheetItem = new StatementBalanceSheetItem();
            balanceSheetItem.setBookId(dto.getId());
            balanceSheetItem.setBalanceSheetId(ConstsSysConfig.SYS_CONFIG_TEMPLATE_ID);

            balanceSheetItem.setAssetOrLiability(item.getAssetOrLiability());
            balanceSheetItem.setItemCode(item.getItemCode());
            balanceSheetItem.setItemName(item.getItemName());
            balanceSheetItem.setSortIndex(item.getSortIndex());
            balanceSheetItem.setLevel(item.getLevel());
            balanceSheetItem.setParentItemCode(item.getParentItemCode());
            balanceSheetItem.setSymbol(item.getSymbol());
            balanceSheetItem.setRule(item.getRule());
            balanceSheetItem.setCurrentBalance(BigDecimal.ZERO);
            balanceSheetItem.setInitialBalance(BigDecimal.ZERO);

            balanceSheetItems.add(balanceSheetItem);
        }

        LambdaQueryWrapper<StandardStatementRules> sRulelqw = Wrappers.lambdaQuery();
        sRulelqw.eq(StandardStatementRules::getStandardId, dto.getStandardId());
        sRulelqw.eq(StandardStatementRules::getType, StatementTypeEnum.balance_sheet.name());
        List<StandardStatementRules> sRuleitems = standardStatementRulesMapper.selectList(sRulelqw);

        List<StatementRules> itemRuls = new ArrayList<>();
        for (StandardStatementRules itemRule : sRuleitems) {
            StatementRules rule = new StatementRules();
            rule.setBookId(dto.getId());
            rule.setType(StatementTypeEnum.balance_sheet.name());
            rule.setType(itemRule.getType());
            rule.setItemCode(itemRule.getItemCode());
            rule.setSubjectCode(itemRule.getSubjectCode());
            rule.setRule(itemRule.getRule());
            rule.setSymbol(itemRule.getSymbol());
            itemRuls.add(rule);
        }

        balanceSheetItemMapper.insertBatch(balanceSheetItems);
        statementRulesMapper.insertBatch(itemRuls);

    }

    /**
     * 数据导出
     */
    @Override
    @Transactional
    public void export(StatementParamsDto dto, HttpServletResponse response) throws IOException {
        StatementBalanceSheet balanceSheet = queryBalanceSheet(dto, true).getData();
        StatementBalanceSheetItemListVo itemListVo = balanceSheet.getItems();
        List<StatementBalanceSheetItem> assets = itemListVo.getAssets();
        List<StatementBalanceSheetItem> liability = itemListVo.getLiability();
        List<StatementBalanceSheetItem> maxData = assets.size() > liability.size() ? assets : liability;
        Book book = bookMapper.selectById(dto.getBookId());

        String templatePath = ResourceUtils
                .getURL("classpath:static/export-template/template-balance-sheet.xlsx")
                .getPath();
        // 注意：Windows下getPath()前会带'/'，可做处理
        if (templatePath.startsWith("/")) {
            templatePath = templatePath.substring(1);
        }
        Path tempFilePath = Files.createTempFile("template-balance-sheet_", ".xlsx");
        File tempFile = tempFilePath.toFile();

        // 组装数据
        List<StatementBalanceSheetExport.AssetLiability> assetLiabilityList = new ArrayList<>();
        StatementBalanceSheetExport data = StatementBalanceSheetExport.builder()
                .companyName(book.getCompanyName())
                .date(balanceSheet.getYearPeriod())
                .assetLiabilityList(assetLiabilityList)
                .build();
        for (int i = 0; i < maxData.size(); i++) {
            StatementBalanceSheetItem assetItem = i < assets.size() ? assets.get(i) : null;
            StatementBalanceSheetItem liabilityItem = i < liability.size() ? liability.get(i) : null;
            StatementBalanceSheetExport.AssetLiability assetLiability = StatementBalanceSheetExport.AssetLiability.builder()
                    .assetItemName(
                            assetItem != null
                                    ? (StatementSymbolEnum.MINUS.getValue().equals(assetItem.getSymbol()) ? "减：" : "") + assetItem.getItemName()
                                    : null
                    )
                    .assetRowNum(assetItem != null ? assetItem.getSortIndex() : null)
                    .assetCurrentBalance(assetItem != null && assetItem.getCurrentBalance() != null ? assetItem.getCurrentBalance() : null)
                    .assetInitialBalance(assetItem != null && assetItem.getInitialBalance() != null ? assetItem.getInitialBalance() : null)
                    .liabilityItemName(
                            liabilityItem != null
                                    ? (StatementSymbolEnum.MINUS.getValue().equals(liabilityItem.getSymbol()) ? "减：" : "") + liabilityItem.getItemName()
                                    : null
                    )
                    .liabilityRowNum(liabilityItem != null ? liabilityItem.getSortIndex() : null)
                    .liabilityCurrentBalance(liabilityItem != null && liabilityItem.getCurrentBalance() != null ? liabilityItem.getCurrentBalance() : null)
                    .liabilityInitialBalance(liabilityItem != null && liabilityItem.getInitialBalance() != null ? liabilityItem.getInitialBalance() : null)
                    .build();
            assetLiabilityList.add(assetLiability);
        }

        // 单项数据渲染
        ExcelParams<StatementBalanceSheetExport> paramsObj = ExcelParams.<StatementBalanceSheetExport>builder()
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
        ExcelParams<List<StatementBalanceSheetExport.AssetLiability>> paramsList = ExcelParams.<List<StatementBalanceSheetExport.AssetLiability>>builder()
                .httpResponse(response)
                .mode(ExcelDataModeEnum.include_list)
                .dataModel(assetLiabilityList)
                .enableMergeCells(false)
                .autoSizeColumns(false)
                .recalculateFormulas(true)
                .templateFilePath(tempFile.getPath())
                .build();

        ExcelExporter.export(paramsList);
        // 最后删除临时文件
        if (tempFile.exists()) tempFile.delete();
    }

    @Override
    public boolean deleteByBookIds(List<String> bookIds) {
        LambdaQueryWrapper<StatementBalanceSheet> slqw = Wrappers.lambdaQuery();
        slqw.in(StatementBalanceSheet::getBookId, bookIds);
        balanceSheetMapper.delete(slqw);

        LambdaQueryWrapper<StatementBalanceSheetItem> sItemlqw = Wrappers.lambdaQuery();
        sItemlqw.in(StatementBalanceSheetItem::getBookId, bookIds);
        balanceSheetItemMapper.delete(sItemlqw);

        LambdaQueryWrapper<StatementRules> sRulelqw = Wrappers.lambdaQuery();
        sRulelqw.in(StatementRules::getBookId, bookIds);
        sRulelqw.eq(StatementRules::getType, "balance_sheet");
        statementRulesMapper.delete(sRulelqw);
        return true;
    }

    /**
     * 指定期结账入库
     *
     * @param dto 结账参数
     * @return 操作结果
     */
    @Override
    @Transactional
    public boolean checkout(Settlement dto) {
        // 月报
        StatementParamsDto monthParamsDto = new StatementParamsDto();
        monthParamsDto.setBookId(dto.getBookId());
        monthParamsDto.setPeriodType(StatementPeriodTypeEnum.MONTH.getValue());
        monthParamsDto.setReportDate(dto.getCurrentTerm());
        balanceSheet(monthParamsDto, true);

        // 季报
        StatementParamsDto quarterParamsDto = new StatementParamsDto();
        quarterParamsDto.setBookId(dto.getBookId());
        quarterParamsDto.setPeriodType(StatementPeriodTypeEnum.QUARTER.getValue());
        quarterParamsDto.setReportDate(dto.getCurrentTerm());
        if (monthParamsDto.isQuarterReportMonth()) {
            balanceSheet(quarterParamsDto, true);
        }

        // 年报
        StatementParamsDto yearParamsDto = new StatementParamsDto();
        yearParamsDto.setBookId(dto.getBookId());
        yearParamsDto.setPeriodType(StatementPeriodTypeEnum.YEAR.getValue());
        yearParamsDto.setReportDate(dto.getCurrentTerm());
        if (monthParamsDto.isYearReportMonth()) {
            balanceSheet(yearParamsDto, true);
        }

        return true;
    }


    /**
     * 刷新信息项对应的余额数据
     *
     * @param items        信息项组
     * @param bookId       所属账簿
     * @param yearPeriod   账期
     * @param updateParams 需要更新的金额项：1：年初，2：期末，3：同时
     */
    @Override
    public void refreshItemsBalance(List<StatementBalanceSheetItem> items,
                                    String bookId, String yearPeriod, int updateParams) {
        // 方便更新参数
        Map<String, StatementBalanceSheetItem> mapSheet = items.stream()
                .collect(Collectors.toMap(StatementBalanceSheetItem::getItemCode, item -> item));
        List<String> itemCodes = items.stream().map(StatementBalanceSheetItem::getItemCode).toList();
        // 规则查询
        LambdaQueryWrapper<StatementRules> lqwRule = Wrappers.lambdaQuery();
        lqwRule.in(StatementRules::getItemCode, itemCodes);
        lqwRule.eq(StatementRules::getBookId, bookId);
        lqwRule.eq(StatementRules::getType, StatementTypeEnum.balance_sheet.name());
        // 相关的所有规则
        List<StatementRules> rules = rulesMapper.selectList(lqwRule);
        // 所有规则下的绑定科目编号
        List<String> subjectCodes = rules.stream().map(StatementRules::getSubjectCode).toList();
        if (CollectionUtils.isNotEmpty(subjectCodes)) {
            // 查询科目余额
            LambdaQueryWrapper<StatementSubjectBalance> lqwSubject = Wrappers.lambdaQuery();
            lqwSubject.in(StatementSubjectBalance::getSubjectCode, subjectCodes);
            lqwSubject.eq(StatementSubjectBalance::getBookId, bookId);
            lqwSubject.eq(StatementSubjectBalance::getYearPeriod, yearPeriod);
            List<StatementSubjectBalance> subjectBalances = subjectBalanceMapper.selectList(lqwSubject);
            Map<String, StatementSubjectBalance> subjectMap = subjectBalances.stream()
                    .collect(Collectors.toMap(StatementSubjectBalance::getSubjectCode, item -> item));
            // 更新对应规则的余额和报表余额
            for (StatementRules statementRules : rules) {
                StatementSubjectBalance subjectBalance = subjectMap.get(statementRules.getSubjectCode());
                updateRuleBalance(subjectBalance, statementRules);
                StatementBalanceSheetItem balanceSheet = mapSheet.get(statementRules.getItemCode());
                if (StatementSymbolEnum.PLUS.getValue().equals(statementRules.getSymbol())) {
                    if (updateParams == 1 || updateParams == 3) {
                        balanceSheet.setInitialBalance(balanceSheet.getInitialBalance().add(statementRules.getOpeningYearBalance()));
                    }
                    if (updateParams == 2 || updateParams == 3) {
                        balanceSheet.setCurrentBalance(balanceSheet.getCurrentBalance().add(statementRules.getClosingBalance()));
                    }
                } else {
                    if (updateParams == 1 || updateParams == 3) {
                        balanceSheet.setInitialBalance(balanceSheet.getInitialBalance().subtract(statementRules.getOpeningYearBalance()));
                    }
                    if (updateParams == 2 || updateParams == 3) {
                        balanceSheet.setCurrentBalance(balanceSheet.getCurrentBalance().subtract(statementRules.getClosingBalance()));
                    }
                }
            }
        }
    }


    /**
     * 对资产/负债中的合计类节点（如：1199、1299、1399）进行数据聚合，并更新这些节点的余额。
     *
     * @param items 数据组
     * @return 结果
     */
    @Override
    public StatementBalanceSheetItemListVo insertSubtotals(List<StatementBalanceSheetItem> items) {
        if (items == null || items.isEmpty()) return null;

        // 分组（资产和负债）
        Map<String, List<StatementBalanceSheetItem>> grouped = items.stream()
                .collect(Collectors.groupingBy(StatementBalanceSheetItem::getAssetOrLiability));

        StatementBalanceSheetItemListVo result = new StatementBalanceSheetItemListVo();

        // 资产
        List<StatementBalanceSheetItem> assetList = grouped.getOrDefault(AssetOrLiabilityEnum.asset.name(), new ArrayList<>());
        buildTreeAndSum(assetList);
        result.setAssets(assetList);

        // 负债 + 所有者权益
        List<StatementBalanceSheetItem> liabilityList = grouped.getOrDefault(AssetOrLiabilityEnum.liability.name(), new ArrayList<>());
        buildTreeAndSum(liabilityList);
        result.setLiability(liabilityList);

        return result;
    }

    /**
     * 构建树 + 递归合计
     *
     * @param flatList 扁平化数据
     */
    private void buildTreeAndSum(List<StatementBalanceSheetItem> flatList) {
        if (flatList == null || flatList.isEmpty()) return;

        // 构建子列表引用（临时构建树结构）
        Map<String, List<StatementBalanceSheetItem>> childMap = new HashMap<>();
        for (StatementBalanceSheetItem item : flatList) {
            String parentCode = item.getParentItemCode();
            if (parentCode != null && !parentCode.isBlank()) {
                childMap.computeIfAbsent(parentCode, k -> new ArrayList<>()).add(item);
            }
        }

        // 递归聚合所有合计节点（自底向上）
        Set<String> visited = new HashSet<>();
        for (StatementBalanceSheetItem item : flatList) {
            if (!visited.contains(item.getItemCode())) {
                sumRecursively(item, childMap, visited);
            }
        }

        // 聚合所有合计节点余额，
        // 递归时已经将子节点余额聚合到父节点上，这里只需要将父节点余额聚合到根节点上即可。
        final StatementBalanceSheetItem[] maxNode = {null};
        final BigDecimal[] currentAllSum = {BigDecimal.ZERO};
        final BigDecimal[] initialAllSum = {BigDecimal.ZERO};
        // 构建映射关系
        Map<String, StatementBalanceSheetItem> itemMap = flatList.stream()
                .collect(Collectors.toMap(StatementBalanceSheetItem::getItemCode, Function.identity()));

        flatList.stream().filter(item -> item.getLevel() == 1).forEach(node -> {
            // 默认定义尾号99为合计项。如：1199、1299、1399、1199_1299
            if (node.getItemCode().endsWith("99")) {
                BigDecimal currentSum = node.getCurrentBalance() != null ? node.getCurrentBalance() : BigDecimal.ZERO;
                BigDecimal initialSum = node.getInitialBalance() != null ? node.getInitialBalance() : BigDecimal.ZERO;

                // 分割拼接节点，叠加余额
                String[] codes = node.getItemCode().split("_");
                for (String code : codes) {
                    code = code.substring(0, 2) + "00";
                    StatementBalanceSheetItem parent = itemMap.get(code);
                    if (parent != null) {
                        currentSum = currentSum.add(parent.getCurrentBalance());
                        initialSum = initialSum.add(parent.getInitialBalance());
                    }
                }
                node.setCurrentBalance(currentSum);
                node.setInitialBalance(initialSum);
                // 避免重复叠加总额，因为节点可能被多次引用，如1199_1299
                if (codes.length == 1) {
                    initialAllSum[0] = initialAllSum[0].add(initialSum);
                    currentAllSum[0] = currentAllSum[0].add(currentSum);
                }

                // 获取最大节点，一般为总计项
                if (maxNode[0] == null || node.getItemCode().compareTo(maxNode[0].getItemCode()) > 0) {
                    maxNode[0] = node;
                }
            }
        });
        flatList.forEach(node -> {
            // 顶级节点不允许出现余额，统计项除外
            if (node.getItemCode().endsWith("00")) {
                node.setCurrentBalance(BigDecimal.ZERO);
                node.setInitialBalance(BigDecimal.ZERO);
            }
        });

        // 设置总计项余额
        if (maxNode[0] != null) {
            maxNode[0].setCurrentBalance(currentAllSum[0]);
            maxNode[0].setInitialBalance(initialAllSum[0]);
        }
    }

    /**
     * 递归合计
     *
     * @param node     当前节点
     * @param childMap 子列表引用
     * @param visited  访问过的节点
     */
    private void sumRecursively(StatementBalanceSheetItem node,
                                Map<String, List<StatementBalanceSheetItem>> childMap,
                                Set<String> visited) {
        if (visited.contains(node.getItemCode())) return;
        visited.add(node.getItemCode());
        List<StatementBalanceSheetItem> children = childMap.getOrDefault(node.getItemCode(), Collections.emptyList());

        BigDecimal currentSum = node.getCurrentBalance() != null ? node.getCurrentBalance() : BigDecimal.ZERO;
        BigDecimal initialSum = node.getInitialBalance() != null ? node.getInitialBalance() : BigDecimal.ZERO;

        for (StatementBalanceSheetItem child : children) {
            sumRecursively(child, childMap, visited);
            if (StatementSymbolEnum.PLUS.getValue().equals(child.getSymbol())) {
                currentSum = currentSum.add(
                        child.getCurrentBalance() != null ? child.getCurrentBalance() : BigDecimal.ZERO
                );
                initialSum = initialSum.add(
                        child.getInitialBalance() != null ? child.getInitialBalance() : BigDecimal.ZERO
                );
            } else if (StatementSymbolEnum.MINUS.getValue().equals(child.getSymbol())){
                currentSum = currentSum.subtract(
                        child.getCurrentBalance() != null ? child.getCurrentBalance() : BigDecimal.ZERO
                );
                initialSum = initialSum.subtract(
                        child.getInitialBalance() != null ? child.getInitialBalance() : BigDecimal.ZERO
                );
            }
        }

        node.setCurrentBalance(currentSum);
        node.setInitialBalance(initialSum);
    }


    @Override
    public void updateRuleBalance(StatementSubjectBalance subjectBalance, StatementRules statementRules) {
        if (subjectBalance != null) {
            statementRules.setOpeningYearBalance(subjectBalance.getOpeningYearBalanceDebit()
                    .subtract(subjectBalance.getOpeningYearBalanceCredit()));
            statementRules.setClosingBalance(subjectBalance.getBalance());
        } else {
            statementRules.setOpeningYearBalance(BigDecimal.ZERO);
            statementRules.setClosingBalance(BigDecimal.ZERO);
        }
    }

}
