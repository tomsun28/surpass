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


package com.surpass.persistence.service;

import com.surpass.entity.Message;
import com.surpass.entity.book.BookSubject;
import com.surpass.entity.book.Settlement;
import com.surpass.entity.statement.StatementSubjectBalance;
import com.surpass.entity.voucher.VoucherAuxiliary;
import com.surpass.enums.StatementSymbolEnum;
import com.surpass.enums.SubjectDirectionEnum;

import java.math.BigDecimal;
import java.util.List;

/**
 * 科目余额表业务接口
 */
public interface StatementSubjectBalanceService {
    StatementSubjectBalance getSubjectBalance(String bookId, String subjectCode);

    public Message<StatementSubjectBalance> getSubjectBalance(StatementSubjectBalance params);

    boolean hasVoucher(String bookId, List<String> codes);

    boolean hasVoucherByCodes(String bookId, List<String> codes);

    List<StatementSubjectBalance> selectIsVoucherByCode(String bookId, List<String> codes);

    void save(BookSubject subject);

    void update(BookSubject subject);

    /**
     * 更新余额
     *
     * @param bookSubject 科目
     * @param balance     变动金额
     * @param symbol      操作数
     * @param direction   方向
     * @param auxiliaries 辅助核算配置
     * @param yearPeriod  账期
     */
    void update(BookSubject bookSubject, BigDecimal balance,
                StatementSymbolEnum symbol, SubjectDirectionEnum direction,
                List<VoucherAuxiliary> auxiliaries, String yearPeriod);

    void delete(List<String> listIds);

    boolean deleteByBookIds(List<String> bookIds);

    StatementSubjectBalance create(BookSubject subject, String currentTerm);

    boolean checkout(Settlement dto) ;

    List<StatementSubjectBalance> selectSubjectBalance(String bookId, List<String> subjectCodes);

    boolean recoverInitial(String bookId);
}
