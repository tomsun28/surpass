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
import lombok.extern.slf4j.Slf4j;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surpass.entity.Message;
import com.surpass.entity.book.Book;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.standard.Standard;
import com.surpass.entity.standard.StandardSubject;
import com.surpass.entity.standard.dto.StandardChangeDto;
import com.surpass.entity.standard.dto.StandardPageDto;
import com.surpass.enums.BookBusinessExceptionEnum;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.mapper.StandardMapper;
import com.surpass.persistence.mapper.BookMapper;
import com.surpass.persistence.mapper.StandardSubjectMapper;
import com.surpass.persistence.service.StandardService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2024/12/27 16:43
 */

@Slf4j
@Service
public class StandardServiceImpl extends ServiceImpl<StandardMapper, Standard> implements StandardService {

    @Autowired
    BookMapper bookMapper;

    @Autowired
    StandardSubjectMapper standardSubjectMapper;

    @Override
    public Message<Page<Standard>> pageList(StandardPageDto dto) {
        LambdaQueryWrapper<Standard> wrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(dto.getName())) {
            wrapper.like(Standard::getName, dto.getName());
        }

        Page<Standard> page = super.page(dto.build(), wrapper);

        return new Message<>(Message.SUCCESS, page);
    }

    @Override
    public Message<String> save(StandardChangeDto dto) {

        Standard standard = new Standard();
        BeanUtil.copyProperties(dto, standard);

        boolean result = super.save(standard);

        return result ? new Message<>(Message.SUCCESS, "新增成功") : new Message<>(Message.FAIL, "新增失败");
    }

    @Override
    public Message<String> update(StandardChangeDto dto) {
        if (dto.getStatus() == 0) {
            //禁用检查
            LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Book::getStandardId, dto.getId());
            List<Book> books = bookMapper.selectList(wrapper);
            if (ObjectUtils.isNotEmpty(books)) {
                throw new BusinessException(
                        500000,
                        String.format("当前会计制度已被账套: %s 所使用, 请先移除后再禁用", books.get(0).getName())
                );
            }
        }

        Standard standard = new Standard();

        BeanUtil.copyProperties(dto, standard);

        boolean result = super.updateById(standard);

        return result ? new Message<>(Message.SUCCESS, "修改成功") : new Message<>(Message.FAIL, "修改失败");
    }

    @Override
    @Transactional
    public Message<String> delete(ListIdsDto dto) {
        List<String> listIds = dto.getListIds();
        //删除检查是否禁用
        LambdaUpdateWrapper<Standard> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(Standard::getId, listIds);
        wrapper.eq(Standard::getStatus, 1);
        List<Standard> list = super.list(wrapper);
        if (ObjectUtils.isNotEmpty(list)) {
            throw new BusinessException(
                    BookBusinessExceptionEnum.DISABLE_BEFORE_DELETE.getCode(),
                    BookBusinessExceptionEnum.DISABLE_BEFORE_DELETE.getMsg()
            );
        }

        //删除制度科目
        standardSubjectMapper.delete(
        		Wrappers.<StandardSubject>lambdaQuery()
                	.in(StandardSubject::getStandardId, listIds));
        boolean result = super.removeByIds(listIds);

        return result ? new Message<>(Message.SUCCESS, "删除成功") : new Message<>(Message.FAIL, "删除失败");
    }
}
