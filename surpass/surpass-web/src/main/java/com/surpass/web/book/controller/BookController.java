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


package com.surpass.web.book.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.Institutions;
import com.surpass.entity.Message;
import com.surpass.entity.book.Book;
import com.surpass.entity.book.dto.BookChangeDto;
import com.surpass.entity.book.dto.BookPageDto;
import com.surpass.entity.book.vo.BookVo;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.idm.UserInfo;
import com.surpass.exception.BusinessException;
import com.surpass.persistence.service.BookService;
import com.surpass.persistence.service.InstitutionsService;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2024/12/31 11:18
 */

@RestController
@RequestMapping("/book")
public class BookController {
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    BookService bookService;

    @Autowired
    InstitutionsService institutionsService;

    @GetMapping(value = { "/fetch" })
    public Message<Page<Book>> fetch(@ParameterObject BookPageDto dto, @CurrentUser UserInfo currentUser) {

        logger.debug("fetch {}",dto);
        dto.setInstId(currentUser.getInstId());
        return bookService.pageList(dto);
    }

    @GetMapping("/get/{id}")
    public Message<Book> getById(@PathVariable(name="id") String id) {

        logger.debug("get {}",id);

        return new Message<>(Message.SUCCESS, bookService.getById(id));
    }

    @PostMapping("/save")
    public Message<String> save(@Validated(value = AddGroup.class) @RequestBody BookChangeDto dto, @CurrentUser UserInfo currentUser) {
        String instId = currentUser.getInstId();

        // 获取机构信息
        Institutions institutions = institutionsService.getById(instId);
        if (institutions == null) {
            throw new BusinessException(50002, "机构信息不存在");
        }

        // 检查账套数量限制（使用 longValue 避免类型比较问题）
        Integer maxBook = institutions.getMaxBook();
        if (maxBook != null && maxBook > 0) {
            long currentCount = bookService.count(Wrappers.<Book>lambdaQuery()
                    .eq(Book::getInstId, instId));

            if (currentCount >= maxBook.longValue()) {
                throw new BusinessException(
                        50001,
                        String.format("您已达到最大账套数量限制：%d 个，已无法继续添加", maxBook)
                );
            }
        }

        // 日志
        logger.debug("save {}", dto);

        // 设置机构ID并保存
        dto.setInstId(instId);
        return bookService.save(dto);
    }

    @PutMapping("/update")
    public Message<String> update(@Validated(value = EditGroup.class) @RequestBody BookChangeDto dto) {
        logger.debug("update {}",dto);
        return bookService.update(dto);
    }

    @DeleteMapping(value = { "/delete" })
    public Message<String> delete(@Validated @RequestBody ListIdsDto dto) {

        logger.debug("delete {}",dto);

        return bookService.delete(dto);
    }

    @GetMapping("/fetchAll")
    public Message<List<BookVo>> listStore(@CurrentUser UserInfo currentUser) {
        return Message.ok(bookService.listBooks(currentUser.getId()));
    }
}
