package com.surpass.entity.invoice.vo;

import com.surpass.entity.invoice.Invoice;
import com.surpass.entity.invoice.InvoiceItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/8/27 15:46
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class InvoiceVo extends Invoice {
    @Serial
    private static final long serialVersionUID = 2235038481129949328L;

    private List<InvoiceItem> items;
}
