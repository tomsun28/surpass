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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 凭证记录编辑对象
 *
 * @author wuyan
 * {@code @date} 2025-01-14
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VoucherChangeDto {
    /**
     * 主键
     */
    @NotNull(message = "编辑对象不能为空", groups = {EditGroup.class})
    private String id;

    /**
     * 凭证字
     */
    @NotNull(message = "凭证字不能为空", groups = {EditGroup.class})
    private String word;

    /**
     * 字头：“收”、“付”、“转”等
     */
    @NotNull(message = "凭证字头不能为空", groups = {AddGroup.class})
    private String wordHead;

    /**
     * 号码
     */
    @NotNull(message = "凭证字号码不能为空", groups = {AddGroup.class})
    private Integer wordNum;

    /**
     * 所属账套
     */
    @NotEmpty(message = "所属账套不能为空", groups = {AddGroup.class, EditGroup.class})
    private String bookId;

    /**
     * 公司ID
     */
    private String companyId;

    /**
     * 公司名称
     */
    @NotEmpty(message = "公司名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String companyName;

    /**
     * 附单据数量
     */
    @NotNull(message = "附单据数量不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer receiptNum;

    /**
     * 借方总金额（元）
     */
    private BigDecimal debitAmount;

    /**
     * 贷方总金额（元）
     */
    private BigDecimal creditAmount;

    /**
     * 年份
     */
    private Integer voucherYear;

    /**
     * 月份
     */
    private Integer voucherMonth;

    /**
     * 日期
     */
    @NotNull(message = "日期不能为空", groups = {AddGroup.class, EditGroup.class})
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date voucherDate;

    /**
     * 是否结转损益：y|n
     */
    private String carryForward;

    /**
     * 审核人ID
     */
    private String auditMemberId;

    /**
     * 审核人姓名
     */
    private String auditMemberName;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auditDate;

    /**
     * 过账人ID
     */
    private String senderId;

    /**
     * 过账人姓名
     */
    private String senderName;

    /**
     * 过账操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date senderDate;

    /**
     * 主管ID
     */
    private String managerId;

    /**
     * 主管姓名
     */
    private String managerName;

    /**
     * 主管操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date managerDate;

    /**
     * 状态：暂存 - draft,审核中 - reviewing，已完成 - completed，被拒绝 - rejected，已取消 - cancelled
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 凭证明细记录
     */
    @NotNull(message = "凭证明细不能为空", groups = {AddGroup.class, EditGroup.class})
    private List<VoucherItemChangeDto> items;
}
