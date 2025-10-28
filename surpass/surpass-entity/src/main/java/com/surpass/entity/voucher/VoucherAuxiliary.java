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


package com.surpass.entity.voucher;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 凭证明细辅助核算关联对象 jbx_voucher_auxiliary
 *
 * @author wuyan
 * {@code @date} 2025-02-23
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("jbx_voucher_auxiliary")
public class VoucherAuxiliary implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 所属账套
     */
    private String bookId;

    /**
     * 凭证ID
     */
    private String voucherId;

    /**
     * 凭证明细ID
     */
    private String voucherItemId;

    /**
     * 辅助核算类型
     */
    private String auxiliary;

    /**
     * 辅助核算类型名称
     */
    private String auxiliaryName;

    /**
     * 核算对象ID
     */
    private String itemId;

    /**
     * 核算对象名称
     */
    private String itemName;

    @TableField(exist = false)
    private String itemCode;
}
