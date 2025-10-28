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


package com.surpass.persistence.mapper;

import com.surpass.entity.standard.StandardStatementBalanceSheet;
import com.surpass.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StandardStatementBalanceSheetMapper extends BaseMapperPlus<StandardStatementBalanceSheetMapper, StandardStatementBalanceSheet, StandardStatementBalanceSheet> {

    /**
     * 增加 sort_index 的方法
     *
     * @param standardId 准则ID
     * @param sortIndex  行号
     * @param id         ID
     */
    int incrementSortIndex(@Param("standardId") String standardId,
                           @Param("sortIndex") int sortIndex,
                           @Param("id") String id);

    /**
     * 减少 sort_index 的方法
     *
     * @param standardId 准则ID
     * @param sortIndex  行号
     * @param id         ID
     */
    int decrementSortIndex(@Param("standardId") String standardId,
                           @Param("sortIndex") int sortIndex,
                           @Param("id") String id);

    /**
     * 增加 code 的方法
     *
     * @param standardId 准则ID
     * @param itemCode   code编码
     * @param id         ID
     */
    int incrementItemCode(@Param("standardId") String standardId,
                          @Param("itemCode") String itemCode,
                          @Param("id") String id);

    /**
     * 减少 code 的方法
     *
     * @param standardId 准则ID
     * @param itemCode   code编码
     * @param id         ID
     */
    int decrementItemCode(@Param("standardId") String standardId,
                          @Param("itemCode") String itemCode,
                          @Param("id") String id);
}
