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
import com.surpass.entity.BaseSubject;

import lombok.Data;
import lombok.EqualsAndHashCode;


import java.io.Serial;


/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/1/15 18:05
 */
@EqualsAndHashCode(callSuper = true)
@TableName("jbx_book_subject")
@Data
public class BookSubject extends BaseSubject{
    @Serial
    private static final long serialVersionUID = -5652938317535496286L;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    String bookId;

    String originalId;

    /*辅助核算数据: 0-否;1-是*/
    Integer isAuxiliary;

    String belongSubjectId;

    @TableField(exist = false)
    Boolean hasVoucher;
}
