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


package com.surpass.entity.book;

import com.baomidou.mybatisplus.annotation.*;
import com.surpass.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2024/12/31 9:54
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("jbx_settlement")
public class Settlement extends BaseEntity implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 5892695992406543326L;

	@TableId(type = IdType.ASSIGN_ID)
    String id;

    /**
     * 账套
     */
    String bookId;

    /**
     * 所属年份
     */
    int year;
    /**
     * 账期
     */
    String yearPeriod;

    /**
     * 期末余额
     */
    BigDecimal endingBalance;

    /**
     * 状态
     */
    Integer status;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic(value="n",delval="y")
    String deleted;

    @TableField(exist = false)
    String period;

    /**
     * 当前期
     */
    @TableField(exist = false)
    String currentTerm;

    /**
     * 下前期
     */
    @TableField(exist = false)
    String nextTerm;

}
