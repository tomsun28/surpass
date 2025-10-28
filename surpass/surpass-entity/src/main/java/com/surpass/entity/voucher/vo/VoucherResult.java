package com.surpass.entity.voucher.vo;

import com.surpass.entity.voucher.dto.VoucherItemChangeDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/8/29 16:37
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherResult {
    private List<VoucherItemChangeDto> items;

    private BigDecimal totalDebit;

    private BigDecimal totalCredit;

    private boolean balanced;
}
