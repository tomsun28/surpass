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


package com.surpass.entity.base;

import com.baomidou.mybatisplus.annotation.*;
import com.surpass.entity.BaseEntity;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 初期余额表 jbx_book_init_balance
 *
 * @author Wuyan
 * {@code @date} 2025-03-11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("jbx_book_init_balance")
public class BookInitBalance extends BaseEntity implements Serializable {
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
     * 种类
     */
    Integer category;

    /**
     * 科目编码
     */
    String code;

    /**
     * 科目名称
     */
    String name;

    /**
     * 借贷方向
     */
    String direction;

    /**
     * 上级目录
     */
    String parentId;

    /**
     * 编码路径
     */
    String idPath;

    /**
     * 级别
     */
    Integer level;

    /**
     * 余额
     */
    BigDecimal balance;

    /**
     * 年初余额（借方）
     */
    BigDecimal openingYearBalanceDebit;

    /**
     * 年初余额（贷方）
     */
    BigDecimal openingYearBalanceCredit;

    /**
     * 本年累计借方总金额（元）
     */
    BigDecimal debitAmount;

    /**
     * 本年累计贷方总金额（元）
     */
    BigDecimal creditAmount;

    /**
     * 单位
     */
    String unit;

    /**
     * 是否为现金类科目
     */
    Integer isCash;

    /**
     * 辅助核算类型，存在则为辅助核算项
     */
    String assistType;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value="n",delval="y")
    String deleted;

}
