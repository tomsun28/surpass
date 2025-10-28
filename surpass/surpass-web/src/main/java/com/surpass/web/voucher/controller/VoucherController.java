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


package com.surpass.web.voucher.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.Message;
import com.surpass.entity.idm.UserInfo;
import com.surpass.entity.voucher.dto.VoucherChangeDto;
import com.surpass.entity.voucher.dto.VoucherItemPageDto;
import com.surpass.entity.voucher.dto.VoucherPageDto;
import com.surpass.entity.voucher.dto.VoucherSuccessiveQueryDto;
import com.surpass.entity.voucher.vo.VoucherItemVo;
import com.surpass.entity.voucher.dto.VoucherSuccessiveDto;
import com.surpass.entity.voucher.vo.VoucherVo;
import com.surpass.enums.VoucherStatusEnum;
import com.surpass.persistence.service.VoucherService;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * 凭证管理接口
 */

@RestController
@RequestMapping("/voucher")
@Slf4j
@RequiredArgsConstructor
public class VoucherController {
    private final VoucherService voucherService;

    /**
     * 明细账查询
     *
     * @param paramsDto 查询参数
     */
    @GetMapping("/items/fetch")
    public Message<Page<VoucherItemVo>> subLedger(@ParameterObject VoucherItemPageDto paramsDto,
                                                  @CurrentUser UserInfo userInfo) {
        paramsDto.setBookId(userInfo.getBookId());
        return voucherService.subLedger(paramsDto);
    }

    /**
     * 凭证项现金流量查询查询
     *
     * @param paramsDto 查询参数
     */
    @GetMapping("/items/fetch-by-cash-flow")
    public Message<Page<VoucherItemVo>> fetchByCashFlow(@ParameterObject VoucherItemPageDto paramsDto,
                                                        @CurrentUser UserInfo userInfo) {
        paramsDto.setBookId(userInfo.getBookId());
        return voucherService.fetchByCashFlow(paramsDto);
    }

    /**
     * 分页查询
     */
    @GetMapping(value = {"/fetch"})
    public Message<Page<VoucherVo>> fetch(@ParameterObject VoucherPageDto dto,
                                          @CurrentUser UserInfo userInfo) {
        dto.setBookId(userInfo.getBookId());
        log.debug("fetch {}", dto);
        return voucherService.pageList(dto);
    }

    /**
     * 查询某项明细
     */
    @GetMapping("/get/{id}")
    public Message<VoucherVo> getById(@PathVariable(name = "id") String id) {
        return voucherService.queryById(id);
    }

    /**
     * 生成一个可用凭证子号
     *
     * @param head  字头
     * @param year  年份
     * @param month 月份
     * @return 新可用字号
     */
    @GetMapping("/able-word-num")
    public Message<Integer> getAbleWordNum(@RequestParam(name = "head", required = false) String head,
                                           @RequestParam(name = "year", required = false) Integer year,
                                           @RequestParam(name = "month", required = false) Integer month,
                                           @CurrentUser UserInfo userInfo) {
        return voucherService.getAbleWordNum(userInfo.getBookId(), head, year, month);
    }

    /**
     * 暂存
     *
     * @param dto 数据
     */
    @PostMapping("/draft")
    public Message<String> draft(@Validated(value = AddGroup.class) @RequestBody VoucherChangeDto dto,
                                 @CurrentUser UserInfo userInfo) {
        dto.setBookId(userInfo.getBookId());
        dto.setStatus(VoucherStatusEnum.DRAFT.getValue());
        if (StringUtils.isEmpty(dto.getId())) {
            return voucherService.save(dto);
        } else {
            return voucherService.update(dto);
        }
    }

    @PutMapping("/update")
    public Message<String> update(@Validated(value = EditGroup.class) @RequestBody VoucherChangeDto dto,
                                  @CurrentUser UserInfo userInfo) {
        dto.setBookId(userInfo.getBookId());
        return voucherService.update(dto);
    }

    @DeleteMapping("/delete/{ids}")
    public Message<String> delete(@PathVariable(name = "ids") List<String> ids,
                                  @CurrentUser UserInfo userInfo) {
        return voucherService.delete(ids, userInfo.getBookId());
    }

    /**
     * 保存&提交
     *
     * @param dto 数据
     */
    @PostMapping("/submit")
    public Message<String> submit(@Validated @RequestBody VoucherChangeDto dto,
                                  @CurrentUser UserInfo userInfo) {
        dto.setBookId(userInfo.getBookId());
        return voucherService.submit(dto, true);
    }

    /**
     * 批量提交
     *
     * @param ids ids
     */
    @PostMapping("/submit/{ids}")
    public Message<String> submitBatch(@PathVariable(name = "ids") List<String> ids,
                                       @CurrentUser UserInfo userInfo) {
        return voucherService.submitBatch(ids, userInfo.getBookId());
    }

    /**
     * 取消
     *
     * @param ids 凭证ID
     */
    @PutMapping("/cancel/{ids}")
    public Message<Integer> cancelByIds(@PathVariable(name = "ids") List<String> ids,
                                        @CurrentUser UserInfo userInfo) {
        return voucherService.cancelByIds(ids, userInfo.getBookId());
    }

    /**
     * 凭证号连续性检查,返回不连续的凭证列表
     */
    @GetMapping("/successive")
    public Message<List<VoucherSuccessiveDto>> checkSuccessive(@CurrentUser UserInfo userInfo,
                                                               VoucherSuccessiveQueryDto query) {
        query.setBookId(userInfo.getBookId());
        return voucherService.checkSuccessiveAll(userInfo.getBookId());
    }

    /**
     * 凭证连续性-凭证号更新
     */
    @PutMapping("/successive")
    public Message<Void> updateSuccessive(@CurrentUser UserInfo userInfo,
                                          @RequestBody @Validated List<VoucherSuccessiveDto> dtos) {
        for (VoucherSuccessiveDto dto : dtos) {
            dto.setBookId(userInfo.getBookId());
        }
        return voucherService.updateSuccessive(dtos);
    }

    /**
     * 凭证审核
     */
    @PutMapping("/audit/{ids}")
    public Message<Void> audit(@PathVariable(name = "ids") List<String> ids,
                               @CurrentUser UserInfo userInfo) {
        return voucherService.audit(ids, userInfo);
    }

    /**
     * 凭证审核-过账
     */
    @PutMapping("/sender/{ids}")
    public Message<Void> sender(@PathVariable(name = "ids") List<String> ids,
                                @CurrentUser UserInfo userInfo) {
        return voucherService.sender(ids, userInfo);
    }

    /**
     * 凭证审核-主管
     */
    @PutMapping("/manage-audit/{ids}")
    public Message<Void> manageAudit(@PathVariable(name = "ids") List<String> ids,
                                     @CurrentUser UserInfo userInfo) {
        return voucherService.manageAudit(ids, userInfo);
    }

    /**
     * 导出功能
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response,
                       @ParameterObject VoucherPageDto dto,
                       @CurrentUser UserInfo userInfo) throws IOException {
        dto.setBookId(userInfo.getBookId());
        voucherService.export(dto, response);
    }
}
