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


package com.surpass.persistence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.Message;
import com.surpass.entity.book.Book;
import com.surpass.entity.book.BookSubject;
import com.surpass.entity.idm.UserInfo;
import com.surpass.entity.voucher.*;
import com.surpass.entity.voucher.dto.*;
import com.surpass.entity.voucher.vo.VoucherItemVo;
import com.surpass.entity.voucher.dto.VoucherSuccessiveDto;
import com.surpass.entity.voucher.vo.VoucherVo;
import com.surpass.enums.*;
import com.surpass.exception.ServiceException;
import com.surpass.persistence.mapper.*;
import com.surpass.persistence.service.ConfigSysService;
import com.surpass.persistence.service.StatementSubjectBalanceService;
import com.surpass.persistence.service.VoucherService;
import com.surpass.persistence.service.BookSubjectService;
import com.surpass.util.DateUtils;
import com.surpass.util.VoucherUtils;
import com.surpass.util.excel.ExcelExporter;
import com.surpass.util.excel.ExcelParams;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 凭证记录Service业务层处理
 *
 * @author wuyan
 * {@code @date} 2025-01-14
 */

@RequiredArgsConstructor
@Service
public class VoucherServiceImpl extends ServiceImpl<VoucherMapper, Voucher> implements VoucherService {

    private final IdentifierGenerator identifierGenerator;
    private final VoucherItemMapper voucherItemMapper;
    private final VoucherWordMapper voucherWordMapper;
    private final VoucherItemAuxiliaryMapper voucherItemAuxiliaryMapper;
    private final UserInfoMapper userInfoMapper;
    private final BookSubjectService bookSubjectService;
    private final StatementSubjectBalanceService subjectBalanceService;
    private final BookMapper bookMapper;
    private final ConfigSysService configSysService;
    private final StandardSubjectCashFlowMapper standardSubjectCashFlowMapper;
    private final VoucherItemCashFlowMapper voucherItemCashFlowMapper;
    private final EmployeeSalarySummaryMapper employeeSalarySummaryMapper;

    @Override
    public Message<Page<VoucherItemVo>> subLedger(VoucherItemPageDto paramsDto) {
        paramsDto.parse();
        return Message.ok(voucherItemMapper.subLedgerPage(paramsDto.build(), paramsDto));
    }

    @Override
    public Message<Page<VoucherItemVo>> fetchByCashFlow(VoucherItemPageDto paramsDto) {
        return Message.ok(voucherItemMapper.fetchByCashFlow(paramsDto.build(), paramsDto));
    }

    @Override
    public Message<List<VoucherSuccessiveDto>> checkSuccessive(VoucherSuccessiveQueryDto query) {
        // 当前期
        String currentTerm = configSysService.getCurrentTerm(query.getBookId());
        Integer year = Integer.valueOf(currentTerm.substring(0, 4));
        Integer month = Integer.valueOf(currentTerm.substring(5, 7));
        // 查询所有符合的凭证
        LambdaQueryWrapper<Voucher> lqw = Wrappers.lambdaQuery();
        lqw.eq(Voucher::getBookId, query.getBookId());
        lqw.eq(Voucher::getWordHead, query.getWordHead());
        lqw.eq(Voucher::getVoucherYear, year);
        lqw.eq(Voucher::getVoucherMonth, month);
        lqw.ge(Voucher::getWordNum, query.getStartWordNumber());
        // 状态设定
        List<String> statusList = new ArrayList<>();
        statusList.add(VoucherStatusEnum.COMPLETED.getValue());
        if (query.getNullify()) {
            statusList.add(VoucherStatusEnum.CANCELLED.getValue());
        }
        lqw.in(Voucher::getStatus, statusList);
        lqw.orderByAsc(Voucher::getVoucherDate, Voucher::getWordNum);
        lqw.select(Voucher::getId, Voucher::getWordNum, Voucher::getWord, Voucher::getWordHead,
                Voucher::getVoucherDate, Voucher::getVoucherYear, Voucher::getVoucherMonth);
        List<Voucher> vouchers = baseMapper.selectList(lqw);
        if (vouchers.isEmpty()) {
            return Message.ok(new ArrayList<>());
        }

        List<VoucherSuccessiveDto> resList = new ArrayList<>();
        int currentNum = query.getStartWordNumber();
        boolean isData = false;
        for (Voucher voucher : vouchers) {
            VoucherSuccessiveDto voucherSuccessiveDto = VoucherSuccessiveDto.builder().build();
            BeanUtils.copyProperties(voucher, voucherSuccessiveDto);
            String sourceWord = VoucherUtils.createWord(voucher.getWordHead(), voucher.getVoucherYear(),
                    voucher.getVoucherMonth(), voucher.getWordNum());
            voucherSuccessiveDto.setSourceWord(sourceWord);

            // 两种方式 1.顺序补齐 2.按日期补齐
            if (VoucherSuccessiveMethodEnum.sequential.name().equals(query.getSuccessiveMethod())) {
                if (!voucher.getWordNum().equals(currentNum)) {
                    isData = true;
                    voucherSuccessiveDto.setWordNum(currentNum);
                    resList.add(voucherSuccessiveDto);
                }
            } else {
                if (!voucher.getWordNum().equals(currentNum)) {
                    isData = true;
                }
                voucherSuccessiveDto.setWordNum(currentNum);
                resList.add(voucherSuccessiveDto);
            }
            String targetWord = VoucherUtils.createWord(voucher.getWordHead(), voucher.getVoucherYear(),
                    voucher.getVoucherMonth(), voucherSuccessiveDto.getWordNum());
            voucherSuccessiveDto.setTargetWord(targetWord);
            currentNum++;
        }
        if (!isData) {
            resList.clear();
        }
        return Message.ok(resList);
    }

    @Override
    public Message<List<VoucherSuccessiveDto>> checkSuccessiveAll(String bookId) {
        List<VoucherSuccessiveDto> data = new ArrayList<>();
        for (String wordHead : VoucherSuccessiveQueryDto.WORD_HEADS) {
            VoucherSuccessiveQueryDto queryDto = VoucherSuccessiveQueryDto.builder()
                    .wordHead(wordHead)
                    .successiveMethod(VoucherSuccessiveMethodEnum.sequential.name())
                    .bookId(bookId)
                    .nullify(true)
                    .startWordNumber(1)
                    .build();
            Message<List<VoucherSuccessiveDto>> successiveRes = checkSuccessive(queryDto);
            data.addAll(successiveRes.getData());
        }
        return Message.ok(data);
    }

    /**
     * 更新凭证号
     *
     * @param dtos 更新凭证号
     */

    @Transactional
    @Override
    public Message<Void> updateSuccessive(List<VoucherSuccessiveDto> dtos) {
        Map<String, VoucherSuccessiveDto> maxWordNumMap = new HashMap<>();
        for (VoucherSuccessiveDto dto : dtos) {
            LambdaUpdateWrapper<Voucher> luw = Wrappers.lambdaUpdate();
            luw.eq(Voucher::getId, dto.getId());
            luw.eq(Voucher::getBookId, dto.getBookId());
            luw.set(Voucher::getWordNum, dto.getWordNum());
            luw.set(Voucher::getWord, dto.getTargetWord());
            baseMapper.update(null, luw);

            LambdaQueryWrapper<VoucherWord> wordLqw = Wrappers.lambdaQuery();
            wordLqw.eq(VoucherWord::getBookId, dto.getBookId());
            wordLqw.eq(VoucherWord::getWordHead, dto.getWordHead());
            wordLqw.eq(VoucherWord::getWordYear, dto.getVoucherYear());
            wordLqw.eq(VoucherWord::getWordMonth, dto.getVoucherMonth());
            wordLqw.eq(VoucherWord::getWordNum, dto.getWordNum());
            List<VoucherWord> voucherWords = voucherWordMapper.selectList(wordLqw);
            if (voucherWords.isEmpty()) {
                // 更新最新的凭证字以便后续使用
                VoucherWord nextWord = VoucherWord.builder()
                        .bookId(dto.getBookId())
                        .wordNum(dto.getWordNum())
                        .wordYear(dto.getVoucherYear())
                        .wordMonth(dto.getVoucherMonth())
                        .wordHead(dto.getWordHead())
                        .word(dto.getTargetWord())
                        .printTitle(dto.getTargetWord())
                        .build();
                nextWord.setId(identifierGenerator.nextId(nextWord).toString());
                voucherWordMapper.insert(nextWord);
            }

            // 根据凭证字头找出最大凭证字号
            if (maxWordNumMap.containsKey(dto.getWordHead())) {
                VoucherSuccessiveDto maxWordNumDto = maxWordNumMap.get(dto.getWordHead());
                if (maxWordNumDto.getWordNum() < dto.getWordNum()) {
                    maxWordNumMap.put(dto.getWordHead(), dto);
                }
            } else {
                maxWordNumMap.put(dto.getWordHead(), dto);
            }
        }

        // 移除多余的凭证字
        maxWordNumMap.forEach((wordHead, dto) -> {
            LambdaQueryWrapper<VoucherWord> wordLqw = Wrappers.lambdaQuery();
            wordLqw.eq(VoucherWord::getBookId, dto.getBookId());
            wordLqw.eq(VoucherWord::getWordHead, dto.getWordHead());
            wordLqw.eq(VoucherWord::getWordYear, dto.getVoucherYear());
            wordLqw.eq(VoucherWord::getWordMonth, dto.getVoucherMonth());
            wordLqw.gt(VoucherWord::getWordNum, dto.getWordNum());
            voucherWordMapper.delete(wordLqw);
        });
        return Message.ok(null);
    }

    /**
     * 生成一个可用凭证子号
     *
     * @param head 字头
     * @param year 年份
     * @return 新的可用字号
     */
    @Override
    public Message<Integer> getAbleWordNum(String bookId, String head, Integer year, Integer month) {
        if (year == null) {
            year = Integer.valueOf(DateUtils.format(new Date(), "yyyy"));
        }
        if (month == null) {
            month = Integer.valueOf(DateUtils.format(new Date(), "MM"));
        }
        Integer latestWordNum = getLatestWordNum(bookId, head, year, month);
        if (latestWordNum == null) {
            return new Message<>(Message.SUCCESS, 1);
        } else {
            return new Message<>(Message.SUCCESS, latestWordNum + 1);
        }
    }

    /**
     * 根据ID查询
     *
     * @param id 主键
     * @return 结果
     */
    @Override
    public Message<VoucherVo> queryById(String id) {
        VoucherVo booksVoucherVo = baseMapper.selectVoById(id);
        if (booksVoucherVo == null) {
            return new Message<>(Message.FAIL, "查询对象不存在");
        }
        UserInfo userInfo = userInfoMapper.selectById(booksVoucherVo.getCreatedBy());
        if (userInfo != null) {
            booksVoucherVo.setCreatedName(userInfo.getDisplayName());
        }
        List<VoucherItemVo> booksVoucherItemVos = queryItems(booksVoucherVo.getId());
        booksVoucherVo.setItems(booksVoucherItemVos);
        return new Message<>(Message.SUCCESS, booksVoucherVo);
    }

    /**
     * 分页查询
     *
     * @param dto 分页参数
     * @return 查询结果
     */
    @Override
    public Message<Page<VoucherVo>> pageList(VoucherPageDto dto) {
        LambdaQueryWrapper<Voucher> lqw = buildQueryWrapper(dto);
        Page<VoucherVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        // 更新制单人名称
        if (!result.getRecords().isEmpty()) {
            List<String> userIds = result.getRecords().stream().map(VoucherVo::getCreatedBy).toList();
            Map<String, String> userMap = new HashMap<>();
            userInfoMapper.selectByIds(userIds).forEach(user -> userMap.put(user.getId(), user.getDisplayName()));
            result.getRecords().forEach(t -> t.setCreatedName(userMap.get(t.getCreatedBy())));
        }
        return new Message<>(Message.SUCCESS, result);
    }

    /**
     * 保存&提交
     *
     * @param dto    数据对象
     * @param update 是否更新数据
     * @return 结果
     */
    @Override
    @Transactional
    public Message<String> submit(VoucherChangeDto dto, boolean update) {
        if (StringUtils.isNotBlank(dto.getId())) {
            Voucher voucher = baseMapper.selectById(dto.getId());
            if (voucher == null) {
                return Message.failed("凭证不存在");
            }
            if (!VoucherStatusEnum.DRAFT.getValue().equals(voucher.getStatus())) {
                return Message.failed("凭证已提交，不允许修改");
            }
        }
        if (update) {
            // 先执行暂存操作
            dto.setStatus(VoucherStatusEnum.DRAFT.getValue());
            Message<String> saveRes;
            if (StringUtils.isEmpty(dto.getId())) {
                saveRes = save(dto);
            } else {
                saveRes = update(dto);
            }
            if (saveRes.getCode() != Message.SUCCESS) {
                return saveRes;
            }
            dto.setId(saveRes.getData());
        }

        // 只有当前期的凭证允许提交，因为会影响到余额数据
        String currentTerm = configSysService.getCurrentTerm(dto.getBookId());
        String voucherDate = DateUtils.format(dto.getVoucherDate(), DateUtils.FORMAT_DATE_YYYY_MM);
        if (!currentTerm.equals(voucherDate)) {
            return Message.failed("已暂存，非当前期不允许提交凭证");
        }

        Book book = bookMapper.selectById(dto.getBookId());
        if (VoucherReviewedOnOffEnum.ON.getCode().equals(book.getVoucherReviewed())) {
            // 再提交创建审核信息,分配审批人，创建审批记录
            dto.setStatus(VoucherStatusEnum.UNDER_REVIEW.getValue());
            // Todo 创建审批记录...
        } else {
            // 直接完成
            dto.setStatus(VoucherStatusEnum.COMPLETED.getValue());
        }

        // 重新提交变更，保证余额信息更新
        Message<String> updateResult = update(dto);

        // 根据科目现金流量默认关系添加凭证项和现金流量关系
        if (updateResult.getCode() == Message.SUCCESS && VoucherStatusEnum.COMPLETED.getValue().equals(dto.getStatus())) {
            setVoucherItemCashFlow(dto);
        }

        return updateResult;
    }

    /**
     * 批量提交
     *
     * @param ids    ids
     * @param bookId 账套id
     * @return 批量提交结果
     */
    @Override
    @Transactional
    public Message<String> submitBatch(List<String> ids, String bookId) {
        if (ids.isEmpty()) {
            return Message.failed("请选择要提交的凭证");
        }
        int count = 0;
        // 遍历处理，保证每一个凭证顺序提交
        for (String id : ids) {
            Message<VoucherVo> infoRes = queryById(id);
            if (infoRes.getCode() != Message.SUCCESS) {
                return new Message<>(infoRes.getCode(), infoRes.getMessage());
            }
            VoucherVo voucherVo = infoRes.getData();
            VoucherChangeDto dto = VoucherChangeDto.builder().build();
            BeanUtils.copyProperties(voucherVo, dto);
            List<VoucherItemVo> items = voucherVo.getItems();
            if (items.isEmpty()) {
                continue;
            }

            // 转换凭证项
            List<VoucherItemChangeDto> voucherItemChangeDtos = items.stream().map(item -> {
                VoucherItemChangeDto itemChangeDto = VoucherItemChangeDto.builder().build();
                BeanUtils.copyProperties(item, itemChangeDto);
                return itemChangeDto;
            }).toList();
            dto.setItems(voucherItemChangeDtos);

            Message<String> submit = submit(dto, false);
            if (submit.getCode() != Message.SUCCESS) {
                return new Message<>(submit.getCode(), submit.getMessage() + " 成功提交" + count + "条。");
            }
            count++;
        }

        return new Message<>(Message.SUCCESS, "成功提交" + count + "条凭证, 忽略" + (ids.size() - count) + "条");
    }

    /**
     * 插入数据
     *
     * @param dto 插入对象
     * @return 插入结果
     */
    @Override
    @Transactional
    public Message<String> save(VoucherChangeDto dto) {
        Voucher voucher = Voucher.builder().build();
        BeanUtil.copyProperties(dto, voucher);
        String currentId = identifierGenerator.nextId(voucher).toString();
        voucher.setId(currentId);
        dto.setId(currentId);

        BooksVoucherItemProvider voucherItemProvider = updateItemsAndCount(voucher, dto, currentId, false);
        List<VoucherItem> insertItems = voucherItemProvider.getItems();
        List<VoucherAuxiliary> insertAuxiliary = voucherItemProvider.getAuxiliary();
        //设置条目所属账套
        for (VoucherItem voucherItem : insertItems) {
            voucherItem.setBookId(dto.getBookId());
        }
        //设置辅助所属账套
        for (VoucherAuxiliary voucherAuxiliary : insertAuxiliary) {
            voucherAuxiliary.setBookId(dto.getBookId());
        }
        // 凭证字校验与建立
        // [字头]字第[年份月份]第[号码]号
        String word = VoucherUtils.createWord(voucher.getWordHead(), voucher.getVoucherYear(), voucher.getVoucherMonth(), voucher.getWordNum());
        Integer latestWordNum = getLatestWordNum(dto.getBookId(), voucher.getWordHead(), voucher.getVoucherYear(), voucher.getVoucherMonth());
        boolean isRepeat = latestWordNum != null && voucher.getWordNum() <= latestWordNum;
        if (isRepeat) {
            // 凭证字号码重复，重新生成
            voucher.setWordNum(latestWordNum + 1);
            word = VoucherUtils.createWord(voucher.getWordHead(), voucher.getVoucherYear(), voucher.getVoucherMonth(), voucher.getWordNum());
        }
        voucher.setWord(word);

        // 更新最新的凭证字以便后续使用
        VoucherWord nextWord = VoucherWord.builder()
                .bookId(dto.getBookId())
                .wordNum(voucher.getWordNum())
                .wordYear(voucher.getVoucherYear())
                .wordMonth(voucher.getVoucherMonth())
                .wordHead(voucher.getWordHead())
                .word(word)
                .printTitle(word)
                .build();
        nextWord.setId(identifierGenerator.nextId(nextWord).toString());
        voucherWordMapper.insert(nextWord);

        if (!insertItems.isEmpty()) {
            boolean saveItems = voucherItemMapper.insertBatch(insertItems);
            if (!saveItems) {
                return new Message<>(Message.FAIL, "新增失败：凭证明细");
            }
            if (!insertAuxiliary.isEmpty()) {
                voucherItemAuxiliaryMapper.insertBatch(insertAuxiliary);
            }
        }
        boolean save = super.save(voucher);
        return save
                ? new Message<>(Message.SUCCESS, isRepeat ? "凭证字号重复，已为您重新编号并保存成功！" : "暂存成功", currentId)
                : new Message<>(Message.FAIL, "暂存失败");
    }

    /**
     * 更新信息
     *
     * @param dto 更新对象
     * @return 结果
     */
    @Override
    @Transactional
    public Message<String> update(VoucherChangeDto dto) {
        String currentId = dto.getId();
//        dto.setWord(null);
//        dto.setWordNum(null);
//        dto.setWordHead(null);
//        dto.setVoucherYear(null);
        Voucher currentVoucher = baseMapper.selectById(currentId);

        Voucher booksVoucher = Voucher.builder().build();
        BeanUtil.copyProperties(dto, booksVoucher);

        BooksVoucherItemProvider booksVoucherItemProvider = updateItemsAndCount(booksVoucher, dto, currentId, false);
        List<VoucherItem> insertItems = booksVoucherItemProvider.getItems();
        List<VoucherAuxiliary> insertAuxiliary = booksVoucherItemProvider.getAuxiliary();

        //设置条目所属账套
        for (VoucherItem voucherItem : insertItems) {
            voucherItem.setBookId(dto.getBookId());
        }
        //设置辅助所属账套
        for (VoucherAuxiliary voucherAuxiliary : insertAuxiliary) {
            voucherAuxiliary.setBookId(dto.getBookId());
        }
        // 凭证字校验与建立
        // [字头]字第[年份月份]第[号码]号
        String word = VoucherUtils.createWord(booksVoucher.getWordHead(), booksVoucher.getVoucherYear(), booksVoucher.getVoucherMonth(), booksVoucher.getWordNum());
        Integer latestWordNum = getLatestWordNum(dto.getBookId(), booksVoucher.getWordHead(), booksVoucher.getVoucherYear(), booksVoucher.getVoucherMonth());
        boolean isRepeat = latestWordNum != null && booksVoucher.getWordNum() <= latestWordNum && !currentVoucher.getWord().equals(word);
        if (isRepeat) {
            // 凭证字号码重复，重新生成
            booksVoucher.setWordNum(latestWordNum + 1);
            word = VoucherUtils.createWord(booksVoucher.getWordHead(), booksVoucher.getVoucherYear(), booksVoucher.getVoucherMonth(), booksVoucher.getWordNum());
            // 更新最新的凭证字以便后续使用
            VoucherWord nextWord = VoucherWord.builder()
                    .bookId(dto.getBookId())
                    .wordNum(booksVoucher.getWordNum())
                    .wordYear(booksVoucher.getVoucherYear())
                    .wordMonth(booksVoucher.getVoucherMonth())
                    .wordHead(booksVoucher.getWordHead())
                    .word(word)
                    .printTitle(word)
                    .build();
            nextWord.setId(identifierGenerator.nextId(nextWord).toString());
            voucherWordMapper.insert(nextWord);
        }
        booksVoucher.setWord(word);

        Voucher dataVoucher = queryById(currentId).getData();
        if (!VoucherStatusEnum.DRAFT.getValue().equals(dataVoucher.getStatus())) {
            return new Message<>(Message.FAIL, "当前不允许修改");
        }

        // 删除以前的明细数据
        voucherItemMapper.delete(new LambdaQueryWrapper<VoucherItem>().eq(VoucherItem::getVoucherId, currentId));
        voucherItemAuxiliaryMapper.delete(new LambdaQueryWrapper<VoucherAuxiliary>().eq(VoucherAuxiliary::getVoucherId, currentId));

        // 插入新数据
        if (!insertItems.isEmpty()) {
            // 只有审核完成，才更新余额
            if (VoucherStatusEnum.COMPLETED.getValue().equals(dto.getStatus())) {
                //if (VoucherStatusEnum.COMPLETED.getValue().equals(dto.getStatus())
                //        && YesNoEnum.n.name().equals(booksVoucher.getCarryForward())) {
                updateSubjectBalance(insertItems, insertAuxiliary, false);
            }
            boolean saveItems = voucherItemMapper.insertBatch(insertItems);
            if (!saveItems) {
                return new Message<>(Message.FAIL, "修改失败:凭证明细");
            }
            if (!insertAuxiliary.isEmpty()) {
                voucherItemAuxiliaryMapper.insertBatch(insertAuxiliary);
            }
        }
        boolean update = super.updateById(booksVoucher);
        return update
                ? new Message<>(Message.SUCCESS, isRepeat ? "凭证字号重复，已为您重新编号并保存成功！" : "修改成功", currentId)
                : new Message<>(Message.FAIL, "修改失败");
    }

    /**
     * 审核
     *
     * @param ids      主键组
     * @param userInfo 审核人信息
     */
    @Override
    @Transactional
    public Message<Void> audit(List<String> ids, UserInfo userInfo) {
        List<Voucher> vouchers = baseMapper.selectByIds(ids);
        List<Voucher> auditVouchers = vouchers.stream()
                .filter(item -> VoucherStatusEnum.UNDER_REVIEW.getValue().equals(item.getStatus()))
                .toList();
        for (Voucher auditVoucher : auditVouchers) {
            VoucherVo voucher = queryById(auditVoucher.getId()).getData();
            voucher.setStatus(VoucherStatusEnum.COMPLETED.getValue());
            voucher.setAuditDate(new Date());
            voucher.setAuditMemberId(userInfo.getId());
            voucher.setAuditMemberName(userInfo.getDisplayName());

            // 转换对象
            VoucherChangeDto voucherChangeDto = new VoucherChangeDto();
            BeanUtils.copyProperties(voucher, voucherChangeDto);
            List<VoucherItemVo> itemVos = voucher.getItems();
            List<VoucherItemChangeDto> items = new ArrayList<>();
            for (VoucherItemVo itemVo : itemVos) {
                VoucherItemChangeDto itemChangeDto = new VoucherItemChangeDto();
                BeanUtils.copyProperties(itemVo, itemChangeDto);
                items.add(itemChangeDto);
            }
            voucherChangeDto.setItems(items);
            // 更新余额和数据
            BooksVoucherItemProvider booksVoucherItemProvider = updateItemsAndCount(auditVoucher, voucherChangeDto, auditVoucher.getId(), true);
            List<VoucherItem> insertItems = booksVoucherItemProvider.getItems();
            List<VoucherAuxiliary> insertAuxiliary = booksVoucherItemProvider.getAuxiliary();

            if (YesNoEnum.n.name().equals(voucher.getCarryForward())) {
                updateSubjectBalance(insertItems, insertAuxiliary, false);
            }
            voucherItemMapper.updateBatchById(insertItems);
            super.updateById(voucher);
        }

        return new Message<>(Message.SUCCESS,
                "操作总数：" + ids.size()
                        + "; 成功：" + auditVouchers.size()
                        + "; 失败：" + (vouchers.size() - auditVouchers.size())
                        + "; 不存在项：" + (ids.size() - vouchers.size())
        );
    }

    /**
     * 过账记录
     */
    @Transactional
    @Override
    public Message<Void> sender(List<String> ids, UserInfo userInfo) {
        List<Voucher> vouchers = baseMapper.selectByIds(ids);
        for (Voucher voucher : vouchers) {
            voucher.setSenderId(userInfo.getId());
            voucher.setSenderDate(new Date());
            voucher.setSenderName(userInfo.getDisplayName());
        }
        boolean b = baseMapper.updateBatchById(vouchers);
        return b ? Message.ok(null) : Message.failed("操作失败");
    }

    /**
     * 主管复审
     */
    @Override
    public Message<Void> manageAudit(List<String> ids, UserInfo userInfo) {
        List<Voucher> vouchers = baseMapper.selectByIds(ids);
        for (Voucher voucher : vouchers) {
            voucher.setManagerId(userInfo.getId());
            voucher.setManagerDate(new Date());
            voucher.setManagerName(userInfo.getDisplayName());
        }
        boolean b = baseMapper.updateBatchById(vouchers);
        return b ? Message.ok(null) : Message.failed("操作失败");
    }

    @Override
    public void export(VoucherPageDto dto, HttpServletResponse response) throws IOException {
        List<VoucherVo> data = pageList(dto).getData().getRecords();

        String templatePath = ResourceUtils
                .getURL("classpath:static/export-template/template-voucher.xlsx")
                .getPath();
        // 注意：Windows下getPath()前会带'/'，可做处理
        if (templatePath.startsWith("/")) {
            templatePath = templatePath.substring(1);
        }
        ExcelParams<List<VoucherVo>> paramsObj = ExcelParams.<List<VoucherVo>>builder()
                .httpResponse(response)
                .dataModel(data)
//                .outputDirectory("C:\\Users\\Administrator\\Desktop\\")
//                .outputFileName("voucher_exported_temp.xlsx")
                .enableMergeCells(true)
                .autoSizeColumns(false)
                .recalculateFormulas(true)
                .templateFilePath(templatePath)
                .build();
        ExcelExporter.export(paramsObj);
    }

    /**
     * 根据ID删除
     *
     * @param ids    ID组
     * @param bookId 账簿ID
     * @return 结果
     */
    @Override
    @Transactional
    public Message<String> delete(List<String> ids, String bookId) {
        if (ids == null || ids.isEmpty()) {
            return new Message<>(Message.SUCCESS);
        }
        // 删除之前先取消
        Message<Integer> cancelRes = cancelByIds(ids, bookId);
        if (cancelRes.getCode() != Message.SUCCESS) {
            return new Message<>(Message.FAIL, cancelRes.getMessage());
        }

        // 删除凭证项和现金流量的关系
        var voucherItems = voucherItemMapper.selectList(
                Wrappers.<VoucherItem>lambdaQuery().in(VoucherItem::getVoucherId, ids)
        );
        if (ObjectUtils.isNotEmpty(voucherItems)) {
            var voucherItemIds = voucherItems.stream()
                    .map(VoucherItem::getId)
                    .toList();

            voucherItemCashFlowMapper.delete(
                    Wrappers.<VoucherItemCashFlow>lambdaQuery().in(VoucherItemCashFlow::getVoucherItemId, voucherItemIds)
            );
        }

        // 删除凭证项
        voucherItemMapper.delete(new LambdaUpdateWrapper<VoucherItem>().in(VoucherItem::getVoucherId, ids));
        voucherItemAuxiliaryMapper.delete(new LambdaQueryWrapper<VoucherAuxiliary>().in(VoucherAuxiliary::getVoucherId, ids));

        // 还原科目余额
        LambdaQueryWrapper<Voucher> lqw = Wrappers.lambdaQuery();
        lqw.in(Voucher::getId, ids);
        lqw.eq(Voucher::getBookId, bookId);
        lqw.eq(Voucher::getStatus, VoucherStatusEnum.COMPLETED.getValue());
        List<Voucher> booksVouchers = baseMapper.selectList(lqw);
        if (!booksVouchers.isEmpty()) {
            booksVouchers.forEach(t -> {
                VoucherVo booksVoucher = queryById(t.getId()).getData();
                LambdaQueryWrapper<VoucherAuxiliary> lqwAux = Wrappers.lambdaQuery();
                lqwAux.eq(VoucherAuxiliary::getVoucherId, booksVoucher.getId());
                List<VoucherAuxiliary> insertAuxiliary = voucherItemAuxiliaryMapper.selectList(lqwAux);
                List<VoucherItem> items = booksVoucher.getItems().stream().map(itemVo -> {
                    VoucherItem build = VoucherItem.builder().build();
                    BeanUtil.copyProperties(itemVo, build);
                    return build;
                }).toList();
                if (YesNoEnum.n.name().equals(booksVoucher.getCarryForward())) {
                    updateSubjectBalance(items, insertAuxiliary, true);
                }
            });
        }

        int update = baseMapper.delete(new LambdaUpdateWrapper<Voucher>().in(Voucher::getId, ids));


        return update == ids.size() ? new Message<>(Message.SUCCESS, "删除成功") : new Message<>(Message.FAIL, "删除失败");
    }

    /**
     * 取消
     *
     * @param ids    凭证ID
     * @param bookId 账簿ID
     * @return 结果
     */
    @Override
    @Transactional
    public Message<Integer> cancelByIds(List<String> ids, String bookId) {
        if (ids == null || ids.isEmpty()) {
            return new Message<>(Message.FAIL, "未选择数据对象");
        }

        // 先查询凭证状态
        LambdaQueryWrapper<Voucher> lqw = Wrappers.lambdaQuery();
        lqw.in(Voucher::getId, ids);
        lqw.eq(Voucher::getStatus, VoucherStatusEnum.UNDER_REVIEW.getValue());
        List<Voucher> booksVouchers = baseMapper.selectList(lqw);

        if (!booksVouchers.isEmpty()) {
            booksVouchers.forEach(t -> t.setStatus(VoucherStatusEnum.CANCELLED.getValue()));
            baseMapper.updateBatchById(booksVouchers);
        }
        voucherItemMapper.delete(new LambdaUpdateWrapper<VoucherItem>().in(VoucherItem::getVoucherId, ids));

        // 更新审批记录状态...
        return new Message<>(Message.SUCCESS, booksVouchers.size());
    }

    /**
     * 构建查询条件
     *
     * @param bo 查询参数
     */
    private LambdaQueryWrapper<Voucher> buildQueryWrapper(VoucherPageDto bo) {
        LambdaQueryWrapper<Voucher> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getBookId() != null, Voucher::getBookId, bo.getBookId());
        lqw.eq(bo.getVoucherYear() != null, Voucher::getVoucherYear, bo.getVoucherYear());
        lqw.eq(bo.getVoucherMonth() != null, Voucher::getVoucherMonth, bo.getVoucherMonth());
        lqw.eq(bo.getVoucherDate() != null, Voucher::getVoucherDate, bo.getVoucherDate());
        lqw.likeRight(StringUtils.isNotBlank(bo.getWord()), Voucher::getWord, bo.getWord());
        lqw.like(StringUtils.isNotBlank(bo.getCompanyName()), Voucher::getCompanyName, bo.getCompanyName());
        return lqw;
    }

    /**
     * 新增或更新凭证时使用，用于统计和生成凭证明细
     *
     * @param booksVoucher 凭证对象
     * @param dto          修改对象
     * @param currentId    住建
     * @param isUpdate     操作方式：是否更新操作，更新不重置ID
     * @return 凭证明细
     */
    private BooksVoucherItemProvider updateItemsAndCount(Voucher booksVoucher, VoucherChangeDto dto, String currentId, boolean isUpdate) {
        booksVoucher.setDebitAmount(new BigDecimal(0));
        booksVoucher.setCreditAmount(new BigDecimal(0));
        List<VoucherItemChangeDto> items = dto.getItems();
        List<VoucherAuxiliary> insertAuxiliary = new ArrayList<>();
        List<VoucherItem> insertItems = items.stream().map(t -> {
            if (!isUpdate) {
                String itemId = identifierGenerator.nextId(booksVoucher).toString();
                t.setId(itemId);
            }
            t.setVoucherId(currentId);
            if (t.getDebitAmount() != null) {
                booksVoucher.setDebitAmount(booksVoucher.getDebitAmount().add(t.getDebitAmount()));
            }
            if (t.getCreditAmount() != null) {
                booksVoucher.setCreditAmount(booksVoucher.getCreditAmount().add(t.getCreditAmount()));
            }
            if (StringUtils.isNotBlank(t.getDetailedSubjectCode())) {
                t.setSubjectCode(t.getDetailedSubjectCode());
            } else if (StringUtils.isNotBlank(t.getSubjectCode())) {
                t.setSubjectCode(t.getSubjectName().split("-")[0]);
            }
            t.setVoucherDate(booksVoucher.getVoucherDate());
            VoucherItem item = VoucherItem.builder().build();
            BeanUtil.copyProperties(t, item);

            // 创建辅助核算数据
            List<VoucherItemAuxiliaryDto> auxiliary = t.getAuxiliary();
            auxiliary.stream().filter(auxiliaryDto -> !auxiliaryDto.getValue().isEmpty())
                    .forEach(auxiliaryDto -> auxiliaryDto.getValue()
                            .forEach(auxiliaryValue -> insertAuxiliary.add(VoucherAuxiliary.builder()
                                    .id(identifierGenerator.nextId(booksVoucher).toString())
                                    .bookId(booksVoucher.getBookId())
                                    .voucherId(currentId)
                                    .voucherItemId(t.getId())
                                    .auxiliary(auxiliaryDto.getId())
                                    .auxiliaryName(auxiliaryDto.getLabel())
                                    .itemId(auxiliaryValue.getValue())
                                    .itemName(auxiliaryValue.getLabel())
                                    .build())));

            return item;
        }).toList();
        if (booksVoucher.getVoucherDate() != null) {
            booksVoucher.setVoucherYear(Integer.valueOf(DateUtils.format(booksVoucher.getVoucherDate(), "yyyy")));
            booksVoucher.setVoucherMonth(Integer.valueOf(DateUtils.format(booksVoucher.getVoucherDate(), "MM")));
        }

        return BooksVoucherItemProvider.builder()
                .items(insertItems)
                .auxiliary(insertAuxiliary)
                .build();
    }

    /**
     * 根据凭证ID获取明细
     *
     * @param voucherId ID
     * @return 凭证明细列表
     */
    private List<VoucherItemVo> queryItems(String voucherId) {
        LambdaQueryWrapper<VoucherItem> lqw = Wrappers.lambdaQuery();
        lqw.eq(VoucherItem::getVoucherId, voucherId);
        List<VoucherItemVo> voucherItemVos = voucherItemMapper.selectVoList(lqw);

        LambdaQueryWrapper<VoucherAuxiliary> lqwAux = Wrappers.lambdaQuery();
        lqwAux.eq(VoucherAuxiliary::getVoucherId, voucherId);
        List<VoucherAuxiliary> voucherAuxiliaries = voucherItemAuxiliaryMapper.selectList(lqwAux);

        // 辅助核算数据
        for (VoucherItemVo voucherItemVo : voucherItemVos) {
            List<VoucherItemAuxiliaryDto> auxiliary = new ArrayList<>();
            voucherAuxiliaries.stream()
                    .filter(t -> t.getVoucherItemId().equals(voucherItemVo.getId()))
                    .collect(Collectors.groupingBy(VoucherAuxiliary::getAuxiliary))
                    .forEach((key, value) -> {
                        VoucherItemAuxiliaryDto itemAuxiliaryDto = VoucherItemAuxiliaryDto.builder()
                                .id(key)
                                .label(value.get(0).getAuxiliaryName())
                                .value(new ArrayList<>())
                                .build();
                        value.forEach(t -> itemAuxiliaryDto.getValue()
                                .add(VoucherItemAuxiliaryDto.BooksVoucherItemAuxiliaryValue.builder()
                                        .label(t.getItemName())
                                        .value(t.getItemId())
                                        .build()
                                ));
                        auxiliary.add(itemAuxiliaryDto);
                    });
            voucherItemVo.setAuxiliary(auxiliary);
        }


        return voucherItemVos;
    }

    /**
     * 获取当前最新凭证号,返回空则不存在最新数据
     *
     * @param head  字头
     * @param year  年份
     * @param month 月份
     * @return 凭证号
     */
    private Integer getLatestWordNum(String bookId, String head, Integer year, Integer month) {
        if (StringUtils.isEmpty(head) || year == null) {
            throw new ServiceException("凭证子头或时间参数异常");
        }

        LambdaQueryWrapper<VoucherWord> wordLambdaQueryWrapper = Wrappers.lambdaQuery();
        wordLambdaQueryWrapper.eq(VoucherWord::getBookId, bookId);
        wordLambdaQueryWrapper.eq(VoucherWord::getWordHead, head);
        wordLambdaQueryWrapper.eq(VoucherWord::getWordYear, year);
        wordLambdaQueryWrapper.eq(VoucherWord::getWordMonth, month);
        wordLambdaQueryWrapper.orderByDesc(VoucherWord::getWordNum);
        Page<VoucherWord> page = new Page<>(1, 1);
        Page<VoucherWord> booksVoucherWordPage = voucherWordMapper.selectPage(page, wordLambdaQueryWrapper);
        List<VoucherWord> voucherWordPageRecords = booksVoucherWordPage.getRecords();

        if (!voucherWordPageRecords.isEmpty()) {
            return voucherWordPageRecords.get(0).getWordNum();
        }
        return null;
    }

    /**
     * 凭证明细临时对象
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class BooksVoucherItemProvider {
        /**
         * 凭证明细列表
         */
        private List<VoucherItem> items;

        /**
         * 凭证明细辅助核算配置项
         */
        private List<VoucherAuxiliary> auxiliary;
    }


    /**
     * 更新科目余额
     *
     * @param insertItems     凭证明细
     * @param insertAuxiliary 辅助核算信息
     * @param isCancel        是否取消，true则反向操作，还原科目余额
     */
    @Transactional
    public void updateSubjectBalance(List<VoucherItem> insertItems, List<VoucherAuxiliary> insertAuxiliary, boolean isCancel) {
        if (insertItems.isEmpty()) {
            return;
        }
        List<String> subjectIds = insertItems.stream().map(VoucherItem::getSubjectId).toList();
        List<BookSubject> booksSubjects = bookSubjectService.listByIds(subjectIds);
        Map<String, BookSubject> subjectMap = booksSubjects.stream()
                .collect(Collectors.toMap(BookSubject::getId, item -> item));
        if (CollectionUtils.isNotEmpty(booksSubjects)) {
            insertItems.forEach(item -> {
                List<VoucherAuxiliary> auxiliaries = insertAuxiliary.stream()
                        .filter(auxiliary -> auxiliary.getVoucherItemId().equals(item.getId()))
                        .toList();
                BookSubject setSubject = subjectMap.get(item.getSubjectId());

                // 借方，更新科目余额和科目余额表
                if (item.getDebitAmount() != null && item.getDebitAmount().compareTo(BigDecimal.ZERO) != 0) {
                    if (isCancel) {
                        subjectBalanceService.update(setSubject, item.getDebitAmount(),
                                StatementSymbolEnum.MINUS, SubjectDirectionEnum.DEBIT, auxiliaries,
                                DateUtils.format(item.getVoucherDate(), "yyyy-MM"));
                    } else {
                        subjectBalanceService.update(setSubject, item.getDebitAmount(),
                                StatementSymbolEnum.PLUS, SubjectDirectionEnum.DEBIT, auxiliaries,
                                DateUtils.format(item.getVoucherDate(), "yyyy-MM"));
                    }
                }
                // 贷方，更新科目余额和科目余额表
                else if (item.getCreditAmount() != null && item.getCreditAmount().compareTo(BigDecimal.ZERO) != 0) {
                    if (isCancel) {
                        subjectBalanceService.update(setSubject, item.getCreditAmount(),
                                StatementSymbolEnum.PLUS, SubjectDirectionEnum.CREDIT, auxiliaries,
                                DateUtils.format(item.getVoucherDate(), "yyyy-MM"));
                    } else {
                        subjectBalanceService.update(setSubject, item.getCreditAmount(),
                                StatementSymbolEnum.MINUS, SubjectDirectionEnum.CREDIT, auxiliaries,
                                DateUtils.format(item.getVoucherDate(), "yyyy-MM"));
                    }
                }

            });
        }
    }

    /**
     * {@code @Description:} 根据科目现金流量默认关系添加凭证项和现金流量关系
     * {@code @Param:} [dto]
     * {@code @return:} void
     * {@code @Author:} xZen
     * {@code @Date:} 2025/4/23 9:43
     */
    private void setVoucherItemCashFlow(VoucherChangeDto dto) {
        if (dto == null || StringUtils.isEmpty(dto.getId())) {
            return;
        }

        List<VoucherItemCashFlow> subjectCashFlows = standardSubjectCashFlowMapper.getSubjectCashFlow(dto);

        if (CollectionUtils.isEmpty(subjectCashFlows)) {
            return;
        }

        String bookId = dto.getBookId();
        List<VoucherItemChangeDto> items = dto.getItems();
        List<String> subjectIds = items.stream()
                .map(VoucherItemChangeDto::getSubjectId)
                .toList();
        List<BookSubject> bookSubjects = bookSubjectService.listByIds(subjectIds);

        // 检查凭证中是否包含现金类科目
        boolean hasCashSubject = bookSubjects.stream()
                .anyMatch(subject -> subject.getIsCash() == 1);


        // 如果没有现金类科目，剔除所有主表现金流量项
        if (!hasCashSubject) {
            subjectCashFlows = subjectCashFlows.stream()
                    .filter(flow -> flow.getCashFlowItemType() != 0)
                    .toList();
        }


        for (VoucherItemCashFlow item : subjectCashFlows) {
            // 如果科目方向与现金流方向相同，金额取反
            if (Objects.equals(item.getSubjectDirection(), item.getDirection()) && item.getCashFlowBalance() != null) {
                item.setCashFlowBalance(item.getCashFlowBalance().negate());
            }

            item.setBookId(bookId);
        }

        voucherItemCashFlowMapper.insert(subjectCashFlows);
    }

    /**
     * 删除凭证及相关条目
     */
    @Override
    public boolean deleteByBookIds(List<String> bookIds) {
        //删除凭证
        LambdaQueryWrapper<Voucher> lqw = Wrappers.lambdaQuery();
        lqw.in(Voucher::getBookId, bookIds);
        baseMapper.delete(lqw);
        //删除凭证条目
        LambdaQueryWrapper<VoucherItem> lqwItem = Wrappers.lambdaQuery();
        lqwItem.in(VoucherItem::getBookId, bookIds);
        voucherItemMapper.delete(lqwItem);
        return false;
    }

    /**
     * 当前期凭证全部改为暂存
     */
	@Override
	public boolean recoverDraft(String bookId) {
		//获取当前期
		String currentTerm = configSysService.getCurrentTerm(bookId);
		YearMonth currentTermYearMonth = YearMonth.parse(currentTerm);
		LambdaUpdateWrapper<Voucher> updateWrapper =new LambdaUpdateWrapper<>();
		updateWrapper.set(Voucher::getStatus, VoucherStatusEnum.DRAFT.getValue());
		updateWrapper.eq(Voucher::getBookId, bookId);
		updateWrapper.eq(Voucher::getVoucherYear, currentTermYearMonth.getYear());
		updateWrapper.eq(Voucher::getVoucherMonth, currentTermYearMonth.getMonthValue());
		return this.update(updateWrapper);
	}

}
