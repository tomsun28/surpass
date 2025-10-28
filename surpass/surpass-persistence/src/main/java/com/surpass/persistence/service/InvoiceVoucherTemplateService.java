package com.surpass.persistence.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.invoice.InvoiceVoucherTemplate;
import com.surpass.entity.invoice.dto.InvoiceChangeDto;
import com.surpass.entity.invoice.dto.InvoiceDefaultDto;
import com.surpass.entity.invoice.dto.InvoiceVoucherChangeDto;
import com.surpass.entity.invoice.dto.InvoiceVoucherPageDto;
import com.surpass.entity.invoice.vo.InvoiceVoucherVo;

/**
 * @author 24096
 */
public interface InvoiceVoucherTemplateService extends IService<InvoiceVoucherTemplate> {
    Message<String> save(InvoiceVoucherChangeDto dto);

    Message<Page<InvoiceVoucherTemplate>> pageList(InvoiceVoucherPageDto dto);

    Message<String> setDefault(InvoiceDefaultDto dto);

    Message<String> delete(ListIdsDto dto);

    Message<InvoiceVoucherVo> getById(String id);

    Message<String> update(InvoiceVoucherChangeDto dto);
}
