package com.surpass.entity.fa.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/7/9 16:23
 */

@Data
public class ProvisionDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date voucherDate;

    private String wordHead;

    private String summary;

    private String bookId;
}
