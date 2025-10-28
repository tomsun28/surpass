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

import java.io.Serial;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.surpass.entity.BaseEntity;
import lombok.*;

/**
 * 凭证字对象 jbx_voucher_word
 *
 * @author wuyan
 * {@code @date} 2025-01-14
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("jbx_voucher_word")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherWord extends BaseEntity implements Serializable {
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
     * 字头：“收”、“付”、“转”等
     */
    private String wordHead;

    /**
     * 年份
     */
    private Integer wordYear;

    /**
     * 月份
     */
    private Integer wordMonth;

    /**
     * 号码
     */
    private Integer wordNum;

    /**
     * 凭证字，例：收字第2024第0001号
     */
    private String word;

    /**
     * 打印标题
     */
    private String printTitle;

    /**
     * 是否默认
     */
    private Integer isDefault;
}
