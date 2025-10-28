package com.surpass.entity.invoice.vo;

import com.surpass.entity.invoice.InvoiceVoucherItem;
import com.surpass.entity.invoice.InvoiceVoucherTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/8/29 10:01
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class InvoiceVoucherVo extends InvoiceVoucherTemplate {
    @Serial
    private static final long serialVersionUID = -2763053386128933159L;

    private List<InvoiceVoucherItem> items;
}
