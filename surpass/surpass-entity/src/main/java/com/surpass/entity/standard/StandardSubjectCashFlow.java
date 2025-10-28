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


package com.surpass.entity.standard;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.surpass.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/4/18 10:05
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "jbx_standard_subject_cash_flow")
public class StandardSubjectCashFlow extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1705305465180125164L;

    @TableId(type = IdType.ASSIGN_ID)
    String id;

    /**
     * 现金流量项编码
     */
    String itemCode;

    /**
     * 科目编码
     */
    String subjectCode;

    /**
     * 余额方向
     */
    String direction;

    /**
     * 准则ID
     */
    String standardId;

    /**
     * 账套ID
     */
    String bookId;

    /**
     * 是否为模板:0-否;1-是
     */
    Integer isTemplate;
}
