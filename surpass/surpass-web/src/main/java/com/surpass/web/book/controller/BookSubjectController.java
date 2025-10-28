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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.entity.Message;
import com.surpass.entity.book.BookSubject;
import com.surpass.entity.book.dto.BookSubjectTreeDto;
import com.surpass.entity.book.dto.SubjectChangeDto;
import com.surpass.entity.book.dto.SubjectPageDto;
import com.surpass.entity.dto.BookQueryDto;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.persistence.service.BookSubjectService;
import com.surpass.validate.AddGroup;
import com.surpass.validate.EditGroup;

import java.util.List;

import org.dromara.hutool.core.tree.MapTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/1/17 14:40
 */

@RestController
@RequestMapping("/booksubject")
public class BookSubjectController {
    private static final Logger logger = LoggerFactory.getLogger(BookSubjectController.class);

    @Autowired
    BookSubjectService bookSubjectService;

    @GetMapping(value = {"/tree/{bookId}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Message<List<MapTree<String>>> treeByBookId(@PathVariable(name = "bookId") String bookId) {
        List<MapTree<String>> tree = bookSubjectService.tree(bookId);
        return new Message<>(Message.SUCCESS, tree);
    }

    @GetMapping(value = {"/fetch"})
    public Message<Page<BookSubject>> fetch(@ParameterObject SubjectPageDto dto) {

        logger.debug("fetchBySet {}", dto);

        return bookSubjectService.pageList(dto);
    }

    @PostMapping("/save")
    public Message<String> save(@Validated(value = AddGroup.class) @RequestBody SubjectChangeDto dto) {
        return bookSubjectService.save(dto);
    }

    @GetMapping("/get")
    public Message<BookSubject> getById(@ParameterObject BookQueryDto dto) {
        return new Message<>(Message.SUCCESS, bookSubjectService.getById(dto.getBookId(), dto.getId()));
    }

    @PutMapping("/update")
    public Message<String> update(@Validated(value = EditGroup.class) @RequestBody SubjectChangeDto dto) {
        return bookSubjectService.update(dto);
    }


    @DeleteMapping("/delete")
    public Message<String> delete(@Validated @RequestBody ListIdsDto dto) {
        return bookSubjectService.delete(dto);
    }

    @GetMapping(value = {"/reorgDisplayName"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Message<String> reorgDisplayName(@ParameterObject BookSubjectTreeDto dto) {
        return bookSubjectService.reorgDisplayName(dto.getBookId());
    }
}
