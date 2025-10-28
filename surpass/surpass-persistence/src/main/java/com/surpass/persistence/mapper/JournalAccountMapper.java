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

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.entity.journal.JournalAccount;
import com.surpass.entity.journal.dto.JournalAccountPageDto;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface JournalAccountMapper extends BaseMapper<JournalAccount> {
    Page<JournalAccount> pageList(Page page, @Param("dto") JournalAccountPageDto dto);

    /**
     * 收入
     * @param accId
     * @param income
     * @return
     */
    @Update("update jbx_journal_account set balance = balance + #{income} where id = #{accId}")
    public int income(@Param ("accId") String accId,@Param ("income") BigDecimal income);

    /**
     * 支出
     * @param accId
     * @param expenditure
     * @return
     */
    @Update("update jbx_journal_account set balance = balance - #{expenditure} where id = #{accId}")
    public int expenditure(@Param ("accId") String accId,@Param ("expenditure") BigDecimal expenditure);

    /**
     * 转结： 本期期初余额=余额
     * @param bookId
     * @return
     */
    @Update("update jbx_journal_account set opening_balance = balance where book_id = #{bookId}")
    public int checkout(@Param ("bookId") String bookId);


}
