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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.surpass.entity.Message;
import com.surpass.entity.idm.UserInfo;
import com.surpass.entity.voucher.Voucher;
import com.surpass.entity.voucher.dto.VoucherChangeDto;
import com.surpass.entity.voucher.dto.VoucherItemPageDto;
import com.surpass.entity.voucher.dto.VoucherPageDto;
import com.surpass.entity.voucher.dto.VoucherSuccessiveQueryDto;
import com.surpass.entity.voucher.vo.VoucherItemVo;
import com.surpass.entity.voucher.dto.VoucherSuccessiveDto;
import com.surpass.entity.voucher.vo.VoucherVo;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public interface VoucherService extends IService<Voucher> {

    Message<VoucherVo> queryById(String id);

    Message<Page<VoucherVo>> pageList(VoucherPageDto dto);

    Message<String> save(VoucherChangeDto dto);

    Message<String> update(VoucherChangeDto dto);

    Message<String> delete(List<String> ids, String bookId);

    Message<Integer> getAbleWordNum(String bookId, String head, Integer year, Integer month);

    Message<String> submit(VoucherChangeDto dto, boolean update);

    Message<Integer> cancelByIds(List<String> ids, String bookId);

    Message<Page<VoucherItemVo>> subLedger(VoucherItemPageDto paramsDto);

    Message<Page<VoucherItemVo>> fetchByCashFlow(VoucherItemPageDto paramsDto);

    Message<List<VoucherSuccessiveDto>> checkSuccessive(VoucherSuccessiveQueryDto query);

    /**
     * 规整所有凭证
     *
     * @param bookId 账簿ID
     * @return 结果
     */
    Message<List<VoucherSuccessiveDto>> checkSuccessiveAll(String bookId);

    Message<Void> updateSuccessive(List<VoucherSuccessiveDto> dtos);

    Message<Void> audit(List<String> ids, UserInfo userInfo);

    Message<Void> sender(List<String> ids, UserInfo userInfo);

    Message<Void> manageAudit(List<String> ids, UserInfo userInfo);

    void export(VoucherPageDto dto, HttpServletResponse response) throws IOException;

    boolean deleteByBookIds(List<String> bookIds);

    Message<String> submitBatch(List<String> ids, String bookId);

    boolean recoverDraft(String bookId);
}
