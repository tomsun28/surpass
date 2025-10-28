package com.surpass.entity.invoice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/8/29 10:38
 */

@Data
public class InvoiceDefaultDto {
    @NotEmpty(message = "ID不能为空")
    private String id;

    @NotEmpty(message = "发票方向不能为空")
    private String direction;

    private String bookId;
}
