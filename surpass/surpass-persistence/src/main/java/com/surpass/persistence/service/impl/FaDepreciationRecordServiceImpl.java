package com.surpass.persistence.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.Message;
import com.surpass.entity.book.Book;
import com.surpass.entity.book.BookSubject;
import com.surpass.entity.fa.FaDepreciationRecord;
import com.surpass.entity.fa.FixedAssets;
import com.surpass.entity.fa.dto.DepreciationRecordPageDto;
import com.surpass.entity.fa.dto.ProvisionDto;
import com.surpass.entity.fa.vo.AssetDepreciationVo;
import com.surpass.entity.voucher.dto.VoucherChangeDto;
import com.surpass.entity.voucher.dto.VoucherItemChangeDto;
import com.surpass.enums.VoucherStatusEnum;
import com.surpass.persistence.mapper.BookMapper;
import com.surpass.persistence.mapper.FaDepreciationRecordMapper;
import com.surpass.persistence.mapper.FixedAssetsMapper;
import com.surpass.persistence.service.BookSubjectService;
import com.surpass.persistence.service.ConfigSysService;
import com.surpass.persistence.service.FaDepreciationRecordService;
import com.surpass.persistence.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/7/9 10:58
 */

@Service
@RequiredArgsConstructor
public class FaDepreciationRecordServiceImpl extends ServiceImpl<FaDepreciationRecordMapper, FaDepreciationRecord> implements FaDepreciationRecordService {

    private final FaDepreciationRecordMapper faDepreciationRecordMapper;

    private final ConfigSysService configSysService;

    private final FixedAssetsMapper fixedAssetsMapper;

    private final BookSubjectService bookSubjectService;

    private final VoucherService voucherService;

    private final BookMapper bookMapper;

    @Override
    public  Message<Map<String, Object>> pageList(DepreciationRecordPageDto dto) {
        dto.setStartPeriod(dto.getBelongDateRange()[0]);
        dto.setEndPeriod(dto.getBelongDateRange()[1]);
        dto.setYear(String.valueOf(LocalDate.now().getYear()));

        // 查询分页原始记录（FaDepreciationRecord 需有 monthlyMapStr 字段）
        Page<AssetDepreciationVo> assetDepreciationVoPage = faDepreciationRecordMapper.pageList(dto.build(), dto);
        List<AssetDepreciationVo> records = assetDepreciationVoPage.getRecords();

        // 构建月份列表
        List<String> monthList = getPeriodRange(dto.getStartPeriod(), dto.getEndPeriod());

        // 转换记录为 map
        List<Map<String, Object>> mappedRecords = records.stream().map(record -> {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("assetId", record.getAssetId());
            row.put("assetName", record.getAssetName());
            row.put("originalValue", record.getOriginalValue());
            row.put("beginAccumulatedDepreciation", record.getBeginAccumulatedDepreciation());
            row.put("currentYearDepreciation", record.getCurrentYearDepreciation());
            row.put("endAccumulatedDepreciation", record.getEndAccumulatedDepreciation());
            row.put("endNetValue", record.getEndNetValue());
            row.put("assetCode", record.getAssetCode());
            row.put("categoryCode", record.getCategoryCode());
            row.put("categoryName", record.getCategoryName());

            Map<String, BigDecimal> monthlyMap = record.getMonthlyMap(); // 自定义解析 monthlyMapStr
            for (String month : monthList) {
                row.put(month, monthlyMap.getOrDefault(month, BigDecimal.ZERO));
            }
            return row;
        }).toList();

        // 构造新的分页对象，注意保留分页元信息
        Page<Map<String, Object>> resultPage = new Page<>(assetDepreciationVoPage.getCurrent(),
                assetDepreciationVoPage.getSize(), assetDepreciationVoPage.getTotal());
        resultPage.setRecords(mappedRecords);

        // 封装 monthList + 分页记录
        Map<String, Object> result = new HashMap<>();
        result.put("monthList", monthList);
        result.put("records", resultPage);

        return Message.ok(result);
    }


    private List<String> getPeriodRange(String start, String end) {
        List<String> list = new ArrayList<>();
        YearMonth startYm = YearMonth.parse(start);
        YearMonth endYm = YearMonth.parse(end);
        while (!startYm.isAfter(endYm)) {
            list.add(startYm.toString());
            startYm = startYm.plusMonths(1);
        }
        return list;
    }

    @Override
    @Transactional
    public Message<String> generateProvision(ProvisionDto dto) {
        String bookId = dto.getBookId();
        String currentTerm = configSysService.getCurrentTerm(bookId);
        Book book = bookMapper.selectById(bookId);
        int year = Integer.parseInt(currentTerm.split("-")[0]);
        int month = Integer.parseInt(currentTerm.split("-")[1]);
        String summary = dto.getSummary();
        String wordHead = dto.getWordHead();
        Date voucherDate = dto.getVoucherDate();

        // 获取所有需要折旧的资产（平均年限法）
        List<FixedAssets> averageList = getFixedAssetsList(1, currentTerm, bookId);
        // 获取所有需要折旧的资产（双倍余额递减法）
        List<FixedAssets> doubleDeclineList = getFixedAssetsList(2, currentTerm, bookId);
        if (averageList.isEmpty() && doubleDeclineList.isEmpty()) {
            return Message.failed("无可计提资产");
        }

        // 准备资产 ID 列表
        List<String> assetIds = Stream.concat(averageList.stream(), doubleDeclineList.stream())
                .map(FixedAssets::getId)
                .toList();

        // 一次性查出所有资产的累计折旧
        Map<String, BigDecimal> accumulatedMap = faDepreciationRecordMapper.getAccumulatedMap(assetIds, bookId);

        List<FaDepreciationRecord> records = new ArrayList<>();
        List<FixedAssets> updatedAssets = new ArrayList<>();

        // 借方合并Map：<expense_subject_code, 金额>
        Map<String, BigDecimal> debitMap = new HashMap<>();
        // 贷方合并Map：<depreciation_subject_code, 金额>
        Map<String, BigDecimal> creditMap = new HashMap<>();

        List<FixedAssets> allAssets = new ArrayList<>();
        allAssets.addAll(averageList);
        allAssets.addAll(doubleDeclineList);

        for (FixedAssets asset : allAssets) {
            BigDecimal accumulated = accumulatedMap.getOrDefault(asset.getId(), BigDecimal.ZERO);
            FaDepreciationRecord record;
            if (asset.getDepreciationMethod() == 1) {
                record = calculateByAverageMethod(asset, accumulated, currentTerm, dto);
            } else {
                record = calculateByDoubleDecliningMethod(asset, accumulated, currentTerm, dto);
            }
            records.add(record);
            updatedAssets.add(asset);

            // 借方
            debitMap.merge(asset.getExpenseSubjectCode(), record.getDepreciationAmount(), BigDecimal::add);
            // 贷方
            creditMap.merge(asset.getDepreciationSubjectCode(), record.getDepreciationAmount(), BigDecimal::add);
        }

        // 批量插入记录
        super.saveBatch(records);
        // 批量更新资产净值
        fixedAssetsMapper.updateById(updatedAssets);

        // 生成折旧凭证
        List<VoucherItemChangeDto> items = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : debitMap.entrySet()) {
            String subjectCode = entry.getKey();
            BigDecimal amount = entry.getValue();
            BookSubject subject = bookSubjectService.selectSubject(bookId, subjectCode);

            VoucherItemChangeDto item = new VoucherItemChangeDto();
            item.setSubjectId(subject.getId());
            item.setSubjectCode(subject.getCode());
            item.setSubjectName(subject.getCode() + "-" + subject.getName());
            item.setSummary(summary);
            item.setDebitAmount(amount);
            item.setSubjectBalance(subject.getBalance());
            item.setAuxiliary(List.of());
            item.setDetailedAccounts("");
            items.add(item);
        }

        for (Map.Entry<String, BigDecimal> entry : creditMap.entrySet()) {
            String subjectCode = entry.getKey();
            BigDecimal amount = entry.getValue();
            BookSubject subject = bookSubjectService.selectSubject(bookId, subjectCode);

            VoucherItemChangeDto item = new VoucherItemChangeDto();
            item.setSubjectId(subject.getId());
            item.setSubjectCode(subject.getCode());
            item.setSubjectName(subject.getCode() + "-" + subject.getName());
            item.setSummary(summary);
            item.setCreditAmount(amount);
            item.setSubjectBalance(subject.getBalance());
            item.setAuxiliary(List.of());
            item.setDetailedAccounts("");
            items.add(item);
        }

        BigDecimal totalAmount = records.stream()
                .map(FaDepreciationRecord::getDepreciationAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        VoucherChangeDto voucher = new VoucherChangeDto();
        voucher.setWordHead(wordHead);
        voucher.setWordNum(voucherService.getAbleWordNum(dto.getBookId(), wordHead, year, month).getData());
        voucher.setBookId(bookId);
        voucher.setCompanyName(book.getCompanyName());
        voucher.setVoucherDate(voucherDate);
        voucher.setVoucherYear(year);
        voucher.setVoucherMonth(month);
        voucher.setDebitAmount(totalAmount);
        voucher.setCreditAmount(totalAmount);
        voucher.setItems(items);
        voucher.setStatus(VoucherStatusEnum.DRAFT.getValue());
        voucher.setRemark(String.format("计提%d年%d月折旧", year, month));

        voucherService.save(voucher);


        return Message.ok("计提成功");
    }

    List<FixedAssets> getFixedAssetsList(Integer method, String currentTerm, String bookId) {
        FixedAssets fixedAssets = new FixedAssets();
        fixedAssets.setAssetStatus("active");
        fixedAssets.setBookId(bookId);
        fixedAssets.setDepreciationMethod(method);
        fixedAssets.setCurrentTerm(currentTerm);

        return faDepreciationRecordMapper.getFixedAssetsList(fixedAssets);
    }

    // 折旧计算方法-平均年限法
    private FaDepreciationRecord calculateByAverageMethod(FixedAssets asset, BigDecimal accumulated, String currentTerm, ProvisionDto dto) {
        BigDecimal originalValue = asset.getOriginalValue();
        BigDecimal residualRate = asset.getResidualRate().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        int terms = asset.getDepreciationTerms();

        BigDecimal depreciable = originalValue.multiply(BigDecimal.ONE.subtract(residualRate));
        BigDecimal monthlyDepreciation = depreciable.divide(BigDecimal.valueOf(terms), 2, RoundingMode.HALF_UP);

        BigDecimal newAccumulated = accumulated.add(monthlyDepreciation);
        BigDecimal newNetValue = originalValue.subtract(newAccumulated);

        FaDepreciationRecord record = new FaDepreciationRecord();
        record.setAssetId(asset.getId());
        record.setBookId(asset.getBookId());
        record.setPeriod(currentTerm);
        record.setDepreciationMethod(1);
        record.setDepreciationAmount(monthlyDepreciation);
        record.setAccumulatedDepreciation(newAccumulated);
        record.setNetValue(newNetValue);
        record.setDeleted("n");

        // 更新 asset 对象的 netValue（供后续批量更新）
        asset.setNetValue(newNetValue);
        return record;
    }

    // 折旧计算方法-双倍余额递减法
    private FaDepreciationRecord calculateByDoubleDecliningMethod(FixedAssets asset, BigDecimal accumulated, String currentTerm, ProvisionDto dto) {
        BigDecimal originalValue = asset.getOriginalValue();
        BigDecimal residualRate = asset.getResidualRate().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        int terms = asset.getDepreciationTerms();

        BigDecimal bookValue = originalValue.subtract(accumulated);
        BigDecimal rate = BigDecimal.valueOf(2).divide(BigDecimal.valueOf(terms), 4, RoundingMode.HALF_UP);
        BigDecimal currentDepreciation = bookValue.multiply(rate).setScale(2, RoundingMode.HALF_UP);

        BigDecimal depreciableMinimum = originalValue.multiply(BigDecimal.ONE.subtract(residualRate));
        BigDecimal maxAllowedDepreciation = depreciableMinimum.subtract(accumulated);

        if (currentDepreciation.compareTo(maxAllowedDepreciation) > 0) {
            currentDepreciation = maxAllowedDepreciation;
        }

        BigDecimal newAccumulated = accumulated.add(currentDepreciation);
        BigDecimal newNetValue = originalValue.subtract(newAccumulated);

        FaDepreciationRecord record = new FaDepreciationRecord();
        record.setAssetId(asset.getId());
        record.setBookId(asset.getBookId());
        record.setPeriod(currentTerm);
        record.setDepreciationMethod(2);
        record.setDepreciationAmount(currentDepreciation);
        record.setAccumulatedDepreciation(newAccumulated);
        record.setNetValue(newNetValue);
        record.setDeleted("n");

        asset.setNetValue(newNetValue);
        return record;
    }


}
