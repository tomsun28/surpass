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
import com.surpass.entity.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.util.Date;

/**
 * 凭证记录分页查询对象
 *
 * @author wuyan
 * {@code @date} 2025-01-14
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class VoucherPageDto extends PageQuery {
    /**
	 *
	 */
	@Serial
    private static final long serialVersionUID = 346900791952374861L;

	/**
     * 凭证字
     */
    private String word;

    /**
     * 凭证字字头：“收”、“付”、“转”等
     */
    private String wordHead;

    /**
     * 凭证字号码
     */
    private Integer wordNum;

    /**
     * 所属账套
     */
    private String bookId;

    /**
     * 公司名称
     */
    private String companyName;

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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date voucherDate;

    /**
     * 是否结转损益
     */
    private Integer carryForward;

    /**
     * 审核人姓名
     */
    private String auditMemberName;

    /**
     * 过账人姓名
     */
    private String senderName;

    /**
     * 主管姓名
     */
    private String managerName;

    /**
     * 状态：暂存 - Draft,审核中 - Reviewing，已完成 - Completed，被拒绝 - Rejected，已取消 - Cancelled
     */
    private String status;
}
