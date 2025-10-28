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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.surpass.entity.BaseEntity;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 凭证明细对象 jbx_voucher_item
 *
 * @author wuyan
 * {@code @date} 2025-01-14
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("jbx_voucher_item")
public class VoucherItem extends BaseEntity implements Serializable {
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
     * 业务日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date voucherDate;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 会计科目ID
     */
    private String subjectId;

    /**
     * 科目名称
     */
    private String subjectName;

    /**
     * 科目编号
     */
    private String subjectCode;

    /**
     * 借方金额
     */
    private BigDecimal debitAmount;

    /**
     * 贷方金额
     */
    private BigDecimal creditAmount;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 期初累计借方
     */
    private BigDecimal cumulativeDebit;

    /**
     * 期初累计贷方
     */
    private BigDecimal cumulativeCredit;

    /**
     * 科目余额
     */
    private BigDecimal subjectBalance;

    /**
     * 结转损益
     */
    private Integer carryForward;

    /**
     * 删除标记
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;
}
