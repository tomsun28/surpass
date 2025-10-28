package com.surpass.entity.invoice.dto;

import com.surpass.entity.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/8/29 9:57
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class InvoiceVoucherPageDto extends PageQuery {
    @Serial
    private static final long serialVersionUID = -4342508957896373014L;

    private String bookId;

    private String direction;

    private String templateName;
}
