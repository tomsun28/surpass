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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 凭证明细对象 jbx_books_voucher_item
 *
 * @author wuyan
 * {@code @date} 2025-01-14
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VoucherItemChangeDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @NotNull(message = "编辑对象不能为空", groups = {EditGroup.class})
    private String id;

    /**
     * 凭证ID
     */
    @NotNull(message = "凭证对象不能为空", groups = {AddGroup.class, EditGroup.class})
    private String voucherId;

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date voucherDate;
    /**
     * 摘要
     */
    @NotEmpty(message = "摘要不能为空", groups = {AddGroup.class, EditGroup.class})
    private String summary;

    /**
     * 会计科目ID
     */
    @NotEmpty(message = "会计科目不能为空", groups = {AddGroup.class, EditGroup.class})
    private String subjectId;

    /**
     * 科目名称
     */
    @NotEmpty(message = "会计科目不能为空", groups = {AddGroup.class, EditGroup.class})
    private String subjectName;

    /**
     * 科目编号
     */
    @NotEmpty(message = "科目编号不能为空", groups = {AddGroup.class, EditGroup.class})
    private String subjectCode;

    /**
     * 辅助名称
     */
    private String detailedAccounts;

    /**
     * 二级科目编号
     */
    private String detailedSubjectCode;

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
    private Integer price;

    /**
     * 期初累计借方
     */
    private Integer cumulativeDebit;

    /**
     * 期初累计贷方
     */
    private Integer cumulativeCredit;

    /**
     * 结转损益
     */
    private Integer carryForward;

    /**
     * 科目余额
     */
    private BigDecimal subjectBalance;

    /**
     * 辅助核算配置
     */
    private List<VoucherItemAuxiliaryDto> auxiliary;
}
