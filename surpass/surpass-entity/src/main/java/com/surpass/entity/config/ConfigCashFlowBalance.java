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


package com.surpass.entity.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.surpass.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.math.BigDecimal;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/3/18 17:39
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("jbx_config_cash_flow_balance")
@NoArgsConstructor
public class ConfigCashFlowBalance extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 5544663576920519176L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 排序序号
     */
    private Integer sortIndex;

    /**
     * 财务项目的名称
     */
    private String itemName;

    /**
     * 财务项目的助记码
     */
    private String itemCode;

    /**
     * 是否为计算行 0-否;1-是
     */
    private Integer isResult;

    /**
     * 是否可编辑金额 0-否;1-是
     */
    private Integer isEdit;

    /**
     * 是否为标题项目 0-否;1-是
     */
    private Integer isTitle;

    /**
     * 本年累计金额
     */
    private BigDecimal balance;

    /**
     * 账套ID
     */
    private String bookId;

    /**
     * 是否为主表选择项目 0-否;1-是
     */
    private Integer isMain;

    /**
     * 是否为补充资料选择项目 0-否;1-是
     */
    private Integer isAdditional;

    /**
     * 是否为补充资料选择项目 1-借;2-贷
     */
    private Integer direction;

    public ConfigCashFlowBalance(String itemName, String itemCode) {
        this.itemName = itemName;
        this.itemCode = itemCode;
    }
}
