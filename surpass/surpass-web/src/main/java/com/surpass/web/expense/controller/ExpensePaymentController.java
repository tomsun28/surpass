package com.surpass.web.expense.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surpass.authn.annotation.CurrentUser;
import com.surpass.entity.Message;
import com.surpass.entity.dto.ListIdsDto;
import com.surpass.entity.expense.dto.ExpensePaymentChangeDto;
import com.surpass.entity.expense.dto.ExpensePaymentPageDto;
import com.surpass.entity.expense.vo.ExpensePaymentVo;
import com.surpass.entity.idm.UserInfo;
import com.surpass.persistence.service.expense.ExpensePaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expensePayment")
@Slf4j
@RequiredArgsConstructor
public class ExpensePaymentController {

    private final ExpensePaymentService expensePaymentService;

    @GetMapping("/get")
    public Message<ExpensePaymentVo> get(@RequestParam("id") String id, @CurrentUser UserInfo userInfo) {
        return expensePaymentService.queryById(id);
    }

    @GetMapping("/fetch")
    public Message<Page<ExpensePaymentVo>> fetch(ExpensePaymentPageDto dto, @CurrentUser UserInfo userInfo) {
        return expensePaymentService.pageList(dto);
    }

    @PostMapping("/save")
    public Message<String> save(@Validated @RequestBody ExpensePaymentChangeDto dto, @CurrentUser UserInfo userInfo) {
        return expensePaymentService.save(dto);
    }

    @PutMapping("/update")
    public Message<String> update(@Validated @RequestBody ExpensePaymentChangeDto dto) {
        return expensePaymentService.update(dto);
    }

    @DeleteMapping("/delete")
    public Message<String> delete(@Validated @RequestBody ListIdsDto dto) {
        return expensePaymentService.delete(dto);
    }
}
