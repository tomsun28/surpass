package com.surpass.persistence.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.invoice.InvoiceItem;
import com.surpass.persistence.mapper.InvoiceItemMapper;
import com.surpass.persistence.service.InvoiceItemService;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/8/22 9:48
 */

@Service
public class InvoiceItemServiceImpl extends ServiceImpl<InvoiceItemMapper, InvoiceItem> implements InvoiceItemService {

}
