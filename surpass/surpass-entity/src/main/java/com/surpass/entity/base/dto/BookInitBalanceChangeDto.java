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


package com.surpass.entity.base.dto;

import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 初始余额编辑对象
 *
 * @author wuyan
 * {@code @date} 2025-03-11
 */

@Data
public class BookInitBalanceChangeDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "编辑对象不能为空", groups = {EditGroup.class})
    String id;
    String originId;

    String bookId;

    @NotNull(message = "科目类型不能为空", groups = {AddGroup.class, EditGroup.class})
    Integer category;

    @NotEmpty(message = "科目编码不能为空", groups = {AddGroup.class, EditGroup.class})
    String code;

    @NotEmpty(message = "科目名称不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(max = 21, message = "科目名称的长度不能超过21位", groups = {AddGroup.class, EditGroup.class})
    String name;

    @NotNull(message = "余额方向不能为空", groups = {AddGroup.class, EditGroup.class})
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
    private BigDecimal openingYearBalanceDebit;

    /**
     * 年初余额（贷方）
     */
    private BigDecimal openingYearBalanceCredit;

    /**
     * 借方总金额（元）
     */
    BigDecimal debitAmount;

    /**
     * 贷方总金额（元）
     */
    BigDecimal creditAmount;

    /**
     * 单位
     */
    String unit;

    /**
     * 辅助核算类型，存在则为辅助核算项
     */
    String assistType;

    /**
     * 辅是否为现金流量
     */
    Integer isCash;
}
