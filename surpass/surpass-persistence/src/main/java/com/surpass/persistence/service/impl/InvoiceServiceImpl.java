package com.surpass.persistence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.Message;
import com.surpass.entity.book.Book;
import com.surpass.entity.book.BookSubject;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.fa.FixedAssets;
import com.surpass.entity.invoice.Invoice;
import com.surpass.entity.invoice.InvoiceItem;
import com.surpass.entity.invoice.InvoiceVoucherItem;
import com.surpass.entity.invoice.InvoiceVoucherTemplate;
import com.surpass.entity.invoice.dto.InvoiceChangeDto;
import com.surpass.entity.invoice.dto.InvoicePageDto;
import com.surpass.entity.invoice.vo.InvoiceVo;
import com.surpass.entity.voucher.dto.GenerateVoucherDto;
import com.surpass.entity.voucher.dto.VoucherChangeDto;
import com.surpass.entity.voucher.dto.VoucherItemChangeDto;
import com.surpass.entity.voucher.vo.VoucherResult;
import com.surpass.enums.SubjectDirectionEnum;
import com.surpass.enums.VoucherStatusEnum;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.*;
import com.surpass.persistence.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/8/22 9:47
 */

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl extends ServiceImpl<InvoiceMapper, Invoice> implements InvoiceService {

    private final IdentifierGenerator identifierGenerator;

    private final InvoiceItemMapper invoiceItemMapper;

    private final InvoiceMapper invoiceMapper;

    private final BookMapper bookMapper;

    private final ConfigSysService configSysService;

    private final BookSubjectService bookSubjectService;

    private final VoucherService voucherService;

    private final InvoiceVoucherTemplateMapper invoiceVoucherTemplateMapper;

    private final InvoiceVoucherItemMapper invoiceVoucherItemMapper;

    @Override
    @Transactional
    public Message<String> save(InvoiceChangeDto dto) {
        Invoice invoice = BeanUtil.copyProperties(dto, Invoice.class);
        String invoiceId = identifierGenerator.nextId(invoice).toString();

        List<InvoiceItem> items = dto.getItems();
        //批量添加明细
        if (ObjectUtils.isNotEmpty(items)) {
            items.forEach(item -> item.setInvoiceId(invoiceId));
            invoiceItemMapper.insert(items);
        }

        invoice.setId(invoiceId);
        boolean result = super.save(invoice);
        return result ? Message.ok("新增成功") : Message.failed("新增失败");
    }

    @Override
    public Message<Page<Invoice>> pageList(InvoicePageDto dto) {

        Page<Invoice> invoicePage = invoiceMapper.pageList(dto.build(), dto);

        return Message.ok(invoicePage);
    }

    @Override
    public Message<InvoiceVo> getById(String id) {

        InvoiceVo invoiceVo = new InvoiceVo();

        Invoice invoice = super.getById(id);
        if (Objects.nonNull(invoice)) {
            BeanUtil.copyProperties(invoice, invoiceVo);
            List<InvoiceItem> invoiceItems = invoiceItemMapper.selectList(Wrappers.<InvoiceItem>lambdaQuery()
                    .eq(InvoiceItem::getInvoiceId, invoice.getId()));
            invoiceVo.setItems(invoiceItems);
        }

        return Message.ok(invoiceVo);
    }

    @Override
    @Transactional
    public Message<String> delete(ListIdsDto dto) {
        List<String> ids = dto.getListIds();
        boolean result = super.removeBatchByIds(ids);
        if (result) {
            invoiceItemMapper.delete(Wrappers.<InvoiceItem>lambdaQuery()
                    .in(InvoiceItem::getInvoiceId, ids));
        }
        return result ? new Message<>(Message.SUCCESS, "删除成功") : new Message<>(Message.FAIL, "删除失败");
    }

    @Override
    @Transactional
    public Message<String> update(InvoiceChangeDto dto) {
        List<InvoiceItem> newItems = dto.getItems();
        String id = dto.getId();

        // 数据库中已有的
        List<InvoiceItem> oldItems = invoiceItemMapper.selectList(
                Wrappers.<InvoiceItem>lambdaQuery().eq(InvoiceItem::getInvoiceId, id)
        );

        // 将 oldItems 按主键 ID 建 map（方便比对）
        Map<String, InvoiceItem> oldMap = oldItems.stream()
                .collect(Collectors.toMap(InvoiceItem::getId, Function.identity()));

        // 将 newItems 按主键 ID 建 map（有的新增可能还没 id，可以先用 null/uuid 占位）
        Map<String, InvoiceItem> newMap = newItems.stream()
                .filter(item -> item.getId() != null && !item.getId().isBlank())
                .collect(Collectors.toMap(InvoiceItem::getId, Function.identity()));

        // 1️⃣ 找出要删除的（旧有但新没有）
        List<String> toDelete = oldItems.stream()
                .map(InvoiceItem::getId)
                .filter(idInOld -> !newMap.containsKey(idInOld))
                .toList();

        // 2️⃣ 找出要新增的（新有但 id 为 null）
        List<InvoiceItem> toInsert = newItems.stream()
                .filter(item -> item.getId() == null
                        || item.getId().isBlank())
                .map(item -> {
                    item.setInvoiceId(id);
                    return item;
                })
                .toList();

        // 3️⃣ 找出要更新的（id 相同）
        List<InvoiceItem> toUpdate = newItems.stream()
                .filter(item -> item.getId() != null
                        && !item.getId().isBlank()
                        && oldMap.containsKey(item.getId()))
                .toList();

        // 批量处理
        if (!toDelete.isEmpty()) {
            invoiceItemMapper.deleteByIds(toDelete);
        }
        if (!toInsert.isEmpty()) {
            invoiceItemMapper.insert(toInsert);
        }
        if (!toUpdate.isEmpty()) {
            invoiceItemMapper.updateById(toUpdate);
        }

        Invoice invoice = BeanUtil.copyProperties(dto, Invoice.class);
        boolean result = super.updateById(invoice);

        return result ? Message.ok("修改成功") : Message.failed("修改失败");
    }

    @Override
    @Transactional
    public Message<String> generateVoucher(GenerateVoucherDto dto) {
        String bookId = dto.getBookId();
        Book book = bookMapper.selectById(bookId);
        String currentTerm = configSysService.getCurrentTerm(bookId);
        int year = Integer.parseInt(currentTerm.split("-")[0]);
        int month = Integer.parseInt(currentTerm.split("-")[1]);

        //获取发票
        Invoice invoice = super.getById(dto.getId());
        //获取默认发票模板
        List<InvoiceVoucherTemplate> invoiceVoucherTemplates = invoiceVoucherTemplateMapper.selectList(Wrappers.<InvoiceVoucherTemplate>lambdaQuery()
                .eq(InvoiceVoucherTemplate::getBookId, bookId)
                .eq(InvoiceVoucherTemplate::getStatus, 1)
                .eq(InvoiceVoucherTemplate::getDirection, dto.getDirection())
                .eq(InvoiceVoucherTemplate::getIsDefault, 1));
        if (ObjectUtils.isEmpty(invoiceVoucherTemplates)) {
            throw new BusinessException(50001, "请先生成对应的且已经启用的默认发票凭证模板，再进行凭证生成");
        }
        InvoiceVoucherTemplate invoiceVoucherTemplate = invoiceVoucherTemplates.get(0);

        VoucherResult result = createVoucherItems(bookId, invoice, invoiceVoucherTemplate);
        if (!result.isBalanced()) {
            throw new BusinessException(50001, "借贷不平衡，请调整发票凭证模板");
        }

        int todayDay = LocalDate.now().getDayOfMonth();
        String voucherDateStr = currentTerm + "-" + String.format("%02d", todayDay);
        Date voucherDate = Date.from(LocalDate.parse(voucherDateStr)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());

        BigDecimal amount = result.getTotalCredit();
        //凭证参数-新增发票凭证
        VoucherChangeDto voucherChangeDto = createVoucherChangeDto(book, bookId, invoiceVoucherTemplate.getWordHead(),
                voucherDate, year, month, amount);
        voucherChangeDto.setRemark(String.format("%d年%d月新增发票: %s", year, month, invoice.getInvoiceNo()));
        voucherChangeDto.setItems(result.getItems());
        voucherChangeDto.setStatus(VoucherStatusEnum.DRAFT.getValue());

        int code = voucherService.save(voucherChangeDto).getCode();
        if (code != 0) {
            return Message.failed("生成失败");
        }

        LambdaUpdateWrapper<Invoice> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Invoice::getInvoiceVoucherId, voucherChangeDto.getId());
        updateWrapper.eq(Invoice::getId, dto.getId());
        super.update(updateWrapper);
        return Message.ok(voucherChangeDto.getId());
    }

    @Override
    public Message<String> deleteVoucher(GenerateVoucherDto dto) {
        Invoice invoice = super.getById(dto.getId());
        LambdaUpdateWrapper<Invoice> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Invoice::getId, dto.getId());
        List<String> ids = new ArrayList<>();
        updateWrapper.set(Invoice::getInvoiceVoucherId, "");
        ids.add(invoice.getInvoiceVoucherId());
        voucherService.delete(ids, invoice.getBookId());
        super.update(updateWrapper);

        return Message.ok("删除成功！");
    }

    private VoucherResult createVoucherItems(String bookId, Invoice invoice, InvoiceVoucherTemplate invoiceVoucherTemplate) {
        List<InvoiceVoucherItem> invoiceVoucherItems = invoiceVoucherItemMapper.selectList(
                Wrappers.<InvoiceVoucherItem>lambdaQuery()
                        .eq(InvoiceVoucherItem::getTemplateId, invoiceVoucherTemplate.getId())
        );

        List<VoucherItemChangeDto> voucherItems = new ArrayList<>();
        BigDecimal totalDebit = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;

        for (InvoiceVoucherItem item : invoiceVoucherItems) {
            BookSubject bookSubject = bookSubjectService.selectSubject(bookId, item.getSubjectCode());

            VoucherItemChangeDto dto = new VoucherItemChangeDto();
            dto.setSubjectId(bookSubject.getId());
            dto.setSubjectCode(bookSubject.getCode());
            dto.setSubjectName(bookSubject.getCode() + "-" + bookSubject.getName());

            BigDecimal amount = BigDecimal.ZERO;
            switch (item.getSelectedValue()) {
                case "TOTAL_AMOUNT":
                    amount = invoice.getTotalAmount();
                    break;
                case "TAX_AMOUNT":
                    amount = invoice.getTaxAmount();
                    break;
                case "TOTAL_WITH_TAX":
                    amount = invoice.getTotalWithTax();
                    break;
                default:
                    log.warn("未知的选值: {}" + item.getSelectedValue());
                    break;
            }

            if (SubjectDirectionEnum.DEBIT.getValue().equals(item.getDirection())) {
                dto.setDebitAmount(amount);
                totalDebit = totalDebit.add(amount);
            } else if (SubjectDirectionEnum.CREDIT.getValue().equals(item.getDirection())) {
                dto.setCreditAmount(amount);
                totalCredit = totalCredit.add(amount);
            }

            dto.setSubjectBalance(bookSubject.getBalance());
            dto.setAuxiliary(List.of());
            dto.setDetailedAccounts("");

            // 摘要处理
            String summaryTemplate = item.getSummary();
            String summary;
            if (summaryTemplate != null && summaryTemplate.contains("{发票号码}")) {
                summary = summaryTemplate.replace("{发票号码}", " " + invoice.getInvoiceNo());
            } else {
                summary = summaryTemplate;
            }
            dto.setSummary(summary);

            voucherItems.add(dto);
        }

        boolean balanced = totalDebit.compareTo(totalCredit) == 0;
        return new VoucherResult(voucherItems, totalDebit, totalCredit, balanced);
    }

    private VoucherChangeDto createVoucherChangeDto(Book book, String bookId, String wordHead,
                                                    Date voucherDate, Integer year, Integer month, BigDecimal amount) {

        Integer wordNum = voucherService.getAbleWordNum(bookId, wordHead, null, null).getData();

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

}
