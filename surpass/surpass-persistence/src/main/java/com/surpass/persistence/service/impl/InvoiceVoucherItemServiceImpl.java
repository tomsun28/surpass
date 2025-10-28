package com.surpass.persistence.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.invoice.InvoiceVoucherItem;
import com.surpass.persistence.mapper.InvoiceVoucherItemMapper;
import com.surpass.persistence.service.InvoiceVoucherItemService;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/8/28 11:28
 */

@Service
public class InvoiceVoucherItemServiceImpl extends ServiceImpl<InvoiceVoucherItemMapper, InvoiceVoucherItem> implements InvoiceVoucherItemService {
}
