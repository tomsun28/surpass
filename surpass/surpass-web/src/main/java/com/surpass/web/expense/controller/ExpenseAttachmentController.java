package com.surpass.web.expense.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.expense.dto.ExpenseAttachmentChangeDto;
import com.surpass.entity.expense.dto.ExpenseAttachmentPageDto;
import com.surpass.entity.expense.vo.ExpenseAttachmentVo;
import com.surpass.entity.idm.UserInfo;
import com.surpass.persistence.service.expense.ExpenseAttachmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expenseAttachment")
@Slf4j
@RequiredArgsConstructor
public class ExpenseAttachmentController {

    private final ExpenseAttachmentService expenseAttachmentService;

    @GetMapping("/get")
    public Message<ExpenseAttachmentVo> get(@RequestParam("id") String id, @CurrentUser UserInfo userInfo) {
        return expenseAttachmentService.queryById(id);
    }

    @GetMapping("/fetch")
    public Message<Page<ExpenseAttachmentVo>> fetch(ExpenseAttachmentPageDto dto, @CurrentUser UserInfo userInfo) {
        return expenseAttachmentService.pageList(dto);
    }

    @PostMapping("/save")
    public Message<String> save(@Validated @RequestBody ExpenseAttachmentChangeDto dto, @CurrentUser UserInfo userInfo) {
        return expenseAttachmentService.save(dto);
    }

    @PutMapping("/update")
    public Message<String> update(@Validated @RequestBody ExpenseAttachmentChangeDto dto) {
        return expenseAttachmentService.update(dto);
    }

    @DeleteMapping("/delete")
    public Message<String> delete(@Validated @RequestBody ListIdsDto dto) {
        return expenseAttachmentService.delete(dto);
    }
}
