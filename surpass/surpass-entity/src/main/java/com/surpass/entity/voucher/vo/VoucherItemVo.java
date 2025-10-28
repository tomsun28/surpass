/*
 * Copyright [2025] [Surpass of copyright http://www.surpass.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


package com.surpass.entity.voucher.vo;

import com.surpass.entity.voucher.VoucherItem;
import com.surpass.entity.voucher.dto.VoucherItemAuxiliaryDto;

import lombok.*;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.List;

/**
 * 凭证明细视图对象
 *
 * @author wuyan
 * {@code @date} 2025-01-14
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class VoucherItemVo extends VoucherItem {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 凭证字号
     */
    private String word;

    /**
     * 辅助核算配置
     */
    private List<VoucherItemAuxiliaryDto> auxiliary;

    /**
     * 辅助核算显示名称
     */
    private String auxiliaryLabel;

    /**
     * 现金流量项
     */
    private String cashFlowItemCode;

    /**
     * 现金流量额
     */
    private BigDecimal cashFlowBalance;

    /**
     * 凭证项目Id
     */
    private String voucherItemId;

    /**
     * 凭证项目类型
     */
    private Integer cashFlowItemType;

    private Integer entryNo;
}
