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
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.Message;
import com.surpass.entity.book.BookSubject;
import com.surpass.entity.book.dto.BookChangeDto;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.standard.StandardSubject;
import com.surpass.entity.voucher.VoucherTemplateItem;
import com.surpass.entity.voucher.dto.VoucherTemplateItemChangeDto;
import com.surpass.entity.voucher.dto.VoucherTemplateItemPageDto;
import com.surpass.enums.BookBusinessExceptionEnum;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.BookSubjectMapper;
import com.surpass.persistence.mapper.StandardSubjectMapper;
import com.surpass.persistence.mapper.VoucherTemplateItemMapper;
import com.surpass.persistence.service.VoucherTemplateItemService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2024/12/31 11:15
 */

@Service
public class VoucherTemplateItemServiceImpl extends ServiceImpl<VoucherTemplateItemMapper, VoucherTemplateItem> implements VoucherTemplateItemService {
    @Autowired
    IdentifierGenerator identifierGenerator;

    @Autowired
    VoucherTemplateItemMapper voucherTemplateItemMapper;

    @Autowired
    BookSubjectMapper bookSubjectMapper;

    @Autowired
    StandardSubjectMapper bookStandardSubjectMapper;

    @Override
    public Message<Page<VoucherTemplateItem>> pageList(VoucherTemplateItemPageDto dto) {
        Page<VoucherTemplateItem> page = voucherTemplateItemMapper.pageList(dto.build(), dto);

        return new Message<>(Message.SUCCESS, page);
    }

    @Override
    @Transactional
    public Message<String> save(VoucherTemplateItemChangeDto dto) {

        String primaryId = identifierGenerator.nextId(dto).toString();

        dto.setId(primaryId);

        //账套科目
        //saveSubject(dto);


        //新增账套
        VoucherTemplateItem voucherTemplateItem = new VoucherTemplateItem();
        BeanUtil.copyProperties(dto, voucherTemplateItem);
        boolean saveResult = super.save(voucherTemplateItem);

        return saveResult ? new Message<>(Message.SUCCESS, "新增成功") : new Message<>(Message.FAIL, "新增失败");
    }

    @Override
    @Transactional
    public Message<String> update(VoucherTemplateItemChangeDto dto) {


        //更新账套
    	VoucherTemplateItem voucherTemplateItem = new VoucherTemplateItem();
        BeanUtil.copyProperties(dto, voucherTemplateItem);
        boolean result = super.updateById(voucherTemplateItem);
        return result ? new Message<>(Message.SUCCESS, "修改成功") : new Message<>(Message.FAIL, "修改失败");
    }

    /**
     * @Description: 插入关联账套和科目
     * @Param: [dto]
     * @return: void
     * @Author: xZen
     * @Date: 2025/1/2 15:01
     */
    private void saveSubject(BookChangeDto dto) {
        String standardId = dto.getStandardId();
        List<StandardSubject> standardSubjects = bookStandardSubjectMapper.selectList(Wrappers.<StandardSubject>lambdaQuery()
                .eq(StandardSubject::getStandardId, standardId)
                .eq(StandardSubject::getStatus, 1)
        );

        if (ObjectUtils.isNotEmpty(standardSubjects)) {

            // 创建新数据集合（避免直接修改原始集合）
            List<BookSubject> newSubjects = standardSubjects.stream()
                    .map(original -> {
                        BookSubject setSubject = new BookSubject();
                        setSubject.setBookId(dto.getId());
                        BeanUtil.copyProperties(original, setSubject,
                                "id","deleted", "createdBy", "createdDate", "modifiedBy", "modifiedDate");
                        setSubject.setOriginalId(original.getId());
                        return setSubject;
                    })
                    .toList();

            // 批量插入新数据（确保 Mapper 支持批量插入）
            bookSubjectMapper.insert(newSubjects);

            // 构建 ID 映射关系：原始 ID -> 新 ID
            // `getOriginalId` 返回旧的 ID
            Map<String, String> idMapping = newSubjects.stream()
                    .collect(Collectors.toMap(BookSubject::getOriginalId, BookSubject::getId));

            // 更新新数据集合中的 parent_id
            newSubjects.forEach(subject -> {
                String newParentId = null;
                if (subject.getParentId() != null) {
                    newParentId = idMapping.get(subject.getParentId());
                    subject.setParentId(newParentId);
                }
            });

            bookSubjectMapper.updateById(newSubjects);

            //newSubjects.forEach(subject -> subject.setIdPath(generateIdPath(subject.getParentId(), subject.getId())));

            bookSubjectMapper.updateById(newSubjects);
        }
    }

    @Override
    @Transactional
    public Message<String> delete(ListIdsDto dto) {
        List<String> listIds = dto.getListIds();

        //校验是否为活跃状态
        LambdaQueryWrapper<VoucherTemplateItem> wrapper = new LambdaQueryWrapper<>();
        //wrapper.eq(VoucherTemplateItem::getStatus, 1);
        wrapper.in(VoucherTemplateItem::getId, listIds);
        List<VoucherTemplateItem> books = voucherTemplateItemMapper.selectList(wrapper);
        if (ObjectUtils.isNotEmpty(books)) {
            throw new BusinessException(
                    BookBusinessExceptionEnum.DISABLE_BEFORE_DELETE.getCode(),
                    BookBusinessExceptionEnum.DISABLE_BEFORE_DELETE.getMsg()
            );
        }

        //删除关联科目
        LambdaQueryWrapper<BookSubject> subjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        subjectLambdaQueryWrapper.in(BookSubject::getBookId, listIds);
        bookSubjectMapper.delete(subjectLambdaQueryWrapper);

        //删除账套数据
        boolean result = super.removeByIds(listIds);

        return result ? new Message<>(Message.SUCCESS, "删除成功") : new Message<>(Message.FAIL, "删除失败");
    }

}
