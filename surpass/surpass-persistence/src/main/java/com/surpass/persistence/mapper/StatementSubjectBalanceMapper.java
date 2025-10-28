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

import com.surpass.entity.statement.StatementSubjectBalance;
import com.surpass.entity.statement.dto.StatementParamsDto;
import com.surpass.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StatementSubjectBalanceMapper extends BaseMapperPlus<StatementSubjectBalanceMapper, StatementSubjectBalance, StatementSubjectBalance> {
    List<StatementSubjectBalance> groupCodeSubjectBalance(@Param("dto") StatementParamsDto dto,
                                                          @Param("allMonths") List<String> allMonths,
                                                          @Param("minPeriod") String minPeriod,
                                                          @Param("maxPeriod") String maxPeriod);

    List<StatementSubjectBalance> getGeneralLedger(@Param("dto") StatementParamsDto dto,
                                                          @Param("allMonths") List<String> allMonths,
                                                          @Param("minPeriod") String minPeriod,
                                                          @Param("maxPeriod") String maxPeriod);

    List<StatementSubjectBalance> getTAccountDetails(@Param("dto") StatementParamsDto dto,
                                                   @Param("allMonths") List<String> allMonths,
                                                   @Param("minPeriod") String minPeriod,
                                                   @Param("maxPeriod") String maxPeriod);

    /**
     * 获取期初余额
     * @param dto 查询参数
     * @param startMonth 开始月份，格式：YYYY-MM
     * @return 期初余额列表
     */
    List<StatementSubjectBalance> getOpeningBalance(@Param("dto") StatementParamsDto dto, @Param("startMonth") String startMonth);

    List<StatementSubjectBalance> getClosingBalance(@Param("dto") StatementParamsDto dto, @Param("endMonth") String endMonth);

    long recoverInitial(@Param("bookId") String bookId,@Param("yearPeriod") String yearPeriod);
}
