package com.surpass.entity.invoice.dto;

import com.surpass.entity.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/8/27 10:13
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class InvoicePageDto extends PageQuery {
    @Serial
    private static final long serialVersionUID = -3749938938751678116L;

    private String bookId;

    private String startDate;

    private String endDate;

    private String direction;

    private String invoiceCode;

    private String invoiceNo;

    private String invoiceType;
}
