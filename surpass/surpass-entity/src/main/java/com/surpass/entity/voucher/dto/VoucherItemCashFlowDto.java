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


package com.surpass.entity.voucher.dto;

import com.surpass.entity.voucher.VoucherItemCashFlow;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/3/26 18:24
 */

@Data
public class VoucherItemCashFlowDto {

    @Valid
    @NotEmpty(message = "入参不能为空")
    private List<VoucherItemCashFlow> voucherItemCashFlowDtos;

    private String bookId;

    private String voucherId;

    private Boolean isEdit;

    private Integer cashFlowItemType;

    private String voucherDate;
}
