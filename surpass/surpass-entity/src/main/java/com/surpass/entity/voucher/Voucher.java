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
import com.surpass.enums.VoucherStatusEnum;
import com.surpass.util.DateUtils;
import com.surpass.util.excel.ExcelExportCfg;
import lombok.*;
import com.surpass.entity.BaseEntity;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 凭证记录对象 jbx_voucher
 *
 * @author wuyan
 * {@code @date} 2025-01-14
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("jbx_voucher")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voucher extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 凭证字
     */
    private String word;

    /**
     * 字头：“收”、“付”、“转”等
     */
    private String wordHead;

    /**
     * 号码
     */
    private Integer wordNum;

    /**
     * 所属账套
     */
    private String bookId;

    /**
     * 公司ID
     */
    private String companyId;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 附单据数量
     */
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
    @JsonFormat(pattern = DateUtils.FORMAT_DATE_DEFAULT, timezone = "GMT+8")
    @ExcelExportCfg(dateFormat = DateUtils.FORMAT_DATE_DEFAULT)
    private Date voucherDate;

    /**
     * 是否结转损益:y|n
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
    @ExcelExportCfg(dateFormat = DateUtils.FORMAT_DATE_YYYY_MM_DD_HH_MM_SS)
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
    @ExcelExportCfg(dateFormat = DateUtils.FORMAT_DATE_YYYY_MM_DD_HH_MM_SS)
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
    @JsonFormat(pattern = DateUtils.FORMAT_DATE_YYYY_MM_DD_HH_MM_SS, timezone = "GMT+8")
    @ExcelExportCfg(dateFormat = DateUtils.FORMAT_DATE_YYYY_MM_DD_HH_MM_SS)
    private Date managerDate;

    /**
     * 状态：暂存 - draft,审核中 - reviewing，已完成 - completed，被拒绝 - rejected，已取消 - cancelled
     */
    @ExcelExportCfg(enumClass = VoucherStatusEnum.class)
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标记
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value = "n", delval = "y")
    private String deleted;
}
