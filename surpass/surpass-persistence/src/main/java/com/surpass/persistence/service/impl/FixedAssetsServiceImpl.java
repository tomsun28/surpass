package com.surpass.persistence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.Message;
import com.surpass.entity.book.Book;
import com.surpass.entity.book.BookSubject;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.fa.FixedAssets;
import com.surpass.entity.fa.dto.FixedAssetsChangeDto;
import com.surpass.entity.fa.dto.FixedAssetsPageDto;
import com.surpass.entity.voucher.dto.GenerateVoucherDto;
import com.surpass.entity.voucher.dto.VoucherChangeDto;
import com.surpass.entity.voucher.dto.VoucherItemChangeDto;
import com.surpass.enums.VoucherStatusEnum;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.BookMapper;
import com.surpass.persistence.mapper.FixedAssetsMapper;
import com.surpass.persistence.service.BookSubjectService;
import com.surpass.persistence.service.ConfigSysService;
import com.surpass.persistence.service.FixedAssetsService;
import com.surpass.persistence.service.VoucherService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/7/3 15:51
 */

@Service
@RequiredArgsConstructor
public class FixedAssetsServiceImpl extends ServiceImpl<FixedAssetsMapper, FixedAssets> implements FixedAssetsService {

    private final FixedAssetsMapper fixedAssetsMapper;

    private final BookMapper bookMapper;

    private final ConfigSysService configSysService;

    private final BookSubjectService bookSubjectService;

    private final VoucherService voucherService;

    @Override
    public Message<Page<FixedAssets>> pageList(FixedAssetsPageDto dto) {
        Page<FixedAssets> fixedAssetsPage = fixedAssetsMapper.pageList(dto.build(), dto);

        return Message.ok(fixedAssetsPage);
    }

    @Override
    public Message<FixedAssets> getById(String id) {
        FixedAssets byId = super.getById(id);

        // 触发 getter 自动处理即可（如果你用了 @TableField(exist = false) 且没有被忽略返回）
        byId.setUsageDepartmentList(byId.getUsageDepartmentList());

        return Message.ok(byId);
    }

    @Override
    public Message<String> save(FixedAssetsChangeDto dto) {
        checkNameAndCode(dto, false);

        FixedAssets fixedAssets = BeanUtil.copyProperties(dto, FixedAssets.class);

        if (dto.getUsageDepartmentList() != null) {
            fixedAssets.setUsageDepartment(String.join(",", dto.getUsageDepartmentList()));
        }

        boolean result = super.save(fixedAssets);

        return result ? Message.ok("新增成功") : Message.failed("新增失败");
    }

    @Override
    public Message<String> update(FixedAssetsChangeDto dto) {
        checkNameAndCode(dto, true);

        FixedAssets fixedAssets = BeanUtil.copyProperties(dto, FixedAssets.class);

        if (dto.getUsageDepartmentList() != null) {
            fixedAssets.setUsageDepartment(String.join(",", dto.getUsageDepartmentList()));
        }

        boolean result = super.updateById(fixedAssets);

        return result ? Message.ok("修改成功") : Message.failed("修改失败");
    }

    @Override
    public Message<String> delete(ListIdsDto dto) {
        boolean result = super.removeBatchByIds(dto.getListIds());

        return result ? new Message<>(Message.SUCCESS, "删除成功") : new Message<>(Message.FAIL, "删除失败");
    }

    @Override
    @Transactional
    public Message<String> generateVoucher(GenerateVoucherDto dto) {
        String bookId = dto.getBookId();
        Book book = bookMapper.selectById(bookId);
        String currentTerm = configSysService.getCurrentTerm(bookId);
        int year = Integer.parseInt(currentTerm.split("-")[0]);
        int month = Integer.parseInt(currentTerm.split("-")[1]);

        FixedAssets fixedAssets = super.getById(dto.getId());
        Integer voucherType = dto.getVoucherType();

        if (voucherType == 1 && StringUtils.isNotBlank(fixedAssets.getAssetVoucherId())) {
            return Message.ok("新增资产凭证已生成");
        } else if (voucherType == 2 && StringUtils.isNotBlank(fixedAssets.getCleanUpVoucherId())) {
            return Message.ok("清理资产凭证已生成");
        }

        List<VoucherItemChangeDto> voucherItems = new ArrayList<>();
        if (voucherType == 1) {
            VoucherItemChangeDto voucherItemDto = createVoucherItemDto(bookId, 1, fixedAssets, false);
            VoucherItemChangeDto voucherItemDtoPurchase = createVoucherItemDto(bookId, 1, fixedAssets, true);
            voucherItems.add(voucherItemDto);
            voucherItems.add(voucherItemDtoPurchase);
        } else {
            voucherItems.addAll(createCleanUpVoucherItems(bookId, fixedAssets));
        }

        int todayDay = LocalDate.now().getDayOfMonth();
        String voucherDateStr = currentTerm + "-" + String.format("%02d", todayDay);
        Date voucherDate = Date.from(LocalDate.parse(voucherDateStr)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());

        BigDecimal amount = fixedAssets.getOriginalValue();
        VoucherChangeDto voucherChangeDto = createVoucherChangeDto(book, bookId, voucherDate, year, month, amount);
        if (voucherType == 1) {
            voucherChangeDto.setRemark(String.format("%d年%d月新增资产: %s", year, month, fixedAssets.getAssetName()));
        } else {
            voucherChangeDto.setRemark(String.format("%d年%d月清理资产: %s", year, month, fixedAssets.getAssetName()));
        }
        voucherChangeDto.setItems(voucherItems);
        voucherChangeDto.setStatus(VoucherStatusEnum.DRAFT.getValue());

        voucherService.save(voucherChangeDto);

        LambdaUpdateWrapper<FixedAssets> updateWrapper = new LambdaUpdateWrapper<>();
        if (voucherType == 1) {
            updateWrapper.set(FixedAssets::getAssetVoucherId, voucherChangeDto.getId());
        } else {
            updateWrapper.set(FixedAssets::getCleanUpVoucherId, voucherChangeDto.getId());
        }
        updateWrapper.eq(FixedAssets::getId, dto.getId());
        super.update(updateWrapper);

        return Message.ok(voucherChangeDto.getId());
    }


    private VoucherItemChangeDto createVoucherItemDto(String bookId, Integer voucherType,

                                                      FixedAssets fixedAssets, boolean isPurchase) {
        BookSubject bookSubject;
        VoucherItemChangeDto itemDto = new VoucherItemChangeDto();
        itemDto.setSummary("购入" + fixedAssets.getAssetName());
        if (isPurchase) {
            bookSubject = bookSubjectService.selectSubject(bookId, fixedAssets.getPurchaserSubjectCode());
            itemDto.setCreditAmount(fixedAssets.getOriginalValue());
        } else {
            bookSubject = bookSubjectService.selectSubject(bookId, fixedAssets.getAssetSubjectCode());
            itemDto.setDebitAmount(fixedAssets.getOriginalValue());
        }

        itemDto.setSubjectId(bookSubject.getId());
        itemDto.setSubjectBalance(bookSubject.getBalance());
        itemDto.setAuxiliary(List.of());
        itemDto.setSubjectCode(bookSubject.getCode());
        itemDto.setSubjectName(bookSubject.getCode() + "-" + bookSubject.getName());
        itemDto.setDetailedAccounts("");

        return itemDto;
    }

    private void checkNameAndCode(FixedAssetsChangeDto dto, boolean isEdit) {
        String id = dto.getId();
        String name = dto.getAssetName();
        String code = dto.getCode();
        String bookId = dto.getBookId();

        // 校验名称是否重复
        boolean nameExists = super.lambdaQuery()
                .eq(FixedAssets::getBookId, bookId)
                .eq(FixedAssets::getAssetName, name)
                .ne(isEdit, FixedAssets::getId, id)
                .exists();

        // 校验编码是否重复
        boolean codeExists = super.lambdaQuery()
                .eq(FixedAssets::getBookId, bookId)
                .eq(FixedAssets::getCode, code)
                .ne(isEdit, FixedAssets::getId, id)
                .exists();

        if (nameExists && codeExists) {
            throw new BusinessException(50001, "分类名称和分类编码均已存在");
        } else if (nameExists) {
            throw new BusinessException(50001, "分类名称已存在");
        } else if (codeExists) {
            throw new BusinessException(50001, "分类编码已存在");
        }
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

    private List<VoucherItemChangeDto> createCleanUpVoucherItems(String bookId, FixedAssets fixedAssets) {
        List<VoucherItemChangeDto> items = new ArrayList<>();

        BigDecimal netValue = fixedAssets.getNetValue();
        BigDecimal originalValue = fixedAssets.getOriginalValue();
        BigDecimal depreciationAmount = originalValue.subtract(netValue);
        String summary = "清理" + fixedAssets.getAssetName();

        BookSubject liquidationSubject = bookSubjectService.selectSubject(bookId, fixedAssets.getLiquidationSubjectCode());
        VoucherItemChangeDto debitCleanUp = new VoucherItemChangeDto();
        debitCleanUp.setSubjectId(liquidationSubject.getId());
        debitCleanUp.setSubjectCode(liquidationSubject.getCode());
        debitCleanUp.setSubjectName(liquidationSubject.getCode() + "-" + liquidationSubject.getName());
        debitCleanUp.setSummary(summary);
        debitCleanUp.setDebitAmount(originalValue);
        debitCleanUp.setSubjectBalance(liquidationSubject.getBalance());
        debitCleanUp.setAuxiliary(List.of());
        debitCleanUp.setDetailedAccounts("");
        items.add(debitCleanUp);

        BookSubject depreciationSubject = bookSubjectService.selectSubject(bookId, fixedAssets.getDepreciationSubjectCode());
        VoucherItemChangeDto debitDepreciation = new VoucherItemChangeDto();
        debitDepreciation.setSubjectId(depreciationSubject.getId());
        debitDepreciation.setSubjectCode(depreciationSubject.getCode());
        debitDepreciation.setSubjectName(depreciationSubject.getCode() + "-" + depreciationSubject.getName());
        debitDepreciation.setSummary(summary);
        debitDepreciation.setDebitAmount(depreciationAmount);
        debitDepreciation.setSubjectBalance(depreciationSubject.getBalance());
        debitDepreciation.setAuxiliary(List.of());
        debitDepreciation.setDetailedAccounts("");
        items.add(debitDepreciation);

        BookSubject assetSubject = bookSubjectService.selectSubject(bookId, fixedAssets.getAssetSubjectCode());
        VoucherItemChangeDto creditAsset = new VoucherItemChangeDto();
        creditAsset.setSubjectId(assetSubject.getId());
        creditAsset.setSubjectCode(assetSubject.getCode());
        creditAsset.setSubjectName(assetSubject.getCode() + "-" + assetSubject.getName());
        creditAsset.setSummary(summary);
        creditAsset.setCreditAmount(originalValue);
        creditAsset.setSubjectBalance(assetSubject.getBalance());
        creditAsset.setAuxiliary(List.of());
        creditAsset.setDetailedAccounts("");
        items.add(creditAsset);

        return items;
    }

    @Override
    public Message<String> deleteVoucher(GenerateVoucherDto dto) {
        Integer voucherType = dto.getVoucherType();
        FixedAssets fixedAssets = super.getById(dto.getId());
        LambdaUpdateWrapper<FixedAssets> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(FixedAssets::getId, dto.getId());
        List<String> ids = new ArrayList<>();
        if(voucherType.equals(1)) {
            updateWrapper.set(FixedAssets::getAssetVoucherId, "");
            ids.add(fixedAssets.getAssetVoucherId());
        }else {
            updateWrapper.set(FixedAssets::getCleanUpVoucherId, "");
            ids.add(fixedAssets.getCleanUpVoucherId());
        }
        voucherService.delete(ids, fixedAssets.getBookId());
        super.update(updateWrapper);

        return Message.ok("删除成功！");
    }
}
