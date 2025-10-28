package com.surpass.persistence.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.invoice.Invoice;
import com.surpass.entity.invoice.dto.InvoiceChangeDto;
import com.surpass.entity.invoice.dto.InvoicePageDto;
import com.surpass.entity.invoice.vo.InvoiceVo;
import com.surpass.entity.voucher.dto.GenerateVoucherDto;

public interface InvoiceService extends IService<Invoice> {
    Message<String> save(InvoiceChangeDto dto);

    Message<Page<Invoice>> pageList(InvoicePageDto dto);

    Message<InvoiceVo> getById(String id);

    Message<String> delete(ListIdsDto dto);

    Message<String> update(InvoiceChangeDto dto);

    Message<String> generateVoucher(GenerateVoucherDto dto);

    Message<String> deleteVoucher(GenerateVoucherDto dto);
}
