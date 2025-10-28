package com.surpass.persistence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.invoice.Invoice;
import com.surpass.entity.invoice.InvoiceItem;
import com.surpass.entity.invoice.InvoiceVoucherItem;
import com.surpass.entity.invoice.InvoiceVoucherTemplate;
import com.surpass.entity.invoice.dto.InvoiceDefaultDto;
import com.surpass.entity.invoice.dto.InvoiceVoucherChangeDto;
import com.surpass.entity.invoice.dto.InvoiceVoucherPageDto;
import com.surpass.entity.invoice.vo.InvoiceVoucherVo;
import com.surpass.enums.BookBusinessExceptionEnum;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.InvoiceVoucherItemMapper;
import com.surpass.persistence.mapper.InvoiceVoucherTemplateMapper;
import com.surpass.persistence.service.InvoiceVoucherTemplateService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/8/28 10:21
 */

@Service
@RequiredArgsConstructor
public class InvoiceVoucherTemplateServiceImpl extends ServiceImpl<InvoiceVoucherTemplateMapper, InvoiceVoucherTemplate> implements InvoiceVoucherTemplateService {

    private final IdentifierGenerator identifierGenerator;

    private final InvoiceVoucherTemplateMapper invoiceVoucherTemplateMapper;

    private final InvoiceVoucherItemMapper invoiceVoucherItemMapper;

    @Override
    @Transactional
    public Message<String> save(InvoiceVoucherChangeDto dto) {
        //判断当期方向是否已经存在凭证模板
        LambdaQueryWrapper<InvoiceVoucherTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InvoiceVoucherTemplate::getBookId, dto.getBookId());
        wrapper.eq(InvoiceVoucherTemplate::getStatus, 1);
        wrapper.eq(InvoiceVoucherTemplate::getDirection, dto.getDirection());
        wrapper.eq(InvoiceVoucherTemplate::getIsDefault, 1);
        long count = super.count(wrapper);
        if (count == 0) {
            dto.setIsDefault(1);
        }

        InvoiceVoucherTemplate invoiceVoucherTemplate = BeanUtil.copyProperties(dto, InvoiceVoucherTemplate.class);
        String templateId = identifierGenerator.nextId(invoiceVoucherTemplate).toString();

        List<InvoiceVoucherItem> items = dto.getItems();
        //批量添加明细
        if (ObjectUtils.isNotEmpty(items)) {
            items.forEach(item -> {
                item.setTemplateId(templateId);
                item.setBookId(dto.getBookId());
            });
            invoiceVoucherItemMapper.insert(items);
        }

        invoiceVoucherTemplate.setId(templateId);
        boolean result = super.save(invoiceVoucherTemplate);
        return result ? Message.ok("新增成功") : Message.failed("新增失败");
    }

    @Override
    public Message<Page<InvoiceVoucherTemplate>> pageList(InvoiceVoucherPageDto dto) {
        Page<InvoiceVoucherTemplate> invoiceVoucherTemplatePage = invoiceVoucherTemplateMapper.pageList(dto.build(), dto);

        return Message.ok(invoiceVoucherTemplatePage);
    }

    @Override
    @Transactional
    public Message<String> setDefault(InvoiceDefaultDto dto) {
        String bookId = dto.getBookId();
        String id = dto.getId();
        String direction = dto.getDirection();

        // 1. 先把同账簿、同方向的模板设为非默认
        LambdaUpdateWrapper<InvoiceVoucherTemplate> clearDefaultWrapper = new LambdaUpdateWrapper<>();
        clearDefaultWrapper.eq(InvoiceVoucherTemplate::getBookId, bookId)
                .eq(InvoiceVoucherTemplate::getDirection, direction)
                .set(InvoiceVoucherTemplate::getIsDefault, 0);
        super.update(clearDefaultWrapper);

        // 2. 再把指定模板设为默认
        LambdaUpdateWrapper<InvoiceVoucherTemplate> setDefaultWrapper = new LambdaUpdateWrapper<>();
        setDefaultWrapper.eq(InvoiceVoucherTemplate::getId, id)
                .set(InvoiceVoucherTemplate::getIsDefault, 1);
        boolean result = super.update(setDefaultWrapper);

        return result ? Message.ok("设定成功") : Message.failed("设定失败");
    }

    @Override
    public Message<String> delete(ListIdsDto dto) {
        List<String> listIds = dto.getListIds();

        //校验是否为活跃状态
        LambdaQueryWrapper<InvoiceVoucherTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InvoiceVoucherTemplate::getStatus, 1);
        wrapper.in(InvoiceVoucherTemplate::getId, listIds);
        long count = super.count(wrapper);
        if (count > 0) {
            throw new BusinessException(
                    BookBusinessExceptionEnum.DISABLE_BEFORE_DELETE.getCode(),
                    BookBusinessExceptionEnum.DISABLE_BEFORE_DELETE.getMsg()
            );
        }

        boolean result = super.removeByIds(listIds);

        if (result) {
            invoiceVoucherItemMapper.delete(Wrappers.<InvoiceVoucherItem>lambdaQuery()
                    .in(InvoiceVoucherItem::getTemplateId, listIds));
        }

        return result ? new Message<>(Message.SUCCESS, "删除成功") : new Message<>(Message.FAIL, "删除失败");
    }

    @Override
    public Message<InvoiceVoucherVo> getById(String id) {

        InvoiceVoucherVo invoiceVoucherVo = new InvoiceVoucherVo();

        InvoiceVoucherTemplate invoiceVoucherTemplate = super.getById(id);
        if (Objects.nonNull(invoiceVoucherTemplate)) {
            BeanUtil.copyProperties(invoiceVoucherTemplate, invoiceVoucherVo);
            List<InvoiceVoucherItem> invoiceVoucherItems = invoiceVoucherItemMapper.selectList(Wrappers.<InvoiceVoucherItem>lambdaQuery()
                    .eq(InvoiceVoucherItem::getTemplateId, invoiceVoucherTemplate.getId())
                    .orderByAsc(InvoiceVoucherItem::getSeqNo));
            invoiceVoucherVo.setItems(invoiceVoucherItems);
        }

        return Message.ok(invoiceVoucherVo);
    }

    @Override
    @Transactional
    public Message<String> update(InvoiceVoucherChangeDto dto) {
        List<InvoiceVoucherItem> newItems = dto.getItems();
        String id = dto.getId();

        // 数据库中已有的
        List<InvoiceVoucherItem> oldItems = invoiceVoucherItemMapper.selectList(
                Wrappers.<InvoiceVoucherItem>lambdaQuery().eq(InvoiceVoucherItem::getTemplateId, id)
        );

        // 将 oldItems 按主键 ID 建 map（方便比对）
        Map<String, InvoiceVoucherItem> oldMap = oldItems.stream()
                .collect(Collectors.toMap(InvoiceVoucherItem::getId, Function.identity()));

        // 将 newItems 按主键 ID 建 map（有的新增可能还没 id，可以先用 null/'' 占位）
        Map<String, InvoiceVoucherItem> newMap = newItems.stream()
                .filter(item -> item.getId() != null && !item.getId().isBlank())
                .collect(Collectors.toMap(InvoiceVoucherItem::getId, Function.identity()));

        // 1️⃣ 找出要删除的（旧有但新没有）
        List<String> toDelete = oldItems.stream()
                .map(InvoiceVoucherItem::getId)
                .filter(idInOld -> !newMap.containsKey(idInOld))
                .toList();

        // 2️⃣ 找出要新增的（新有但 id 为 null，或者旧里没有）
        List<InvoiceVoucherItem> toInsert = newItems.stream()
                .filter(item -> item.getId() == null
                        || item.getId().isBlank())
                .map(item -> {
                    item.setTemplateId(id);
                    item.setBookId(dto.getBookId());
                    return item;
                })
                .toList();

        // 3️⃣ 找出要更新的（id 相同）
        List<InvoiceVoucherItem> toUpdate = newItems.stream()
                .filter(item -> item.getId() != null
                        && !item.getId().isBlank()
                        && oldMap.containsKey(item.getId()))
                .toList();

        // 批量处理
        if (!toDelete.isEmpty()) {
            invoiceVoucherItemMapper.deleteByIds(toDelete);
        }
        if (!toInsert.isEmpty()) {
            invoiceVoucherItemMapper.insert(toInsert);
        }
        if (!toUpdate.isEmpty()) {
            invoiceVoucherItemMapper.updateById(toUpdate);
        }

        InvoiceVoucherTemplate invoiceVoucherTemplate = BeanUtil.copyProperties(dto, InvoiceVoucherTemplate.class);
        boolean result = super.updateById(invoiceVoucherTemplate);

        return result ? Message.ok("修改成功") : Message.failed("修改失败");
    }
}
